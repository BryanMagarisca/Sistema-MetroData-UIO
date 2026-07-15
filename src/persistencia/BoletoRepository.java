package persistencia;

import modelo.Boleto;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BoletoRepository {
    private static final String FILE_PATH = "boletos.dat";
    private static BoletoRepository instancia;
    private List<Boleto> boletos;

    private BoletoRepository() {
        boletos = cargarBoletos();
    }

    public static BoletoRepository getInstancia() {
        if (instancia == null) instancia = new BoletoRepository();
        return instancia;
    }

    public void guardarBoleto(Boleto b) {
        boletos.add(b);
        serializar();
    }

    public List<Boleto> getBoletosOrdenados() {
        List<Boleto> ordenados = new ArrayList<>(boletos);
        ordenados.sort(Comparator.comparing(Boleto::getCedula)
                .thenComparing(Boleto::getFechaHora));
        return ordenados;
    }

    @SuppressWarnings("unchecked")
    private List<Boleto> cargarBoletos() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Boleto>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void serializar() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(boletos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}