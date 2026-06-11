package main.business.dto;

import java.sql.Date;

public class CarritoSolicitudDTO {

    private final int idCarrito;
    private final int idEmpleado;
    private final String empleadoNombre;
    private final int idArticulo;
    private final String articuloNombre;
    private final int cantidadPedida;
    private final int cantidadEntregada;
    private final int stockActual;
    private final String usoDestino;
    private final Date fechaPeticion;
    private final String estado;

    public CarritoSolicitudDTO(CarritoBuilder builder) {
        this.idCarrito = builder.idCarrito;
        this.idEmpleado = builder.idEmpleado;
        this.empleadoNombre = builder.empleadoNombre;
        this.idArticulo = builder.idArticulo;
        this.articuloNombre = builder.articuloNombre;
        this.cantidadPedida = builder.cantidadPedida;
        this.cantidadEntregada = builder.cantidadEntregada;
        this.stockActual = builder.stockActual;
        this.usoDestino = builder.usoDestino;
        this.fechaPeticion = builder.fechaPeticion;
        this.estado = builder.estado;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public String getEmpleadoNombre() {
        return empleadoNombre;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public String getArticuloNombre() {
        return articuloNombre;
    }

    public int getCantidadPedida() {
        return cantidadPedida;
    }

    public int getCantidadEntregada() {
        return cantidadEntregada;
    }

    public int getStockActual() {
        return stockActual;
    }

    public String getUsoDestino() {
        return usoDestino;
    }

    public Date getFechaPeticion() {
        return fechaPeticion;
    }

    public String getEstado() {
        return estado;
    }

    public static class CarritoBuilder {

        private int idCarrito;
        private int idEmpleado;
        private String empleadoNombre = "";
        private int idArticulo;
        private String articuloNombre = "";
        private int cantidadPedida;
        private int cantidadEntregada;
        private int stockActual = 0;
        private String usoDestino;
        private Date fechaPeticion;
        private String estado;

        public CarritoBuilder setIdCarrito(int id) {
            this.idCarrito = id;
            return this;
        }

        public CarritoBuilder setIdEmpleado(int id) {
            this.idEmpleado = id;
            return this;
        }

        public CarritoBuilder setEmpleadoNombre(String n) {
            this.empleadoNombre = n;
            return this;
        }

        public CarritoBuilder setIdArticulo(int id) {
            this.idArticulo = id;
            return this;
        }

        public CarritoBuilder setArticuloNombre(String n) {
            this.articuloNombre = n;
            return this;
        }

        public CarritoBuilder setCantidadPedida(int c) {
            this.cantidadPedida = c;
            return this;
        }

        public CarritoBuilder setCantidadEntregada(int c) {
            this.cantidadEntregada = c;
            return this;
        }

        public CarritoBuilder setStockActual(int s) {
            this.stockActual = s;
            return this;
        }

        public CarritoBuilder setUsoDestino(String u) {
            this.usoDestino = u;
            return this;
        }

        public CarritoBuilder setFechaPeticion(Date f) {
            this.fechaPeticion = f;
            return this;
        }

        public CarritoBuilder setEstado(String e) {
            this.estado = e;
            return this;
        }

        public CarritoSolicitudDTO build() {
            return new CarritoSolicitudDTO(this);
        }
    }
}
