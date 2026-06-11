package main.business.dao;

public class NotFoundException extends Exception {
  public NotFoundException(String message) {
    super(message);
  }
}