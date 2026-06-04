package main.application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
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

public class FXMLModificarDepartamentoController implements Initializable {

    @FXML
    private ComboBox<SucursalDTO> cbSucursal;
    @FXML
    private ComboBox<DepartamentoDTO> cbDepartamento;
    @FXML
    private ComboBox<EmpleadoDTO> cbEncargado;

    private SucursalDAO sucursalDAO = new SucursalDAO();
    private DepartamentoDAO deptoDAO = new DepartamentoDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private List<EmpleadoDTO> todosLosEmpleados;
    private List<DepartamentoDTO> todosLosDeptos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatosBase();
        configurarListeners();
    }

    private void cargarDatosBase() {
        try {
            cbSucursal.setItems(FXCollections.observableArrayList(sucursalDAO.getAll()));
            todosLosEmpleados = empleadoDAO.getAll();
            todosLosDeptos = deptoDAO.getAll();
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar los datos", Alert.AlertType.ERROR);
        }
    }

    private void configurarListeners() {
        // Al seleccionar sucursal, cargamos solo sus departamentos y sus empleados
        cbSucursal.valueProperty().addListener((obs, oldV, sucursal) -> {
            if (sucursal != null) {
                // 1. Filtrar Departamentos de esta sucursal
                List<DepartamentoDTO> deptosValidos = todosLosDeptos.stream()
                        .filter(d -> d.getIDSucursal() == sucursal.getIDSucursal())
                        .collect(Collectors.toList());

                // 2. Extraer los IDs de esos departamentos
                List<Integer> idsDeptosValidos = deptosValidos.stream()
                        .map(DepartamentoDTO::getIDDepartamento).collect(Collectors.toList());

                // 3. Filtrar Empleados que pertenecen a esos departamentos (SOLO DE ESTA SUCURSAL)
                List<EmpleadoDTO> empleadosValidos = todosLosEmpleados.stream()
                        .filter(e -> idsDeptosValidos.contains(e.getIDDepartamento()))
                        .collect(Collectors.toList());

                cbDepartamento.setItems(FXCollections.observableArrayList(deptosValidos));
                cbEncargado.setItems(FXCollections.observableArrayList(empleadosValidos));

                cbDepartamento.setDisable(false);
                cbEncargado.setDisable(false);
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
            // Se asume que tu método updateOne de DepartamentoDAO actualiza el id_encargado
            deptoSeleccionado.setIdEncargado(encargadoSeleccionado.getIDEmpleado());
            deptoDAO.updateOne(deptoSeleccionado);
            Utilidades.mostrarAlertaSimple("Éxito", "Encargado asignado correctamente.", Alert.AlertType.INFORMATION);
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se pudo actualizar el departamento.", Alert.AlertType.ERROR);
        }
    }
}
