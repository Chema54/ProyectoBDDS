package main.common;

import main.business.dao.NotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Optional;
import javax.xml.stream.XMLStreamException;
import org.apache.logging.log4j.Logger;

public class ExceptionHandler {

    private static final String SQL_UNKNOWN_ERROR = "Error desconocido al procesar la solicitud. Por favor, inténtelo más tarde.";

    public static UserDisplayableException handleUnexpectedException(Logger logger, Exception e, String message) {
        logger.error("Error Inesperado: " + e.getMessage(), e);
        String finalMessage = message + " Error inesperado. Por favor, comuniquese con el desarrollador si el error persiste.";
        return new UserDisplayableException(finalMessage.trim(), e);
    }

    private static UserDisplayableException handleXMLStreamException(Logger logger, IOException e) {
        logger.error("Error al Cargar la Interfaz gráfica: Error de análisis XML. Verifique el archivo FXML.", e);
        return new UserDisplayableException(
                "Error al procesar archivo de la interfaz gráfica. Por favor, comuniquese al desarrollador si el error persiste.",
                e
        );
    }

    public static UserDisplayableException handleGUILoadIOException(Logger logger, IOException e) {
        if (e.getCause() instanceof XMLStreamException) {
            return handleXMLStreamException(logger, e);
        }

        if (e.getCause() instanceof ClassNotFoundException) {
            logger.error("Error al cargar la interfaz gráfica: Clase no encontrada. Verifique el archivo FXML.", e);
            return new UserDisplayableException(
                    "Error al cargar la interfaz gráfica. Por favor, comuníquese con el desarrollador si el error persiste.",
                    e
            );
        }

        return handleIOException(logger, e, "Error al cargar la interfaz gráfica");
    }

    public static UserDisplayableException handleIOException(Logger logger, IOException e, String message) {
        if (e instanceof FileNotFoundException) {
            return handleFileNotFoundExceptionMessage(logger, (FileNotFoundException) e, message);
        }

        if (e instanceof AccessDeniedException) {
            return handleAccessDeniedExceptionMessage(logger, (AccessDeniedException) e, message);
        }

        logger.error("Error de Entrada/Salida Desconocido: {}", e);
        String finalMessage = message + " Error de Entrada/Salida desconocido. Por favor, inténtelo más tarde.";
        return new UserDisplayableException(finalMessage.trim(), e);
    }

    private static UserDisplayableException handleFileNotFoundExceptionMessage(Logger logger, FileNotFoundException e, String message) {
        logger.error("Archivo no Encontrado (Verificar Ruta de Archivo): {}", e);
        String finalMessage = message + " Error de archivo no encontrado. Por favor, verifique la ruta del archivo.";
        return new UserDisplayableException(finalMessage.trim(), e);
    }

    private static UserDisplayableException handleAccessDeniedExceptionMessage(Logger logger, AccessDeniedException e, String message) {
        logger.error("Acceso Denegado (Verificar Permisos): {}", e);
        String finalMessage = message + " Acceso denegado. Por favor, verifique los permisos del archivo.";
        return new UserDisplayableException(finalMessage.trim(), e);
    }

    public static NotFoundException handleNotFoundException(Logger logger, String entity) {
        logger.warn("Recurso no Encontrado: {}", entity);
        return new NotFoundException("El " + entity + " no se encontró. Por favor, verifique la información ingresada.");
    }

    public static UserDisplayableException handleSQLException(Logger logger, SQLException e) {
        return handleSQLException(logger, e, "");
    }

    public static UserDisplayableException handleSQLException(Logger logger, SQLException e, String message) {
        String state = Optional.ofNullable(e.getSQLState()).orElse("");
        String finalMessage = message + " ";

        switch (state.substring(0, 2)) {
            case "08":
                finalMessage += getSQLConnectionErrorMessage(logger, e, state);
                break;
            case "23":
                finalMessage += getSQLIntegrityErrorMessage(logger, e);
                break;
            case "28":
                finalMessage += getSQLAuthenticationErrorMessage(logger, e);
                break;
            case "42":
                finalMessage += getSQLSyntaxErrorMessage(logger, e);
                break;
            default:
                logger.error("Error SQL desconocido: {}", e);
                finalMessage += SQL_UNKNOWN_ERROR;
        }

        return new UserDisplayableException(finalMessage.trim(), e);
    }

    private static String getSQLConnectionErrorMessage(Logger logger, SQLException e, String state) {
        if ("08S01".equals(state)) {
            logger.error("Error de comunicación: {}", e);
            return "Error de comunicación con la base de datos. Por favor, inténtelo más tarde.";
        }

        logger.error("Error de conexión: {}", e);
        return "Error de conexión a la base de datos. Por favor, inténtelo más tarde.";
    }

    private static String getSQLIntegrityErrorMessage(Logger logger, SQLException e) {
        logger.error("Error de integridad: {}", e);
        return "Error de integridad de datos. Por favor, revise la información ingresada.";
    }

    private static String getSQLAuthenticationErrorMessage(Logger logger, SQLException e) {
        logger.error("Error de autenticación: {}", e);
        return "Error de autenticación de base de datos. Por favor, contacte al administrador del sistema.";
    }

    private static String getSQLSyntaxErrorMessage(Logger logger, SQLException e) {
        logger.error("Error de sintaxis: {}", e);
        return "Error de sintaxis en la consulta. Por favor, contacte al administrador del sistema.";
    }
}
