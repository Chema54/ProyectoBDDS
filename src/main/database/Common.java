package main.database;

import java.sql.Date;
import java.time.LocalDateTime;

public class Common {

    public static Date fromLocalDateTime(LocalDateTime localDateTime) {
        return Date.valueOf(localDateTime.toLocalDate());
    }
}
