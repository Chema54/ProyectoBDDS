package main.business.dto;

public class DirectorioEmpleadoDTO {
    private String numeroPersonal;
    private String nombre;
    private String apellidos;
    private String departamento;
    private String sucursal;
    private String usuario;
    private int idSucursal; // Lo guardamos oculto para poder filtrar rápido

    public DirectorioEmpleadoDTO(String numeroPersonal, String nombre, String apellidos, String departamento, String sucursal, String usuario, int idSucursal) {
        this.numeroPersonal = numeroPersonal;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.departamento = departamento;
        this.sucursal = sucursal;
        this.usuario = usuario;
        this.idSucursal = idSucursal;
    }

    public String getNumeroPersonal() { return numeroPersonal; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getDepartamento() { return departamento; }
    public String getSucursal() { return sucursal; }
    public String getUsuario() { return usuario; }
    public int getIdSucursal() { return idSucursal; }
}