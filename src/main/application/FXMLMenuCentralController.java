/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import main.common.Utilidades;

/**
 * FXML Controller class
 *
 * @author leninrevan
 */
public class FXMLMenuCentralController implements Initializable {
    
    Utilidades utilidades = new Utilidades();
    @FXML
    private StackPane stackPane_VistaCentral;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void irModificarUsuarios(ActionEvent event) {
    }

    @FXML
    private void irMostrarUsuarios(ActionEvent event) {
        utilidades.abrirNuevaPantalla("/main/resources/gui/FXMLMostrarUsuariosView.fxml", stackPane_VistaCentral);
    }

    @FXML
    private void irRegistrarUsuarios(ActionEvent event) {
        utilidades.abrirNuevaPantalla("/main/resources/gui/FXMLRegistroUsuariosView.fxml", stackPane_VistaCentral);
    }

    @FXML
    private void irRegistrarEmpleados(ActionEvent event) {
        utilidades.abrirNuevaPantalla("/main/resources/gui/FXMLRegistrarEmpleadosView.fxml", stackPane_VistaCentral);        
    }

    @FXML
    private void irModificarEmpleados(ActionEvent event) {
        utilidades.abrirNuevaPantalla("/main/resources/gui/FXMLModificarEmpleadosView.fxml", stackPane_VistaCentral);                
    }

    @FXML
    private void irMostrarEmpleados(ActionEvent event) {
    }

    
}
