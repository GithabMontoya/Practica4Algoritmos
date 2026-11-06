package com.example.p4algoritmos.Modelo;

public class ListaCircular<T> {
    private NodoCircularSimple<T> inicio;
    private NodoCircularSimple<T> fin;

    public ListaCircular() {
        this.inicio = null;
        this.fin = null;
    }

    public void insertaInicio(T dato) {
        NodoCircularSimple<T> n = new NodoCircularSimple<>(dato, null);
        if (inicio == null) {
            inicio = n;
            fin = n;
            n.setSig(inicio);
        } else {
            n.setSig(inicio);
            inicio = n;
            fin.setSig(inicio);
        }
    }

    public void insertaFinal(T dato) {
        NodoCircularSimple<T> n = new NodoCircularSimple<>(dato, null);
        if (inicio == null) {
            inicio = n;
            fin = n;
            n.setSig(inicio);
        } else {
            n.setSig(inicio);
            fin.setSig(n);
            fin = n;
        }
    }

    public T eliminaInicio() {
        T datoEliminado = null;
        if (inicio == null) {
            System.out.println("Lista vacia");
        } else {
            datoEliminado = inicio.getInfo();
            if (inicio == fin) {
                inicio = null;
                fin = null;
            } else {
                fin.setSig(inicio.getSig());
                inicio = inicio.getSig();
            }
        }
        return datoEliminado;
    }

    public T eliminaFinal() {
        T datoEliminado = null;
        if (inicio == null) {
            System.out.println("Lista vacia");
        } else {
            datoEliminado = fin.getInfo();
            if (inicio == fin) {
                inicio = null;
                fin = null;
            } else {
                NodoCircularSimple<T> r = inicio;
                while (r.getSig() != fin) {
                    r = r.getSig();
                }
                fin = r;
                fin.setSig(inicio);
            }
        }
        return datoEliminado;
    }

    public String recorre() {
        String cadena = "";
        if (inicio == null) {
            System.out.println("Lista vacia");
        } else {
            NodoCircularSimple<T> r = inicio;
            do {
                cadena += r.getInfo() + " ";
                r = r.getSig();
            } while (r != inicio);
        }
        return cadena;
    }
}