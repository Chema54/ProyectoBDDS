package main.business.dto;

import java.sql.Date;

public class ReporteBajaDTO {

    private String articulo;
    private Date fecha;
    private String motivo;
    private int cantidadRestante;

    public ReporteBajaDTO(String articulo, Date fecha, String motivo, int cantidadRestante) {
        this.articulo = articulo;
        this.fecha = fecha;
        this.motivo = motivo;
        this.cantidadRestante = cantidadRestante;
    }

    public String getArticulo() {
        return articulo;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public int getCantidadRestante() {
        return cantidadRestante;
    }
}
