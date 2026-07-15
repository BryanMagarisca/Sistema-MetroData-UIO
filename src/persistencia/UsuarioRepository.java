package persistencia;

import modelo.Usuario;
import utilidades.PersistenciaUtil;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    private static final String FILE_PATH = "usuarios.dat";
    private static UsuarioRepository instancia;
    private List<Usuario> usuarios;

    private UsuarioRepository() {
        cargar();
        asegurarSuperAdmin();
    }

    public static UsuarioRepository getInstancia() {
        if (instancia == null) instancia = new UsuarioRepository();
        return instancia;
    }

    @SuppressWarnings("unchecked")
    private void cargar() {
        Object data = PersistenciaUtil.cargar(FILE_PATH);
        usuarios = (data instanceof List) ? (List<Usuario>) data : new ArrayList<>();
    }

    private void asegurarSuperAdmin() {
        if (usuarios.stream().noneMatch(u -> u.getRol().equals("SUPER_ADMIN"))) {
            usuarios.add(new Usuario("Super Administrador", "admin", "admin", "SUPER_ADMIN"));
            guardar();
        }
    }

    public void guardar() { PersistenciaUtil.guardar(usuarios, FILE_PATH); }
    public List<Usuario> getUsuarios() { return usuarios; }
    
    public Usuario autenticar(String username, String password) {
        return usuarios.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst().orElse(null);
    }
}