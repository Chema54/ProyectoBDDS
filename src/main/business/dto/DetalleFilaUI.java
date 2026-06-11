package main.business.dto;

public class DetalleFilaUI {

    private final int idArticulo;
    private final String articuloNombre;
    private final int idSucursal;
    private final String sucursalNombre;
    private final int cantidad;
    private final double costo;

    public DetalleFilaUI(int idArticulo, String articuloNombre, int idSucursal, String sucursalNombre, int cantidad, double costo) {
        this.idArticulo = idArticulo;
        this.articuloNombre = articuloNombre;
        this.idSucursal = idSucursal;
        this.sucursalNombre = sucursalNombre;
        this.cantidad = cantidad;
        this.costo = costo;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public String getArticuloNombre() {
        return articuloNombre;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public String getSucursalNombre() {
        return sucursalNombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getCosto() {
        return costo;
    }

    public double getTotal() {
        return cantidad * costo;
    }
}
