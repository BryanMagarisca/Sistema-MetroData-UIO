package estructuras;

import java.io.Serializable;

public class Pila<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Nodo<T> tope;
    private int tamaño;

    private static class Nodo<T> implements Serializable {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    public void push(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamaño++;
    }

    public T pop() {
        if (estaVacia()) return null;
        T dato = tope.dato;
        tope = tope.siguiente;
        tamaño--;
        return dato;
    }

    public T peek() {
        if (estaVacia()) return null;
        return tope.dato;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int getTamaño() {
        return tamaño;
    }
}
