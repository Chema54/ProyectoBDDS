package main.business.dto;

import main.business.dto.enumeraiones.UsuarioRol;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDTO {

    private int idEmpleado; 
    private String usuario;
    private String contraseña;
    private UsuarioRol rol;
    private boolean tieneAcceso;

    public UsuarioDTO(int idEmpleado, String usuario, String contraseña, UsuarioRol rol, boolean tieneAcceso) {
        this.idEmpleado = idEmpleado;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.tieneAcceso = tieneAcceso;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public UsuarioRol getRol() {
        return rol;
    }

    public boolean isTieneAcceso() {
        return tieneAcceso;
    }

    @Override
    public boolean equals(Object instance) {

        if (this == instance) {
            return true;
        }

        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }

        UsuarioDTO that = (UsuarioDTO) instance;

        return usuario.equals(that.usuario)
                && tieneAcceso == that.tieneAcceso
                && rol == that.rol;
    }

    public boolean hasPasswordMatch(String candidate) {
        return BCrypt.checkpw(candidate, this.contraseña);
    }

    public static String getGeneratedHashedPassword(String plain) {
        return BCrypt.hashpw(plain + "@Password", BCrypt.gensalt());
    }
    
    // ==========================================
    // FIX: PARA QUE SE VEA BIEN EN EL COMBOBOX
    // ==========================================
    @Override
    public String toString() {
        return this.usuario + " (" + this.rol.name() + ")";
    }
}
