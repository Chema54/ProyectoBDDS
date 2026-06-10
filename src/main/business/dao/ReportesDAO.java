package main.business.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.business.dto.ReporteBajaDTO;
import main.business.dto.ReporteKardexDTO;
import main.business.dto.ReportePedidoDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReportesDAO {
    private static final Logger LOGGER = LogManager.getLogger(ReportesDAO.class);

    // Obtiene Ingresos o Egresos (Kardex) filtrados por fecha
    public List<ReporteKardexDTO> getMovimientosKardex(String tipoMovimiento, Date inicio, Date fin) throws UserDisplayableException {
        String query = "SELECT k.fecha, k.cantidad, k.costo, k.costo_promedio, a.nombre AS articulo, p.descripcion AS partida " +
                       "FROM Kardex k " +
                       "JOIN Articulo a ON k.id_articulo = a.id_articulo " +
                       "LEFT JOIN PartidaPresupuestal p ON a.id_partida = p.id_partida " +
                       "WHERE k.tipo_movimiento = ? AND k.fecha BETWEEN ? AND ? " +
                       "ORDER BY p.descripcion, k.fecha";
        
        List<ReporteKardexDTO> lista = new ArrayList<>();
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, tipoMovimiento);
            ps.setDate(2, inicio);
            ps.setDate(3, fin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ReporteKardexDTO(
                        rs.getString("articulo"), rs.getString("partida"), rs.getDate("fecha"),
                        rs.getInt("cantidad"), rs.getDouble("costo"), rs.getDouble("costo_promedio")
                    ));
                }
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al cargar el Kardex.");
        }
        return lista;
    }

    public List<ReportePedidoDTO> getPedidos() throws UserDisplayableException {
        String query = "SELECT b.fecha, b.cantidad_a_pedir, a.nombre AS articulo FROM BitacoraPedido b JOIN Articulo a ON b.id_articulo = a.id_articulo";
        List<ReportePedidoDTO> lista = new ArrayList<>();
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ReportePedidoDTO(rs.getString("articulo"), rs.getInt("cantidad_a_pedir"), rs.getDate("fecha")));
            }
        } catch (SQLException e) { throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al cargar pedidos."); }
        return lista;
    }

    public List<ReporteBajaDTO> getBajas() throws UserDisplayableException {
        String query = "SELECT b.fecha, b.motivo, b.cantidad_restante, a.nombre AS articulo FROM BitacoraBaja b JOIN Articulo a ON b.id_articulo = a.id_articulo";
        List<ReporteBajaDTO> lista = new ArrayList<>();
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ReporteBajaDTO(rs.getString("articulo"), rs.getDate("fecha"), rs.getString("motivo"), rs.getInt("cantidad_restante")));
            }
        } catch (SQLException e) { throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al cargar bajas."); }
        return lista;
    }

    // El método maestro que limpia la tabla de pedidos DESPUÉS de exportar a Excel
    public void vaciarBitacoraPedidos() throws UserDisplayableException {
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM BitacoraPedido")) {
            ps.executeUpdate();
        } catch (SQLException e) { throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al vaciar la bitácora."); }
    }
}