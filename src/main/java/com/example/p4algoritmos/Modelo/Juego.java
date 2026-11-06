package com.example.p4algoritmos.Modelo;

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
}