package main.business.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.business.dao.shape.CompleteDAOShape;
import main.business.dto.SucursalDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author josem
 */
public class SucursalDAO extends CompleteDAOShape<SucursalDTO, Integer> {
    private static final Logger LOGGER = LogManager.getLogger(SucursalDAO.class);
    private static final String CREATE_QUERY = "INSERT INTO Sucursal (id_sucursal, nombre, direccion) VALUES (?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM Sucursal";
    private static final String GET_QUERY = "SELECT * FROM Sucursal WHERE id_sucursal = ?";
    private static final String UPDATE_QUERY = "UPDATE Sucursal SET direccion = ?, nombre = ? WHERE id_sucursal = ?";
    private static final String DELETE_QUERY = "DELETE FROM Sucursal WHERE id_sucursal";
  
    @Override
    public SucursalDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        return new SucursalDTO.SucursalBuilder()
            .setIDSucursal(resultSet.getInt("id_sucursal"))
            .setNombre(resultSet.getString("nombre"))
            .setDireccion(resultSet.getString("direccion"))
            .build();
    }

    @Override
    public void createOne(SucursalDTO sucursalDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, sucursalDTO.getIDSucursal());
            statement.setString(2, sucursalDTO.getNombre());
            statement.setString(3, sucursalDTO.getDireccion());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el articulo.");
        }
    }

    @Override
    public List<SucursalDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<SucursalDTO> list = new ArrayList<>();

            while (resultSet.next()) {
              list.add(createDTOInstanceFromResultSet(resultSet));
          }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los articulos.");
        }
    }

    @Override
    public SucursalDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setInt(1, id);

            SucursalDTO sucursalDTO = null;

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    sucursalDTO = createDTOInstanceFromResultSet(resultSet);
                }
            }

            return sucursalDTO;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el articulo.");
        }
    }

    @Override
    public void updateOne(SucursalDTO sucursalDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, sucursalDTO.getDireccion());
            statement.setString(2, sucursalDTO.getNombre());
            statement.setInt(3, sucursalDTO.getIDSucursal());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el articulo.");
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
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la práctica.");
        }
    }

}