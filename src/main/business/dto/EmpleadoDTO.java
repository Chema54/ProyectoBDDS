package main.business.dto;

public class EmpleadoDTO {

    private final int idEmpleado;
    private final String nombre;
    private final String apellidos;
    private final int idDepartamento;
    private final String numeroPersonal;
    private final String telefono;
    private final String correo;

    public EmpleadoDTO(EmpleadoBuilder builder) {
        this.idEmpleado = builder.idEmpleado;
        this.nombre = builder.nombre;
        this.apellidos = builder.apellidos;
        this.idDepartamento = builder.idDepartamento;
        this.numeroPersonal = builder.numeroPersonal;
        this.telefono = builder.telefono;
        this.correo = builder.correo;
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

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }
        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }

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
        protected String numeroPersonal;
        protected String telefono;
        protected String correo;

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

        public EmpleadoBuilder setNumeroPersonal(String numeroPersonal) {
            this.numeroPersonal = numeroPersonal;
            return this;
        }

        public EmpleadoBuilder setTelefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public EmpleadoBuilder setCorreo(String correo) {
            this.correo = correo;
            return this;
        }

        public EmpleadoDTO build() {
            return new EmpleadoDTO(this);
        }
    }

    @Override
    public String toString() {
        return this.nombre + " " + this.apellidos + " - " + this.numeroPersonal;
    }
}
