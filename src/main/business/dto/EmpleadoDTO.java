/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dto;

/**
 *
 * @author josem
 */
public class EmpleadoDTO {
    private final int idEmpleado;
    private final String nombre;
    private final String apellidos;
    private final int idDepartamento;

    public EmpleadoDTO(EmpleadoBuilder builder) {
        this.idEmpleado = builder.idEmpleado;
        this.nombre = builder.nombre;
        this.apellidos = builder.apellidos;
        this.idDepartamento = builder.idDepartamento;
    }

    public int getIDEmpleado() {
        return idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }
    
    public int getIDDepartamento() {
        return idDepartamento;
    }


    @Override
    public boolean equals(Object instance) {
        if (this == instance) return true;
        if (instance == null || getClass() != instance.getClass()) return false;

        EmpleadoDTO that = (EmpleadoDTO) instance;

        return idEmpleado == that.idEmpleado 
                && nombre.equals(that.nombre) 
                && apellidos.equals(that.apellidos)
                && idDepartamento == that.idDepartamento;
        }

    public static class EmpleadoBuilder {
        protected int idEmpleado;
        protected String nombre;
        protected String apellidos;
        protected int idDepartamento;

        public EmpleadoBuilder setIDEmpleado(int idEmpleado) {
            this.idEmpleado = idEmpleado;
            return this;
        }

        public EmpleadoBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public EmpleadoBuilder setApellidos(String apellidos) {
            this.apellidos = apellidos;
            return this;
        }
        
        public EmpleadoBuilder setIDDepartamento(int idDepartamento) {
            this.idDepartamento = idDepartamento;
            return this;
        }

        public EmpleadoDTO build() {
            return new EmpleadoDTO(this);
        }
    }

    @Override
    public String toString() {
        return "Empleado: " + idEmpleado + ", Nombre: " + nombre + ", Apellidos: " + apellidos + ", Departamento: " + idDepartamento;
    }
}
