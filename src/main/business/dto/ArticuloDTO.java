/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dto;

/**
 *
 * @author josem
 */
public class ArticuloDTO {
    private final int idArticulo;
    private final String descripcion;
    private final String partidaPresupuestal;

    public ArticuloDTO(ArticuloBuilder builder) {
        this.idArticulo = builder.idArticulo;
        this.descripcion = builder.descripcion;
        this.partidaPresupuestal = builder.partidaPresupuestal;
    }

    public int getIDArticulo() {
        return idArticulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPartidaPresupuestal() {
        return partidaPresupuestal;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) return true;
        if (instance == null || getClass() != instance.getClass()) return false;

        ArticuloDTO that = (ArticuloDTO) instance;

        return idArticulo == that.idArticulo && descripcion.equals(that.descripcion) && partidaPresupuestal.equals(that.partidaPresupuestal);
        }

    public static class ArticuloBuilder {
        protected int idArticulo;
        protected String descripcion;
        protected String partidaPresupuestal;

        public ArticuloBuilder setIDArticulo(int idArticulo) {
            this.idArticulo = idArticulo;
            return this;
        }

        public ArticuloBuilder setDescripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public ArticuloBuilder setPartidaPresupuestal(String partidaPresupuestal) {
            this.partidaPresupuestal = partidaPresupuestal;
            return this;
        }

        public ArticuloDTO build() {
            return new ArticuloDTO(this);
        }
    }

    @Override
    public String toString() {
        return "Articulo: " + idArticulo + ", Descripcion: " + descripcion;
    }
}
