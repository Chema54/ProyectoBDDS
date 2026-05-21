/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dto;

import main.business.dto.enumeraiones.UsuarioRol;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author josem
 */
public class UsuarioDTO {

    private int idEmpleado; 
    private String usuario;
    private String contraseña;
    private UsuarioRol rol;
    private boolean tieneAcceso;

    // Actualizamos el constructor para recibirlo
    public UsuarioDTO(int idEmpleado, String usuario, String contraseña, UsuarioRol rol, boolean tieneAcceso) {
        this.idEmpleado = idEmpleado;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.tieneAcceso = tieneAcceso;
    }

    // Su respectivo Getter para que el DAO lo pueda leer
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
}
