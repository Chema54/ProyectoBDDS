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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author josem
 */
public class ArticuloDAO extends CompleteDAOShape<ArticuloDTO, Integer> {
    private static final Logger LOGGER = LogManager.getLogger(ArticuloDAO.class);
    private static final String CREATE_QUERY = "INSERT INTO Articulo (id_articulo, descripcion, partida_presupuestal) VALUES (?, ?, ?)";
    private static final String GET_ALL_QUERY = "SELECT * FROM Articulo";
    private static final String GET_QUERY = "SELECT * FROM Articulo WHERE id_articulo = ?";
    private static final String UPDATE_QUERY = "UPDATE Articulo SET partida_presupuestal = ?, descripcion = ? WHERE id_articulo = ?";
    private static final String DELETE_QUERY = "DELETE FROM Articulo WHERE id_articulo";
  
    @Override
    public ArticuloDTO getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException {
        return new ArticuloDTO.ArticuloBuilder()
            .setIDArticulo(resultSet.getInt("id_articulo"))
            .setDescripcion(resultSet.getString("descripcion"))
            .setPartidaPresupuestal(resultSet.getString("reason_of_assignation"))
            .build();
    }

    @Override
    public void createOne(ArticuloDTO articuloDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, articuloDTO.getIDArticulo());
            statement.setString(2, articuloDTO.getDescripcion());
            statement.setString(3, articuloDTO.getPartidaPresupuestal());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el articulo.");
        }
    }

    @Override
    public List<ArticuloDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ArticuloDTO> list = new ArrayList<>();

            while (resultSet.next()) {
              list.add(createDTOInstanceFromResultSet(resultSet));
          }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar los articulos.");
        }
    }

    @Override
    public ArticuloDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setInt(1, id);

            ArticuloDTO articuloDTO = null;

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    articuloDTO = createDTOInstanceFromResultSet(resultSet);
                }
            }

            return articuloDTO;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el articulo.");
        }
    }

    @Override
    public void updateOne(ArticuloDTO articuloDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, articuloDTO.getPartidaPresupuestal());
            statement.setString(2, articuloDTO.getDescripcion());
            statement.setInt(3, articuloDTO.getIDArticulo());
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
