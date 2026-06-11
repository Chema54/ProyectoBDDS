package main.business.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.business.dao.shape.CompleteDAOShape;
import main.business.dto.DirectorioEmpleadoDTO;
import main.business.dto.EmpleadoDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpleadoDAO extends CompleteDAOShape<EmpleadoDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(EmpleadoDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO Empleado (nombre, apellidos, id_departamento, numero_personal, telefono, correo) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_ALL_QUERY
            = "SELECT * FROM Empleado";
    private static final String GET_QUERY
            = "SELECT * FROM Empleado WHERE id_empleado = ?";
    private static final String UPDATE_QUERY
            = "UPDATE Empleado SET nombre = ?, apellidos = ?, id_departamento = ?, numero_personal = ?, telefono = ?, correo = ? WHERE id_empleado = ?";
    private static final String DELETE_QUERY
            = "DELETE FROM Empleado WHERE id_empleado = ?";

    @Override
    public void createOne(EmpleadoDTO empleadoDTO) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, empleadoDTO.getNombre());
            statement.setString(2, empleadoDTO.getApellidos());
            statement.setInt(3, empleadoDTO.getIDDepartamento());
            statement.setString(4, empleadoDTO.getNumeroPersonal());
            statement.setString(5, empleadoDTO.getTelefono());
            statement.setString(6, empleadoDTO.getCorreo());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el empleado.");
        }
    }

    @Override
    public List<EmpleadoDTO> getAll() throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); ResultSet resultSet = statement.executeQuery()) {
            List<EmpleadoDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                EmpleadoDTO empleadoDTO = new EmpleadoDTO.EmpleadoBuilder()
                        .setIDEmpleado(resultSet.getInt("id_empleado"))
                        .setNombre(resultSet.getString("nombre"))
                        .setApellidos(resultSet.getString("apellidos"))
                        .setIDDepartamento(resultSet.getInt("id_departamento"))
                        .setNumeroPersonal(resultSet.getString("numero_personal"))
                        .setTelefono(resultSet.getString("telefono"))
                        .setCorreo(resultSet.getString("correo"))
                        .build();
                list.add(empleadoDTO);
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los empleados.");
        }
    }

    @Override
    public EmpleadoDTO getOne(Integer id) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new EmpleadoDTO.EmpleadoBuilder()
                            .setIDEmpleado(resultSet.getInt("id_empleado"))
                            .setNombre(resultSet.getString("nombre"))
                            .setApellidos(resultSet.getString("apellidos"))
                            .setIDDepartamento(resultSet.getInt("id_departamento"))
                            .setNumeroPersonal(resultSet.getString("numero_personal"))
                            .setTelefono(resultSet.getString("telefono"))
                            .setCorreo(resultSet.getString("correo"))
                            .build();
                }
            }
            return null;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el empleado.");
        }
    }

    @Override
    public void updateOne(EmpleadoDTO empleadoDTO) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, empleadoDTO.getNombre());
            statement.setString(2, empleadoDTO.getApellidos());
            statement.setInt(3, empleadoDTO.getIDDepartamento());
            statement.setString(4, empleadoDTO.getNumeroPersonal());
            statement.setString(5, empleadoDTO.getTelefono());
            statement.setString(6, empleadoDTO.getCorreo());
            statement.setInt(7, empleadoDTO.getIDEmpleado());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el empleado.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el empleado.");
        }
    }

    public List<DirectorioEmpleadoDTO> getVistaDirectorio() throws UserDisplayableException {
        String query = "SELECT * FROM Vista_Directorio_Empleados";
        List<main.business.dto.DirectorioEmpleadoDTO> list = new ArrayList<>();

        try (Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(query); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                list.add(new main.business.dto.DirectorioEmpleadoDTO(
                        rs.getString("numero_personal"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("departamento"),
                        rs.getString("sucursal"),
                        rs.getString("usuario"),
                        rs.getInt("id_sucursal")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al cargar la vista del directorio.");
        }
    }
}
