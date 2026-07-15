package utilidades;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GeneradorQR {

    public static ImageIcon generarImagenQR(String contenido, int size, String idBoleto) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size, size);
        g.setColor(Color.BLACK);
        Random r = new Random(contenido.hashCode());
        
        int cellSize = size / 20;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if ((i < 4 && j < 4) || (i > 15 && j < 4) || (i < 4 && j > 15)) {
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                } else if (r.nextBoolean()) {
                    g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
        }
        g.dispose();

        // Guardado físico automático
        guardarImagen(image, idBoleto);
        
        return new ImageIcon(image);
    }

    private static void guardarImagen(BufferedImage img, String id) {
        try {
            File carpeta = new File("qr");
            if (!carpeta.exists()) carpeta.mkdirs();
            File output = new File(carpeta, id + ".png");
            ImageIO.write(img, "png", output);
            System.out.println("QR guardado en: " + output.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
