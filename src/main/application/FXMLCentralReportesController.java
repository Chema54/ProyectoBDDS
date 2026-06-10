package main.application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import main.business.dao.ReportesDAO;
import main.business.dto.ReporteBajaDTO;
import main.business.dto.ReporteKardexDTO;
import main.business.dto.ReportePedidoDTO;
import main.common.GeneradorReportes;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLCentralReportesController implements Initializable {

    // INGRESOS
    @FXML private DatePicker dpInicioIngresos;
    @FXML private DatePicker dpFinIngresos;
    @FXML private TableView<ReporteKardexDTO> tablaIngresos;
    @FXML private TableColumn<ReporteKardexDTO, String> colPartidaIngreso;
    @FXML private TableColumn<ReporteKardexDTO, String> colArticuloIngreso;
    @FXML private TableColumn<ReporteKardexDTO, java.sql.Date> colFechaIngreso;
    @FXML private TableColumn<ReporteKardexDTO, Integer> colCantIngreso;
    @FXML private TableColumn<ReporteKardexDTO, Double> colCostoIngreso;

    // EGRESOS
    @FXML private DatePicker dpInicioEgresos;
    @FXML private DatePicker dpFinEgresos;
    @FXML private TableView<ReporteKardexDTO> tablaEgresos;
    @FXML private TableColumn<ReporteKardexDTO, String> colPartidaEgreso;
    @FXML private TableColumn<ReporteKardexDTO, String> colArticuloEgreso;
    @FXML private TableColumn<ReporteKardexDTO, java.sql.Date> colFechaEgreso;
    @FXML private TableColumn<ReporteKardexDTO, Integer> colCantEgreso;
    @FXML private TableColumn<ReporteKardexDTO, Double> colCostoEgreso;

    // PEDIDOS Y ARTÍCULOS FANTASMA
    @FXML private TextField txtArticuloFantasma;
    @FXML private TextField txtCantidadFantasma;
    @FXML private TableView<ReportePedidoDTO> tablaPedidos;
    @FXML private TableColumn<ReportePedidoDTO, String> colArticuloPedido;
    @FXML private TableColumn<ReportePedidoDTO, Integer> colCantPedido;
    @FXML private TableColumn<ReportePedidoDTO, java.sql.Date> colFechaPedido;

    // BAJAS
    @FXML private TableView<ReporteBajaDTO> tablaBajas;
    @FXML private TableColumn<ReporteBajaDTO, String> colArticuloBaja;
    @FXML private TableColumn<ReporteBajaDTO, java.sql.Date> colFechaBaja;
    @FXML private TableColumn<ReporteBajaDTO, String> colMotivoBaja;
    @FXML private TableColumn<ReporteBajaDTO, Integer> colRestanteBaja;

    private ReportesDAO reportesDAO = new ReportesDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarBitacoraBajas();
        cargarBitacoraPedidos();
    }

    private void configurarColumnas() {
        colPartidaIngreso.setCellValueFactory(new PropertyValueFactory<>("partida"));
        colArticuloIngreso.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        colFechaIngreso.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCantIngreso.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCostoIngreso.setCellValueFactory(new PropertyValueFactory<>("costo"));

        colPartidaEgreso.setCellValueFactory(new PropertyValueFactory<>("partida"));
        colArticuloEgreso.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        colFechaEgreso.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colCantEgreso.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCostoEgreso.setCellValueFactory(new PropertyValueFactory<>("costoPromedio"));

        colArticuloPedido.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        colCantPedido.setCellValueFactory(new PropertyValueFactory<>("cantidadPedir"));
        colFechaPedido.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        colArticuloBaja.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        colFechaBaja.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colMotivoBaja.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colRestanteBaja.setCellValueFactory(new PropertyValueFactory<>("cantidadRestante"));
    }

    private void cargarBitacoraBajas() {
        try { tablaBajas.setItems(FXCollections.observableArrayList(reportesDAO.getBajas())); } 
        catch (UserDisplayableException e) { Utilidades.mostrarAlertaSimple("Error", "Error al cargar bajas.", Alert.AlertType.ERROR); }
    }

    private void cargarBitacoraPedidos() {
        try { tablaPedidos.setItems(FXCollections.observableArrayList(reportesDAO.getPedidos())); } 
        catch (UserDisplayableException e) { Utilidades.mostrarAlertaSimple("Error", "Error al cargar pedidos.", Alert.AlertType.ERROR); }
    }

    @FXML
    private void btnFiltrarIngresos(ActionEvent event) {
        if (dpInicioIngresos.getValue() == null || dpFinIngresos.getValue() == null) return;
        try {
            tablaIngresos.setItems(FXCollections.observableArrayList(reportesDAO.getMovimientosKardex("entrada", java.sql.Date.valueOf(dpInicioIngresos.getValue()), java.sql.Date.valueOf(dpFinIngresos.getValue()))));
        } catch (UserDisplayableException e) {}
    }

    @FXML
    private void btnFiltrarEgresos(ActionEvent event) {
        if (dpInicioEgresos.getValue() == null || dpFinEgresos.getValue() == null) return;
        try {
            tablaEgresos.setItems(FXCollections.observableArrayList(reportesDAO.getMovimientosKardex("salida", java.sql.Date.valueOf(dpInicioEgresos.getValue()), java.sql.Date.valueOf(dpFinEgresos.getValue()))));
        } catch (UserDisplayableException e) {}
    }

    // ==========================================
    // REGLA: AÑADIR ÍTEMS QUE NO TENEMOS EN BASE AL EXCEL
    // ==========================================
    @FXML
    private void btnAgregarFantasma(ActionEvent event) {
        if (txtArticuloFantasma.getText().isEmpty() || txtCantidadFantasma.getText().isEmpty()) return;
        try {
            int cant = Integer.parseInt(txtCantidadFantasma.getText());
            // Insertamos la fila fantasma directamente en la tabla de la interfaz (sin tocar la BD)
            tablaPedidos.getItems().add(new ReportePedidoDTO(txtArticuloFantasma.getText(), cant, new java.sql.Date(System.currentTimeMillis())));
            txtArticuloFantasma.clear();
            txtCantidadFantasma.clear();
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Error", "La cantidad debe ser número.", Alert.AlertType.ERROR);
        }
    }

    // ==========================================
    // EXPORTACIONES A ARCHIVOS FÍSICOS
    // ==========================================
    @FXML
    private void btnExportarIngresosPDF(ActionEvent event) {
        if (tablaIngresos.getItems().isEmpty()) return;
        File archivo = pedirRutaGuardado("Reporte_Ingresos.pdf", "Archivos PDF (*.pdf)", "*.pdf");
        if (archivo != null) {
            try {
                GeneradorReportes.generarReporteKardex(archivo.getAbsolutePath(), "Ingresos", new ArrayList<>(tablaIngresos.getItems()));
                Utilidades.mostrarAlertaSimple("Éxito", "PDF de Ingresos guardado correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception e) { Utilidades.mostrarAlertaSimple("Error", "No se pudo generar el PDF.", Alert.AlertType.ERROR); }
        }
    }

    @FXML
    private void btnExportarEgresosPDF(ActionEvent event) {
        if (tablaEgresos.getItems().isEmpty()) return;
        File archivo = pedirRutaGuardado("Reporte_Egresos.pdf", "Archivos PDF (*.pdf)", "*.pdf");
        if (archivo != null) {
            try {
                GeneradorReportes.generarReporteKardex(archivo.getAbsolutePath(), "Egresos", new ArrayList<>(tablaEgresos.getItems()));
                Utilidades.mostrarAlertaSimple("Éxito", "PDF de Egresos guardado correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception e) { Utilidades.mostrarAlertaSimple("Error", "No se pudo generar el PDF.", Alert.AlertType.ERROR); }
        }
    }

    @FXML
    private void btnExportarPedidosExcel(ActionEvent event) {
        if (tablaPedidos.getItems().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Aviso", "No hay pedidos para exportar.", Alert.AlertType.WARNING);
            return;
        }
        File archivo = pedirRutaGuardado("Bitacora_Pedidos.xlsx", "Archivos Excel (*.xlsx)", "*.xlsx");
        if (archivo != null) {
            try {
                // 1. Exporta lo que está en la tabla (incluyendo fantasmas) a Excel
                GeneradorReportes.generarBitacoraPedidosExcel(archivo.getAbsolutePath(), new ArrayList<>(tablaPedidos.getItems()));
                
                // 2. REGLA CUMPLIDA: Vaciar la bitácora real de la base de datos
                reportesDAO.vaciarBitacoraPedidos();
                
                Utilidades.mostrarAlertaSimple("Éxito", "Excel guardado. La bitácora en base de datos ha sido vaciada.", Alert.AlertType.INFORMATION);
                cargarBitacoraPedidos(); // Recarga la tabla para verla en blanco
                
            } catch (Exception e) { Utilidades.mostrarAlertaSimple("Error", "No se pudo generar el Excel.", Alert.AlertType.ERROR); }
        }
    }

    // Método que levanta la ventanita de Windows/Linux para elegir dónde guardar
    private File pedirRutaGuardado(String nombrePorDefecto, String descFiltro, String extFiltro) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte");
        fileChooser.setInitialFileName(nombrePorDefecto);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(descFiltro, extFiltro));
        return fileChooser.showSaveDialog(tablaIngresos.getScene().getWindow());
    }
}