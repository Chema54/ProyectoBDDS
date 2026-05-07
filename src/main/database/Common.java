/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.database;

/**
 *
 * @author josem
 */
import java.sql.Date;
import java.time.LocalDateTime;

public class Common {
  public static Date fromLocalDateTime(LocalDateTime localDateTime) {
    return Date.valueOf(localDateTime.toLocalDate());
  }
}
