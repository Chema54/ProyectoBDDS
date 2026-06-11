package main.business.dto;

import java.sql.Date;

public class ReportePedidoDTO {

    private String articulo;
    private int cantidadPedir;
    private Date fecha;

    public ReportePedidoDTO(String articulo, int cantidadPedir, Date fecha) {
        this.articulo = articulo;
        this.cantidadPedir = cantidadPedir;
        this.fecha = fecha;
    }

    public String getArticulo() {
        return articulo;
    }

    public int getCantidadPedir() {
        return cantidadPedir;
    }

    public Date getFecha() {
        return fecha;
    }
}
