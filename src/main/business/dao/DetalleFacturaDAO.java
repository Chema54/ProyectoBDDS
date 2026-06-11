/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.business.dao.shape.CompleteDAOShape;
import main.business.dto.DetalleFacturaDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class DetalleFacturaDAO extends CompleteDAOShape<DetalleFacturaDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(DetalleFacturaDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO DetalleFactura (id_detalle, id_factura, id_articulo, cantidad, costo) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY
            = "SELECT * FROM DetalleFactura";

    private static final String GET_QUERY
            = "SELECT * FROM DetalleFactura WHERE id_detalle = ?";

    private static final String UPDATE_QUERY
            = "UPDATE DetalleFactura SET id_factura = ?, id_articulo = ?, cantidad = ?, costo = ? WHERE id_detalle = ?";

    private static final String DELETE_QUERY
            = "DELETE FROM DetalleFactura WHERE id_detalle = ?";

    @Override
    public void createOne(DetalleFacturaDTO detalleFacturaDTO) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, detalleFacturaDTO.getIDDetalle());
            statement.setInt(2, detalleFacturaDTO.getIDFactura());
            statement.setInt(3, detalleFacturaDTO.getIDArticulo());
            statement.setInt(4, detalleFacturaDTO.getCantidad());
            statement.setDouble(5, detalleFacturaDTO.getCosto());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el detalle de factura.");
        }
    }

    @Override
    public List<DetalleFacturaDTO> getAll() throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); ResultSet resultSet = statement.executeQuery()) {
            List<DetalleFacturaDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                DetalleFacturaDTO detalleFacturaDto = new DetalleFacturaDTO.DetalleFacturaBuilder()
                        .setIDDetalle(resultSet.getInt("id_detalle"))
                        .setIDFactura(resultSet.getInt("id_factura"))
                        .setIDArticulo(resultSet.getInt("id_articulo"))
                        .setCantidad(resultSet.getInt("cantidad"))
                        .setCosto(resultSet.getDouble("costo"))
                        .build();
                list.add(detalleFacturaDto);
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los detalles de factura.");
        }
    }

    @Override
    public DetalleFacturaDTO getOne(Integer id) throws UserDisplayableException {

        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_QUERY); ResultSet resultSet = statement.executeQuery();) {
            statement.setInt(1, id);
            if (resultSet.next()) {
                return new DetalleFacturaDTO.DetalleFacturaBuilder()
                        .setIDDetalle(resultSet.getInt("id_detalle"))
                        .setIDFactura(resultSet.getInt("id_factura"))
                        .setIDArticulo(resultSet.getInt("id_articulo"))
                        .setCantidad(resultSet.getInt("cantidad"))
                        .setCosto(resultSet.getDouble("costo"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el detalle de factura.");
        }
    }

    @Override
    public void updateOne(DetalleFacturaDTO detalleFacturaDTO) throws UserDisplayableException {

        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, detalleFacturaDTO.getIDFactura());
            statement.setInt(2, detalleFacturaDTO.getIDArticulo());
            statement.setInt(3, detalleFacturaDTO.getCantidad());
            statement.setDouble(4, detalleFacturaDTO.getCosto());
            statement.setInt(5, detalleFacturaDTO.getIDDetalle());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el detalle de factura.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el detalle de factura.");
        }
    }
}
