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
import main.business.dto.UsuarioDTO;
import main.business.dto.enumeraiones.UsuarioRol;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author josem
 */
public class UsuarioDAO extends CompleteDAOShape<UsuarioDTO, String> {
    private static final Logger LOGGER = LogManager.getLogger(UsuarioDAO.class);
    private static final String CREATE_QUERY = "INSERT INTO Usuario (nombre_usuario, password, rol) VALUES (?, ?, ?)";
    private static final String GET_QUERY = "SELECT * FROM Usuario WHERE nombre_usuario = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM Usuario";
    private static final String UPDATE_QUERY = "UPDATE Usuario SET password = ?, role = ? WHERE nombre_usuario = ?";
    private static final String DELETE_QUERY = "DELETE FROM Usuario WHERE nombre_usuario = ?";

    @Override
    public UsuarioDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        return new UsuarioDTO(
            resultSet.getString("nombre_usuario"),
            resultSet.getString("password"),
            UsuarioRol.valueOf(resultSet.getString("rol")),
            resultSet.getBoolean("has_access")
        );
    }

  @Override
  public void createOne(UsuarioDTO usuarioDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, usuarioDTO.getUsuario());
      statement.setString(2, usuarioDTO.getContraseña());
      statement.setString(3, usuarioDTO.getRol().toDBString());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear la cuenta.");
    }
  }

  @Override
  public List<UsuarioDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<UsuarioDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las cuentas.");
    }
  }

  @Override
  public UsuarioDTO getOne(String nombreUsuario) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, nombreUsuario);

      UsuarioDTO accountDTO = null;

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          accountDTO = createDTOInstanceFromResultSet(resultSet);
        }
      }

      return accountDTO;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar la cuenta.");
    }
  }

  @Override
  public void updateOne(UsuarioDTO usuarioDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, BCrypt.hashpw(usuarioDTO.getContraseña(), BCrypt.gensalt()));
      statement.setString(2, usuarioDTO.getRol().toDBString());
      statement.setString(3, usuarioDTO.getUsuario());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar la cuenta.");
    }
  }

  @Override
  public void deleteOne(String nombreUsuario) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, nombreUsuario);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la cuenta.");
    }
  }
}
