package main.business.dto;

public class DepartamentoDTO {
    private final int idDepartamento;
    private final String nombreDepartamento;
    private final int idSucursal;
    private Integer idEncargado;

    public DepartamentoDTO(DepartamentoBuilder builder) {
        this.idDepartamento = builder.idDepartamento;
        this.nombreDepartamento = builder.nombreDepartamento;
        this.idSucursal = builder.idSucursal;
        this.idEncargado = builder.idEncargado;
    }

    public int getIDDepartamento() { return idDepartamento; }
    public String getNombreDepartamento() { return nombreDepartamento; }
    public int getIDSucursal() { return idSucursal; }
    public Integer getIdEncargado() { return idEncargado; }

    public void setIdEncargado(Integer idEncargado) {
        this.idEncargado = idEncargado;
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
        private Integer idEncargado;

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
        
        public DepartamentoBuilder setIdEncargado(Integer idEncargado) {
            // Manejo seguro para que si viene un 0 o null de la base de datos se asigne como nulo
            this.idEncargado = (idEncargado != null && idEncargado > 0) ? idEncargado : null;
            return this;
        }

        public DepartamentoDTO build() {
            return new DepartamentoDTO(this);
        }
    }

    @Override
    public String toString() {
        return nombreDepartamento;
    }
}