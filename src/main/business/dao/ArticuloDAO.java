package main.business.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.business.dto.ArticuloDTO;
import main.business.dao.shape.CompleteDAOShape;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArticuloDAO extends CompleteDAOShape<ArticuloDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ArticuloDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO Articulo (descripcion, id_partida, estado, nombre) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_QUERY
            = "SELECT * FROM Articulo;";
    private static final String GET_QUERY
            = "SELECT * FROM Articulo WHERE id_articulo = ?";
    private static final String UPDATE_QUERY
            = "UPDATE Articulo SET descripcion = ?, id_partida = ?, estado = ?, nombre = ? WHERE id_articulo = ?";
    private static final String DELETE_QUERY
            = "DELETE FROM Articulo WHERE id_articulo = ?";

    @Override
    public void createOne(ArticuloDTO articuloDTO) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, articuloDTO.getDescripcion());
            statement.setInt(2, articuloDTO.getIdPartida());
            statement.setString(3, articuloDTO.getEstado());
            statement.setString(4, articuloDTO.getNombre());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el articulo.");
        }
    }

    @Override
    public List<ArticuloDTO> getAll() throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); ResultSet resultSet = statement.executeQuery()) {
            List<ArticuloDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                ArticuloDTO articuloDTO = new ArticuloDTO.ArticuloBuilder()
                        .setIDArticulo(resultSet.getInt("id_articulo"))
                        .setDescripcion(resultSet.getString("descripcion"))
                        .setIdPartida(resultSet.getInt("id_partida"))
                        .setEstado(resultSet.getString("estado"))
                        .setNombre(resultSet.getString("nombre"))
                        .build();
                list.add(articuloDTO);
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los articulos.");
        }
    }

    @Override
    public ArticuloDTO getOne(Integer id) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new ArticuloDTO.ArticuloBuilder()
                            .setIDArticulo(resultSet.getInt("id_articulo"))
                            .setDescripcion(resultSet.getString("descripcion"))
                            .setIdPartida(resultSet.getInt("id_partida"))
                            .setEstado(resultSet.getString("estado"))
                            .setNombre(resultSet.getString("nombre"))
                            .build();
                }
                return null;
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el articulo.");
        }
    }

    @Override
    public void updateOne(ArticuloDTO articuloDTO) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, articuloDTO.getDescripcion());
            statement.setInt(2, articuloDTO.getIdPartida());
            statement.setString(3, articuloDTO.getEstado());
            statement.setString(4, articuloDTO.getNombre());
            statement.setInt(5, articuloDTO.getIDArticulo());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el articulo.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el articulo.");
        }
    }

    public void darDeBaja(int idArticulo, String motivo) throws UserDisplayableException {
        String setMotivo = "SET @motivo_baja = ?";
        String updateEstado = "UPDATE Articulo SET estado = 'inactivo' WHERE id_articulo = ?";

        try (Connection conn = DBConnector.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement psMotivo = conn.prepareStatement(setMotivo)) {
                    psMotivo.setString(1, motivo);
                    psMotivo.execute();
                }
                try (PreparedStatement psUpdate = conn.prepareStatement(updateEstado)) {
                    psUpdate.setInt(1, idArticulo);
                    psUpdate.executeUpdate();
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                try (java.sql.Statement st = conn.createStatement()) {
                    st.execute("SET @motivo_baja = NULL");
                } catch (Exception ignored) {
                }
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al dar de baja el artículo mediante Trigger.");
        }
    }

    public List<ArticuloDTO> getAllActivos() throws UserDisplayableException {
        String query = "SELECT * FROM Articulo WHERE estado = 'activo'";
        List<ArticuloDTO> list = new ArrayList<>();
        try (Connection connection = DBConnector.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(new ArticuloDTO.ArticuloBuilder()
                        .setIDArticulo(resultSet.getInt("id_articulo"))
                        .setDescripcion(resultSet.getString("descripcion"))
                        .setIdPartida(resultSet.getInt("id_partida"))
                        .setEstado(resultSet.getString("estado"))
                        .setNombre(resultSet.getString("nombre"))
                        .build());
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al cargar artículos activos.");
        }
        return list;
    }
}
