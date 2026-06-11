package main.business.dto;

import java.util.Date;

public class BitacoraBajaDTO {

    private final int idBitacora;
    private final int idArticulo;
    private final Date fecha;
    private final String motivo;
    private final int cantidadRestante;

    public BitacoraBajaDTO(BitacoraBajaBuilder builder) {
        this.idBitacora = builder.idBitacora;
        this.idArticulo = builder.idArticulo;
        this.fecha = builder.fecha;
        this.motivo = builder.motivo;
        this.cantidadRestante = builder.cantidadRestante;
    }

    public int getIDBitacora() {
        return idBitacora;
    }

    public int getIDArticulo() {
        return idArticulo;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public int getCantidadRestante() {
        return cantidadRestante;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) return true;
        if (instance == null || getClass() != instance.getClass()) return false;

        BitacoraBajaDTO that = (BitacoraBajaDTO) instance;

        return idBitacora == that.idBitacora
                && idArticulo == that.idArticulo
                && cantidadRestante == that.cantidadRestante
                && fecha.equals(that.fecha)
                && motivo.equals(that.motivo);
    }

    public static class BitacoraBajaBuilder {

        protected int idBitacora;
        protected int idArticulo;
        protected Date fecha;
        protected String motivo;
        protected int cantidadRestante;

        public BitacoraBajaBuilder setIDBitacora(int idBitacora) {
            this.idBitacora = idBitacora;
            return this;
        }

        public BitacoraBajaBuilder setIDArticulo(int idArticulo) {
            this.idArticulo = idArticulo;
            return this;
        }

        public BitacoraBajaBuilder setFecha(Date fecha) {
            this.fecha = fecha;
            return this;
        }

        public BitacoraBajaBuilder setMotivo(String motivo) {
            this.motivo = motivo;
            return this;
        }

        public BitacoraBajaBuilder setCantidadRestante(int cantidadRestante) {
            this.cantidadRestante = cantidadRestante;
            return this;
        }

        public BitacoraBajaDTO build() {
            return new BitacoraBajaDTO(this);
        }
    }

    @Override
    public String toString() {
        return "BitacoraBaja: " + idBitacora
                + ", Articulo: " + idArticulo
                + ", Fecha: " + fecha
                + ", Motivo: " + motivo
                + ", Cantidad restante: " + cantidadRestante;
    }
}