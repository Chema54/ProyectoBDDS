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
import main.business.dao.UsuarioDAO;
import main.business.dto.UsuarioDTO;
import main.business.dto.enumeraiones.UsuarioRol;
import main.common.UserDisplayableException;
import static main.common.Utilidades.mostrarAlertaSimple;

/**
 * FXML Controller class
 *
 * @author leninrevan
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tf_usuario;
    @FXML
    private PasswordField tf_password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
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
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            UsuarioDTO usuario = usuarioDAO.getOne(usernameInput);

            if (usuario == null || !usuario.isTieneAcceso()) {
                mostrarAlertaSimple("Error", "No es posible acceder", Alert.AlertType.ERROR);
                return;
            }

            if (usuario.hasPasswordMatch(passwordInput + "@Password")) {
                
                if (usuario.getRol() == UsuarioRol.CENTRAL) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/gui/FXMLMenuCentralView.fxml"));
                        Parent root = loader.load();
                        
                        Stage stage = new Stage();
                        stage.setTitle("Sistema Global Finance - Menú Central");
                        stage.setScene(new Scene(root));
                        
                        Stage currentStage = (Stage) tf_usuario.getScene().getWindow();
                        currentStage.close();
                        stage.setResizable(true);
                        stage.setMaximized(true);
                        stage.show();
                        
                    } catch (IOException e) {
                        mostrarAlertaSimple("Error", "No se pudo cargar la vista del menú central.", Alert.AlertType.ERROR);
                        e.printStackTrace();
                    }
                } else {
                    mostrarAlertaSimple("Información", "Acceso correcto. Vista no disponible para este rol.", Alert.AlertType.INFORMATION);
                }
                
            } else {
                mostrarAlertaSimple("Error", "Credenciales incorrectas", Alert.AlertType.ERROR);
            }

        } catch (UserDisplayableException e) {
            mostrarAlertaSimple("Error", "Error del sistema", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btn_Salir(ActionEvent event) {
        System.exit(0);
    }
}