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
import javafx.scene.control.TextField;
import main.business.dao.EmpleadoDAO;
import main.business.dao.UsuarioDAO;
import main.business.dto.EmpleadoDTO;
import main.business.dto.UsuarioDTO;
import main.business.dto.enumeraiones.UsuarioRol;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLRegistroUsuariosController implements Initializable {

    @FXML private ComboBox<EmpleadoDTO> cbEmpleado; // ¡Asegúrate de agregar este ComboBox en tu FXML!
    @FXML private TextField tfNombreUsuario;
    @FXML private ComboBox<UsuarioRol> cbRol;
    @FXML private PasswordField pwPassword;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCombos();
    }    

    private void cargarCombos() {
        try {
            // Cargar los empleados para ligarlos a la cuenta
            cbEmpleado.setItems(FXCollections.observableArrayList(empleadoDAO.getAll()));
            
            // Cargar todos los roles del Enum
            cbRol.setItems(FXCollections.observableArrayList(UsuarioRol.values()));
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar los catálogos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        if (cbEmpleado.getValue() == null || cbRol.getValue() == null || tfNombreUsuario.getText().isEmpty() || pwPassword.getText().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // 1. Construimos el DTO con la contraseña en Texto Plano (el DAO se encargará de cifrarla)
            UsuarioDTO nuevoUsuario = new UsuarioDTO(
                cbEmpleado.getValue().getIDEmpleado(),
                tfNombreUsuario.getText().trim(),
                pwPassword.getText(),
                cbRol.getValue(),
                true // tieneAcceso = true por defecto al crearlo
            );

            // 2. Mandamos a guardar a BD (Y crear en MariaDB)
            usuarioDAO.createOne(nuevoUsuario);

            Utilidades.mostrarAlertaSimple("Éxito", "Usuario de Sistema y Motor de Base de Datos creado correctamente.", Alert.AlertType.INFORMATION);
            
            // Limpiar formulario
            cbEmpleado.setValue(null);
            cbRol.setValue(null);
            tfNombreUsuario.clear();
            pwPassword.clear();

        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "Fallo al crear el usuario. Es posible que el nombre ya exista.", Alert.AlertType.ERROR);
        }
    }
}