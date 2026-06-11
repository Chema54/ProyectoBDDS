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

    @FXML
    private ComboBox<UsuarioDTO> cbBuscarUsuario;
    @FXML
    private PasswordField txtNuevaPassword;
    @FXML
    private ComboBox<UsuarioRol> cbRol;
    @FXML
    private ComboBox<String> cbEstado;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbEstado.setItems(FXCollections.observableArrayList("Activo", "Inactivo"));
        cargarListas();
        cbBuscarUsuario.valueProperty().addListener((obs, oldVal, usuarioSeleccionado) -> {
            if (usuarioSeleccionado != null) {
                cbRol.setValue(usuarioSeleccionado.getRol());
                cbEstado.setValue(usuarioSeleccionado.isTieneAcceso() ? "Activo" : "Inactivo");
                txtNuevaPassword.clear();
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
            String passwordFinal = txtNuevaPassword.getText().isEmpty()
                    ? usuarioOriginal.getContraseña()
                    : UsuarioDTO.getGeneratedHashedPassword(txtNuevaPassword.getText());

            boolean acceso = cbEstado.getValue().equals("Activo");

            UsuarioDTO usuarioActualizado = new UsuarioDTO(
                    usuarioOriginal.getIdEmpleado(),
                    usuarioOriginal.getUsuario(),
                    passwordFinal,
                    cbRol.getValue(),
                    acceso
            );

            usuarioDAO.updateOne(usuarioActualizado);

            Utilidades.mostrarAlertaSimple("Éxito", "Cuenta de sistema actualizada correctamente.", Alert.AlertType.INFORMATION);

            cbBuscarUsuario.setValue(null);
            txtNuevaPassword.clear();
            cargarListas();

        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo actualizar el usuario en la Base de Datos.", Alert.AlertType.ERROR);
        }
    }
}
