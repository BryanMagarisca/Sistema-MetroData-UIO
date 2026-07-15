package utilidades;

import java.io.*;

public class PersistenciaUtil {

    public static void guardar(Object objeto, String nombreArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(objeto);
        } catch (IOException e) {
            System.err.println("Error al guardar en " + nombreArchivo + ": " + e.getMessage());
        }
    }

    public static Object cargar(String nombreArchivo) {
        File file = new File(nombreArchivo);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar desde " + nombreArchivo + ": " + e.getMessage());
            return null;
        }
    }
}
