/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dto;

/**
 *
 * @author josem
 */
public class ProveedorDTO {

    private final int idProveedor;
    private final String razonSocial;
    private final String rfc;
    private final String domicilioFiscal;
    private final String telefono;

    public ProveedorDTO(ProveedorBuilder builder) {
        this.idProveedor = builder.idProveedor;
        this.razonSocial = builder.razonSocial;
        this.rfc = builder.rfc;
        this.domicilioFiscal = builder.domicilioFiscal;
        this.telefono = builder.telefono;
    }

    public int getIDProveedor() {
        return idProveedor;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getRFC() {
        return rfc;
    }

    public String getDomicilioFiscal() {
        return domicilioFiscal;
    }

    public String getTelefono() {
        return telefono;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) return true;
        if (instance == null || getClass() != instance.getClass()) return false;

        ProveedorDTO that = (ProveedorDTO) instance;

        return idProveedor == that.idProveedor
                && razonSocial.equals(that.razonSocial)
                && rfc.equals(that.rfc)
                && domicilioFiscal.equals(that.domicilioFiscal)
                && telefono.equals(that.telefono);
    }

    public static class ProveedorBuilder {

        protected int idProveedor;
        protected String razonSocial;
        protected String rfc;
        protected String domicilioFiscal;
        protected String telefono;

        public ProveedorBuilder setIDProveedor(int idProveedor) {
            this.idProveedor = idProveedor;
            return this;
        }

        public ProveedorBuilder setRazonSocial(String razonSocial) {
            this.razonSocial = razonSocial;
            return this;
        }

        public ProveedorBuilder setRFC(String rfc) {
            this.rfc = rfc;
            return this;
        }

        public ProveedorBuilder setDomicilioFiscal(String domicilioFiscal) {
            this.domicilioFiscal = domicilioFiscal;
            return this;
        }

        public ProveedorBuilder setTelefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public ProveedorDTO build() {
            return new ProveedorDTO(this);
        }
    }

    @Override
    public String toString() {
        return "Proveedor: " + idProveedor
                + ", Razón social: " + razonSocial
                + ", RFC: " + rfc
                + ", Domicilio fiscal: " + domicilioFiscal
                + ", Teléfono: " + telefono;
    }
}