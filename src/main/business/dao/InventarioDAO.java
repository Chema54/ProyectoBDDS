package main.business.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.business.dao.shape.CompleteDAOShape;
import main.business.dto.InventarioDTO;
import main.business.dto.VistaInventarioDTO; // Importado para limpiar el código
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InventarioDAO extends CompleteDAOShape<InventarioDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(InventarioDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO Inventario (id_sucursal, id_articulo, cantidad, stock_minimo, stock_maximo) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY
            = "SELECT * FROM Inventario";

    private static final String GET_QUERY
            = "SELECT * FROM Inventario WHERE id_articulo = ?";

    private static final String UPDATE_QUERY
            = "UPDATE Inventario SET cantidad = ?, stock_minimo = ?, stock_maximo = ? WHERE id_sucursal = ? AND id_articulo = ?";

    private static final String DELETE_QUERY
            = "DELETE FROM Inventario WHERE id_articulo = ?";

    @Override
    public void createOne(InventarioDTO inventarioDTO) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); 
                PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            
            statement.setInt(1, inventarioDTO.getIDSucursal());
            statement.setInt(2, inventarioDTO.getIDArticulo());
            // Nota: Cambiar a setInt si en la BD son enteros
            statement.setString(3, inventarioDTO.getCantidad());
            statement.setString(4, inventarioDTO.getStockMinimo());
            statement.setString(5, inventarioDTO.getStockMaximo());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear el inventario.");
        }
    }

    @Override
    public List<InventarioDTO> getAll() throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); 
                PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); 
                ResultSet resultSet = statement.executeQuery()) {
            
            List<InventarioDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                InventarioDTO inventarioDTO = new InventarioDTO.InventarioBuilder()
                        .setIDSucursal(resultSet.getInt("id_sucursal"))
                        .setIDArticulo(resultSet.getInt("id_articulo"))
                        .setCantidad(resultSet.getString("cantidad")) // Cambiar a getInt si aplica
                        .setStockMinimo(resultSet.getString("stock_minimo"))
                        .setStockMaximo(resultSet.getString("stock_maximo"))
                        .build();
                list.add(inventarioDTO);
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar el inventario.");
        }
    }

    @Override
    public InventarioDTO getOne(Integer id) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); 
                PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) { 
                if (resultSet.next()) {
                    return new InventarioDTO.InventarioBuilder()
                            .setIDSucursal(resultSet.getInt("id_sucursal"))
                            .setIDArticulo(resultSet.getInt("id_articulo"))
                            .setCantidad(resultSet.getString("cantidad"))
                            .setStockMinimo(resultSet.getString("stock_minimo"))
                            .setStockMaximo(resultSet.getString("stock_maximo"))
                            .build();
                }
            }
            return null;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el inventario.");
        }
    }

    @Override
    public void updateOne(InventarioDTO inventarioDTO) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); 
                PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            
            statement.setString(1, inventarioDTO.getCantidad());
            statement.setString(2, inventarioDTO.getStockMinimo());
            statement.setString(3, inventarioDTO.getStockMaximo());
            statement.setInt(4, inventarioDTO.getIDSucursal());
            statement.setInt(5, inventarioDTO.getIDArticulo());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el inventario.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws UserDisplayableException {
        try (
                Connection connection = DBConnector.getInstance().getConnection(); 
                PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el inventario.");
        }
    }

    public List<VistaInventarioDTO> getVistaInventario(Integer idSucursalFiltro) throws UserDisplayableException {
        StringBuilder query = new StringBuilder("SELECT * FROM Vista_Inventario_Sucursales");
        if (idSucursalFiltro != null && idSucursalFiltro > 0) {
            query.append(" WHERE id_sucursal = ?");
        }

        try (
                Connection connection = DBConnector.getInstance().getConnection(); 
                PreparedStatement statement = connection.prepareStatement(query.toString())) {
            
            if (idSucursalFiltro != null && idSucursalFiltro > 0) {
                statement.setInt(1, idSucursalFiltro);
            }

            List<VistaInventarioDTO> list = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    list.add(new VistaInventarioDTO(
                            rs.getInt("id_sucursal"),
                            rs.getString("nombre_sucursal"),
                            rs.getInt("id_articulo"),
                            rs.getString("nombre_articulo"),
                            rs.getInt("stock_actual"),
                            rs.getInt("stock_minimo"),
                            rs.getInt("stock_maximo")
                    ));
                }
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al leer la vista de Inventario.");
        }
    }
}