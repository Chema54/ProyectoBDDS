package main.common;

public class SesionGlobal {
    private static SesionGlobal instance;
    private int idUsuarioActual;
    private int idSucursalActual;
    private String rolActual;

    private SesionGlobal() {}

    public static SesionGlobal getInstance() {
        if (instance == null) {
            instance = new SesionGlobal();
        }
        return instance;
    }

    public void iniciarSesion(int idUsuario, int idSucursal, String rol) {
        this.idUsuarioActual = idUsuario;
        this.idSucursalActual = idSucursal;
        this.rolActual = rol;
    }

    public int getIdSucursalActual() { return idSucursalActual; }
    public int getIdUsuarioActual() { return idUsuarioActual; }
    public String getRolActual() { return rolActual; }

    public void cerrarSesion() {
        this.idUsuarioActual = 0;
        this.idSucursalActual = 0;
        this.rolActual = null;
    }
}