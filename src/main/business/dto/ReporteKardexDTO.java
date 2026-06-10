package main.business.dto;
import java.sql.Date;
public class ReporteKardexDTO {
    private String articulo;
    private String partida;
    private Date fecha;
    private int cantidad;
    private double costo;
    private double costoPromedio;

    public ReporteKardexDTO(String articulo, String partida, Date fecha, int cantidad, double costo, double costoPromedio) {
        this.articulo = articulo; this.partida = partida; this.fecha = fecha;
        this.cantidad = cantidad; this.costo = costo; this.costoPromedio = costoPromedio;
    }
    public String getArticulo() { return articulo; }
    public String getPartida() { return partida; }
    public Date getFecha() { return fecha; }
    public int getCantidad() { return cantidad; }
    public double getCosto() { return costo; }
    public double getCostoPromedio() { return costoPromedio; }
}