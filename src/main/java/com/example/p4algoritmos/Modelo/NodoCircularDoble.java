package com.example.p4algoritmos.Modelo;

public class NodoCircularDoble<T> {
    private T info;
    private NodoCircularDoble<T> sig;
    private NodoCircularDoble<T> ant;

    public NodoCircularDoble(T info, NodoCircularDoble<T> ant, NodoCircularDoble<T> sig) {
        this.info = info;
        this.ant = ant;
        this.sig = sig;
    }

    public NodoCircularDoble() {
        this.info = null;
        this.ant = null;
        this.sig = null;
    }

    public T getInfo() { return info; }
    public void setInfo(T info) { this.info = info; }
    public NodoCircularDoble<T> getSig() { return sig; }
    public void setSig(NodoCircularDoble<T> sig) { this.sig = sig; }
    public NodoCircularDoble<T> getAnt() { return ant; }
    public void setAnt(NodoCircularDoble<T> ant) { this.ant = ant; }

    @Override
    public String toString() {
        return info.toString();
    }
}