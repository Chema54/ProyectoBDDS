package main.application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.business.dao.CarritoSolicitudDAO;
import main.business.dto.CarritoSolicitudDTO;
import main.common.SesionGlobal;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLAprobarSalidasController implements Initializable {

    @FXML private TableView<CarritoSolicitudDTO> tablaAprobar;
    @FXML private TableColumn<CarritoSolicitudDTO, Integer> colId;
    @FXML private TableColumn<CarritoSolicitudDTO, String> colEmpleado;
    @FXML private TableColumn<CarritoSolicitudDTO, String> colArticulo;
    @FXML private TableColumn<CarritoSolicitudDTO, Integer> colCantidadPedida;
    @FXML private TableColumn<CarritoSolicitudDTO, Integer> colStock; // NUEVA COLUMNA DE STOCK
    @FXML private TableColumn<CarritoSolicitudDTO, String> colUso;
    
    @FXML private TextField txtCantidadReal;
    
    private CarritoSolicitudDAO carritoDAO = new CarritoSolicitudDAO();
    private CarritoSolicitudDTO solicitudSeleccionada = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarPendientes();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCarrito"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<>("empleadoNombre"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("articuloNombre"));
        colCantidadPedida.setCellValueFactory(new PropertyValueFactory<>("cantidadPedida"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockActual")); // ENLAZADO AL DTO
        colUso.setCellValueFactory(new PropertyValueFactory<>("usoDestino"));
    }

    private void cargarPendientes() {
        // SACAMOS LA SUCURSAL ESTRICTAMENTE DE LA SESIÓN GLOBAL
        int idSucursal = SesionGlobal.getInstance().getIdSucursalActual();
        
        if (idSucursal == 0) {
            Utilidades.mostrarAlertaSimple("Error de Seguridad", "No se detectó una sesión de sucursal válida. Cierra sesión e inténtalo de nuevo.", Alert.AlertType.ERROR);
            return;
        }

        try {
            tablaAprobar.setItems(FXCollections.observableArrayList(carritoDAO.getPendientes(idSucursal)));
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error BD", "Problema cargando la bandeja.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void seleccionarFila() {
        solicitudSeleccionada = tablaAprobar.getSelectionModel().getSelectedItem();
        if (solicitudSeleccionada != null) {
            txtCantidadReal.setText(String.valueOf(solicitudSeleccionada.getCantidadPedida()));
        }
    }

    @FXML
    private void btnRechazarSalida(ActionEvent event) {
        if (solicitudSeleccionada == null) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Selecciona una petición.", Alert.AlertType.WARNING);
            return;
        }

        try {
            carritoDAO.rechazarSalida(solicitudSeleccionada.getIdCarrito());
            Utilidades.mostrarAlertaSimple("Éxito", "Solicitud rechazada correctamente.", Alert.AlertType.INFORMATION);
            txtCantidadReal.clear();
            cargarPendientes(); 
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error BD", "Problema al rechazar salida.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnAprobarYGenerarPDF(ActionEvent event) {
        if (solicitudSeleccionada == null || txtCantidadReal.getText().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Selecciona una petición.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int entregado = Integer.parseInt(txtCantidadReal.getText());
            int sucursalReal = SesionGlobal.getInstance().getIdSucursalActual();

            if (entregado > solicitudSeleccionada.getStockActual()) {
                Utilidades.mostrarAlertaSimple("Stock Insuficiente", "No puedes entregar más de lo que tienes en el almacén (" + solicitudSeleccionada.getStockActual() + ").", Alert.AlertType.ERROR);
                return;
            }

            // 1. Aprueba y descuenta en Base de Datos
            carritoDAO.aprobarSalida(solicitudSeleccionada.getIdCarrito(), entregado, sucursalReal);
            
            // 2. EXPORTACIÓN REAL DEL ACUSE PDF
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Guardar Acuse PDF");
            fileChooser.setInitialFileName("Acuse_Salida_" + solicitudSeleccionada.getIdCarrito() + ".pdf");
            fileChooser.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
            java.io.File archivoPDF = fileChooser.showSaveDialog(txtCantidadReal.getScene().getWindow());
            
            if (archivoPDF != null) {
                main.common.GeneradorReportes.generarAcuseSalida(archivoPDF.getAbsolutePath(), solicitudSeleccionada, entregado);
            }
            
            Utilidades.mostrarAlertaSimple("Éxito", "Salida aprobada y Acuse PDF generado.", Alert.AlertType.INFORMATION);
            txtCantidadReal.clear();
            cargarPendientes();
            
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Error", "Cantidad inválida.", Alert.AlertType.ERROR);
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error BD", "Problema al aprobar salida en base de datos.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            // === ESTA ES LA LÍNEA QUE FALTABA PARA QUE JAVA NO LLORE ===
            Utilidades.mostrarAlertaSimple("Advertencia", "La salida se aprobó, pero hubo un error generando el PDF.", Alert.AlertType.WARNING);
            e.printStackTrace();
        }
    }
}