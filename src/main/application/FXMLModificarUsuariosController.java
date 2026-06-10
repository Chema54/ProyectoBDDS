package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import main.business.dao.UsuarioDAO;
import main.business.dto.UsuarioDTO;
import main.business.dto.enumeraiones.UsuarioRol;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLModificarUsuariosController implements Initializable {

    @FXML private ComboBox<UsuarioDTO> cbBuscarUsuario;
    @FXML private PasswordField txtNuevaPassword;
    @FXML private ComboBox<UsuarioRol> cbRol;
    @FXML private ComboBox<String> cbEstado;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Llenamos la lista de Estados (Activo/Inactivo)
        cbEstado.setItems(FXCollections.observableArrayList("Activo", "Inactivo"));
        cargarListas();
        
        // Cuando seleccionas una cuenta, se carga su Rol y su Estado
        cbBuscarUsuario.valueProperty().addListener((obs, oldVal, usuarioSeleccionado) -> {
            if (usuarioSeleccionado != null) {
                cbRol.setValue(usuarioSeleccionado.getRol());
                cbEstado.setValue(usuarioSeleccionado.isTieneAcceso() ? "Activo" : "Inactivo");
                txtNuevaPassword.clear(); // Limpiamos la contraseña por seguridad
            }
        });
    }    

    private void cargarListas() {
        try {
            cbBuscarUsuario.setItems(FXCollections.observableArrayList(usuarioDAO.getAll()));
            cbRol.setItems(FXCollections.observableArrayList(UsuarioRol.values()));
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "Fallo al cargar usuarios.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnGuardarCambios(ActionEvent event) {
        UsuarioDTO usuarioOriginal = cbBuscarUsuario.getValue();
        
        if (usuarioOriginal == null || cbRol.getValue() == null || cbEstado.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Seleccione un usuario y complete los campos obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Si el usuario escribió una nueva contraseña, la ciframos con Bcrypt. 
            // Si la dejó en blanco, conservamos la que ya tenía en la base de datos.
            String passwordFinal = txtNuevaPassword.getText().isEmpty() ? 
                                   usuarioOriginal.getContraseña() : 
                                   UsuarioDTO.getGeneratedHashedPassword(txtNuevaPassword.getText());

            boolean acceso = cbEstado.getValue().equals("Activo");

            UsuarioDTO usuarioActualizado = new UsuarioDTO(
                    usuarioOriginal.getIdEmpleado(),
                    usuarioOriginal.getUsuario(),
                    passwordFinal,
                    cbRol.getValue(),
                    acceso
            );

            // Mandamos a la base de datos (Esto actualizará su estado y su contraseña)
            usuarioDAO.updateOne(usuarioActualizado);
            
            // Opcional: Como extra, aquí podrías ejecutar los comandos DDL (GRANT) si les cambias el rol,
            // pero para los alcances del sistema, con actualizar la tabla lógica basta.
            
            Utilidades.mostrarAlertaSimple("Éxito", "Cuenta de sistema actualizada correctamente.", Alert.AlertType.INFORMATION);
            
            cbBuscarUsuario.setValue(null);
            txtNuevaPassword.clear();
            cargarListas();
            
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo actualizar el usuario en la Base de Datos.", Alert.AlertType.ERROR);
        }
    }
}