package estructuras;

import java.io.Serializable;
import java.util.function.Consumer;

public class ArbolBinarioBusqueda<T extends Comparable<T>> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private NodoBST<T> raiz;

    private static class NodoBST<T> implements Serializable {
        T dato;
        NodoBST<T> izquierdo;
        NodoBST<T> derecho;

        NodoBST(T dato) {
            this.dato = dato;
        }
    }

    public void insertar(T dato) {
        raiz = insertarRec(raiz, dato);
    }

    private NodoBST<T> insertarRec(NodoBST<T> actual, T dato) {
        if (actual == null) return new NodoBST<>(dato);
        if (dato.compareTo(actual.dato) < 0) actual.izquierdo = insertarRec(actual.izquierdo, dato);
        else if (dato.compareTo(actual.dato) > 0) actual.derecho = insertarRec(actual.derecho, dato);
        return actual;
    }

    public T buscar(T objetivo) {
        return buscarRec(raiz, objetivo);
    }

    private T buscarRec(NodoBST<T> actual, T objetivo) {
        if (actual == null) return null;
        if (objetivo.compareTo(actual.dato) == 0) return actual.dato;
        return objetivo.compareTo(actual.dato) < 0 ? buscarRec(actual.izquierdo, objetivo) : buscarRec(actual.derecho, objetivo);
    }

    public void eliminar(T dato) {
        raiz = eliminarRec(raiz, dato);
    }

    private NodoBST<T> eliminarRec(NodoBST<T> actual, T dato) {
        if (actual == null) return null;
        if (dato.compareTo(actual.dato) < 0) actual.izquierdo = eliminarRec(actual.izquierdo, dato);
        else if (dato.compareTo(actual.dato) > 0) actual.derecho = eliminarRec(actual.derecho, dato);
        else {
            if (actual.izquierdo == null) return actual.derecho;
            if (actual.derecho == null) return actual.izquierdo;
            actual.dato = encontrarMin(actual.derecho);
            actual.derecho = eliminarRec(actual.derecho, actual.dato);
        }
        return actual;
    }

    private T encontrarMin(NodoBST<T> raiz) {
        T min = raiz.dato;
        while (raiz.izquierdo != null) {
            min = raiz.izquierdo.dato;
            raiz = raiz.izquierdo;
        }
        return min;
    }

    public void inorden(Consumer<T> accion) {
        inordenRec(raiz, accion);
    }

    private void inordenRec(NodoBST<T> actual, Consumer<T> accion) {
        if (actual != null) {
            inordenRec(actual.izquierdo, accion);
            accion.accept(actual.dato);
            inordenRec(actual.derecho, accion);
        }
    }
}
