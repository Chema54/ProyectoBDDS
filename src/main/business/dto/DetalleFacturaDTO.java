package main.business.dto;

public class DetalleFacturaDTO {

    private final int idDetalle;
    private final int idFactura;
    private final int idArticulo;
    private final int cantidad;
    private final double costo;

    public DetalleFacturaDTO(DetalleFacturaBuilder builder) {
        this.idDetalle = builder.idDetalle;
        this.idFactura = builder.idFactura;
        this.idArticulo = builder.idArticulo;
        this.cantidad = builder.cantidad;
        this.costo = builder.costo;
    }

    public int getIDDetalle() {
        return idDetalle;
    }

    public int getIDFactura() {
        return idFactura;
    }

    public int getIDArticulo() {
        return idArticulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getCosto() {
        return costo;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) return true;
        if (instance == null || getClass() != instance.getClass()) return false;

        DetalleFacturaDTO that = (DetalleFacturaDTO) instance;

        return idDetalle == that.idDetalle
                && idFactura == that.idFactura
                && idArticulo == that.idArticulo
                && cantidad == that.cantidad
                && Double.compare(costo, that.costo) == 0;
    }

    public static class DetalleFacturaBuilder {

        protected int idDetalle;
        protected int idFactura;
        protected int idArticulo;
        protected int cantidad;
        protected double costo;

        public DetalleFacturaBuilder setIDDetalle(int idDetalle) {
            this.idDetalle = idDetalle;
            return this;
        }

        public DetalleFacturaBuilder setIDFactura(int idFactura) {
            this.idFactura = idFactura;
            return this;
        }

        public DetalleFacturaBuilder setIDArticulo(int idArticulo) {
            this.idArticulo = idArticulo;
            return this;
        }

        public DetalleFacturaBuilder setCantidad(int cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        public DetalleFacturaBuilder setCosto(double costo) {
            this.costo = costo;
            return this;
        }

        public DetalleFacturaDTO build() {
            return new DetalleFacturaDTO(this);
        }
    }

    @Override
    public String toString() {
        return "DetalleFactura: " + idDetalle
                + ", Factura: " + idFactura
                + ", Articulo: " + idArticulo
                + ", Cantidad: " + cantidad
                + ", Costo: " + costo;
    }
}
