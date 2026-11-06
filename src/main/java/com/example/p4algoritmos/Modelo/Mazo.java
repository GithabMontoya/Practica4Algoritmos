package com.example.p4algoritmos.Modelo;
/**
 * Write a description of class Mazo here.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
import java.util.Random;

public class Mazo {
    private ListaCircularDoble<CartaInglesa> cartas = new ListaCircularDoble<>();

    public Mazo() {
        llenar();
        mezclar();
    }

    public ListaCircularDoble<CartaInglesa> getCartas() {
        return cartas;
    }

    public CartaInglesa obtenerUnaCarta() {
        if (cartas.getInicio() == null) {
            return null;
        }
        return cartas.eliminaInicio();
    }

    private void mezclar() {
        Random azar = new Random();
        ListaCircularDoble<CartaInglesa> mezcladas = new ListaCircularDoble<>();
        while (cartas.getInicio() != null) {
            int posicion = azar.nextInt(cartas.contarNodos());
            mezcladas.insertaFinal(cartas.eliminaPosicion(posicion));
        }
        cartas = mezcladas;
    }

    private void llenar() {
        for (int i = 2; i <= 14; i++) {
            for (Palo palo : Palo.values()) {
                cartas.insertaFinal(new CartaInglesa(i, palo, palo.getColor()));
            }
        }
    }

    public void ordenar() {
        cartas.ordenarLista();
    }

    @Override
    public String toString() {
        return cartas.recorre();
    }
}