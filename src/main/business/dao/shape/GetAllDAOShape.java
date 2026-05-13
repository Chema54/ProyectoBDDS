/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dao.shape;

import java.util.List;
import main.common.UserDisplayableException;

/**
 *
 * @author josem
 */
public interface GetAllDAOShape<T> {
  List<T> getAll() throws UserDisplayableException;
}
