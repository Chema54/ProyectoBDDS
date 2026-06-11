package main.application;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.business.dao.DepartamentoDAO;
import main.business.dao.EmpleadoDAO;
import main.business.dao.SucursalDAO;
import main.business.dao.UsuarioDAO;
import main.business.dto.DepartamentoDTO;
import main.business.dto.EmpleadoDTO;
import main.business.dto.SucursalDTO;
import main.business.dto.UsuarioDTO;
import main.common.UserDisplayableException;

public class FXMLMostrarEmpleadosController implements Initializable {

    @FXML private ComboBox<SucursalDTO> cbSucursalFiltro;
    @FXML private TableView<EmpleadoDTO> tablaEmpleados;
    @FXML private TableColumn<EmpleadoDTO, Integer> colId;
    @FXML private TableColumn<EmpleadoDTO, String> colNombre;
    @FXML private TableColumn<EmpleadoDTO, String> colApellidos;
    @FXML private TableColumn<EmpleadoDTO, String> colDepartamento;
    @FXML private TableColumn<EmpleadoDTO, String> colIdSucursal;
    @FXML private TableColumn<EmpleadoDTO, String> colSucursal;
    @FXML private TableColumn<EmpleadoDTO, String> colUsuario;

    private ObservableList<EmpleadoDTO> listaTodosLosEmpleados = FXCollections.observableArrayList();
    private SucursalDAO sucursalDAO = new SucursalDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private DepartamentoDAO deptoDAO = new DepartamentoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    private Map<Integer, DepartamentoDTO> mapaDepartamentos = new HashMap<>();
    private Map<Integer, SucursalDTO> mapaSucursales = new HashMap<>();
    private Map<Integer, String> mapaUsuarios = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCachesAuxiliares();
        configurarColumnas();
        cargarSucursales();
        cargarTodosLosEmpleados();
        configurarFiltro();
    }

    private void cargarCachesAuxiliares() {
        try {
            for (DepartamentoDTO d : deptoDAO.getAll()) {
                mapaDepartamentos.put(d.getIDDepartamento(), d);
            }
            for (SucursalDTO s : sucursalDAO.getAll()) {
                mapaSucursales.put(s.getIDSucursal(), s);
            }
            for (UsuarioDTO u : usuarioDAO.getAll()) {
                mapaUsuarios.put(u.getIdEmpleado(), u.getUsuario());
            }
        } catch (UserDisplayableException e) {
            System.out.println("Error al cargar caches: " + e.getMessage());
        }
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("IDEmpleado"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

        colDepartamento.setCellValueFactory(cellData -> {
            DepartamentoDTO depto = mapaDepartamentos.get(cellData.getValue().getIDDepartamento());
            return new SimpleStringProperty(depto != null ? depto.getNombreDepartamento() : "Sin Depto");
        });

        colIdSucursal.setCellValueFactory(cellData -> {
             DepartamentoDTO depto = mapaDepartamentos.get(cellData.getValue().getIDDepartamento());
             return new SimpleStringProperty(depto != null ? String.valueOf(depto.getIDSucursal()) : "N/A");
        });

        colSucursal.setCellValueFactory(cellData -> {
            DepartamentoDTO depto = mapaDepartamentos.get(cellData.getValue().getIDDepartamento());
            if(depto != null) {
                SucursalDTO suc = mapaSucursales.get(depto.getIDSucursal());
                return new SimpleStringProperty(suc != null ? suc.getNombre() : "Sin Sucursal");
            }
            return new SimpleStringProperty("N/A");
        });

        colUsuario.setCellValueFactory(cellData -> {
            String nombreUsuario = mapaUsuarios.get(cellData.getValue().getIDEmpleado());
            return new SimpleStringProperty(nombreUsuario != null ? nombreUsuario : "Sin cuenta");
        });
    }

    private void cargarSucursales() {
        try {
            List<SucursalDTO> sucursalesBD = sucursalDAO.getAll();
            cbSucursalFiltro.setItems(FXCollections.observableArrayList(sucursalesBD));
        } catch (UserDisplayableException e) {
            System.out.println("Error al cargar sucursales: " + e.getMessage());
        }
    }

    private void cargarTodosLosEmpleados() {
        try {
            List<EmpleadoDTO> empleadosBD = empleadoDAO.getAll();
            listaTodosLosEmpleados.setAll(empleadosBD);
            tablaEmpleados.setItems(listaTodosLosEmpleados);
        } catch (UserDisplayableException e) {
            System.out.println("Error al cargar empleados: " + e.getMessage());
        }
    }

    private void configurarFiltro() {
        cbSucursalFiltro.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                tablaEmpleados.setItems(listaTodosLosEmpleados);
            } else {
                filtrarPorSucursal(newValue.getIDSucursal());
            }
        });
    }

    private void filtrarPorSucursal(int idSucursal) {
        try {
            List<DepartamentoDTO> deptosDeSucursal = DepartamentoDAO.obtenerDepartamentoPorSucursal(idSucursal);
            List<Integer> idsValidos = deptosDeSucursal.stream()
                    .map(DepartamentoDTO::getIDDepartamento)
                    .collect(Collectors.toList());

            ObservableList<EmpleadoDTO> empleadosFiltrados = FXCollections.observableArrayList();
            for (EmpleadoDTO emp : listaTodosLosEmpleados) {
                if (idsValidos.contains(emp.getIDDepartamento())) {
                    empleadosFiltrados.add(emp);
                }
            }
            tablaEmpleados.setItems(empleadosFiltrados);

        } catch (SQLException | NullPointerException | UserDisplayableException e) {
            System.out.println("Error al filtrar: " + e.getMessage());
        }
    }

    @FXML
    private void btnLimpiarFiltro(ActionEvent event) {
        cbSucursalFiltro.setValue(null);
    }
}