package main.business.dto.enumeraiones;

public enum UsuarioRol {
    CENTRAL(1),
    SUCURSAL(2),
    SALIDAS(3),
    DEPARTAMENTO(4);

    private final int idRol;

    UsuarioRol(int idRol) {
        this.idRol = idRol;
    }

    public int getIdRol() {
        return idRol;
    }
}
