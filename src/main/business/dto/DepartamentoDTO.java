package main.business.dto;

public class DepartamentoDTO {

    private final int idDepartamento;
    private final String nombreDepartamento;
    private final int idSucursal;

    public DepartamentoDTO(DepartamentoBuilder builder) {
        this.idDepartamento = builder.idDepartamento;
        this.nombreDepartamento = builder.nombreDepartamento;
        this.idSucursal = builder.idSucursal;
    }

    public int getIDDepartamento() {
        return idDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public int getIDSucursal() {
        return idSucursal;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) return true;
        if (instance == null || getClass() != instance.getClass()) return false;

        DepartamentoDTO that = (DepartamentoDTO) instance;

        return idDepartamento == that.idDepartamento
                && nombreDepartamento.equals(that.nombreDepartamento)
                && idSucursal == that.idSucursal;
    }

    public static class DepartamentoBuilder {

        private int idDepartamento;
        private String nombreDepartamento;
        private int idSucursal;

        public DepartamentoBuilder setIDDepartamento(int idDepartamento) {
            this.idDepartamento = idDepartamento;
            return this;
        }

        public DepartamentoBuilder setNombreDepartamento(String nombreDepartamento) {
            this.nombreDepartamento = nombreDepartamento;
            return this;
        }

        public DepartamentoBuilder setIDSucursal(int idSucursal) {
            this.idSucursal = idSucursal;
            return this;
        }

        public DepartamentoDTO build() {
            return new DepartamentoDTO(this);
        }
    }

    @Override
    public String toString() {
        return "Departamento: " + idDepartamento
                + ", Nombre: " + nombreDepartamento
                + ", Sucursal: " + idSucursal;
    }
}