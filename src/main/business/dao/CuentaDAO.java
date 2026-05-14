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
import main.business.dto.CuentaDTO;
import main.business.dto.enumeraiones.CuentaRol;
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
public class CuentaDAO extends CompleteDAOShape<CuentaDTO, String> {
    private static final Logger LOGGER = LogManager.getLogger(CuentaDAO.class);
    private static final String CREATE_QUERY = "INSERT INTO Account (email, password, role) VALUES (?, ?, ?)";
    private static final String GET_QUERY = "SELECT * FROM Account WHERE email = ?";
    private static final String GET_ALL_QUERY = "SELECT * FROM Account";
    private static final String UPDATE_QUERY = "UPDATE Account SET password = ?, role = ? WHERE email = ?";
    private static final String DELETE_QUERY = "DELETE FROM Account WHERE email = ?";
    private static final String CHECK_COORDINATOR_ACCOUNT = "SELECT COUNT(*) FROM Account WHERE role = 'COORDINATOR'";

    @Override
    public CuentaDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        return new CuentaDTO(
            resultSet.getString("email"),
            resultSet.getString("password"),
            CuentaRol.valueOf(resultSet.getString("role")),
            resultSet.getBoolean("has_access")
        );
    }

  @Override
  public void createOne(CuentaDTO cuentaDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
    ) {
      statement.setString(1, cuentaDTO.getUsuario());
      statement.setString(2, cuentaDTO.getContraseña());
      statement.setString(3, cuentaDTO.getRol().toDBString());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear la cuenta.");
    }
  }

  @Override
  public List<CuentaDTO> getAll() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
      ResultSet resultSet = statement.executeQuery()
    ) {
      List<CuentaDTO> list = new ArrayList<>();

      while (resultSet.next()) {
        list.add(createDTOInstanceFromResultSet(resultSet));
      }

      return list;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las cuentas.");
    }
  }

  @Override
  public CuentaDTO getOne(String email) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(GET_QUERY)
    ) {
      statement.setString(1, email);

      CuentaDTO accountDTO = null;

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
  public void updateOne(CuentaDTO cuentaDTO) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
    ) {
      statement.setString(1, BCrypt.hashpw(cuentaDTO.getContraseña(), BCrypt.gensalt()));
      statement.setString(2, cuentaDTO.getRol().toDBString());
      statement.setString(3, cuentaDTO.getUsuario());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar la cuenta.");
    }
  }

  @Override
  public void deleteOne(String email) throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
    ) {
      statement.setString(1, email);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la cuenta.");
    }
  }

  public boolean hasCoordinatorAccount() throws UserDisplayableException {
    try (
      Connection connection = DBConnector.getInstance().getConnection();
      PreparedStatement statement = connection.prepareStatement(CHECK_COORDINATOR_ACCOUNT);
      ResultSet resultSet = statement.executeQuery()
    ) {
      if (resultSet.next()) {
        return resultSet.getInt(1) > 0;
      }

      return false;
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible verificar la existencia de una cuenta de coordinador.");
    }
  }
}
