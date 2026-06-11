/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dao.auth;

import main.business.dto.UsuarioDTO;
import main.business.dto.enumeraiones.UsuarioRol;
import main.common.UserDisplayableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthClient {
    private static final Logger LOGGER = LogManager.getLogger(AuthClient.class.getName());
    private static AuthClient instance;
    private UsuarioDTO currentUser;

    private AuthClient() {
        this.currentUser = null;
    }

    public static synchronized AuthClient getInstance() {
        if (instance == null) {
            instance = new AuthClient();
        }
        return instance;
    }

    public UsuarioDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UsuarioDTO currentUser) {
        this.currentUser = currentUser;
    }

    public void cerrarSesion() {
        if (this.currentUser != null) {
            LOGGER.info("Sesión cerrada exitosamente para el usuario activo.");
            this.currentUser = null;
        }
    }

    private void verificarSesionActiva() throws IllegalStateException {
        if (currentUser == null) {
            LOGGER.warn("Intento de acceso a operaciones del sistema sin una sesión activa.");
            throw new IllegalStateException("No existe un usuario que haya iniciado sesión en el sistema.");
        }
    }
}

