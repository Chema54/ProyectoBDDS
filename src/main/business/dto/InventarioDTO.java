package main.business.dto;

public class InventarioDTO {

    private final int idSucursal;
    private final int idArticulo;
    private final String cantidad;
    private final String stockMinimo;
    private final String stockMaximo;

    public InventarioDTO(InventarioBuilder builder) {
        this.idSucursal = builder.idSucursal;
        this.idArticulo = builder.idArticulo;
        this.cantidad = builder.cantidad;
        this.stockMinimo = builder.stockMinimo;
        this.stockMaximo = builder.stockMaximo;
    }

    public int getIDSucursal() {
        return idSucursal;
    }

    public int getIDArticulo() {
        return idArticulo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getStockMinimo() {
        return stockMinimo;
    }

    public String getStockMaximo() {
        return stockMaximo;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }
        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }

        InventarioDTO that = (InventarioDTO) instance;

        return idSucursal == that.idSucursal
                && idArticulo == that.idArticulo
                && cantidad.equals(that.cantidad)
                && stockMinimo.equals(that.stockMinimo)
                && stockMaximo.equals(that.stockMaximo);
    }

    public static class InventarioBuilder {

        protected int idSucursal;
        protected int idArticulo;
        protected String cantidad;
        protected String stockMinimo;
        protected String stockMaximo;

        public InventarioBuilder setIDSucursal(int idSucursal) {
            this.idSucursal = idSucursal;
            return this;
        }

        public InventarioBuilder setIDArticulo(int idArticulo) {
            this.idArticulo = idArticulo;
            return this;
        }

        public InventarioBuilder setCantidad(String cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        public InventarioBuilder setStockMinimo(String stockMinimo) {
            this.stockMinimo = stockMinimo;
            return this;
        }

        public InventarioBuilder setStockMaximo(String stockMaximo) {
            this.stockMaximo = stockMaximo;
            return this;
        }

        public InventarioDTO build() {
            return new InventarioDTO(this);
        }
    }

    @Override
    public String toString() {
        return "Sucursal: " + idSucursal
                + ", Articulo: " + idArticulo
                + ", Cantidad: " + cantidad
                + ", Stock mínimo: " + stockMinimo
                + ", Stock máximo: " + stockMaximo;
    }
}
