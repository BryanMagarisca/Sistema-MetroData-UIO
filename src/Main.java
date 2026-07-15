import vista.MainFrame;
import vista.principal.PantallaInstitucional;
import vista.login.PantallaLogin;
import vista.login.PantallaRegistro;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            
            // Inicializar las pantallas de flujo público
            PantallaInstitucional institucional = new PantallaInstitucional(mainFrame);
            PantallaLogin login = new PantallaLogin(mainFrame);
            PantallaRegistro registro = new PantallaRegistro(mainFrame);
            
            mainFrame.agregarPanel(institucional, "INSTITUCIONAL");
            mainFrame.agregarPanel(login, "LOGIN");
            mainFrame.agregarPanel(registro, "REGISTRO");
            
            // Iniciar en la pantalla institucional
            mainFrame.mostrarPanel("INSTITUCIONAL");
            mainFrame.setVisible(true);
        });
    }
}