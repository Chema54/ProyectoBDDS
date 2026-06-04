package main.business.dto;

/**
 *
 * @author josem
 */

public class ArticuloDTO {
    private final int idArticulo;
    private final String descripcion;
    private final int idPartida;
    private final String estado;
    private final String nombre;

    public ArticuloDTO(ArticuloBuilder builder) {
        this.idArticulo = builder.idArticulo;
        this.descripcion = builder.descripcion;
        this.idPartida = builder.idPartida;
        this.estado = builder.estado;
        this.nombre = builder.nombre;
    }

    public int getIDArticulo() { return idArticulo; }
    public String getDescripcion() { return descripcion; }
    public int getIdPartida() { return idPartida; }
    public String getEstado() { return estado; }
    public String getNombre() { return nombre; }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) return true;
        if (instance == null || getClass() != instance.getClass()) return false;

        ArticuloDTO that = (ArticuloDTO) instance;
        return idArticulo == that.idArticulo 
                && idPartida == that.idPartida
                && descripcion.equals(that.descripcion)
                && estado.equals(that.estado)
                && nombre.equals(that.nombre);
    }

    public static class ArticuloBuilder {
        protected int idArticulo;
        protected String descripcion;
        protected int idPartida;
        protected String estado;
        protected String nombre;

        public ArticuloBuilder setIDArticulo(int idArticulo) {
            this.idArticulo = idArticulo;
            return this;
        }

        public ArticuloBuilder setDescripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public ArticuloBuilder setIdPartida(int idPartida) {
            this.idPartida = idPartida;
            return this;
        }

        public ArticuloBuilder setEstado(String estado) {
            this.estado = estado;
            return this;
        }

        public ArticuloBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public ArticuloDTO build() {
            return new ArticuloDTO(this);
        }
    }

    @Override
    public String toString() {
        return "Articulo: " + idArticulo + ", Nombre: " + nombre + ", Descripcion: " + descripcion;
    }
}