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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import main.common.Utilidades;

/**
 * FXML Controller class
 *
 * @author leninrevan
 */
public class FXMLRegistroEmpleadosController implements Initializable {

    @FXML
    private TextField tfNombreEmpleado;
    @FXML
    private TextField tfApellidosEmpleado;
    @FXML
    private ComboBox<?> cbSucursal;
    @FXML
    private ComboBox<?> cbDepartamentok;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Tu inicialización normal
    }


    @FXML
    private void btnGuardar(ActionEvent event) {
    }
}