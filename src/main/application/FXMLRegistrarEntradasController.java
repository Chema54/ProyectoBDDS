package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.business.dao.ArticuloDAO;
import main.business.dao.EntradasDAO;
import main.business.dao.ProveedorDAO;
import main.business.dao.SucursalDAO;
import main.business.dto.ArticuloDTO;
import main.business.dto.DetalleFilaUI;
import main.business.dto.ProveedorDTO;
import main.business.dto.SucursalDTO;
import main.common.UserDisplayableException;
import main.common.Utilidades;
import main.database.Common;

public class FXMLRegistrarEntradasController implements Initializable {

    @FXML private TextField txtFolio;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<ProveedorDTO> cbProveedor;
    
    @FXML private ComboBox<SucursalDTO> cbSucursal;
    @FXML private ComboBox<ArticuloDTO> cbArticulo;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtCosto;
    
    @FXML private TableView<DetalleFilaUI> tablaDetalles;
    @FXML private TableColumn<DetalleFilaUI, String> colSucursal;
    @FXML private TableColumn<DetalleFilaUI, String> colArticulo;
    @FXML private TableColumn<DetalleFilaUI, Integer> colCantidad;
    @FXML private TableColumn<DetalleFilaUI, Double> colCosto;
    @FXML private TableColumn<DetalleFilaUI, Double> colTotal;
    @FXML private Label lblTotalFactura;

    private ProveedorDAO proveedorDAO = new ProveedorDAO();
    private SucursalDAO sucursalDAO = new SucursalDAO();
    private ArticuloDAO articuloDAO = new ArticuloDAO();
    private EntradasDAO entradasDAO = new EntradasDAO();
    
    private ObservableList<DetalleFilaUI> listaDetalles = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarCombos();
    }

    private void configurarColumnas() {
        colSucursal.setCellValueFactory(new PropertyValueFactory<>("sucursalNombre"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("articuloNombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaDetalles.setItems(listaDetalles);
    }

    private void cargarCombos() {
        try {
            cbProveedor.setItems(FXCollections.observableArrayList(proveedorDAO.getAll()));
            cbSucursal.setItems(FXCollections.observableArrayList(sucursalDAO.getAll()));
            cbArticulo.setItems(FXCollections.observableArrayList(articuloDAO.getAllActivos()));
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "No se cargaron los catálogos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnAgregarFila(ActionEvent event) {
        if (cbSucursal.getValue() == null || cbArticulo.getValue() == null || txtCantidad.getText().isEmpty() || txtCosto.getText().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Llena todos los campos del artículo.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            double costo = Double.parseDouble(txtCosto.getText());

            DetalleFilaUI fila = new DetalleFilaUI(
                cbArticulo.getValue().getIDArticulo(), cbArticulo.getValue().getNombre(),
                cbSucursal.getValue().getIDSucursal(), cbSucursal.getValue().getNombre(),
                cantidad, costo
            );
            
            listaDetalles.add(fila);
            actualizarTotalFactura();
            
            // Limpiar para el siguiente
            cbArticulo.setValue(null); txtCantidad.clear(); txtCosto.clear();
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Error", "Cantidad o Costo inválidos (Solo números).", Alert.AlertType.ERROR);
        }
    }

    private void actualizarTotalFactura() {
        double total = listaDetalles.stream().mapToDouble(DetalleFilaUI::getTotal).sum();
        lblTotalFactura.setText(String.format("Total Factura: $%.2f", total));
    }

    @FXML
    private void btnGuardarFactura(ActionEvent event) {
        if (txtFolio.getText().isEmpty() || dpFecha.getValue() == null || cbProveedor.getValue() == null || listaDetalles.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Faltan datos de la factura o no hay artículos.", Alert.AlertType.WARNING);
            return;
        }

        try {
            String folio = txtFolio.getText().trim();
            java.sql.Date fechaSql = Common.fromLocalDateTime(dpFecha.getValue().atStartOfDay());
            int idProveedor = cbProveedor.getValue().getIDProveedor();

            // Guardamos cada fila llamando al Procedimiento Almacenado
            for (DetalleFilaUI fila : listaDetalles) {
                entradasDAO.registrarEntradaKardex(folio, fechaSql, idProveedor, fila.getIdSucursal(), fila.getIdArticulo(), fila.getCantidad(), fila.getCosto());
            }

            Utilidades.mostrarAlertaSimple("Éxito", "Factura, Inventarios y Kardex actualizados correctamente.", Alert.AlertType.INFORMATION);
            
            // Limpiar pantalla
            txtFolio.clear(); dpFecha.setValue(null); cbProveedor.setValue(null);
            listaDetalles.clear(); actualizarTotalFactura();
            
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "Ocurrió un problema guardando la factura.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnQuitarFila(ActionEvent event) {
        DetalleFilaUI seleccionado = tablaDetalles.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaDetalles.remove(seleccionado);
            actualizarTotalFactura(); // Recalcula el dinero total
        } else {
            main.common.Utilidades.mostrarAlertaSimple("Advertencia", "Selecciona una fila de la tabla para quitarla.", javafx.scene.control.Alert.AlertType.WARNING);
        }
    }
}