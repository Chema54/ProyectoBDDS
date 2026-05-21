/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dao.shape;

import main.common.UserDisplayableException;

/**
 *
 * @author josem
 */
public interface UpdateOneDAOShape<T> {
  void updateOne(T dataObject) throws UserDisplayableException;
}
