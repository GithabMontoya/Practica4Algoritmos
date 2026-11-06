package com.example.p4algoritmos.Modelo;

public class NodoCircularSimple<T> {
    private T info;
    private NodoCircularSimple<T> sig;

    public NodoCircularSimple(T info, NodoCircularSimple<T> sig) {
        this.info = info;
        this.sig = sig;
    }

    public NodoCircularSimple() {
        this.info = null;
        this.sig = null;
    }

    public T getInfo() { return info; }
    public void setInfo(T info) { this.info = info; }
    public NodoCircularSimple<T> getSig() { return sig; }
    public void setSig(NodoCircularSimple<T> sig) { this.sig = sig; }

    @Override
    public String toString() {
        return info.toString();
    }
}