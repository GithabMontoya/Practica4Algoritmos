package com.example.p4algoritmos.Modelo;

public class Cola <T>{
    public T[] cola;
    public int inicio;
    public int fin;
    public int MAX;

    public Cola(int max){
        MAX = max;
        cola = (T[]) new Object[MAX + 1];
        inicio = 0;
        fin = 0;
    }

    public void insertarDato(T dato){
        if(fin < MAX){
            fin++;
            cola[fin] = dato;
            if(fin == 1){
                inicio = 1;
            }
        } else {
            System.out.println("Desbordamiento");
        }
    }

    public T eliminarDato(){
        T dato;
        if (inicio != 0){
            dato = cola[inicio];
            cola[inicio] = null;
            if(inicio == fin){
                inicio = 0;
                fin = 0;
            } else {
                inicio = inicio + 1;
            }
            return dato;
        } else {
            System.out.println("Subdesbordamiento");
            return null;
        }
    }

}
