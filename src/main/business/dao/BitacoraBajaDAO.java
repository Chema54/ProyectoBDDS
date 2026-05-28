/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.business.dao.shape.CompleteDAOShape;
import main.business.dto.BitacoraBajaDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class BitacoraBajaDAO extends CompleteDAOShape<BitacoraBajaDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(BitacoraBajaDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO BitacoraBaja (id_bitacora, id_articulo, fecha, motivo, cantidad_restante) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM BitacoraBaja";

    private static final String GET_QUERY =
            "SELECT * FROM BitacoraBaja WHERE id_bitacora = ?";

    private static final String UPDATE_QUERY =
            "UPDATE BitacoraBaja SET id_articulo = ?, fecha = ?, motivo = ?, cantidad_restante = ? WHERE id_bitacora = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM BitacoraBaja WHERE id_bitacora = ?";
    
    @Override
    public void createOne(BitacoraBajaDTO bitacoraBajaDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, bitacoraBajaDTO.getIDBitacora());
            statement.setInt(2, bitacoraBajaDTO.getIDArticulo());
            statement.setDate(3, new Date(bitacoraBajaDTO.getFecha().getTime()));
            statement.setString(4, bitacoraBajaDTO.getMotivo());
            statement.setInt(5, bitacoraBajaDTO.getCantidadRestante());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible crear la bitácora de baja.");
        }
    }

    @Override
    public List<BitacoraBajaDTO> getAll() throws UserDisplayableException {

        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<BitacoraBajaDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                BitacoraBajaDTO bitacoraBajaDTO =new BitacoraBajaDTO.BitacoraBajaBuilder()
                .setIDBitacora(resultSet.getInt("id_bitacora"))
                .setIDArticulo(resultSet.getInt("id_articulo"))
                .setFecha(resultSet.getDate("fecha"))
                .setMotivo(resultSet.getString("motivo"))
                .setCantidadRestante(resultSet.getInt("cantidad_restante"))
                .build();
                list.add(bitacoraBajaDTO);
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER,e,"No ha sido posible cargar las bitácoras de baja.");
        }
    }

    @Override
    public BitacoraBajaDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY);
            ResultSet resultSet = statement.executeQuery();
        ) {
            statement.setInt(1, id);
            if (resultSet.next()) {
                return new BitacoraBajaDTO.BitacoraBajaBuilder()
                .setIDBitacora(resultSet.getInt("id_bitacora"))
                .setIDArticulo(resultSet.getInt("id_articulo"))
                .setFecha(resultSet.getDate("fecha"))
                .setMotivo(resultSet.getString("motivo"))
                .setCantidadRestante(resultSet.getInt("cantidad_restante"))
                .build();
            }
            return null;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER,e,"No ha sido posible obtener la bitácora de baja.");
        }
    }

    @Override
    public void updateOne(BitacoraBajaDTO bitacoraBajaDTO) throws UserDisplayableException {

        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, bitacoraBajaDTO.getIDArticulo());
            statement.setDate(2, new Date(bitacoraBajaDTO.getFecha().getTime()));
            statement.setString(3, bitacoraBajaDTO.getMotivo());
            statement.setInt(4, bitacoraBajaDTO.getCantidadRestante());
            statement.setInt(5, bitacoraBajaDTO.getIDBitacora());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar la bitácora de baja.");
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
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la bitácora de baja.");
        }
    }
}
