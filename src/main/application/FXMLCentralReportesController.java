package main.application;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
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

    // PEDIDOS
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
    @FXML
    private ComboBox<?> cbArticuloKardex;
    @FXML
    private TableView<?> tablaKardex;
    @FXML
    private TableColumn<?, ?> colFechaK;
    @FXML
    private TableColumn<?, ?> colTipoK;
    @FXML
    private TableColumn<?, ?> colCantK;
    @FXML
    private TableColumn<?, ?> colCostoUnitK;
    @FXML
    private TableColumn<?, ?> colCostoPromK;
    @FXML
    private TableColumn<?, ?> colRefK;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarBitacoraBajas();
        cargarBitacoraPedidos();

        // ==========================================
        // AUTO-CARGAR LOS MOVIMIENTOS DE HOY
        // ==========================================
        
        // 1. Ponemos la fecha de la computadora en los 4 DatePickers
        java.time.LocalDate hoy = java.time.LocalDate.now();
        
        dpInicioIngresos.setValue(hoy);
        dpFinIngresos.setValue(hoy);
        dpInicioEgresos.setValue(hoy);
        dpFinEgresos.setValue(hoy);

        // 2. Simulamos el "Clic" en los botones para que se llene la tabla
        btnFiltrarIngresos(null);
        btnFiltrarEgresos(null);
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

    // ==========================================
    // FIX: EXCEPCIONES MANEJADAS Y VISIBLES
    // ==========================================
    @FXML
    private void btnFiltrarIngresos(ActionEvent event) {
        if (dpInicioIngresos.getValue() == null || dpFinIngresos.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Selecciona fecha de inicio y fin.", Alert.AlertType.WARNING);
            return;
        }
        try {
            tablaIngresos.setItems(FXCollections.observableArrayList(reportesDAO.getMovimientosKardex("entrada", java.sql.Date.valueOf(dpInicioIngresos.getValue()), java.sql.Date.valueOf(dpFinIngresos.getValue()))));
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error BD", "Problema de lectura en el Kardex de Ingresos.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void btnFiltrarEgresos(ActionEvent event) {
        if (dpInicioEgresos.getValue() == null || dpFinEgresos.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Selecciona fecha de inicio y fin.", Alert.AlertType.WARNING);
            return;
        }
        try {
            tablaEgresos.setItems(FXCollections.observableArrayList(reportesDAO.getMovimientosKardex("salida", java.sql.Date.valueOf(dpInicioEgresos.getValue()), java.sql.Date.valueOf(dpFinEgresos.getValue()))));
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error BD", "Problema de lectura en el Kardex de Egresos.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void btnAgregarFantasma(ActionEvent event) {
        if (txtArticuloFantasma.getText().isEmpty() || txtCantidadFantasma.getText().isEmpty()) return;
        try {
            int cant = Integer.parseInt(txtCantidadFantasma.getText());
            tablaPedidos.getItems().add(new ReportePedidoDTO(txtArticuloFantasma.getText(), cant, new java.sql.Date(System.currentTimeMillis())));
            txtArticuloFantasma.clear();
            txtCantidadFantasma.clear();
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Error", "La cantidad debe ser número.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnExportarIngresosPDF(ActionEvent event) {
        if (tablaIngresos.getItems().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Aviso", "No hay ingresos para exportar.", Alert.AlertType.WARNING); return;
        }
        File archivo = pedirRutaGuardado("Reporte_Ingresos.pdf", "Archivos PDF (*.pdf)", "*.pdf");
        if (archivo != null) {
            try {
                GeneradorReportes.generarReporteKardex(archivo.getAbsolutePath(), "Ingresos", new ArrayList<>(tablaIngresos.getItems()));
                Utilidades.mostrarAlertaSimple("Éxito", "PDF de Ingresos generado.", Alert.AlertType.INFORMATION);
            } catch (Exception e) { Utilidades.mostrarAlertaSimple("Error", "Fallo al crear PDF.", Alert.AlertType.ERROR); }
        }
    }

    @FXML
    private void btnExportarEgresosPDF(ActionEvent event) {
        if (tablaEgresos.getItems().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Aviso", "No hay egresos para exportar.", Alert.AlertType.WARNING); return;
        }
        File archivo = pedirRutaGuardado("Reporte_Egresos.pdf", "Archivos PDF (*.pdf)", "*.pdf");
        if (archivo != null) {
            try {
                GeneradorReportes.generarReporteKardex(archivo.getAbsolutePath(), "Egresos", new ArrayList<>(tablaEgresos.getItems()));
                Utilidades.mostrarAlertaSimple("Éxito", "PDF de Egresos generado.", Alert.AlertType.INFORMATION);
            } catch (Exception e) { Utilidades.mostrarAlertaSimple("Error", "Fallo al crear PDF.", Alert.AlertType.ERROR); }
        }
    }

    @FXML
    private void btnExportarPedidosExcel(ActionEvent event) {
        if (tablaPedidos.getItems().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Aviso", "No hay pedidos.", Alert.AlertType.WARNING); return;
        }
        File archivo = pedirRutaGuardado("Bitacora_Pedidos.xlsx", "Archivos Excel (*.xlsx)", "*.xlsx");
        if (archivo != null) {
            try {
                GeneradorReportes.generarBitacoraPedidosExcel(archivo.getAbsolutePath(), new ArrayList<>(tablaPedidos.getItems()));
                reportesDAO.vaciarBitacoraPedidos();
                Utilidades.mostrarAlertaSimple("Éxito", "Excel generado y bitácora limpiada.", Alert.AlertType.INFORMATION);
                cargarBitacoraPedidos();
            } catch (Exception e) { Utilidades.mostrarAlertaSimple("Error", "Fallo al exportar Excel.", Alert.AlertType.ERROR); }
        }
    }

    private File pedirRutaGuardado(String nombrePorDefecto, String descFiltro, String extFiltro) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte");
        fileChooser.setInitialFileName(nombrePorDefecto);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(descFiltro, extFiltro));
        return fileChooser.showSaveDialog(tablaIngresos.getScene().getWindow());
    }

    @FXML
    private void btnGenerarKardex(ActionEvent event) {
    }

    @FXML
    private void btnExportarKardexPDF(ActionEvent event) {
    }
}