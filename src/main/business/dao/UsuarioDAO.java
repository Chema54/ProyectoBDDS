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
 * @author josem
 */
public class UsuarioDAO extends CompleteDAOShape<UsuarioDTO, String> {
    private static final Logger LOGGER = LogManager.getLogger(UsuarioDAO.class);
    
    private static final String GET_QUERY = 
        "SELECT u.*, ur.id_rol FROM Usuario u " +
        "INNER JOIN Usuario_Rol ur ON u.id_usuario = ur.id_usuario " +
        "WHERE u.nombre_usuario = ?";
        
    private static final String GET_ALL_QUERY = 
        "SELECT u.*, ur.id_rol FROM Usuario u " +
        "INNER JOIN Usuario_Rol ur ON u.id_usuario = ur.id_usuario";
        
    private static final String UPDATE_QUERY = 
        "UPDATE Usuario SET password = ?, estado = ? WHERE nombre_usuario = ?";
        
    private static final String DELETE_QUERY = 
        "DELETE FROM Usuario WHERE nombre_usuario = ?";

    @Override
    public UsuarioDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        int idRolDB = resultSet.getInt("id_rol");
        UsuarioRol rolEnum = UsuarioRol.CENTRAL; 
        
        if (idRolDB == 2) {
            rolEnum = UsuarioRol.SUCURSAL;
        } else if (idRolDB == 3) {
            rolEnum = UsuarioRol.SALIDAS;
        }

        return new UsuarioDTO(
            resultSet.getInt("id_empleado"),
            resultSet.getString("nombre_usuario"),
            resultSet.getString("password"),
            rolEnum,
            resultSet.getString("estado").equalsIgnoreCase("activo")
        );
    }

    @Override
    public void createOne(UsuarioDTO usuario) throws UserDisplayableException {
        String queryUsuario = "INSERT INTO Usuario (id_empleado, nombre_usuario, password, fecha_registro, estado) VALUES (?, ?, ?, ?, ?)";
        String queryRol = "INSERT INTO Usuario_Rol (id_usuario, id_rol) VALUES (?, ?)";

        Connection con = null;
        PreparedStatement psUsuario = null;
        PreparedStatement psRol = null;
        java.sql.ResultSet rs = null;

        try {
            con = DBConnector.getInstance().getConnection();
            con.setAutoCommit(false); 

            psUsuario = con.prepareStatement(queryUsuario, java.sql.Statement.RETURN_GENERATED_KEYS);
            psUsuario.setInt(1, usuario.getIdEmpleado());
            psUsuario.setString(2, usuario.getUsuario());
            psUsuario.setString(3, usuario.getContraseña()); 
            psUsuario.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now())); 
            psUsuario.setString(5, usuario.isTieneAcceso() ? "activo" : "inactivo"); 
            psUsuario.executeUpdate();

            rs = psUsuario.getGeneratedKeys();
            int idUsuarioGenerado = 0;
            if (rs.next()) {
                idUsuarioGenerado = rs.getInt(1);
            }

            psRol = con.prepareStatement(queryRol);
            psRol.setInt(1, idUsuarioGenerado);
            psRol.setInt(2, usuario.getRol().getIdRol());
            psRol.executeUpdate();

            con.commit(); 

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { LOGGER.error(ex); }
            }
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear la cuenta.");
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { LOGGER.error(e); }
            try { if (psUsuario != null) psUsuario.close(); } catch (SQLException e) { LOGGER.error(e); }
            try { if (psRol != null) psRol.close(); } catch (SQLException e) { LOGGER.error(e); }
            try { if (con != null) con.close(); } catch (SQLException e) { LOGGER.error(e); }
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
            list.add(getDTOInstanceFromResultSet(resultSet));
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
              accountDTO = getDTOInstanceFromResultSet(resultSet); // Corregido el nombre del método
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
          statement.setString(1, usuarioDTO.getContraseña());
          statement.setString(2, usuarioDTO.isTieneAcceso() ? "activo" : "inactivo");
          statement.setString(3, usuarioDTO.getUsuario());
          statement.executeUpdate();
          
          String updateRolQuery = "UPDATE Usuario_Rol ur INNER JOIN Usuario u ON u.id_usuario = ur.id_usuario SET ur.id_rol = ? WHERE u.nombre_usuario = ?";
          try (PreparedStatement statementRol = connection.prepareStatement(updateRolQuery)) {
              statementRol.setInt(1, usuarioDTO.getRol().getIdRol());
              statementRol.setString(2, usuarioDTO.getUsuario());
              statementRol.executeUpdate();
          }
          
        } catch (SQLException e) {
          throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar la cuenta.");
        }
    }

    @Override
    public void deleteOne(String nombreUsuario) throws UserDisplayableException {
        String deleteRolQuery = "DELETE ur FROM Usuario_Rol ur INNER JOIN Usuario u ON u.id_usuario = ur.id_usuario WHERE u.nombre_usuario = ?";
        try (
          Connection connection = DBConnector.getInstance().getConnection();
          PreparedStatement statementRol = connection.prepareStatement(deleteRolQuery);
          PreparedStatement statementUser = connection.prepareStatement(DELETE_QUERY)
        ) {
          statementRol.setString(1, nombreUsuario);
          statementRol.executeUpdate();
          
          statementUser.setString(1, nombreUsuario);
          statementUser.executeUpdate();
        } catch (SQLException e) {
          throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la cuenta.");
        }
    }
}