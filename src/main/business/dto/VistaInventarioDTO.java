package main.business.dto;

/**
 * Objeto de Transferencia de Datos (DTO) para representar la información
 * consolidada de la vista "Vista_Inventario_Sucursales".
 */
public class VistaInventarioDTO {
    
    private int idSucursal;
    private String nombreSucursal;
    private int idArticulo;
    private String nombreArticulo;
    private int stockActual;
    private int stockMinimo;
    private int stockMaximo;

    public VistaInventarioDTO(int idSucursal, String nombreSucursal, int idArticulo, String nombreArticulo, int stockActual, int stockMinimo, int stockMaximo) {
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;
        this.idArticulo = idArticulo;
        this.nombreArticulo = nombreArticulo;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.stockMaximo = stockMaximo;
    }

    public int getIdSucursal() {
        return idSucursal;
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

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setStockMaximo(int stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

}