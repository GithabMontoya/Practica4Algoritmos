package com.example.p4algoritmos.Modelo;

public class ListaDoble<T> {
    private NodoDoble<T> inicio;

    public ListaDoble() {
        this.inicio = null;
    }

    public void insertaInicio(T dato) {
        NodoDoble<T> n = new NodoDoble<>();
        n.setInfo(dato);
        n.setSig(inicio);
        n.setAnt(null);

        if (inicio != null) {
            inicio.setAnt(n);
        }
        inicio = n;
    }

    public void insertaFinal(T dato) {
        NodoDoble<T> n = new NodoDoble<>();
        n.setInfo(dato);
        n.setSig(null);

        if (inicio == null) {
            n.setAnt(inicio);
            inicio = n;
        } else {
            NodoDoble<T> r = inicio;
            while (r.getSig() != null) {
                r = r.getSig();
            }
            r.setSig(n);
            n.setAnt(r);
        }
    }

    public T eliminaInicio() {
        T datoEliminado = null;
        if (inicio == null) {
            System.out.println("Lista vacia");
        } else {
            datoEliminado = inicio.getInfo();
            if (inicio.getSig() == null) {
                inicio = null;
            } else {
                inicio = inicio.getSig();
                inicio.setAnt(null);
            }
        }
        return datoEliminado;
    }

    public T eliminaFinal() {
        T datoEliminado = null;
        if (inicio == null) {
            System.out.println("Lista vacia");
        } else {
            if (inicio.getSig() == null) {
                datoEliminado = inicio.getInfo();
                inicio = null;
            } else {
                NodoDoble<T> r = inicio;
                while (r.getSig() != null) {
                    r = r.getSig();
                }
                datoEliminado = r.getInfo();
                r.getAnt().setSig(null);
            }
        }
        return datoEliminado;
    }

    public String recorre() {
        String cadena = "";
        if (inicio == null) {
            System.out.println("Lista vacia");
        } else {
            NodoDoble<T> r = inicio;
            while (r != null) {
                System.out.println(r.getInfo());
                cadena += r.getInfo() + "\n";
                r = r.getSig();
            }
        }
        return cadena;
    }
}
