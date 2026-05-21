package main.business.dao.shape;

import java.sql.ResultSet;
import java.sql.SQLException;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
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
public abstract class DAOShape<T> {
  protected static final Logger LOGGER = LogManager.getLogger(DAOShape.class);
  /**
   * Gets a DTO instance from the ResultSet. This method is intended to be used by the createDTOInstanceFromResultSet method
   * to create a DTO instance from the ResultSet along with error handling.
   *
   * @return a DTO instance populated with data from the ResultSet.
   * @throws SQLException if there is an error accessing the ResultSet.
   */
  public abstract T getDTOInstanceFromResultSet(ResultSet resultSet) throws SQLException;

  /**
   * Creates a DTO instance from the provided ResultSet. This method builds upon the getDTOInstanceFromResultSet method
   * to create a DTO instance from the ResultSet, handling any exceptions that may occur.
   *
   * @param resultSet the ResultSet containing data from the database.
   * @return a DTO instance populated with data from the ResultSet.
   * @throws UserDisplayableException if there is an error creating the DTO instance.
   */
  public T createDTOInstanceFromResultSet(ResultSet resultSet) throws UserDisplayableException {
    try {
      return getDTOInstanceFromResultSet(resultSet);
    } catch (SQLException e) {
      throw ExceptionHandler.handleSQLException(LOGGER, e);
    }
  }
}
