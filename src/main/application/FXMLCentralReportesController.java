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
import main.business.dao.ArticuloDAO;
import main.business.dao.ReportesDAO;
import main.business.dto.ArticuloDTO;
import main.business.dto.ReporteBajaDTO;
import main.business.dto.ReporteKardexDTO;
import main.business.dto.ReportePedidoDTO;
import main.common.GeneradorReportes;
import main.common.UserDisplayableException;
import main.common.Utilidades;

public class FXMLCentralReportesController implements Initializable {

    @FXML
    private ComboBox<ArticuloDTO> cbArticuloKardex;
    @FXML
    private TableView<ReporteKardexDTO> tablaKardex;
    @FXML
    private TableColumn<ReporteKardexDTO, java.sql.Date> colFechaK;
    @FXML
    private TableColumn<ReporteKardexDTO, String> colTipoK;
    @FXML
    private TableColumn<ReporteKardexDTO, Integer> colCantK;
    @FXML
    private TableColumn<ReporteKardexDTO, Double> colCostoUnitK;
    @FXML
    private TableColumn<ReporteKardexDTO, Double> colCostoPromK;
    @FXML
    private TableColumn<ReporteKardexDTO, String> colRefK;

    @FXML
    private DatePicker dpInicioIngresos;
    @FXML
    private DatePicker dpFinIngresos;
    @FXML
    private TableView<ReporteKardexDTO> tablaIngresos;
    @FXML
    private TableColumn<ReporteKardexDTO, String> colPartidaIngreso;
    @FXML
    private TableColumn<ReporteKardexDTO, String> colArticuloIngreso;
    @FXML
    private TableColumn<ReporteKardexDTO, java.sql.Date> colFechaIngreso;
    @FXML
    private TableColumn<ReporteKardexDTO, Integer> colCantIngreso;
    @FXML
    private TableColumn<ReporteKardexDTO, Double> colCostoIngreso;

    @FXML
    private DatePicker dpInicioEgresos;
    @FXML
    private DatePicker dpFinEgresos;
    @FXML
    private TableView<ReporteKardexDTO> tablaEgresos;
    @FXML
    private TableColumn<ReporteKardexDTO, String> colPartidaEgreso;
    @FXML
    private TableColumn<ReporteKardexDTO, String> colArticuloEgreso;
    @FXML
    private TableColumn<ReporteKardexDTO, java.sql.Date> colFechaEgreso;
    @FXML
    private TableColumn<ReporteKardexDTO, Integer> colCantEgreso;
    @FXML
    private TableColumn<ReporteKardexDTO, Double> colCostoEgreso;

    // PEDIDOS
    @FXML
    private TextField txtArticuloFantasma;
    @FXML
    private TextField txtCantidadFantasma;
    @FXML
    private TableView<ReportePedidoDTO> tablaPedidos;
    @FXML
    private TableColumn<ReportePedidoDTO, String> colArticuloPedido;
    @FXML
    private TableColumn<ReportePedidoDTO, Integer> colCantPedido;
    @FXML
    private TableColumn<ReportePedidoDTO, java.sql.Date> colFechaPedido;

    // BAJAS
    @FXML
    private TableView<ReporteBajaDTO> tablaBajas;
    @FXML
    private TableColumn<ReporteBajaDTO, String> colArticuloBaja;
    @FXML
    private TableColumn<ReporteBajaDTO, java.sql.Date> colFechaBaja;
    @FXML
    private TableColumn<ReporteBajaDTO, String> colMotivoBaja;
    @FXML
    private TableColumn<ReporteBajaDTO, Integer> colRestanteBaja;

    private ReportesDAO reportesDAO = new ReportesDAO();
    private ArticuloDAO articuloDAO = new ArticuloDAO();
    @FXML
    private TextField txtBuscarBaja;
    private javafx.collections.ObservableList<ReporteBajaDTO> listaBajasOriginal; // Memoria caché para buscar rápido

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarBitacoraBajas();
        cargarBitacoraPedidos();

        try {
            cbArticuloKardex.setItems(FXCollections.observableArrayList(articuloDAO.getAll()));
            if (!cbArticuloKardex.getItems().isEmpty()) {
                cbArticuloKardex.getSelectionModel().selectFirst();
                btnGenerarKardex(null);
            }
        } catch (Exception e) {
        }

        java.time.LocalDate hoy = java.time.LocalDate.now();
        dpInicioIngresos.setValue(hoy);
        dpFinIngresos.setValue(hoy);
        dpInicioEgresos.setValue(hoy);
        dpFinEgresos.setValue(hoy);
        btnFiltrarIngresos(null);
        btnFiltrarEgresos(null);
    }

    private void configurarColumnas() {
        colFechaK.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colTipoK.setCellValueFactory(new PropertyValueFactory<>("tipoMovimiento"));
        colCantK.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCostoUnitK.setCellValueFactory(new PropertyValueFactory<>("costo"));
        colCostoPromK.setCellValueFactory(new PropertyValueFactory<>("costoPromedio"));
        colRefK.setCellValueFactory(new PropertyValueFactory<>("referencia"));

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
        try {
            listaBajasOriginal = FXCollections.observableArrayList(reportesDAO.getBajas());
            tablaBajas.setItems(listaBajasOriginal);
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "Error al cargar bajas.", Alert.AlertType.ERROR);
        }
    }

    private void cargarBitacoraPedidos() {
        try {
            tablaPedidos.setItems(FXCollections.observableArrayList(reportesDAO.getPedidos()));
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error", "Error al cargar pedidos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnGenerarKardex(ActionEvent event) {
        if (cbArticuloKardex.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Advertencia", "Selecciona un artículo.", Alert.AlertType.WARNING);
            return;
        }
        try {
            tablaKardex.setItems(FXCollections.observableArrayList(reportesDAO.getKardexArticulo(cbArticuloKardex.getValue().getIDArticulo())));
        } catch (UserDisplayableException e) {
            Utilidades.mostrarAlertaSimple("Error BD", "Problema al cargar el Kardex.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void btnExportarKardexPDF(ActionEvent event) {
        if (tablaKardex.getItems().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Aviso", "No hay datos.", Alert.AlertType.WARNING);
            return;
        }
        File archivo = pedirRutaGuardado("Tarjeta_Kardex_" + cbArticuloKardex.getValue().getNombre() + ".pdf", "Archivos PDF (*.pdf)", "*.pdf");
        if (archivo != null) {
            try {
                GeneradorReportes.generarKardexArticuloPDF(archivo.getAbsolutePath(), cbArticuloKardex.getValue().getNombre(), new ArrayList<>(tablaKardex.getItems()));
                Utilidades.mostrarAlertaSimple("Éxito", "Kardex PDF generado.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                Utilidades.mostrarAlertaSimple("Error", "Fallo al crear PDF.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void btnFiltrarIngresos(ActionEvent event) {
        if (dpInicioIngresos.getValue() == null || dpFinIngresos.getValue() == null) {
            return;
        }
        try {
            tablaIngresos.setItems(FXCollections.observableArrayList(reportesDAO.getMovimientosKardex("entrada", java.sql.Date.valueOf(dpInicioIngresos.getValue()), java.sql.Date.valueOf(dpFinIngresos.getValue()))));
        } catch (UserDisplayableException e) {
        }
    }

    @FXML
    private void btnFiltrarEgresos(ActionEvent event) {
        if (dpInicioEgresos.getValue() == null || dpFinEgresos.getValue() == null) {
            return;
        }
        try {
            tablaEgresos.setItems(FXCollections.observableArrayList(reportesDAO.getMovimientosKardex("salida", java.sql.Date.valueOf(dpInicioEgresos.getValue()), java.sql.Date.valueOf(dpFinEgresos.getValue()))));
        } catch (UserDisplayableException e) {
        }
    }

    @FXML
    private void btnAgregarFantasma(ActionEvent event) {
        if (txtArticuloFantasma.getText().isEmpty() || txtCantidadFantasma.getText().isEmpty()) {
            return;
        }
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
            return;
        }
        File archivo = pedirRutaGuardado("Reporte_Ingresos.pdf", "Archivos PDF (*.pdf)", "*.pdf");
        if (archivo != null) {
            try {
                GeneradorReportes.generarReporteKardex(archivo.getAbsolutePath(), "Ingresos", new ArrayList<>(tablaIngresos.getItems()));
                Utilidades.mostrarAlertaSimple("Éxito", "PDF generado.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                Utilidades.mostrarAlertaSimple("Error", "Fallo al crear PDF.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void btnExportarEgresosPDF(ActionEvent event) {
        if (tablaEgresos.getItems().isEmpty()) {
            return;
        }
        File archivo = pedirRutaGuardado("Reporte_Egresos.pdf", "Archivos PDF (*.pdf)", "*.pdf");
        if (archivo != null) {
            try {
                GeneradorReportes.generarReporteKardex(archivo.getAbsolutePath(), "Egresos", new ArrayList<>(tablaEgresos.getItems()));
                Utilidades.mostrarAlertaSimple("Éxito", "PDF generado.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                Utilidades.mostrarAlertaSimple("Error", "Fallo al crear PDF.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void btnExportarPedidosExcel(ActionEvent event) {
        if (tablaPedidos.getItems().isEmpty()) {
            return;
        }
        File archivo = pedirRutaGuardado("Bitacora_Pedidos.xlsx", "Archivos Excel (*.xlsx)", "*.xlsx");
        if (archivo != null) {
            try {
                GeneradorReportes.generarBitacoraPedidosExcel(archivo.getAbsolutePath(), new ArrayList<>(tablaPedidos.getItems()));
                reportesDAO.vaciarBitacoraPedidos();
                Utilidades.mostrarAlertaSimple("Éxito", "Excel generado y bitácora limpiada.", Alert.AlertType.INFORMATION);
                cargarBitacoraPedidos();
            } catch (Exception e) {
                Utilidades.mostrarAlertaSimple("Error", "Fallo al exportar Excel.", Alert.AlertType.ERROR);
            }
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
    private void btnBuscarBaja(ActionEvent event) {
        String filtro = txtBuscarBaja.getText().toLowerCase().trim();

        if (filtro.isEmpty()) {
            tablaBajas.setItems(listaBajasOriginal);
            return;
        }

        javafx.collections.transformation.FilteredList<ReporteBajaDTO> filtrada
                = new javafx.collections.transformation.FilteredList<>(listaBajasOriginal, b
                        -> b.getArticulo() != null && b.getArticulo().toLowerCase().contains(filtro)
                );

        tablaBajas.setItems(filtrada);
    }

    @FXML
    private void btnLimpiarFiltroBajas(ActionEvent event) {
        txtBuscarBaja.clear();
        tablaBajas.setItems(listaBajasOriginal);
    }

    @FXML
    private void btnExportarBajasExcel(ActionEvent event) {
        if (tablaBajas.getItems().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Aviso", "No hay registros de bajas para exportar.", Alert.AlertType.WARNING);
            return;
        }

        File archivo = pedirRutaGuardado("Bitacora_Bajas.xlsx", "Archivos Excel (*.xlsx)", "*.xlsx");

        if (archivo != null) {
            try {
                GeneradorReportes.generarBitacoraBajasExcel(archivo.getAbsolutePath(), new ArrayList<>(tablaBajas.getItems()));
                Utilidades.mostrarAlertaSimple("Éxito", "Excel de Bajas generado correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                Utilidades.mostrarAlertaSimple("Error", "Fallo al exportar el Excel de Bajas.", Alert.AlertType.ERROR);
            }
        }
    }

}
