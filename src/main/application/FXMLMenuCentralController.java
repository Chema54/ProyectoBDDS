/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.application;

import main.common.VentanaInterna;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import main.common.Utilidades;

/**
 * FXML Controller class
 *
 * @author leninrevan
 */
public class FXMLMenuCentralController implements Initializable {
    
    Utilidades utilidades = new Utilidades();
    private StackPane stackPane_VistaCentral;
    @FXML
    private AnchorPane pane_Escritorio;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    



    @FXML
    private void irMostrarUsuarios(ActionEvent event) {
        utilidades.abrirNuevaPantalla("/main/resources/gui/FXMLMostrarUsuariosView.fxml", stackPane_VistaCentral);
    }

    @FXML
    private void irRegistrarUsuarios(ActionEvent event) {
        try {
            // 1. Cargas el FXML del formulario que quieres mostrar adentro
            Node formulario = FXMLLoader.load(getClass().getResource("/main/resources/gui/FXMLRegistroUsuariosView.fxml"));

            // 2. Lo envuelves en tu componente personalizado de ventana flotante
            VentanaInterna ventana = new VentanaInterna("Registrar Usuario", formulario);

            // 3. Posición inicial en el escritorio para que no se encimen todas arriba a la izquierda
            ventana.setLayoutX(50);
            ventana.setLayoutY(50);

            // 4. Lo agregas al AnchorPane/Pane central
            pane_Escritorio.getChildren().add(ventana);

        } catch (IOException ex) {
            System.out.println("Error al cargar el formulario: " + ex.getMessage());
        }
    }

    @FXML
    private void irRegistrarEmpleados(ActionEvent event) {
        try {
            // 1. Cargas el FXML del formulario que quieres mostrar adentro
            Node formulario = FXMLLoader.load(getClass().getResource("/main/resources/gui/FXMLRegistroEmpleadosView.fxml"));

            // 2. Lo envuelves en tu componente personalizado de ventana flotante
            VentanaInterna ventana = new VentanaInterna("Registrar Usuario", formulario);

            // 3. Posición inicial en el escritorio para que no se encimen todas arriba a la izquierda
            ventana.setLayoutX(50);
            ventana.setLayoutY(50);

            // 4. Lo agregas al AnchorPane/Pane central
            pane_Escritorio.getChildren().add(ventana);

        } catch (IOException ex) {
            System.out.println("Error al cargar el formulario: " + ex.getMessage());
        }
    }

    @FXML
    private void irModificarEmpleados(ActionEvent event) {
        try {
            // 1. Cargas el FXML del formulario que quieres mostrar adentro
            Node formulario = FXMLLoader.load(getClass().getResource("/main/resources/gui/FXMLModificarEmpleadosView.fxml"));

            // 2. Lo envuelves en tu componente personalizado de ventana flotante
            VentanaInterna ventana = new VentanaInterna("Registrar Usuario", formulario);

            // 3. Posición inicial en el escritorio para que no se encimen todas arriba a la izquierda
            ventana.setLayoutX(50);
            ventana.setLayoutY(50);

            // 4. Lo agregas al AnchorPane/Pane central
            pane_Escritorio.getChildren().add(ventana);

        } catch (IOException ex) {
            System.out.println("Error al cargar el formulario: " + ex.getMessage());
        }
    }

    @FXML
    private void irModificarUsuarios(ActionEvent event) {
    }

    @FXML
    private void irMostrarEmpleados(ActionEvent event) {
    }


    
}
