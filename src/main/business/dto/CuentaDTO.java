/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dto;

import main.business.dto.enumeraiones.CuentaRol;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author josem
 */
public class CuentaDTO {

    private String usuario;
    private String contraseña;
    private CuentaRol rol;
    private boolean tieneAcceso;

    public CuentaDTO(String usuario, String contraseña, CuentaRol rol, boolean tieneAcceso) {
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.tieneAcceso = tieneAcceso;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public CuentaRol getRol() {
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

        CuentaDTO that = (CuentaDTO) instance;

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
