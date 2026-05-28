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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import main.business.dao.DepartamentoDAO;
import main.business.dao.EmpleadoDAO;
import main.business.dao.SucursalDAO;
import main.business.dto.DepartamentoDTO;
import main.business.dto.EmpleadoDTO;
import main.business.dto.SucursalDTO;
import main.common.UserDisplayableException;
import main.common.Utilidades;
import main.common.VentanaInterna;

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
    private ComboBox<SucursalDTO> cbSucursal;

    private ObservableList<SucursalDTO> sucursales;
    private ObservableList<DepartamentoDTO> departamentos;
    private SucursalDAO sucursalesDAO;
    @FXML
    private ComboBox<DepartamentoDTO> cbDepartamento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Tu inicialización normal
        sucursalesDAO = new SucursalDAO();
        cargarInformacionSucursales();
        configurarSeleccionarSucursal();
    }

    @FXML
    private void btnGuardar(ActionEvent event) {

        if (tfNombreEmpleado.getText().isEmpty() || tfApellidosEmpleado.getText().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Campos vacíos", "Debes llenar todos los campos.", Alert.AlertType.WARNING);
            return;
        }

        if (cbSucursal.selectionModelProperty().getValue() == null) {
            Utilidades.mostrarAlertaSimple( "Sucursal requerida", "Debes seleccionar una sucursal.",Alert.AlertType.WARNING);
            return;
        }

        if (cbDepartamento.selectionModelProperty().getValue() == null) {
            Utilidades.mostrarAlertaSimple("Departamento requerido", "Debes seleccionar un departamento.",Alert.AlertType.WARNING);
            return;
        }
        
        guardarEmpleado();
        
    }

    private void cargarInformacionSucursales() {
        try {
            sucursales = FXCollections.observableArrayList();
            List<SucursalDTO> sucursalesBD = sucursalesDAO.getAll();
            sucursales.addAll(sucursalesBD);
            cbSucursal.setItems(sucursales);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple("Error nulo", "Apareció un error no inicializado", Alert.AlertType.ERROR);
        } catch (UserDisplayableException ex) {
            Utilidades.mostrarAlertaSimple("Error", "Apareció un error", Alert.AlertType.ERROR);
        }
    }

    private void configurarSeleccionarSucursal() {
        cbSucursal.valueProperty().addListener(new ChangeListener<SucursalDTO>() {
            @Override
            public void changed(ObservableValue<? extends SucursalDTO> observable, SucursalDTO oldValue, SucursalDTO newValue) {
                if (newValue != null) {
                    cargarInformacionDepartamentos(newValue.getIDSucursal());
                }
            }

        });
    }

    private void cargarInformacionDepartamentos(int idSucursal) {
        try {
            departamentos = FXCollections.observableArrayList();
            List<DepartamentoDTO> departamentosBD = DepartamentoDAO.obtenerDepartamentoPorSucursal(idSucursal);
            departamentos.addAll(departamentosBD);
            cbDepartamento.setItems(departamentos);
        } catch (SQLException ex) {
            Utilidades.mostrarAlertaSimple("Error al cargar info de carreras", "Ocurrió un problema con la base de datos, intente de nuevo más tarde", Alert.AlertType.ERROR);
        } catch (NullPointerException ex) {
            Utilidades.mostrarAlertaSimple("Error nulo", "Apareció un error no inicializado", Alert.AlertType.ERROR);
        } catch (UserDisplayableException ex) {
            Utilidades.mostrarAlertaSimple("Error", "Apareció un error", Alert.AlertType.ERROR);
        } 
    }
    
    private void guardarEmpleado(){
        try {

            EmpleadoDTO empleado = new EmpleadoDTO.EmpleadoBuilder()
                    .setNombre(tfNombreEmpleado.getText())
                    .setApellidos(tfApellidosEmpleado.getText())
                    .setIDDepartamento(cbDepartamento.getSelectionModel().getSelectedItem().getIDDepartamento())
                    .build();

            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            empleadoDAO.createOne(empleado);
            Utilidades.mostrarAlertaSimple("Éxito", "Empleado registrado correctamente.", Alert.AlertType.INFORMATION);
            limpiarVentana();
        } catch (UserDisplayableException ex) {

            Utilidades.mostrarAlertaSimple("Error", "No se pudo registrar al empleado", Alert.AlertType.ERROR);
        }
    }
    private void limpiarVentana() {
        tfNombreEmpleado.setText("");
        tfApellidosEmpleado.setText("");
        cbDepartamento.selectionModelProperty().setValue(null);
        cbSucursal.selectionModelProperty().setValue(null);
        cargarInformacionSucursales();
        configurarSeleccionarSucursal();
    }
}
