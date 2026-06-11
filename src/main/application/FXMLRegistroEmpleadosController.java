package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.business.dao.DepartamentoDAO;
import main.business.dao.EmpleadoDAO;
import main.business.dao.SucursalDAO;
import main.business.dto.DepartamentoDTO;
import main.business.dto.EmpleadoDTO;
import main.business.dto.SucursalDTO;
import main.business.dto.UsuarioDTO;
import main.common.SesionGlobal;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLRegistroEmpleadosController implements Initializable {

    @FXML private TextField tfNombreEmpleado;
    @FXML private TextField tfApellidosEmpleado;
    @FXML private TextField tfNumPersonal;
    @FXML private TextField tfTelefono;
    @FXML private TextField tfCorreo;
    @FXML private ComboBox<SucursalDTO> cbSucursal;
    @FXML private ComboBox<DepartamentoDTO> cbDepartamento;

    private ObservableList<SucursalDTO> sucursales;
    private ObservableList<DepartamentoDTO> departamentos;
    private SucursalDAO sucursalesDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sucursalesDAO = new SucursalDAO();
        configurarSeleccionarSucursal();
        cargarInformacionSucursales();
        
        verificarRolUsuario();
    }

    private void verificarRolUsuario() {
        SesionGlobal sesion = SesionGlobal.getInstance();
        String rolUsuario = sesion.getRolActual();
        int idSucursalAsignada = sesion.getIdSucursalActual();
        if (rolUsuario != null && "SUCURSAL".equalsIgnoreCase(rolUsuario.trim())) {
            if (sucursales != null && !sucursales.isEmpty()) {
                SucursalDTO sucursalAsignada = sucursales.stream()
                        .filter(s -> s.getIDSucursal() == idSucursalAsignada)
                        .findFirst()
                        .orElse(null);

                if (sucursalAsignada != null) {
                    cbSucursal.getSelectionModel().select(sucursalAsignada);
                    cbSucursal.setDisable(true);
                }
            }
        }
    }
    
    @FXML
    private void btnGuardar(ActionEvent event) {
        if (tfNombreEmpleado.getText().trim().isEmpty() || tfApellidosEmpleado.getText().trim().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Campos vacíos", "El Nombre y Apellido son obligatorios.", Alert.AlertType.WARNING);
            return;
        }
        if (cbSucursal.getSelectionModel().getSelectedItem() == null || cbDepartamento.getSelectionModel().getSelectedItem() == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Debes seleccionar Sucursal y Departamento.", Alert.AlertType.WARNING);
            return;
        }
        guardarEmpleado();
    }

    private void cargarInformacionSucursales() {
        try {
            sucursales = FXCollections.observableArrayList(sucursalesDAO.getAll());
            cbSucursal.setItems(sucursales);
        } catch (Exception ex) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar las sucursales.", Alert.AlertType.ERROR);
        }
    }

    private void configurarSeleccionarSucursal() {
        cbSucursal.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cargarInformacionDepartamentos(newValue.getIDSucursal());
            }
        });
    }

    private void cargarInformacionDepartamentos(int idSucursal) {
        try {
            departamentos = FXCollections.observableArrayList(DepartamentoDAO.obtenerDepartamentoPorSucursal(idSucursal));
            cbDepartamento.setItems(departamentos);
        } catch (Exception ex) {
            Utilidades.mostrarAlertaSimple("Error", "Problema al cargar departamentos.", Alert.AlertType.ERROR);
        } 
    }
    
    private void guardarEmpleado(){
        try {
            EmpleadoDTO empleado = new EmpleadoDTO.EmpleadoBuilder()
                    .setNombre(tfNombreEmpleado.getText().trim())
                    .setApellidos(tfApellidosEmpleado.getText().trim())
                    .setIDDepartamento(cbDepartamento.getSelectionModel().getSelectedItem().getIDDepartamento())
                    .setNumeroPersonal(tfNumPersonal.getText().trim())
                    .setTelefono(tfTelefono.getText().trim())
                    .setCorreo(tfCorreo.getText().trim())
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
        tfNombreEmpleado.clear();
        tfApellidosEmpleado.clear();
        tfNumPersonal.clear();
        tfTelefono.clear();
        tfCorreo.clear();
        cbDepartamento.getSelectionModel().clearSelection();
        String rolUsuario = SesionGlobal.getInstance().getRolActual();
        if (rolUsuario == null || !"SUCURSAL".equalsIgnoreCase(rolUsuario)) {
            cbSucursal.getSelectionModel().clearSelection();        
        }
    }
}