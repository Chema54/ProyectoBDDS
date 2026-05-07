/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.common;

/**
 *
 * @author josem
 */
public class UserDisplayableException extends Exception {
  public UserDisplayableException(String message) {
    super(message);
  }

  public UserDisplayableException(String message, Throwable cause) {
    super(message, cause);
  }
}
