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

    public List<ReporteKardexDTO> getMovimientosKardex(String tipoMovimiento, Date inicio, Date fin) throws UserDisplayableException {
        String query = "SELECT * FROM Vista_Kardex_Completo "
                + "WHERE tipo_movimiento = ? AND DATE(fecha) BETWEEN ? AND ? "
                + "ORDER BY partida, fecha";

        List<ReporteKardexDTO> lista = new ArrayList<>();
        try (Connection conn = DBConnector.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, tipoMovimiento);
            ps.setDate(2, inicio);
            ps.setDate(3, fin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ReporteKardexDTO(rs.getString("articulo"), rs.getString("partida") != null ? rs.getString("partida") : "Sin Partida", rs.getDate("fecha"), rs.getInt("cantidad"), rs.getDouble("costo"), rs.getDouble("costo_promedio"), rs.getString("tipo_movimiento"), rs.getString("referencia")));
                }
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al cargar movimientos.");
        }
        return lista;
    }

    public List<ReporteKardexDTO> getKardexArticulo(int idArticulo) throws UserDisplayableException {
        String query = "SELECT * FROM Vista_Kardex_Completo "
                + "WHERE id_articulo = ? ORDER BY fecha ASC, id_kardex ASC";
        List<ReporteKardexDTO> lista = new ArrayList<>();
        try (Connection conn = DBConnector.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idArticulo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ReporteKardexDTO(rs.getString("articulo"), rs.getString("partida"), rs.getDate("fecha"), rs.getInt("cantidad"), rs.getDouble("costo"), rs.getDouble("costo_promedio"), rs.getString("tipo_movimiento"), rs.getString("referencia")));
                }
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al cargar Kardex del artículo.");
        }
        return lista;
    }

    public List<ReportePedidoDTO> getPedidos() throws UserDisplayableException {
        String query = "SELECT b.fecha, b.cantidad_a_pedir, a.nombre AS articulo FROM BitacoraPedido b JOIN Articulo a ON b.id_articulo = a.id_articulo";
        List<ReportePedidoDTO> lista = new ArrayList<>();
        try (Connection conn = DBConnector.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ReportePedidoDTO(rs.getString("articulo"), rs.getInt("cantidad_a_pedir"), rs.getDate("fecha")));
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error cargar pedidos.");
        }
        return lista;
    }

    public List<ReporteBajaDTO> getBajas() throws UserDisplayableException {
        String query = "SELECT b.fecha, b.motivo, b.cantidad_restante, a.nombre AS articulo FROM BitacoraBaja b JOIN Articulo a ON b.id_articulo = a.id_articulo";
        List<ReporteBajaDTO> lista = new ArrayList<>();
        try (Connection conn = DBConnector.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ReporteBajaDTO(rs.getString("articulo"), rs.getDate("fecha"), rs.getString("motivo"), rs.getInt("cantidad_restante")));
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error cargar bajas.");
        }
        return lista;
    }

    public void vaciarBitacoraPedidos() throws UserDisplayableException {
        try (Connection conn = DBConnector.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM BitacoraPedido")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al vaciar bitácora.");
        }
    }

    public java.util.List<ReporteKardexDTO> getReporteAgrupadoPorPartida(String tipoMovimiento, java.sql.Date inicio, java.sql.Date fin) throws main.common.UserDisplayableException {
        String query = "SELECT " +
                       "partida, " +
                       "'VARIOS (Agrupado)' AS articulo, " + 
                       "MAX(fecha) AS fecha, " +
                       "SUM(cantidad) AS cantidad, " +
                       "SUM(importe_total) AS costo, " + 
                       "0.0 AS costo_promedio, " +
                       "'Resumen por Partida' AS referencia " +
                       "FROM Vista_Kardex_Partidas " +
                       "WHERE tipo_movimiento = ? AND fecha BETWEEN ? AND ? " +
                       "GROUP BY partida"; 
        java.util.List<ReporteKardexDTO> lista = new java.util.ArrayList<>();
        try (java.sql.Connection con = main.database.DBConnector.getInstance().getConnection(); java.sql.PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, tipoMovimiento);
            ps.setDate(2, inicio);
            ps.setDate(3, fin);

            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new main.business.dto.ReporteKardexDTO(
                            rs.getString("articulo"),
                            rs.getString("partida"),
                            rs.getDate("fecha"),
                            rs.getInt("cantidad"),
                            rs.getDouble("costo"),
                            rs.getDouble("costo_promedio"),
                            tipoMovimiento,
                            rs.getString("referencia")
                    ));
                }
            }
            return lista;
        } catch (java.sql.SQLException e) {
            throw main.common.ExceptionHandler.handleSQLException(org.apache.logging.log4j.LogManager.getLogger(ReportesDAO.class), e, "Error al generar reporte agrupado usando la Vista.");
        }
    }
}
