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

    private InventarioDAO inventarioDAO = new InventarioDAO();
    private ObservableList<VistaInventarioDTO> listaOriginal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarInventario();
    }    

    private void configurarColumnas() {
        colSucursal.setCellValueFactory(new PropertyValueFactory<>("idSucursal"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("nombreArticulo"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colMinimo.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
        colMaximo.setCellValueFactory(new PropertyValueFactory<>("stockMaximo"));
        
        // FIX RÚBRICA: Alertas visuales para Stock Mínimo y Máximo
        colStock.setCellFactory(column -> new javafx.scene.control.TableCell<VistaInventarioDTO, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); 
                    setStyle("");
                } else {
                    setText(String.valueOf(item));
                    VistaInventarioDTO fila = getTableView().getItems().get(getIndex());
                    
                    // Alerta: Por debajo del mínimo (Rojo) - Punto 14 de la Rúbrica
                    if (item < fila.getStockMinimo()) {
                        setStyle("-fx-background-color: #ffcccc; -fx-text-fill: #990000; -fx-font-weight: bold;");
                    } 
                    // Alerta: Por encima del máximo (Naranja/Amarillo) - Punto 15 de la Rúbrica
                    else if (item > fila.getStockMaximo()) {
                        setStyle("-fx-background-color: #ffe5b4; -fx-text-fill: #cc7a00; -fx-font-weight: bold;");
                    } 
                    // Stock normal
                    else {
                        setStyle("");
                    }
                }
            }
        });
    }

    private void cargarInventario() {
        try {
            int idSucursal = SesionGlobal.getInstance().getIdSucursalActual();
            String rol = SesionGlobal.getInstance().getRolActual();
            
            // Si es Central, manda "0" para traer todas. Si es Sucursal, manda su propio ID.
            Integer filtro = (rol.equals("CENTRAL") || rol.equals("SALIDAS")) ? 0 : idSucursal;
            
            listaOriginal = FXCollections.observableArrayList(inventarioDAO.getVistaInventario(filtro));
            tablaInventario.setItems(listaOriginal);
            
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error BD", "Problema al cargar el Inventario.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnBuscar(ActionEvent event) {
        String filtro = txtBuscar.getText().toLowerCase().trim();
        if (filtro.isEmpty()) {
            tablaInventario.setItems(listaOriginal);
            return;
        }
        
        FilteredList<VistaInventarioDTO> filtrada = new FilteredList<>(listaOriginal, v -> 
            v.getNombreArticulo() != null && v.getNombreArticulo().toLowerCase().contains(filtro)
        );
        tablaInventario.setItems(filtrada);
    }

    @FXML
    private void btnLimpiar(ActionEvent event) {
        txtBuscar.clear();
        tablaInventario.setItems(listaOriginal);
    }

    @FXML
    private void btnFiltroFaltantes(ActionEvent event) {
    }

    @FXML
    private void btnFiltroExcedentes(ActionEvent event) {
    }
}