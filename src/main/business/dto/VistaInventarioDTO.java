package main.business.dto;

public class VistaInventarioDTO {

    private int idSucursal;
    private int idArticulo;
    private String nombreArticulo;
    private int stockActual;
    private int stockMinimo;
    private int stockMaximo;

    public VistaInventarioDTO(int idSucursal, int idArticulo, String nombreArticulo, int stockActual, int stockMinimo, int stockMaximo) {
        this.idSucursal = idSucursal;
        this.idArticulo = idArticulo;
        this.nombreArticulo = nombreArticulo;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
    }

    public int getIdSucursal() {
        return idSucursal;
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
