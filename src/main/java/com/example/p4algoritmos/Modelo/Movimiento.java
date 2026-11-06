package com.example.p4algoritmos.Modelo;

/**
 * Un movimiento: pasar "cantidad" cartas del final de la pila "origen" a la pila "destino".
 * Se guarda en el historial para poder hacer Undo.
 */
public class Movimiento {
    public final Pila<CartaInglesa> origen;
    public final int cantidad;
    public final Pila<CartaInglesa> destino;

    public Movimiento(Pila<CartaInglesa> origen, int cantidad, Pila<CartaInglesa> destino) {
        this.origen = origen;
        this.cantidad = cantidad;
        this.destino = destino;
    }
}

