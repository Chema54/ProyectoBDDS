package main.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import main.business.dao.DepartamentoDAO;
import main.business.dao.EmpleadoDAO;
import main.business.dao.UsuarioDAO;
import main.business.dto.DepartamentoDTO;
import main.business.dto.EmpleadoDTO;
import main.business.dto.UsuarioDTO;
import main.common.SesionGlobal;
import main.common.UserDisplayableException;
import static main.common.Utilidades.mostrarAlertaSimple;

public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tf_usuario;
    @FXML
    private PasswordField tf_password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void btn_IniciarSesion(ActionEvent event) {
        String usernameInput = tf_usuario.getText().trim();
        String passwordInput = tf_password.getText();

        if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
            mostrarAlertaSimple("Error", "Te falta rellenar ambos campos", Alert.AlertType.ERROR);
            return;
        }

        try {
            main.database.DBConnector.getInstance().resetCredentials();

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            UsuarioDTO usuario = usuarioDAO.getOne(usernameInput);

            if (usuario == null || !usuario.isTieneAcceso()) {
                mostrarAlertaSimple("Error", "No es posible acceder. Credenciales o estado incorrectos.", Alert.AlertType.ERROR);
                return;
            }

            if (usuario.hasPasswordMatch(passwordInput + "@Password")) {
                try {
                    EmpleadoDAO empleadoDAO = new EmpleadoDAO();
                    DepartamentoDAO deptoDAO = new DepartamentoDAO();

                    int idEmpleadoAsociado = usuario.getIdEmpleado();
                    EmpleadoDTO empleado = empleadoDAO.getOne(idEmpleadoAsociado);
                    DepartamentoDTO departamento = deptoDAO.getOne(empleado.getIDDepartamento());

                    SesionGlobal.getInstance().iniciarSesion(
                            idEmpleadoAsociado,
                            departamento.getIDSucursal(),
                            usuario.getRol().name()
                    );
                } catch (Exception e) {
                    mostrarAlertaSimple("Error Crítico", "Fallo al rastrear la sucursal del empleado en la base de datos.", Alert.AlertType.ERROR);
                    return;
                }
                try {
                    main.database.DBConnector.getInstance().changeCredentials(usernameInput, passwordInput);
                    try (java.sql.Connection testCon = main.database.DBConnector.getInstance().getConnection()) {
                        System.out.println("Acceso nativo a MariaDB concedido para: " + usernameInput);
                    }
                } catch (Exception e) {
                    mostrarAlertaSimple("Bloqueo de Motor", "MariaDB rechazó tu usuario nativo. ¿Se creó correctamente en la base de datos?", Alert.AlertType.ERROR);
                    main.database.DBConnector.getInstance().resetCredentials();
                    return;
                }

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/gui/FXMLMenuCentralView.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) tf_usuario.getScene().getWindow();

                    stage.setTitle("Sistema Global Finance - Menú Central");
                    stage.setScene(new Scene(root));
                    stage.setResizable(true);
                    stage.setMaximized(true);

                } catch (IOException e) {
                    mostrarAlertaSimple("Error", "No se pudo cargar la vista del menú central.", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }

            } else {
                mostrarAlertaSimple("Error", "Credenciales incorrectas", Alert.AlertType.ERROR);
            }

        } catch (UserDisplayableException e) {
            mostrarAlertaSimple("Error", "Error del sistema al conectar con la base de datos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_Salir(ActionEvent event) {
        System.exit(0);
    }
}
