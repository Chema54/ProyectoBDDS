package main.business.dto;

public class InventarioDetalladoDTO {
    private String nombreSucursal;
    private String nombreArticulo;
    private int stockActual;
    private int stockMinimo;
    private int stockMaximo;

    public InventarioDetalladoDTO(String nombreSucursal, String nombreArticulo, int stockActual, int stockMinimo, int stockMaximo) {
        this.nombreSucursal = nombreSucursal;
        this.nombreArticulo = nombreArticulo;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
    }

    public String getNombreSucursal() { return nombreSucursal; }
    public String getNombreArticulo() { return nombreArticulo; }
    public int getStockActual() { return stockActual; }
    public int getStockMinimo() { return stockMinimo; }
    public int getStockMaximo() { return stockMaximo; }
}