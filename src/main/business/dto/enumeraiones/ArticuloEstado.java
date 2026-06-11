/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dto.enumeraiones;

public enum ArticuloEstado {
    ACTIVO("Activo"),   
    INACTIVO("Inactivo");  

    private final String idEstado;

    ArticuloEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getidEstado() {
        return idEstado;
    }
}
