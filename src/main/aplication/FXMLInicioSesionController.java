package main.aplication;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btn_IniciarSesion(ActionEvent event) {
        
    }

    @FXML
    private void btn_Salir(ActionEvent event) {
        System.exit(0);
    }
    
}
