package main.business.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    // ==========================================
    // 1. DEPARTAMENTO: METER AL CARRITO
    // ==========================================
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
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se pudo registrar la petición.");
        }
    }

    // ==========================================
    // 2. SALIDAS: VER LA BANDEJA (Con joins para nombres y stock)
    // ==========================================
    public List<CarritoSolicitudDTO> getPendientes(int idSucursal) throws UserDisplayableException {
        String query = "SELECT c.*, a.nombre AS nombre_articulo, CONCAT(e.nombre, ' ', e.apellidos) AS nombre_empleado, IFNULL(i.cantidad, 0) AS stock_actual " +
                       "FROM CarritoSolicitud c " +
                       "INNER JOIN Articulo a ON c.id_articulo = a.id_articulo " +
                       "INNER JOIN Empleado e ON c.id_empleado = e.id_empleado " +
                       "LEFT JOIN Inventario i ON c.id_articulo = i.id_articulo AND i.id_sucursal = ? " +
                       "WHERE c.estado = 'Pendiente'";
        
        List<CarritoSolicitudDTO> lista = new ArrayList<>();
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
             
            ps.setInt(1, idSucursal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CarritoSolicitudDTO dto = new CarritoSolicitudDTO.CarritoBuilder()
                            .setIdCarrito(rs.getInt("id_carrito"))
                            .setIdEmpleado(rs.getInt("id_empleado"))
                            .setIdArticulo(rs.getInt("id_articulo"))
                            .setCantidadPedida(rs.getInt("cantidad_pedida"))
                            .setUsoDestino(rs.getString("uso_destino"))
                            .setFechaPeticion(rs.getDate("fecha_peticion"))
                            .setEmpleadoNombre(rs.getString("nombre_empleado"))
                            .setArticuloNombre(rs.getString("nombre_articulo"))
                            .setStockActual(rs.getInt("stock_actual"))
                            .build();
                    lista.add(dto);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al cargar la bandeja de pendientes.");
        }
    }

    // ==========================================
    // 3. SALIDAS: RECHAZAR
    // ==========================================
    public void rechazarSalida(int idCarrito) throws UserDisplayableException {
        String query = "UPDATE CarritoSolicitud SET estado = 'Rechazado' WHERE id_carrito = ?";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idCarrito);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al rechazar la solicitud.");
        }
    }

    // ==========================================
    // 4. SALIDAS: APROBAR (LLAMADA AL PROCEDURE)
    // ==========================================
    public void aprobarSalida(int idCarrito, int cantidadEntregada, int idSucursal) throws UserDisplayableException {
        String query = "{CALL SP_AprobarSalida(?, ?, ?)}";
        try (Connection conn = DBConnector.getInstance().getConnection();
             CallableStatement cs = conn.prepareCall(query)) {
            
            cs.setInt(1, idCarrito);
            cs.setInt(2, cantidadEntregada);
            cs.setInt(3, idSucursal);
            cs.execute();
            
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al ejecutar el procedimiento de aprobación de salida.");
        }
    }
}