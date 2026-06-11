package main.business.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.business.dao.shape.CompleteDAOShape;
import main.business.dto.DepartamentoDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DepartamentoDAO extends CompleteDAOShape<DepartamentoDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(DepartamentoDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO Departamento (nombre_departamento, id_sucursal, id_encargado) VALUES (?, ?, ?)";
    private static final String GET_ALL_QUERY
            = "SELECT * FROM Departamento";
    private static final String GET_QUERY
            = "SELECT * FROM Departamento WHERE id_departamento = ?";
    private static final String UPDATE_QUERY
            = "UPDATE Departamento SET nombre_departamento = ?, id_sucursal = ?, id_encargado = ? WHERE id_departamento = ?";
    private static final String DELETE_QUERY
            = "DELETE FROM Departamento WHERE id_departamento = ?";

    @Override
    public void createOne(DepartamentoDTO departamentoDTO) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, departamentoDTO.getNombreDepartamento());
            statement.setInt(2, departamentoDTO.getIDSucursal());

            if (departamentoDTO.getIdEncargado() != null) {
                statement.setInt(3, departamentoDTO.getIdEncargado());
            } else {
                statement.setNull(3, java.sql.Types.INTEGER);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el departamento.");
        }
    }

    @Override
    public List<DepartamentoDTO> getAll() throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); ResultSet resultSet = statement.executeQuery()) {

            List<DepartamentoDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                DepartamentoDTO departamentoDTO = new DepartamentoDTO.DepartamentoBuilder()
                        .setIDDepartamento(resultSet.getInt("id_departamento"))
                        .setNombreDepartamento(resultSet.getString("nombre_departamento"))
                        .setIDSucursal(resultSet.getInt("id_sucursal"))
                        .setIdEncargado(resultSet.getInt("id_encargado"))
                        .build();
                list.add(departamentoDTO);
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los departamentos.");
        }
    }

    @Override
    public DepartamentoDTO getOne(Integer id) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new DepartamentoDTO.DepartamentoBuilder()
                            .setIDDepartamento(resultSet.getInt("id_departamento"))
                            .setNombreDepartamento(resultSet.getString("nombre_departamento"))
                            .setIDSucursal(resultSet.getInt("id_sucursal"))
                            .setIdEncargado(resultSet.getInt("id_encargado"))
                            .build();
                }
            }
            return null;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el departamento.");
        }
    }

    @Override
    public void updateOne(DepartamentoDTO departamentoDTO) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, departamentoDTO.getNombreDepartamento());
            statement.setInt(2, departamentoDTO.getIDSucursal());

            if (departamentoDTO.getIdEncargado() != null) {
                statement.setInt(3, departamentoDTO.getIdEncargado());
            } else {
                statement.setNull(3, java.sql.Types.INTEGER);
            }

            statement.setInt(4, departamentoDTO.getIDDepartamento());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el departamento.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el departamento.");
        }
    }

    public static List<DepartamentoDTO> obtenerDepartamentoPorSucursal(int idSucursal) throws SQLException, UserDisplayableException {
        String query = "SELECT * FROM Departamento WHERE id_sucursal = ?";
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idSucursal);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<DepartamentoDTO> departamentos = new ArrayList<>();
                while (resultSet.next()) {
                    departamentos.add(new DepartamentoDTO.DepartamentoBuilder()
                            .setIDDepartamento(resultSet.getInt("id_departamento"))
                            .setNombreDepartamento(resultSet.getString("nombre_departamento"))
                            .setIDSucursal(resultSet.getInt("id_sucursal"))
                            .setIdEncargado(resultSet.getInt("id_encargado"))
                            .build());
                }
                return departamentos;
            }
        }
    }
}
