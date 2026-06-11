package main.business.dto;

import java.sql.Date;

public class ReporteKardexDTO {
    private String articulo;
    private String partida;
    private Date fecha;
    private int cantidad;
    private double costo;
    private double costoPromedio;
    private String tipoMovimiento;
    private String referencia;

    public ReporteKardexDTO(String articulo, String partida, Date fecha, int cantidad, double costo, double costoPromedio, String tipoMovimiento, String referencia) {
        this.articulo = articulo;
        this.partida = partida;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.costo = costo;
        this.costoPromedio = costoPromedio;
        this.tipoMovimiento = tipoMovimiento;
        this.referencia = referencia;
    }

    public String getArticulo() { return articulo; }
    public String getPartida() { return partida; }
    public Date getFecha() { return fecha; }
    public int getCantidad() { return cantidad; }
    public double getCosto() { return costo; }
    public double getCostoPromedio() { return costoPromedio; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public String getReferencia() { return referencia; }
}