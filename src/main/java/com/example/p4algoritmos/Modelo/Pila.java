package com.example.p4algoritmos.Modelo;

public class Pila <T>{
    T[] pila;
    int tope;

    public Pila (int valorMaximo) {
        pila = (T[]) new Object[valorMaximo];
        tope = -1;
    }

    public Pila (){
        pila = (T[]) new Object[10];
        tope = -1;
    }

    public boolean pilaLlena(){
        return tope >= pila.length-1;
    }

    public boolean pilaVacia(){
        return tope <= -1;
    }

    public void push(T dato){
        if(pilaLlena()){
            System.out.println("Desbordamiento");
        } else {
            tope ++;
            pila[tope] = dato;
        }
    }

    public T pop(){
        T dato;
        if(pilaVacia()){
            System.out.println("Pila vacia");
            return null;
        } else {
            dato = pila[tope];
            pila[tope] = null;
            tope--;
            return dato;
        }
    }

    public T peek(){
        T dato;
        if(pilaVacia()){
            System.out.println("Pila vacia");
            return null;
        } else {
            dato = pila[tope];
            return dato;
        }
    }



    @Override
    public String toString() {
        if (pilaVacia()) {
            return "Pila vacia";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = tope; i >= 0; i--){
            sb.append(pila[i]);
            if (i > 0){
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
