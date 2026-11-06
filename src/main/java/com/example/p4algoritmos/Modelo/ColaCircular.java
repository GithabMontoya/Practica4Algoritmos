package com.example.p4algoritmos.Modelo;

public class ColaCircular <T>{
    private T[] colaCircular;
    private int inicio;
    private int fin;
    private int MAX;

    public ColaCircular(int max){
        MAX = max;
        colaCircular = (T[]) new Object[MAX + 1];
        fin = 0;
        inicio = 0;
    }

    public void insertarDato(T dato){
        if((fin == MAX && inicio == 1) || (fin + 1 == inicio)){
            System.out.println("Desbordamiento");
        } else {
            if (fin == MAX) {
                fin = 1;
            } else {
                fin++;
            }
            if (inicio == 0) {
                inicio = 1;
            }
            colaCircular[fin] = dato;
        }
    }

    public T eliminarDato(){
        if(inicio == 0){
            System.out.println("Subdesbordamiento");
            return null;
        } else{
            T dato;
            dato = colaCircular[inicio];
            colaCircular[inicio] = null;
            if(inicio == fin){
                inicio = 0;
                fin = 0;
            } else if(inicio == MAX){
                inicio = 1;
            } else {
                inicio ++;
            }
            return dato;
        }
    }


}
