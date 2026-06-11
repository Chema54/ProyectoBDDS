package main.business.dto;

public class VistaInventarioDTO {

    private String nombreSucursal;
    private int idArticulo;
    private String nombreArticulo;
    private int stockActual;
    private int stockMinimo;
    private int stockMaximo;

    public VistaInventarioDTO(String nombreSucursal, int idArticulo, String nombreArticulo, int stockActual, int stockMinimo, int stockMaximo) {
        this.nombreSucursal = nombreSucursal;
        this.idArticulo = idArticulo;
        this.nombreArticulo = nombreArticulo;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public int getStockActual() {
        return stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public int getStockMaximo() {
        return stockMaximo;
    }
}
