    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.common;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;

/**
 *
 * @author leninrevan
 */
public class Utilidades {
    public static void mostrarAlertaSimple(String titulo, String contenido, AlertType tipo){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setContentText(contenido);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    public void abrirNuevaPantalla(String rutaFXML, StackPane vista) {
        try {
            int totalPantallas = vista.getChildren().size();
            if (totalPantallas > 0) {
                Node pantallaActual = vista.getChildren().get(totalPantallas - 1);
                pantallaActual.setDisable(true);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent nuevaVista = loader.load();
            vista.getChildren().add(nuevaVista);

            nuevaVista.requestFocus();

        } catch (IOException | NullPointerException ex) {
            mostrarAlertaSimple("Error", "No se puede cargar esta vista", AlertType.ERROR);
        }
    }
    
    public void regresarPantallaAnterior(StackPane vista) {
        int totalPantallas = vista.getChildren().size();

        if (totalPantallas > 1) {
            vista.getChildren().remove(totalPantallas - 1);

            Node pantallaFondo = vista.getChildren().get(totalPantallas - 2);
            pantallaFondo.setDisable(false); 
            pantallaFondo.requestFocus();
        }
    }
}
