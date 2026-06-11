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
import javafx.scene.control.TextField;
import main.business.dao.SucursalDAO;
import main.business.dto.SucursalDTO;
import main.common.UserDisplayableException;
import main.common.Utilidades;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author josem
 */
public class FXMLRegistroSucursalController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(FXMLRegistroSucursalController.class);

    @FXML private TextField tfNombreSucursal;
    @FXML private TextField tfDireccionSucursal;

    private SucursalDAO sucursalDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sucursalDAO = new SucursalDAO();
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        if (tfNombreSucursal.getText().trim().isEmpty() || tfDireccionSucursal.getText().trim().isEmpty()) {
            Utilidades.mostrarAlertaSimple(
                "Campos vacíos", 
                "El Nombre y la Dirección de la sucursal son obligatorios.", 
                Alert.AlertType.WARNING
            );
            return;
        }
        
        guardarSucursal();
    }

    private void guardarSucursal() {
        try {
            SucursalDTO nuevaSucursal = new SucursalDTO.SucursalBuilder()
                    .setNombre(tfNombreSucursal.getText().trim())
                    .setDireccion(tfDireccionSucursal.getText().trim())
                    .build();

            sucursalDAO.createOne(nuevaSucursal);
            
            Utilidades.mostrarAlertaSimple(
                "Éxito", 
                "Sucursal registrada correctamente.", 
                Alert.AlertType.INFORMATION
            );
            
            limpiarVentana();
            
        } catch (UserDisplayableException ex) {
            Utilidades.mostrarAlertaSimple(
                "Error", 
                ex.getMessage() != null ? ex.getMessage() : "No ha sido posible crear la sucursal.", 
                Alert.AlertType.ERROR
            );
        }
    }

    private void limpiarVentana() {
        tfNombreSucursal.clear();
        tfDireccionSucursal.clear();
    }
}
