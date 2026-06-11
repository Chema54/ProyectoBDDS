package main.application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
import main.common.SesionGlobal;
import main.common.Utilidades;
import main.common.UserDisplayableException;

public class FXMLRegistroDepartamentoController implements Initializable {

    @FXML private ComboBox<SucursalDTO> cbSucursal;
    @FXML private ComboBox<EmpleadoDTO> cbEncargado;
    @FXML private TextField tfNombreDepto;

    private ObservableList<SucursalDTO> listaSucursales;
    private ObservableList<EmpleadoDTO> todosLosEmpleados;
    private final DepartamentoDAO deptoDAO = new DepartamentoDAO();
    private final SucursalDAO sucursalDAO = new SucursalDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    
    private List<DepartamentoDTO> todosLosDeptos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarListenerSucursal();
        cargarDatosIniciales();
        verificarRolUsuario();
    }

    private void cargarDatosIniciales() {
        try {
            listaSucursales = FXCollections.observableArrayList(sucursalDAO.getAll());
            cbSucursal.setItems(listaSucursales);
            todosLosDeptos = deptoDAO.getAll(); 
            todosLosEmpleados = FXCollections.observableArrayList(empleadoDAO.getAll());
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la información base.", Alert.AlertType.ERROR);
        }
    }

    private void verificarRolUsuario() {
        SesionGlobal sesion = SesionGlobal.getInstance();
        String rol = sesion.getRolActual();
        int idSucursal = sesion.getIdSucursalActual();

        if (rol != null && "SUCURSAL".equalsIgnoreCase(rol.trim())) {
            SucursalDTO asignada = listaSucursales.stream()
                    .filter(s -> s.getIDSucursal() == idSucursal)
                    .findFirst().orElse(null);

            if (asignada != null) {
                cbSucursal.getSelectionModel().select(asignada);
                cbSucursal.setDisable(true);
                cbEncargado.setPromptText("Selecciona un empleado de tu sucursal...");
            }
        } else {
            cbEncargado.setDisable(true);
            cbEncargado.setPromptText("Selecciona una sucursal primero...");
        }
    }

    private void configurarListenerSucursal() {
        cbSucursal.valueProperty().addListener((obs, oldV, sucursal) -> {
            if (sucursal != null) {
                try {
                    List<Integer> idsDeptosDeEstaSucursal = todosLosDeptos.stream()
                            .filter(d -> d.getIDSucursal() == sucursal.getIDSucursal())
                            .map(DepartamentoDTO::getIDDepartamento)
                            .collect(Collectors.toList());
                    List<EmpleadoDTO> empleadosDeEstaSucursal = todosLosEmpleados.stream()
                            .filter(e -> idsDeptosDeEstaSucursal.contains(e.getIDDepartamento()))
                            .collect(Collectors.toList());
                    cbEncargado.setItems(FXCollections.observableArrayList(empleadosDeEstaSucursal));
                    cbEncargado.setDisable(false);

                } catch (Exception e) {
                    cbEncargado.setDisable(true);
                }
            } else {
                cbEncargado.getItems().clear();
                cbEncargado.setDisable(true);
            }
        });
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        String nombre = tfNombreDepto.getText().trim();
        SucursalDTO sucursal = cbSucursal.getValue();
        EmpleadoDTO encargado = cbEncargado.getValue();

        if (nombre.isEmpty() || sucursal == null) {
            Utilidades.mostrarAlertaSimple("Campos incompletos", 
                "El nombre del departamento y la sucursal son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            DepartamentoDTO nuevoDepto = new DepartamentoDTO.DepartamentoBuilder()
                    .setNombreDepartamento(nombre)
                    .setIDSucursal(sucursal.getIDSucursal())
                    .setIdEncargado(encargado != null ? encargado.getIDEmpleado() : 0)
                    .build();

            deptoDAO.createOne(nuevoDepto);
            Utilidades.mostrarAlertaSimple("Éxito", "Departamento registrado correctamente.", Alert.AlertType.INFORMATION);
            limpiarVentana();
            
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo registrar el departamento.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarVentana() {
        tfNombreDepto.clear();
        cbEncargado.getSelectionModel().clearSelection();
        
        String rol = SesionGlobal.getInstance().getRolActual();
        if (rol == null || !"SUCURSAL".equalsIgnoreCase(rol.trim())) {
            cbSucursal.getSelectionModel().clearSelection();
            cbEncargado.setDisable(true);
        }
    } 
}