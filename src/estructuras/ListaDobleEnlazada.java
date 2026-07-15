package estructuras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListaDobleEnlazada<T> implements Iterable<T> {
    
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamaño;

    public static class Nodo<T> {
        public T dato;
        public Nodo<T> siguiente;
        public Nodo<T> anterior;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    public void agregarAlFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
        tamaño++;
    }

    public List<T> toList() {
        List<T> lista = new ArrayList<>();
        Nodo<T> actual = cabeza;
        while (actual != null) {
            lista.add(actual.dato);
            actual = actual.siguiente;
        }
        return lista;
    }

    public Nodo<T> getCabeza() {
        return cabeza;
    }

    public Nodo<T> getCola() {
        return cola;
    }

    public int getTamaño() {
        return tamaño;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                T dato = actual.dato;
                actual = actual.siguiente;
                return dato;
            }
        };
    }
}