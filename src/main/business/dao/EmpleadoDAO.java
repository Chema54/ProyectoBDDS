package main.business.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.business.dao.shape.CompleteDAOShape;
import main.business.dto.EmpleadoDTO;
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
public class EmpleadoDAO extends CompleteDAOShape<EmpleadoDTO, Integer> {
    private static final Logger LOGGER = LogManager.getLogger(EmpleadoDAO.class);
    private static final String CREATE_QUERY = "INSERT INTO Empleado (id_empleado, nombre, apellidos, id_departamento) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM Empleado";
    private static final String GET_QUERY = "SELECT * FROM Empleado WHERE id_empleado = ?";
    private static final String UPDATE_QUERY = "UPDATE Empleado SET apellidos = ?, nombre = ? WHERE id_empleado = ? AND id_departamento = ?";
    private static final String DELETE_QUERY = "DELETE FROM Empleado WHERE id_empleado";
  
    @Override
    public EmpleadoDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        return new EmpleadoDTO.EmpleadoBuilder()
            .setIDEmpleado(resultSet.getInt("id_empleado"))
            .setNombre(resultSet.getString("nombre"))
            .setApellidos(resultSet.getString("apellidos"))
            .setIDDepartamento(resultSet.getInt("id_departamento"))
            .build();
    }

    @Override
    public void createOne(EmpleadoDTO empleadoDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, empleadoDTO.getIDEmpleado());
            statement.setString(2, empleadoDTO.getNombre());
            statement.setString(3, empleadoDTO.getApellidos());
            statement.setInt(4, empleadoDTO.getIDDepartamento());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el articulo.");
        }
    }

    @Override
    public List<EmpleadoDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<EmpleadoDTO> list = new ArrayList<>();

            while (resultSet.next()) {
              list.add(createDTOInstanceFromResultSet(resultSet));
          }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los articulos.");
        }
    }

    @Override
    public EmpleadoDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setInt(1, id);

            EmpleadoDTO empleadoDTO = null;

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    empleadoDTO = createDTOInstanceFromResultSet(resultSet);
                }
            }

            return empleadoDTO;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el articulo.");
        }
    }

    @Override
    public void updateOne(EmpleadoDTO empleadoDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, empleadoDTO.getApellidos());
            statement.setString(2, empleadoDTO.getNombre());
            statement.setInt(3, empleadoDTO.getIDEmpleado());
            statement.setInt(4, empleadoDTO.getIDDepartamento());
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