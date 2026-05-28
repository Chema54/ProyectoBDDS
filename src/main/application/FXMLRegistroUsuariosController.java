/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.application;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author leninrevan
 */
public class FXMLRegistroUsuariosController implements Initializable {

    @FXML
    private TextField tfNombreUsuario;
    @FXML
    private ComboBox<?> cbRol;
    @FXML
    private PasswordField pwPassword;

    // aqui seria rol dto para cargar el combobox de rolesprivate ObservableList<UsuarioRol> roles;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    

    @FXML
    private void btnGuardar(ActionEvent event) {
        
    }
    /**
    private void cargarInformacionFacultades(){
        try{
            facultades = FXCollections.observableArrayList();
            List<Facultad> facultadesBD = CatalogoDAO.obtenerFacultades();
            facultades.addAll(facultadesBD);
            cbFacultad.setItems(facultades);
        }catch (NullPointerException ex){
            Utilidades.mostrarAlertaSimple("Error nulo", "Apareció un error no inicializado", Alert.AlertType.ERROR);
        }catch (SQLException ex){
            Utilidades.mostrarAlertaSimple("Error al consultar la info de las facultades", "Ocurrió un problema con la base de datos, intente de nuevo más tarde", Alert.AlertType.ERROR);
        }
    }
    
    private void configurarSeleccionarFacultad(){
        cbFacultad.valueProperty().addListener(new ChangeListener<Facultad>(){
            @Override
            public void changed(ObservableValue<? extends Facultad> observable, Facultad oldValue, Facultad newValue) {
                if(newValue != null){
                    cargarInformacionCarreras(newValue.getIdFacultad());
                }
            }
            
        });
    }
    */
}
