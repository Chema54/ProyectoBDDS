package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import main.business.dto.SucursalDTO;
import main.business.dao.SucursalDAO;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLModificarSucursalController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(FXMLModificarSucursalController.class);

    @FXML private TextField tfNombreSucursal;
    @FXML private TextField tfDireccionSucursal;

    private SucursalDAO sucursalDAO;
    private SucursalDTO sucursalEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sucursalDAO = new SucursalDAO();
    }

    public void inicializarDatos(SucursalDTO sucursal) {
        this.sucursalEdicion = sucursal;
        
        if (sucursalEdicion != null) {
            tfNombreSucursal.setText(sucursalEdicion.getNombre());
            tfDireccionSucursal.setText(sucursalEdicion.getDireccion());
        }
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        if (tfNombreSucursal.getText().trim().isEmpty() || tfDireccionSucursal.getText().trim().isEmpty()) {
            Utilidades.mostrarAlertaSimple(
                "Campos vacíos", 
                "El Nombre y la Dirección de la sucursal son obligatorios para actualizar.", 
                Alert.AlertType.WARNING
            );
            return;
        }
        
        actualizarSucursal();
    }

    private void actualizarSucursal() {
        try {
            SucursalDTO sucursalActualizada = new SucursalDTO.SucursalBuilder()
                    .setIDSucursal(sucursalEdicion.getIDSucursal())
                    .setNombre(tfNombreSucursal.getText().trim())
                    .setDireccion(tfDireccionSucursal.getText().trim())
                    .build();
            sucursalDAO.updateOne(sucursalActualizada);
            
            Utilidades.mostrarAlertaSimple(
                "Éxito", 
                "Sucursal modificada correctamente.", 
                Alert.AlertType.INFORMATION
            );
            
            cerrarVentana();
            
        } catch (UserDisplayableException ex) {
            Utilidades.mostrarAlertaSimple(
                "Error", 
                ex.getMessage() != null ? ex.getMessage() : "No ha sido posible actualizar la sucursal.", 
                Alert.AlertType.ERROR
            );
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) tfNombreSucursal.getScene().getWindow();
        stage.close();
    }
}
