package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.business.dao.ArticuloDAO;
import main.business.dao.CarritoSolicitudDAO;
import main.business.dao.EmpleadoDAO;
import main.business.dto.ArticuloDTO;
import main.business.dto.CarritoSolicitudDTO;
import main.business.dto.EmpleadoDTO;
import main.common.SesionGlobal;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLCrearSolicitudController implements Initializable {

    @FXML private ComboBox<EmpleadoDTO> cbEmpleado;
    @FXML private ComboBox<ArticuloDTO> cbArticulo;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtUso;
    @FXML private TableView<CarritoSolicitudDTO> tablaPendientes;
    
    @FXML private TableColumn<CarritoSolicitudDTO, String> colEmpleado;
    @FXML private TableColumn<CarritoSolicitudDTO, String> colArticulo;
    @FXML private TableColumn<CarritoSolicitudDTO, Integer> colCantidad;
    @FXML private TableColumn<CarritoSolicitudDTO, String> colUso;
    @FXML private TableColumn<CarritoSolicitudDTO, java.sql.Date> colFecha;

    private CarritoSolicitudDAO carritoDAO = new CarritoSolicitudDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private ArticuloDAO articuloDAO = new ArticuloDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarCombos();
        cargarMisPeticiones();
    }

    private void configurarColumnas() {
        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("empleadoNombre"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("articuloNombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadPedida"));
        colUso.setCellValueFactory(new PropertyValueFactory<>("usoDestino"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaPeticion"));
    }

    private void cargarCombos() {
        try {
            cbEmpleado.setItems(FXCollections.observableArrayList(empleadoDAO.getAll()));
            cbArticulo.setItems(FXCollections.observableArrayList(articuloDAO.getAllActivos()));
        } catch (UserDisplayableException e) {}
    }

    private void cargarMisPeticiones() {
        // FIX: Ahora pasamos la sucursal desde la sesión global para que el DAO no marque error
        int idSucursal = SesionGlobal.getInstance().getIdSucursalActual();
        try {
            tablaPendientes.setItems(FXCollections.observableArrayList(carritoDAO.getPendientes(idSucursal)));
        } catch (UserDisplayableException e) {}
    }

    @FXML
    private void btnAgregarPeticion(ActionEvent event) {
        if (cbEmpleado.getValue() == null || cbArticulo.getValue() == null || txtCantidad.getText().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Faltan datos.", Alert.AlertType.WARNING); return;
        }
        try {
            CarritoSolicitudDTO dto = new CarritoSolicitudDTO.CarritoBuilder()
                .setIdEmpleado(cbEmpleado.getValue().getIDEmpleado())
                .setIdArticulo(cbArticulo.getValue().getIDArticulo())
                .setCantidadPedida(Integer.parseInt(txtCantidad.getText()))
                .setUsoDestino(txtUso.getText().trim())
                .setFechaPeticion(new java.sql.Date(System.currentTimeMillis()))
                .build();
            
            carritoDAO.registrarPeticion(dto);
            Utilidades.mostrarAlertaSimple("Éxito", "Enviado a Salidas.", Alert.AlertType.INFORMATION);
            txtCantidad.clear(); txtUso.clear(); cbArticulo.setValue(null);
            cargarMisPeticiones(); // Se recarga la tabla correctamente
        } catch (Exception e) {
            Utilidades.mostrarAlertaSimple("Error", "Revisa la cantidad.", Alert.AlertType.ERROR);
        }
    }
}                       