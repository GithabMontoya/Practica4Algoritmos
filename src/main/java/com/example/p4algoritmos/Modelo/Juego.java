package com.example.p4algoritmos.Modelo;

import java.util.Arrays;

public class Juego {
    public static final int COLUMNAS = 8;
    public static final int CELDAS = 8;
    public static final int FUNDACIONES = 4;

    private final Pila<CartaInglesa>[] columnas = new Pila[COLUMNAS];
    private final Pila<CartaInglesa>[] celdas = new Pila[CELDAS];
    private final Pila<CartaInglesa>[] fundaciones = new Pila[FUNDACIONES];
    private final Pila<Movimiento> historial = new Pila<>(5000);
    private ListaCircular<Movimiento> pistas = null;
    private boolean hayPistas = false;
    private int movimientos = 0;

    public Juego() {
        for (int i = 0; i < COLUMNAS; i++) columnas[i] = new Pila<>(60);
        for (int i = 0; i < CELDAS; i++) celdas[i] = new Pila<>(1);
        for (int i = 0; i < FUNDACIONES; i++) fundaciones[i] = new Pila<>(20);
        repartir();
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

    public int maxMovibles() {
        return celdasLibres() + 1;
    }

    public int movimientos() {
        return movimientos;
    }

    public boolean gano() {
        for (Pila<CartaInglesa> fundacion : fundaciones) {
            if (fundacion.tope != 12) return false;
        }
        return true;
    }

    private boolean esColumna(Pila<CartaInglesa> lugar) {
        return Arrays.asList(columnas).contains(lugar);
    }

    private boolean esFundacion(Pila<CartaInglesa> lugar) {
        return Arrays.asList(fundaciones).contains(lugar);
    }

    private boolean encadenan(CartaInglesa arriba, CartaInglesa abajo) {
        return arriba.getPalo() == abajo.getPalo()
                && arriba.getValorBajo() == abajo.getValorBajo() + 1;
    }

    public int cuantasSeMueven(Pila<CartaInglesa> lugar) {
        if (esFundacion(lugar)) return 0;
        int n = cantidadEn(lugar);
        if (n == 0) return 0;

        int enOrden = 1;
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
        if (columna.pilaVacia()) {
            return carta.getValorBajo() == 13;
        }
        return encadenan(columna.peek(), carta);
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
        return m.cantidad == 1 && m.destino.pilaVacia();
    }

    public boolean mover(Movimiento m) {
        m = corregirFundacion(m);
        if (!esValido(m)) return false;
        pasarCartas(m.origen, m.destino, m.cantidad);
        historial.push(m);
        movimientos++;
        pistas = null;
        return true;
    }

    private Movimiento corregirFundacion(Movimiento m) {
        if (esFundacion(m.destino) && m.cantidad == 1 && cuantasSeMueven(m.origen) >= 1) {
            CartaInglesa carta = cima(m.origen);
            return new Movimiento(m.origen, 1, fundaciones[carta.getPalo().ordinal()]);
        }
        return m;
    }

    public boolean undo() {
        if (historial.pilaVacia()) return false;
        Movimiento m = historial.pop();
        pasarCartas(m.destino, m.origen, m.cantidad);
        pistas = null;
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

    public Movimiento siguientePista() {
        if (pistas == null) calcularPistas();
        if (!hayPistas) return null;

        Movimiento pista = pistas.eliminaInicio();
        pistas.insertaFinal(pista);
        return pista;
    }

    private void calcularPistas() {
        Cola<Movimiento> posibles = new Cola<>(1000);

        for (Pila<CartaInglesa> celda : celdas) {
            agregarDesde(posibles, celda);
        }

        for (Pila<CartaInglesa> columna : columnas) {
            agregarDesde(posibles, columna);
        }

        for (Pila<CartaInglesa> celda : celdas) {
            if (celda.pilaVacia()) {
                for (Pila<CartaInglesa> columna : columnas) {
                    probar(posibles, new Movimiento(columna, 1, celda));
                }
                break;
            }
        }

        pistas = new ListaCircular<>();
        hayPistas = false;
        while (posibles.inicio != 0) {
            pistas.insertaFinal(posibles.eliminarDato());
            hayPistas = true;
        }
    }

    private void agregarDesde(Cola<Movimiento> posibles, Pila<CartaInglesa> origen) {
        for (int cant = cuantasSeMueven(origen); cant >= 1; cant--) {
            for (Pila<CartaInglesa> fundacion : fundaciones) {
                probar(posibles, new Movimiento(origen, cant, fundacion));
            }

            for (Pila<CartaInglesa> columna : columnas) {
                boolean columnaCompleta = esColumna(origen) && cant == cantidadEn(origen);
                if (columnaCompleta && columna.pilaVacia()) continue;
                probar(posibles, new Movimiento(origen, cant, columna));
            }
        }
    }

    private void probar(Cola<Movimiento> posibles, Movimiento m) {
        if (esValido(m)) posibles.insertarDato(m);
    }

    public String describir(Movimiento m) {
        String cartas = (m.cantidad == 1) ? "la carta" : "las " + m.cantidad + " cartas";
        return "Mueve " + cartas + " de " + nombre(m.origen) + " a " + nombre(m.destino) + ".";
    }

    private String nombre(Pila<CartaInglesa> lugar) {
        for (int i = 0; i < COLUMNAS; i++) {
            if (columnas[i] == lugar) return "la columna " + (i + 1);
        }
        for (int i = 0; i < CELDAS; i++) {
            if (celdas[i] == lugar) return "la celda " + (i + 1);
        }
        return "su fundación";
    }
}