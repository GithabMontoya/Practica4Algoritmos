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
}