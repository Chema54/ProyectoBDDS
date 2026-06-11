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
import main.business.dao.DepartamentoDAO;
import main.business.dao.EmpleadoDAO;
import main.business.dao.SucursalDAO;
import main.business.dto.DepartamentoDTO;
import main.business.dto.EmpleadoDTO;
import main.business.dto.SucursalDTO;
import main.common.UserDisplayableException;
import main.common.Utilidades;
import main.common.SesionGlobal;

public class FXMLModificarDepartamentoController implements Initializable {

    @FXML private ComboBox<SucursalDTO> cbSucursal;
    @FXML private ComboBox<DepartamentoDTO> cbDepartamento;
    @FXML private ComboBox<EmpleadoDTO> cbEncargado;

    private SucursalDAO sucursalDAO = new SucursalDAO();
    private DepartamentoDAO deptoDAO = new DepartamentoDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    
    private List<EmpleadoDTO> todosLosEmpleados;
    private List<DepartamentoDTO> todosLosDeptos;
    private ObservableList<SucursalDTO> listaSucursales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarListeners();
        cargarDatosBase();
        verificarRolUsuario();
    }

    private void cargarDatosBase() {
        try {
            listaSucursales = FXCollections.observableArrayList(sucursalDAO.getAll());
            cbSucursal.setItems(listaSucursales);
            
            todosLosEmpleados = empleadoDAO.getAll();
            todosLosDeptos = deptoDAO.getAll();
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar los datos", Alert.AlertType.ERROR);
        }
    }

    private void verificarRolUsuario() {
        SesionGlobal sesion = SesionGlobal.getInstance();
        String rolUsuario = sesion.getRolActual();
        int idSucursalAsignada = sesion.getIdSucursalActual();

        if (rolUsuario != null && "SUCURSAL".equalsIgnoreCase(rolUsuario.trim())) {
            if (listaSucursales != null && !listaSucursales.isEmpty()) {

                SucursalDTO sucursalAsignada = listaSucursales.stream()
                        .filter(s -> s.getIDSucursal() == idSucursalAsignada)
                        .findFirst()
                        .orElse(null);

                if (sucursalAsignada != null) {
                    cbDepartamento.setPromptText("Selecciona un departamento...");
                    cbEncargado.setPromptText("Selecciona al nuevo encargado...");
                    cbSucursal.getSelectionModel().select(sucursalAsignada);
                    cbSucursal.setDisable(true);
                    cbDepartamento.setDisable(false);
                    cbEncargado.setDisable(false);
                }
            }
        }
    }

    private void configurarListeners() {
        cbSucursal.valueProperty().addListener((obs, oldV, sucursal) -> {
            if (sucursal != null) {
                List<DepartamentoDTO> deptosValidos = todosLosDeptos.stream()
                        .filter(d -> d.getIDSucursal() == sucursal.getIDSucursal())
                        .collect(Collectors.toList());
                        
                List<Integer> idsDeptosValidos = deptosValidos.stream()
                        .map(DepartamentoDTO::getIDDepartamento)
                        .collect(Collectors.toList());
                        
                List<EmpleadoDTO> empleadosValidos = todosLosEmpleados.stream()
                        .filter(e -> idsDeptosValidos.contains(e.getIDDepartamento()))
                        .collect(Collectors.toList());

                cbDepartamento.setItems(FXCollections.observableArrayList(deptosValidos));
                cbEncargado.setItems(FXCollections.observableArrayList(empleadosValidos));

                cbDepartamento.setDisable(false);
                cbEncargado.setDisable(false);
            } else {
                cbDepartamento.setDisable(true);
                cbEncargado.setDisable(true);
            }
        });
    }

    @FXML
    private void btnGuardarEncargado(ActionEvent event) {
        DepartamentoDTO deptoSeleccionado = cbDepartamento.getValue();
        EmpleadoDTO encargadoSeleccionado = cbEncargado.getValue();

        if (deptoSeleccionado == null || encargadoSeleccionado == null) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Selecciona un departamento y un encargado.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // RECOMENDACIÓN ARQUITECTÓNICA: Si tus DTOs usan el patrón Builder (como Sucursal o Articulo),
            // lo ideal es reconstruir el objeto modificado en lugar de usar un setter directo (si es que no existe).
            // Si tu DepartamentoDTO sí tiene .setIdEncargado(), esto funcionará perfecto:
            deptoSeleccionado.setIdEncargado(encargadoSeleccionado.getIDEmpleado());
            
            deptoDAO.updateOne(deptoSeleccionado);
            Utilidades.mostrarAlertaSimple("Éxito", "Encargado asignado correctamente.", Alert.AlertType.INFORMATION);
            
            limpiarVentana();
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo actualizar el departamento.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarVentana() {
        cbDepartamento.getSelectionModel().clearSelection();
        cbEncargado.getSelectionModel().clearSelection();
        
        // Al igual que con los empleados, no deseleccionamos la sucursal si es encargado
        String rolUsuario = SesionGlobal.getInstance().getRolActual();
        if (rolUsuario == null || !"SUCURSAL".equalsIgnoreCase(rolUsuario.trim())) {
            cbSucursal.getSelectionModel().clearSelection();
        }
    }
}
