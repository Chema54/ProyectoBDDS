/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.business.dao.shape;

/**
 *
 * @author josem
 */
public abstract class CompleteDAOShape<T, F>
  implements CreateOneDAOShape<T>, GetOneDAOShape<T, F>, GetAllDAOShape<T>, UpdateOneDAOShape<T>, DeleteOneDAOShape<F> {
}
