package com.example.p4algoritmos.Modelo;

import java.util.Arrays;

/**
 * todo es una Pila de cartas, que están ordenadas en distintos lugares
 *  - 8 columnas    (las de abajo)
 *  - 8 celdas      (las de arriba, cada una guarda 1 carta)
 *  - 4 fundaciones (una por palo, de A a K, en el orden del enum Palo)
 *
 * Un movimiento siempre es lo mismo: pasar N cartas de un lugar a otro.
 */
public class Juego {
    public static final int COLUMNAS = 8;
    public static final int CELDAS = 8;
    public static final int FUNDACIONES = 4;

    private final Pila<CartaInglesa>[] columnas = new Pila[COLUMNAS];
    private final Pila<CartaInglesa>[] celdas = new Pila[CELDAS];
    private final Pila<CartaInglesa>[] fundaciones = new Pila[FUNDACIONES];
    private final Pila<Movimiento> historial = new Pila<>(5000);

    public Juego() {
        for (int i = 0; i < COLUMNAS; i++) columnas[i] = new Pila<>(60);
        for (int i = 0; i < CELDAS; i++) celdas[i] = new Pila<>(1);
        for (int i = 0; i < FUNDACIONES; i++) fundaciones[i] = new Pila<>(20);
    }
    private void repartir() {
        Mazo mazo = new Mazo();
        for (int i = 0; i < 4; i++) {
            celdas[i].push(cartaDelMazo(mazo));
        }
        for (int col = 0; col < COLUMNAS; col++) {
            for (int k = 0; k < 6; k++) {
                columnas[col].push(cartaDelMazo(mazo));
            }
        }
    }

    private CartaInglesa cartaDelMazo(Mazo mazo) {
        CartaInglesa carta = mazo.obtenerUnaCarta();
        carta.makeFaceUp();
        return carta;
    }

    // PARA LA VISTA

    public Pila<CartaInglesa> lugarColumna(int i) {
        return columnas[i];
    }

    public Pila<CartaInglesa> lugarCelda(int i) {
        return celdas[i];
    }

    public Pila<CartaInglesa> lugarFundacion(int i) {
        return fundaciones[i];
    }

    public int cantidadEn(Pila<CartaInglesa> lugar) {
        return lugar.tope + 1;
    }

    public CartaInglesa cartaEn(Pila<CartaInglesa> lugar, int pos) {
        Object[] datos = lugar.pila;
        return (CartaInglesa) datos[pos];
    }

    public CartaInglesa cima(Pila<CartaInglesa> lugar) {
        return lugar.pilaVacia() ? null : lugar.peek();
    }

    public int celdasLibres() {
        int libres = 0;
        for (Pila<CartaInglesa> celda : celdas) {
            if (celda.pilaVacia()) libres++;
        }
        return libres;
    }

    // Cuántas cartas se pueden mover juntas, celdas libres + 1.
    public int maxMovibles() {
        return celdasLibres() + 1;
    }

    public boolean gano() {
        for (Pila<CartaInglesa> fundacion : fundaciones) {
            if (fundacion.tope != 12) return false;   // 13 cartas = índice 12
        }
        return true;
    }

    // REGLAS

    private boolean esColumna(Pila<CartaInglesa> lugar) {
        return Arrays.asList(columnas).contains(lugar);
    }


    private boolean esFundacion(Pila<CartaInglesa> lugar) {
        return Arrays.asList(fundaciones).contains(lugar);
    }

    // La carta de abajo puede ir debajo de la de arriba, mismo palo y un valor menos
    private boolean encadenan(CartaInglesa arriba, CartaInglesa abajo) {
        return arriba.getPalo() == abajo.getPalo()
                && arriba.getValorBajo() == abajo.getValorBajo() + 1;
    }

    // Cuántas cartas del final de un lugar se pueden mover juntas.
    public int cuantasSeMueven(Pila<CartaInglesa> lugar) {
        if (esFundacion(lugar)) return 0;
        int n = cantidadEn(lugar);
        if (n == 0) return 0;

        int enOrden = 1;   // cartas seguidas del mismo palo, bajando de uno en uno
        while (enOrden < n && encadenan(cartaEn(lugar, n - enOrden - 1), cartaEn(lugar, n - enOrden))) {
            enOrden++;
        }
        return Math.min(enOrden, maxMovibles());
    }

    public boolean esMovible(Pila<CartaInglesa> lugar, int pos) {
        int n = cantidadEn(lugar);
        return pos >= 0 && pos < n && (n - pos) <= cuantasSeMueven(lugar);
    }

    private boolean cabeEnColumna(CartaInglesa carta, Pila<CartaInglesa> columna) {
        return columna.pilaVacia() || encadenan(columna.peek(), carta);
    }

    private boolean cabeEnFundacion(CartaInglesa carta) {
        Pila<CartaInglesa> fundacion = fundaciones[carta.getPalo().ordinal()];
        int siguiente = fundacion.pilaVacia() ? 1 : fundacion.peek().getValorBajo() + 1;
        return carta.getValorBajo() == siguiente;
    }

    public boolean esValido(Movimiento m) {
        if (m.cantidad < 1 || m.cantidad > cuantasSeMueven(m.origen)) return false;
        CartaInglesa carta = cartaEn(m.origen, cantidadEn(m.origen) - m.cantidad);

        if (m.origen == m.destino) return false;
        if (esColumna(m.destino)) {
            return cabeEnColumna(carta, m.destino);
        }
        if (esFundacion(m.destino)) {
            return m.cantidad == 1
                    && m.destino == fundaciones[carta.getPalo().ordinal()]
                    && cabeEnFundacion(carta);
        }
        // Si no es columna ni fundación, es una celda
        return m.cantidad == 1 && m.destino.pilaVacia();
    }

    // Hace el movimiento si es válido y lo guarda en el historial
    public boolean mover(Movimiento m) {
        if (!esValido(m)) return false;
        pasarCartas(m.origen, m.destino, m.cantidad);
        historial.push(m);
        return true;
    }

    // Deshace el último movimiento
    public boolean undo() {
        if (historial.pilaVacia()) return false;
        Movimiento m = historial.pop();
        pasarCartas(m.destino, m.origen, m.cantidad);
        return true;
    }

    private void pasarCartas(Pila<CartaInglesa> origen, Pila<CartaInglesa> destino, int cantidad) {
        CartaInglesa[] grupo = new CartaInglesa[cantidad];
        for (int i = cantidad - 1; i >= 0; i--) {
            grupo[i] = origen.pop();
        }
        for (CartaInglesa carta : grupo) {
            destino.push(carta);
        }
    }


}