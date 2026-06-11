package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.business.dao.EmpleadoDAO;
import main.business.dao.SucursalDAO;
import main.business.dto.DirectorioEmpleadoDTO;
import main.business.dto.SucursalDTO;
import main.common.SesionGlobal;
import main.common.UserDisplayableException;

public class FXMLMostrarEmpleadosController implements Initializable {

    @FXML
    private ComboBox<SucursalDTO> cbSucursalFiltro;
    @FXML
    private Button btnLimpiar;

    @FXML
    private TableView<DirectorioEmpleadoDTO> tablaEmpleados;
    @FXML
    private TableColumn<DirectorioEmpleadoDTO, String> colNumPersonal;
    @FXML
    private TableColumn<DirectorioEmpleadoDTO, String> colNombre;
    @FXML
    private TableColumn<DirectorioEmpleadoDTO, String> colApellidos;
    @FXML
    private TableColumn<DirectorioEmpleadoDTO, String> colDepartamento;
    @FXML
    private TableColumn<DirectorioEmpleadoDTO, String> colSucursal;
    @FXML
    private TableColumn<DirectorioEmpleadoDTO, String> colUsuario;

    private ObservableList<DirectorioEmpleadoDTO> listaOriginal;
    private SucursalDAO sucursalDAO = new SucursalDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarDatos();
        configurarFiltroYSeguridad();
    }

    private void configurarColumnas() {
        colNumPersonal.setCellValueFactory(new PropertyValueFactory<>("numeroPersonal"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colSucursal.setCellValueFactory(new PropertyValueFactory<>("sucursal"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
    }

    private void cargarDatos() {
        try {
            cbSucursalFiltro.setItems(FXCollections.observableArrayList(sucursalDAO.getAll()));
            listaOriginal = FXCollections.observableArrayList(empleadoDAO.getVistaDirectorio());
            tablaEmpleados.setItems(listaOriginal);

        } catch (UserDisplayableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void configurarFiltroYSeguridad() {
        cbSucursalFiltro.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                tablaEmpleados.setItems(listaOriginal);
            } else {
                FilteredList<DirectorioEmpleadoDTO> filtrada = new FilteredList<>(listaOriginal, emp
                        -> emp.getIdSucursal() == newValue.getIDSucursal()
                );
                tablaEmpleados.setItems(filtrada);
            }
        });
        String rol = SesionGlobal.getInstance().getRolActual();
        if (rol != null && rol.equals("SUCURSAL")) {
            int idMiSucursal = SesionGlobal.getInstance().getIdSucursalActual();

            for (SucursalDTO suc : cbSucursalFiltro.getItems()) {
                if (suc.getIDSucursal() == idMiSucursal) {
                    cbSucursalFiltro.setValue(suc);
                    break;
                }
            }
            cbSucursalFiltro.setDisable(true);
            btnLimpiar.setDisable(true);
        }
    }

    @FXML
    private void btnLimpiarFiltro(ActionEvent event) {
        cbSucursalFiltro.setValue(null);
    }
}
