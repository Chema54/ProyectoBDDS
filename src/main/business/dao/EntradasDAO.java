package main.business.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntradasDAO {
    private static final Logger LOGGER = LogManager.getLogger(EntradasDAO.class);

    public void registrarEntradaKardex(String folio, java.sql.Date fecha, int idProveedor, int idSucursal, int idArticulo, int cantidad, double costo) throws UserDisplayableException {
        String call = "{CALL SP_RegistrarEntrada(?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection connection = DBConnector.getInstance().getConnection();
             CallableStatement statement = connection.prepareCall(call)) {
            
            statement.setString(1, folio);
            statement.setDate(2, fecha);
            statement.setInt(3, idProveedor);
            statement.setInt(4, idSucursal);
            statement.setInt(5, idArticulo);
            statement.setInt(6, cantidad);
            statement.setDouble(7, costo);
            
            statement.execute();
            
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error al registrar la factura en Kardex e Inventario.");
        }
    }
}