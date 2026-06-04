package main.application;

import java.net.URL;
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

public class FXMLMostrarUsuariosController implements Initializable {

    @FXML private ComboBox<SucursalDTO> cbSucursalFiltro;
    @FXML private TableView<UsuarioDTO> tablaUsuarios;
    @FXML private TableColumn<UsuarioDTO, String> colUsername;
    @FXML private TableColumn<UsuarioDTO, String> colEmpleado;
    @FXML private TableColumn<UsuarioDTO, String> colSucursal;
    @FXML private TableColumn<UsuarioDTO, String> colRol;
    @FXML private TableColumn<UsuarioDTO, String> colAcceso;

    private ObservableList<UsuarioDTO> listaTodosLosUsuarios = FXCollections.observableArrayList();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private DepartamentoDAO deptoDAO = new DepartamentoDAO();
    private SucursalDAO sucursalDAO = new SucursalDAO();

    // Mapas de Caché en RAM para velocidad extrema
    private Map<Integer, EmpleadoDTO> mapaEmpleados = new HashMap<>();
    private Map<Integer, DepartamentoDTO> mapaDepartamentos = new HashMap<>();
    private Map<Integer, SucursalDTO> mapaSucursales = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCachesAuxiliares();
        configurarColumnas();
        cargarSucursales();
        cargarTodosLosUsuarios();
        configurarFiltro();
    }

    private void cargarCachesAuxiliares() {
        try {
            for (EmpleadoDTO e : empleadoDAO.getAll()) mapaEmpleados.put(e.getIDEmpleado(), e);
            for (DepartamentoDTO d : deptoDAO.getAll()) mapaDepartamentos.put(d.getIDDepartamento(), d);
            for (SucursalDTO s : sucursalDAO.getAll()) mapaSucursales.put(s.getIDSucursal(), s);
        } catch (UserDisplayableException e) {
            System.out.println("Error al cargar caches: " + e.getMessage());
        }
    }

    private void configurarColumnas() {
        // Datos directos del UsuarioDTO
        colUsername.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        
        // Conversión del Enum a String
        colRol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getRol().name())
        );

        // Conversión de Booleano a Estado String
        colAcceso.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().isTieneAcceso() ? "Activo" : "Inactivo")
        );

        // Cruce: Encontrar el nombre completo del Empleado usando el caché
        colEmpleado.setCellValueFactory(cellData -> {
            EmpleadoDTO emp = mapaEmpleados.get(cellData.getValue().getIdEmpleado());
            return new SimpleStringProperty(emp != null ? emp.getNombre() + " " + emp.getApellidos() : "Desconocido");
        });

        // Cruce profundo: Usuario -> Empleado -> Departamento -> Sucursal
        colSucursal.setCellValueFactory(cellData -> {
            EmpleadoDTO emp = mapaEmpleados.get(cellData.getValue().getIdEmpleado());
            if (emp != null) {
                DepartamentoDTO depto = mapaDepartamentos.get(emp.getIDDepartamento());
                if (depto != null) {
                    SucursalDTO suc = mapaSucursales.get(depto.getIDSucursal());
                    return new SimpleStringProperty(suc != null ? suc.getNombre() : "Sin Sucursal");
                }
            }
            return new SimpleStringProperty("N/A");
        });
    }

    private void cargarSucursales() {
        try {
            cbSucursalFiltro.setItems(FXCollections.observableArrayList(sucursalDAO.getAll()));
        } catch (UserDisplayableException e) {
            System.out.println("Error al cargar sucursales: " + e.getMessage());
        }
    }

    private void cargarTodosLosUsuarios() {
        try {
            listaTodosLosUsuarios.setAll(usuarioDAO.getAll());
            tablaUsuarios.setItems(listaTodosLosUsuarios);
        } catch (UserDisplayableException e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void configurarFiltro() {
        cbSucursalFiltro.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                tablaUsuarios.setItems(listaTodosLosUsuarios);
            } else {
                filtrarPorSucursal(newValue.getIDSucursal());
            }
        });
    }

    private void filtrarPorSucursal(int idSucursalFiltro) {
        // Todo el filtrado se hace en RAM. ¡Súper rápido!
        
        // 1. Qué departamentos pertenecen a esta sucursal?
        List<Integer> deptosValidos = mapaDepartamentos.values().stream()
                .filter(d -> d.getIDSucursal() == idSucursalFiltro)
                .map(DepartamentoDTO::getIDDepartamento)
                .collect(Collectors.toList());

        // 2. Qué empleados trabajan en esos departamentos?
        List<Integer> empleadosValidos = mapaEmpleados.values().stream()
                .filter(e -> deptosValidos.contains(e.getIDDepartamento()))
                .map(EmpleadoDTO::getIDEmpleado)
                .collect(Collectors.toList());

        // 3. Qué usuarios corresponden a esos empleados?
        ObservableList<UsuarioDTO> filtrados = FXCollections.observableArrayList();
        for (UsuarioDTO u : listaTodosLosUsuarios) {
            if (empleadosValidos.contains(u.getIdEmpleado())) {
                filtrados.add(u);
            }
        }
        
        tablaUsuarios.setItems(filtrados);
    }

    @FXML
    private void btnLimpiarFiltro(ActionEvent event) {
        cbSucursalFiltro.setValue(null);
    }
}