/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package main.business.dto.enumeraiones;

/**
 *
 * @author josem
 */
public enum UsuarioRol {
    CENTRAL,
    SUCURSAL,
    SALIDAS;



    @Override
    public String toString() {

        switch (this) {

            case CENTRAL:
                return "Central";

            case SUCURSAL:
                return "Académico";

            case SALIDAS:
                return "Académico Evaluador";


            default:
                return this.name();
        }
    }

    public String toDBString() {
        return this.name();
    }
}
