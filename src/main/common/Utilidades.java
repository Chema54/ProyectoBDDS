/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.common;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
}
