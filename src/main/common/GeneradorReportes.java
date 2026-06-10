package main.common;

import java.io.FileOutputStream;
import java.util.List;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import main.business.dto.CarritoSolicitudDTO;
import main.business.dto.ReporteKardexDTO;
import main.business.dto.ReportePedidoDTO;

public class GeneradorReportes {

    // ==========================================
    // 1. GENERAR ACUSE PDF (Al aprobar salida)
    // ==========================================
    public static void generarAcuseSalida(String ruta, CarritoSolicitudDTO solicitud, int cantidadEntregada) throws Exception {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(ruta));
        documento.open();

        Paragraph titulo = new Paragraph("ACUSE DE SALIDA DE ALMACÉN\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        documento.add(new Paragraph("Folio de Solicitud: " + solicitud.getIdCarrito()));
        documento.add(new Paragraph("Fecha de Aprobación: " + new java.util.Date()));
        documento.add(new Paragraph("Empleado Solicitante: " + solicitud.getEmpleadoNombre()));
        documento.add(new Paragraph("Uso / Destino: " + solicitud.getUsoDestino()));
        documento.add(new Paragraph("\nDETALLE DEL ARTÍCULO:\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));

        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.addCell(crearCeldaEncabezado("Artículo"));
        tabla.addCell(crearCeldaEncabezado("Cantidad Pedida"));
        tabla.addCell(crearCeldaEncabezado("Cantidad Entregada"));

        tabla.addCell(solicitud.getArticuloNombre());
        tabla.addCell(String.valueOf(solicitud.getCantidadPedida()));
        tabla.addCell(String.valueOf(cantidadEntregada));

        documento.add(tabla);
        documento.add(new Paragraph("\n\n_______________________________\nFirma de Almacén", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        documento.close();
    }

    // ==========================================
    // 2. GENERAR REPORTE DE KARDEX (Ingresos/Egresos) EN PDF
    // ==========================================
    public static void generarReporteKardex(String ruta, String tipoReporte, List<ReporteKardexDTO> datos) throws Exception {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream(ruta));
        documento.open();

        Paragraph titulo = new Paragraph("REPORTE DE " + tipoReporte.toUpperCase() + " (KARDEX)\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{3f, 3f, 1.5f, 1f, 1.5f});

        tabla.addCell(crearCeldaEncabezado("Partida Presupuestal"));
        tabla.addCell(crearCeldaEncabezado("Artículo"));
        tabla.addCell(crearCeldaEncabezado("Fecha"));
        tabla.addCell(crearCeldaEncabezado("Cant."));
        tabla.addCell(crearCeldaEncabezado(tipoReporte.equals("Ingresos") ? "Costo Unit." : "Costo Prom."));

        for (ReporteKardexDTO row : datos) {
            tabla.addCell(row.getPartida() != null ? row.getPartida() : "N/A");
            tabla.addCell(row.getArticulo());
            tabla.addCell(row.getFecha().toString());
            tabla.addCell(String.valueOf(row.getCantidad()));
            tabla.addCell("$" + String.format("%.2f", row.getCosto() > 0 ? row.getCosto() : row.getCostoPromedio()));
        }
        documento.add(tabla);
        documento.close();
    }

    // ==========================================
    // 3. GENERAR EXCEL DE PEDIDOS
    // ==========================================
    public static void generarBitacoraPedidosExcel(String ruta, List<ReportePedidoDTO> datos) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bitácora de Pedidos");

        // Fila de encabezado
        Row headerRow = sheet.createRow(0);
        String[] columnas = {"Artículo", "Cantidad a Pedir", "Fecha de Alerta"};
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
        }

        // Datos
        int rowNum = 1;
        for (ReportePedidoDTO dto : datos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dto.getArticulo());
            row.createCell(1).setCellValue(dto.getCantidadPedir());
            row.createCell(2).setCellValue(dto.getFecha().toString());
        }

        // Ajustar columnas
        for (int i = 0; i < columnas.length; i++) { sheet.autoSizeColumn(i); }

        FileOutputStream fileOut = new FileOutputStream(ruta);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    // Método ayudante para estilos de tabla PDF
    private static PdfPCell crearCeldaEncabezado(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE)));
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}