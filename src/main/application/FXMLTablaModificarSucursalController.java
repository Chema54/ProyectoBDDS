package main.application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import main.business.dto.SucursalDTO;
import main.business.dao.SucursalDAO;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLTablaModificarSucursalController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(FXMLTablaModificarSucursalController.class);

    @FXML private TableView<SucursalDTO> tvSucursales;
    @FXML private TableColumn<SucursalDTO, String> tcNombre;
    @FXML private TableColumn<SucursalDTO, String> tcDireccion;

    private SucursalDAO sucursalDAO;
    private ObservableList<SucursalDTO> listaSucursales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sucursalDAO = new SucursalDAO();
        configurarColumnasTabla();
        cargarDatosTabla();
        configurarSeleccionTabla();
    }

    private void configurarColumnasTabla() {
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
    }

    private void cargarDatosTabla() {
        try {
            listaSucursales = FXCollections.observableArrayList(sucursalDAO.getAll());
            tvSucursales.setItems(listaSucursales);
        } catch (UserDisplayableException ex) {
            Utilidades.mostrarAlertaSimple(
                "Error", 
                "No se pudo cargar la lista de sucursales.", 
                Alert.AlertType.ERROR
            );
        }
    }

    private void configurarSeleccionTabla() {
        tvSucursales.setRowFactory(tv -> {
            TableRow<SucursalDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    SucursalDTO sucursalSeleccionada = row.getItem();
                    irVentanaModificacion(sucursalSeleccionada);
                }
            });
            return row;
        });
    }

    private void irVentanaModificacion(SucursalDTO sucursal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/gui/FXMLModificarSucursalView.fxml"));
            Parent root = loader.load();

            FXMLModificarSucursalController controller = loader.getController();
            controller.inicializarDatos(sucursal);

            Stage stage = new Stage();
            stage.setTitle("Modificar Sucursal");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tvSucursales.getScene().getWindow());
            stage.showAndWait();
            cargarDatosTabla();

        } catch (IOException ex) {
            LOGGER.error("Error al cargar la ventana FXMLModificacionSucursal: ", ex);
            Utilidades.mostrarAlertaSimple(
                "Error de navegación", 
                "No se pudo abrir la ventana de modificación.", 
                Alert.AlertType.ERROR
            );
        }
    }
}