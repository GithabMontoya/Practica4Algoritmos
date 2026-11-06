package com.example.p4algoritmos.Modelo;

public class NodoDoble<T> {
    private T info;
    private NodoDoble<T> sig;
    private NodoDoble<T> ant;

    public NodoDoble(T info, NodoDoble<T> ant, NodoDoble<T> sig) {
        this.info = info;
        this.ant = ant;
        this.sig = sig;
    }

    public NodoDoble() {
        this.info = null;
        this.ant = null;
        this.sig = null;
    }

    public T getInfo() { return info; }
    public void setInfo(T info) { this.info = info; }
    public NodoDoble<T> getSig() { return sig; }
    public void setSig(NodoDoble<T> sig) { this.sig = sig; }
    public NodoDoble<T> getAnt() { return ant; }
    public void setAnt(NodoDoble<T> ant) { this.ant = ant; }

    @Override
    public String toString() {
        return info.toString();
    }
}