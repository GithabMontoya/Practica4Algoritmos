package com.example.p4algoritmos.Modelo;

public class ListaSimple<T> {
    private Nodo<T> inicio;

    public ListaSimple() {
        this.inicio = null;
    }

    // Algoritmo insertaInicio
    public void insertaInicio(T dato) {
        Nodo<T> n = new Nodo<>(dato);

        n.setSig(inicio);

        inicio = n;
    }

    // Algoritmo insertaFin
    public void insertaFinal(T dato) { //[cite: 9]
        Nodo<T> n = new Nodo<>(dato);
        if (inicio == null) {
            n.setSig(inicio);
            inicio = n;
        } else {
            Nodo<T> r = inicio;
            while (r.getSig() != null) {
                r = r.getSig();
            }
            r.setSig(n);
            n.setSig(null);
        }
    }

    // Algoritmo eliminaInicio
    public T eliminaInicio() {
        T datoEliminado = null;
        if (inicio == null) {
            System.out.println("Lista vacia");
        } else {
            datoEliminado = inicio.getInfo();
            inicio = inicio.getSig();
        }
        return datoEliminado;
    }

    // Algoritmo eliminaFin
    public T eliminaFinal() {
        T datoEliminado = null;
        if (inicio == null) {
            System.out.println("Lista vacia");
        } else {
            if (inicio.getSig() == null) {
                datoEliminado = inicio.getInfo();
                inicio = null;
            } else {
                Nodo<T> r = inicio;
                Nodo<T> a = r;
                while (r.getSig() != null) {
                    a = r;
                    r = r.getSig();
                }
                datoEliminado = r.getInfo();
                a.setSig(null);
            }
        }
        return datoEliminado;
    }

    public String mostrarLista() {
        String cadena = "";
        if (inicio != null) {
            Nodo<T> r = inicio;
            while (r != null) {
                cadena += r.getInfo() + " ";
                r = r.getSig();
            }
        } else {
            System.out.println("Lista vacia");
        }
        return cadena;
    }
}