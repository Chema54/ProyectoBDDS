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
import main.business.dto.FacturaDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class FacturaDAO extends CompleteDAOShape<FacturaDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(FacturaDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO Factura (id_factura, folio, fecha, id_proveedor) VALUES (?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Factura";

    private static final String GET_QUERY =
            "SELECT * FROM Factura WHERE id_factura = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Factura SET folio = ?, fecha = ?, id_proveedor = ? WHERE id_factura = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Factura WHERE id_factura = ?";

    @Override
    public FacturaDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {

        return new FacturaDTO.FacturaBuilder()
                .setIDFactura(resultSet.getInt("id_factura"))
                .setFolio(resultSet.getString("folio"))
                .setFecha(resultSet.getString("fecha"))
                .setIDProveedor(resultSet.getInt("id_proveedor"))
                .build();
    }

    @Override
    public void createOne(FacturaDTO facturaDTO) throws UserDisplayableException {

        try (
                Connection connection = DBConnector.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {

            statement.setInt(1, facturaDTO.getIDFactura());
            statement.setString(2, facturaDTO.getFolio());
            statement.setString(3, facturaDTO.getFecha());
            statement.setInt(4, facturaDTO.getIDProveedor());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw ExceptionHandler.handleSQLException(
                    LOGGER,
                    e,
                    "No ha sido posible crear la factura."
            );
        }
    }

    @Override
    public List<FacturaDTO> getAll() throws UserDisplayableException {

        try (
                Connection connection = DBConnector.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
                ResultSet resultSet = statement.executeQuery()
        ) {

            List<FacturaDTO> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(getDTOInstanceFromResultSet(resultSet));
            }

            return list;

        } catch (SQLException e) {

            throw ExceptionHandler.handleSQLException(
                    LOGGER,
                    e,
                    "No ha sido posible cargar las facturas."
            );
        }
    }

    @Override
    public FacturaDTO getOne(Integer id) throws UserDisplayableException {

        try (
                Connection connection = DBConnector.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {

            statement.setInt(1, id);

            FacturaDTO facturaDTO = null;

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    facturaDTO = getDTOInstanceFromResultSet(resultSet);
                }
            }

            return facturaDTO;

        } catch (SQLException e) {

            throw ExceptionHandler.handleSQLException(
                    LOGGER,
                    e,
                    "No ha sido posible obtener la factura."
            );
        }
    }

    @Override
    public void updateOne(FacturaDTO facturaDTO) throws UserDisplayableException {

        try (
                Connection connection = DBConnector.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {

            statement.setString(1, facturaDTO.getFolio());
            statement.setString(2, facturaDTO.getFecha());
            statement.setInt(3, facturaDTO.getIDProveedor());
            statement.setInt(4, facturaDTO.getIDFactura());

            statement.executeUpdate();

        } catch (SQLException e) {

            throw ExceptionHandler.handleSQLException(
                    LOGGER,
                    e,
                    "No ha sido posible actualizar la factura."
            );
        }
    }

    @Override
    public void deleteOne(Integer id) throws UserDisplayableException {

        try (
                Connection connection = DBConnector.getInstance().getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw ExceptionHandler.handleSQLException(
                    LOGGER,
                    e,
                    "No ha sido posible eliminar la factura."
            );
        }
    }
}