/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dto;

/**
 *
 * @author josem
 */
public class SucursalDTO {
    private final int idSucursal;
    private final String nombre;
    private final String direccion;

    public SucursalDTO(SucursalBuilder builder) {
        this.idSucursal = builder.idSucursal;
        this.nombre = builder.nombre;
        this.direccion = builder.direccion;
    }

    public int getIDSucursal() {
        return idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) return true;
        if (instance == null || getClass() != instance.getClass()) return false;

        SucursalDTO that = (SucursalDTO) instance;

        return idSucursal == that.idSucursal && nombre.equals(that.nombre) && direccion.equals(that.direccion);
        }

    public static class SucursalBuilder {
        protected int idSucursal;
        protected String nombre;
        protected String direccion;

        public SucursalBuilder setIDSucursal(int idSucursal) {
            this.idSucursal = idSucursal;
            return this;
        }

        public SucursalBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public SucursalBuilder setDireccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public SucursalDTO build() {
            return new SucursalDTO(this);
        }
    }
    /*
    @Override
    public String toString() {
        return "Sucursal: " + idSucursal + ", Nombre: " + nombre + ", Direccion" + direccion;
    }
    */

    @Override
    public String toString() {
        return nombre;
    }
    
}
