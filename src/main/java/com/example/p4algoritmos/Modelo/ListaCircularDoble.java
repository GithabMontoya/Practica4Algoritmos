package com.example.p4algoritmos.Modelo;

public class ListaCircularDoble<T> {
    private NodoCircularDoble<T> inicio;
    private NodoCircularDoble<T> fin;

    public ListaCircularDoble() {
        this.inicio = null;
        this.fin = null;
    }

    public NodoCircularDoble<T> getInicio() {
        return inicio;
    }

    public void insertaInicio(T dato) {
        NodoCircularDoble<T> n = new NodoCircularDoble<>();
        n.setInfo(dato);
        if (inicio == null) {
            inicio = n;
            fin = n;
            n.setSig(inicio);
            n.setAnt(inicio);
        } else {
            n.setSig(inicio);
            inicio.setAnt(n);
            inicio = n;
            fin.setSig(inicio);
            n.setAnt(fin);
        }
    }

    public void insertaFinal(T dato) {
        NodoCircularDoble<T> n = new NodoCircularDoble<>();
        n.setInfo(dato);
        if (inicio == null) {
            inicio = n;
            fin = n;
            n.setSig(inicio);
            n.setAnt(inicio);
        } else {
            n.setSig(inicio);
            inicio.setAnt(n);
            fin.setSig(n);
            n.setAnt(fin);
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
                inicio.setAnt(fin);
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
                NodoCircularDoble<T> r = fin.getAnt();
                r.setSig(inicio);
                inicio.setAnt(r);
                fin = r;
            }
        }
        return datoEliminado;
    }

    public String recorre() {
        String cadena = "";
        if (inicio == null) {
            System.out.println("Lista vacia");
        } else {
            NodoCircularDoble<T> r = inicio;
            do {
                cadena += r.getInfo() + " ";
                r = r.getSig();
            } while (r != inicio);
        }
        return cadena;
    }

    public String mostrarRecursivo(NodoCircularDoble<T> x) {
        if (inicio == null || x == null) return "";
        if (x == fin) {
            return x.getInfo().toString();
        }
        return x.getInfo() + " " + mostrarRecursivo(x.getSig());
    }

    public String mostrarRecursivo() {
        return mostrarRecursivo(inicio);
    }

    public int buscar(T x) {
        if (inicio == null) return -1;
        return buscarRecursivo(x, inicio, 0);
    }

    private int buscarRecursivo(T x, NodoCircularDoble<T> actual, int pos) {
        if (actual.getInfo().equals(x)) {
            return pos;
        }
        if (actual == fin) {
            return -1;
        }
        return buscarRecursivo(x, actual.getSig(), pos + 1);
    }

    private void eliminarNodo(NodoCircularDoble<T> nodo) {
        if (inicio == fin && inicio == nodo) {
            inicio = null;
            fin = null;
        } else if (nodo == inicio) {
            fin.setSig(inicio.getSig());
            inicio = inicio.getSig();
            inicio.setAnt(fin);
        } else if (nodo == fin) {
            NodoCircularDoble<T> aux = fin.getAnt();
            aux.setSig(inicio);
            inicio.setAnt(aux);
            fin = aux;
        } else {
            nodo.getAnt().setSig(nodo.getSig());
            nodo.getSig().setAnt(nodo.getAnt());
        }
    }

    public T eliminaX(T x) {
        if (inicio == null) return null;
        NodoCircularDoble<T> actual = inicio;
        do {
            if (actual.getInfo().equals(x)) {
                T dato = actual.getInfo();
                eliminarNodo(actual);
                return dato;
            }
            actual = actual.getSig();
        } while (actual != inicio);
        return null;
    }

    public T eliminaPosicion(int posicion) {
        if (inicio == null || posicion < 0) return null;
        NodoCircularDoble<T> actual = inicio;
        int posActual = 0;
        do {
            if (posActual == posicion) {
                T dato = actual.getInfo();
                eliminarNodo(actual);
                return dato;
            }
            posActual++;
            actual = actual.getSig();
        } while (actual != inicio);
        return null;
    }

    public void insertaEnPosicion(T dato, int posicion) {
        if (posicion < 0) return;
        if (posicion == 0) {
            insertaInicio(dato);
            return;
        }
        if (inicio == null) return;

        NodoCircularDoble<T> actual = inicio;
        int index = 0;
        while (index < posicion - 1) {
            actual = actual.getSig();
            index++;
            if (actual == inicio) return;
        }

        if (actual == fin) {
            insertaFinal(dato);
            return;
        }

        NodoCircularDoble<T> nuevo = new NodoCircularDoble<>();
        nuevo.setInfo(dato);
        NodoCircularDoble<T> siguiente = actual.getSig();

        actual.setSig(nuevo);
        nuevo.setAnt(actual);
        nuevo.setSig(siguiente);
        siguiente.setAnt(nuevo);
    }

    @SuppressWarnings("unchecked")
    public void ordenarLista() {
        if (inicio == null || inicio == fin) return;
        NodoCircularDoble<T> p = inicio;
        do {
            NodoCircularDoble<T> q = p.getSig();
            while (q != inicio) {
                if (((Comparable<T>) p.getInfo()).compareTo(q.getInfo()) > 0) {
                    T temp = p.getInfo();
                    p.setInfo(q.getInfo());
                    q.setInfo(temp);
                }
                q = q.getSig();
            }
            p = p.getSig();
        } while (p != fin);
    }

    public void eliminarPares() {
        if (inicio == null) return;
        int total = contarNodos();
        NodoCircularDoble<T> actual = inicio;
        for (int i = 0; i < total; i++) {
            NodoCircularDoble<T> siguiente = actual.getSig();
            if (actual.getInfo() instanceof Integer) {
                int valor = (Integer) actual.getInfo();
                if (valor % 2 == 0) {
                    eliminarNodo(actual);
                }
            }
            actual = siguiente;
            if (inicio == null) break;
        }
    }

    public int contarNodos() {
        if (inicio == null) return 0;
        int cont = 0;
        NodoCircularDoble<T> r = inicio;
        do {
            cont++;
            r = r.getSig();
        } while (r != inicio);
        return cont;
    }
}
