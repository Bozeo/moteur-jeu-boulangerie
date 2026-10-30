package com.boulangerie.moteur.model;

public class Stock<T> {
    private T element;

    public Stock() {
        this.element = null;
    }

    public boolean estVide() {
        return element == null;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public T retirer() {
        T temp = this.element;
        this.element = null;
        return temp;
    }

    public void vider() {
        this.element = null;
    }
}
