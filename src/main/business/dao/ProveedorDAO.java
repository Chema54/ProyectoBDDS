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
import main.business.dto.ProveedorDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class ProveedorDAO extends CompleteDAOShape<ProveedorDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ProveedorDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO Proveedor (id_proveedor, razon_social, rfc, domicilio_fiscal, telefono) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY
            = "SELECT * FROM Proveedor";

    private static final String GET_QUERY
            = "SELECT * FROM Proveedor WHERE id_proveedor = ?";

    private static final String UPDATE_QUERY
            = "UPDATE Proveedor SET razon_social = ?, rfc = ?, domicilio_fiscal = ?, telefono = ? WHERE id_proveedor = ?";

    private static final String DELETE_QUERY
            = "DELETE FROM Proveedor WHERE id_proveedor = ?";

    @Override
    public void createOne(ProveedorDTO proveedorDTO) throws UserDisplayableException {

        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);) {
            statement.setInt(1, proveedorDTO.getIDProveedor());
            statement.setString(2, proveedorDTO.getRazonSocial());
            statement.setString(3, proveedorDTO.getRFC());
            statement.setString(4, proveedorDTO.getDomicilioFiscal());
            statement.setString(5, proveedorDTO.getTelefono());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el proveedor.");
        }
    }

    @Override
    public List<ProveedorDTO> getAll() throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); ResultSet resultSet = statement.executeQuery()) {
            List<ProveedorDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                ProveedorDTO proveedorDTO = new ProveedorDTO.ProveedorBuilder()
                        .setIDProveedor(resultSet.getInt("id_proveedor"))
                        .setRazonSocial(resultSet.getString("razon_social"))
                        .setRFC(resultSet.getString("rfc"))
                        .setDomicilioFiscal(resultSet.getString("domicilio_fiscal"))
                        .setTelefono(resultSet.getString("telefono"))
                        .build();
                list.add(proveedorDTO);
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los proveedores.");
        }
    }

    @Override
    public ProveedorDTO getOne(Integer id) throws UserDisplayableException {

        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_QUERY); ResultSet resultSet = statement.executeQuery();) {
            statement.setInt(1, id);
            if (resultSet.next()) {
                return new ProveedorDTO.ProveedorBuilder()
                        .setIDProveedor(resultSet.getInt("id_proveedor"))
                        .setRazonSocial(resultSet.getString("razon_social"))
                        .setRFC(resultSet.getString("rfc"))
                        .setDomicilioFiscal(resultSet.getString("domicilio_fiscal"))
                        .setTelefono(resultSet.getString("telefono"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el proveedor.");
        }
    }

    @Override
    public void updateOne(ProveedorDTO proveedorDTO) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);) {
            statement.setString(1, proveedorDTO.getRazonSocial());
            statement.setString(2, proveedorDTO.getRFC());
            statement.setString(3, proveedorDTO.getDomicilioFiscal());
            statement.setString(4, proveedorDTO.getTelefono());
            statement.setInt(5, proveedorDTO.getIDProveedor());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el proveedor.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el proveedor."
            );
        }
    }
}
