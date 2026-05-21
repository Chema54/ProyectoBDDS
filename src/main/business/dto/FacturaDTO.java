/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dto;

/**
 *
 * @author josem
 */
public class FacturaDTO {

    private final int idFactura;
    private final String folio;
    private final String fecha;
    private final int idProveedor;

    public FacturaDTO(FacturaBuilder builder) {
        this.idFactura = builder.idFactura;
        this.folio = builder.folio;
        this.fecha = builder.fecha;
        this.idProveedor = builder.idProveedor;
    }

    public int getIDFactura() {
        return idFactura;
    }

    public String getFolio() {
        return folio;
    }

    public String getFecha() {
        return fecha;
    }

    public int getIDProveedor() {
        return idProveedor;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) return true;
        if (instance == null || getClass() != instance.getClass()) return false;

        FacturaDTO that = (FacturaDTO) instance;

        return idFactura == that.idFactura
                && folio.equals(that.folio)
                && fecha.equals(that.fecha)
                && idProveedor == that.idProveedor;
    }

    public static class FacturaBuilder {

        protected int idFactura;
        protected String folio;
        protected String fecha;
        protected int idProveedor;

        public FacturaBuilder setIDFactura(int idFactura) {
            this.idFactura = idFactura;
            return this;
        }

        public FacturaBuilder setFolio(String folio) {
            this.folio = folio;
            return this;
        }

        public FacturaBuilder setFecha(String fecha) {
            this.fecha = fecha;
            return this;
        }

        public FacturaBuilder setIDProveedor(int idProveedor) {
            this.idProveedor = idProveedor;
            return this;
        }

        public FacturaDTO build() {
            return new FacturaDTO(this);
        }
    }

    @Override
    public String toString() {
        return "Factura: " + idFactura
                + ", Folio: " + folio
                + ", Fecha: " + fecha
                + ", Proveedor: " + idProveedor;
    }
}
