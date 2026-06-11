package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.business.dao.InventarioDAO;
import main.business.dto.VistaInventarioDTO;
import main.common.SesionGlobal;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLMostrarInventarioController implements Initializable {

    @FXML private TextField txtBuscar;
    @FXML private TableView<VistaInventarioDTO> tablaInventario;
    @FXML private TableColumn<VistaInventarioDTO, Integer> colSucursal;
    @FXML private TableColumn<VistaInventarioDTO, String> colArticulo;
    @FXML private TableColumn<VistaInventarioDTO, Integer> colStock;
    @FXML private TableColumn<VistaInventarioDTO, Integer> colMinimo;
    @FXML private TableColumn<VistaInventarioDTO, Integer> colMaximo;

    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private ObservableList<VistaInventarioDTO> listaOriginal;
    private FilteredList<VistaInventarioDTO> listaFiltrada; // Estructura unificada para filtros

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarInventario();
        configurarBusquedaEnTiempoReal();
    }    

    private void configurarColumnas() {
        colSucursal.setCellValueFactory(new PropertyValueFactory<>("nombreSucursal")); 
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("nombreArticulo"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colMinimo.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
        colMaximo.setCellValueFactory(new PropertyValueFactory<>("stockMaximo"));
        
        colStock.setCellFactory(column -> new javafx.scene.control.TableCell<VistaInventarioDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); 
                    setStyle("");
                    return;
                }
                setText(String.valueOf(item));
                TableRow<?> row = getTableRow();
                if (row != null && row.getItem() instanceof VistaInventarioDTO) {
                    VistaInventarioDTO fila = (VistaInventarioDTO) row.getItem();
                    
                    if (item < fila.getStockMinimo()) {
                        setStyle("-fx-background-color: #ffcccc; -fx-text-fill: #990000; -fx-font-weight: bold;");
                    } 
                    else if (item > fila.getStockMaximo()) {
                        setStyle("-fx-background-color: #ffe5b4; -fx-text-fill: #cc7a00; -fx-font-weight: bold;");
                    } 
                    else {
                        setStyle("");
                    }
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void cargarInventario() {
        try {
            int idSucursal = SesionGlobal.getInstance().getIdSucursalActual();
            String rol = SesionGlobal.getInstance().getRolActual();
            Integer filtro = (rol.equals("CENTRAL") || rol.equals("SALIDAS")) ? 0 : idSucursal;
            
            listaOriginal = FXCollections.observableArrayList(inventarioDAO.getVistaInventario(filtro));
            
            listaFiltrada = new FilteredList<>(listaOriginal, p -> true);
            tablaInventario.setItems(listaFiltrada);
            
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error BD", "Problema al cargar el Inventario.", Alert.AlertType.ERROR);
        }
    }

    private void configurarBusquedaEnTiempoReal() {
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarLista();
        });
    }

    private void filtrarLista() {
        if (listaFiltrada == null) return;
        
        String texto = txtBuscar.getText().toLowerCase().trim();
        listaFiltrada.setPredicate(v -> {
            if (texto.isEmpty()) return true;
            return v.getNombreArticulo() != null && v.getNombreArticulo().toLowerCase().contains(texto);
        });
    }

    @FXML
    private void btnBuscar(ActionEvent event) {
        filtrarLista();
    }

    @FXML
    private void btnLimpiar(ActionEvent event) {
        txtBuscar.clear();
        if (listaFiltrada != null) {
            listaFiltrada.setPredicate(p -> true);
        }
    }
    
    @FXML
    private void btnFiltroFaltantes(ActionEvent event) {
        if (listaOriginal == null) return;
        txtBuscar.clear();
        listaFiltrada.setPredicate(v -> v.getStockActual() < v.getStockMinimo());
    }

    @FXML
    private void btnFiltroExcedentes(ActionEvent event) {
        if (listaOriginal == null) return;
        txtBuscar.clear(); 
        listaFiltrada.setPredicate(v -> v.getStockActual() > v.getStockMaximo());
    }
}