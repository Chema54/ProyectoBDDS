/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import main.common.Utilidades;

/**
 * FXML Controller class
 *
 * @author leninrevan
 */
public class FXMLMenuCentralController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void irAdministrarUsuarios(MouseEvent event) {
        Utilidades.mostrarAlertaSimple("MostrarAlerta", "Prototipando", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void irVerKARDEX(MouseEvent event) {
    }

    @FXML
    private void irVerFacturas(MouseEvent event) {
    }

    @FXML
    private void irAdministrarInventario(MouseEvent event) {
    }

    @FXML
    private void irAdministrarCatalogo(MouseEvent event) {
    }

    @FXML
    private void irVerEntradas(MouseEvent event) {
    }

    @FXML
    private void irVerSalidas(MouseEvent event) {
    }
    
}
