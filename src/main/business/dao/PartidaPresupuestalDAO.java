package main.business.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.business.dao.shape.CompleteDAOShape;
import main.business.dto.PartidaPresupuestalDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PartidaPresupuestalDAO extends CompleteDAOShape<PartidaPresupuestalDTO, Integer> {
    
    // 1. Aquí declaramos el LOGGER de la forma correcta para tu proyecto
    private static final Logger LOGGER = LogManager.getLogger(PartidaPresupuestalDAO.class);

    @Override
    public void createOne(PartidaPresupuestalDTO dto) throws UserDisplayableException {
        String query = "INSERT INTO PartidaPresupuestal (clave, descripcion) VALUES (?, ?)";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, dto.getClave());
            ps.setString(2, dto.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            // 2. Usamos el LOGGER directamente sin forzar el cast
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al crear la partida.");
        }
    }

    @Override
    public List<PartidaPresupuestalDTO> getAll() throws UserDisplayableException {
        String query = "SELECT * FROM PartidaPresupuestal";
        List<PartidaPresupuestalDTO> list = new ArrayList<>();
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new PartidaPresupuestalDTO(rs.getInt("id_partida"), rs.getString("clave"), rs.getString("descripcion")));
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al cargar las partidas.");
        }
        return list;
    }

    @Override
    public PartidaPresupuestalDTO getOne(Integer id) throws UserDisplayableException {
        String query = "SELECT * FROM PartidaPresupuestal WHERE id_partida = ?";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new PartidaPresupuestalDTO(rs.getInt("id_partida"), rs.getString("clave"), rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al obtener la partida.");
        }
        return null;
    }

    @Override
    public void updateOne(PartidaPresupuestalDTO dto) throws UserDisplayableException {
        String query = "UPDATE PartidaPresupuestal SET clave = ?, descripcion = ? WHERE id_partida = ?";
        try (Connection conn = DBConnector.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, dto.getClave());
            ps.setString(2, dto.getDescripcion());
            ps.setInt(3, dto.getIdPartida());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al actualizar la partida.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws UserDisplayableException {} // Opcional por ahora
}