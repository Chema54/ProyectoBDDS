package main.business.dto;

public class PartidaPresupuestalDTO {
    private final int idPartida;
    private final String clave;
    private final String descripcion;

    public PartidaPresupuestalDTO(int idPartida, String clave, String descripcion) {
        this.idPartida = idPartida;
        this.clave = clave;
        this.descripcion = descripcion;
    }

    public int getIdPartida() { return idPartida; }
    public String getClave() { return clave; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() {
        return clave + " - " + descripcion;
    }
}