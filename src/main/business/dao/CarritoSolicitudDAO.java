package main.business.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.business.dto.CarritoSolicitudDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarritoSolicitudDAO {
    private static final Logger LOGGER = LogManager.getLogger(CarritoSolicitudDAO.class);

    // Método para crear peticiones (Rol Departamento/Sucursal)
    public void registrarPeticion(CarritoSolicitudDTO dto) throws UserDisplayableException {
        String query = "INSERT INTO CarritoSolicitud (id_empleado, id_articulo, cantidad_pedida, uso_destino, fecha_peticion, estado) VALUES (?, ?, ?, ?, ?, 'Pendiente')";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, dto.getIdEmpleado());
            ps.setInt(2, dto.getIdArticulo());
            ps.setInt(3, dto.getCantidadPedida());
            ps.setString(4, dto.getUsoDestino());
            ps.setDate(5, dto.getFechaPeticion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al registrar la solicitud.");
        }
    }

    // Método para Rol Salidas (Muestra stock actual del almacén de la sucursal)
    public List<CarritoSolicitudDTO> getPendientes(int idSucursal) throws UserDisplayableException {
        String query = "SELECT c.*, e.nombre, e.apellidos, a.nombre AS nombre_articulo, IFNULL(i.cantidad, 0) AS stock_actual " +
                       "FROM CarritoSolicitud c " +
                       "JOIN Empleado e ON c.id_empleado = e.id_empleado " +
                       "JOIN Articulo a ON c.id_articulo = a.id_articulo " +
                       "LEFT JOIN Inventario i ON c.id_articulo = i.id_articulo AND i.id_sucursal = ? " +
                       "WHERE c.estado = 'Pendiente'";
        List<CarritoSolicitudDTO> list = new ArrayList<>();
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, idSucursal);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CarritoSolicitudDTO.CarritoBuilder()
                        .setIdCarrito(rs.getInt("id_carrito"))
                        .setIdEmpleado(rs.getInt("id_empleado"))
                        .setEmpleadoNombre(rs.getString("nombre") + " " + rs.getString("apellidos"))
                        .setIdArticulo(rs.getInt("id_articulo"))
                        .setArticuloNombre(rs.getString("nombre_articulo"))
                        .setCantidadPedida(rs.getInt("cantidad_pedida"))
                        .setStockActual(rs.getInt("stock_actual"))
                        .setUsoDestino(rs.getString("uso_destino"))
                        .setFechaPeticion(rs.getDate("fecha_peticion"))
                        .setEstado(rs.getString("estado"))
                        .build());
                }
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al cargar peticiones pendientes.");
        }
        return list;
    }

    // Método para Rol Salidas (Llama al SP para aprobar, descontar y costear en Kardex)
    public void aprobarSalida(int idCarrito, int cantidadEntregada, int idSucursal) throws UserDisplayableException {
        String call = "{CALL SP_AprobarSalida(?, ?, ?)}";
        try (Connection conn = DBConnector.getInstance().getConnection();
             CallableStatement cs = conn.prepareCall(call)) {
            cs.setInt(1, idCarrito);
            cs.setInt(2, cantidadEntregada);
            cs.setInt(3, idSucursal);
            cs.execute();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al aprobar la salida y actualizar inventario.");
        }
    }

    // Método para Rol Salidas (Rechaza y desaparece de la bandeja)
    public void rechazarSalida(int idCarrito) throws UserDisplayableException {
        String query = "UPDATE CarritoSolicitud SET estado = 'Rechazada' WHERE id_carrito = ?";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCarrito);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al rechazar la solicitud.");
        }
    }
}