package uns.ac.rs.uks.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtil {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static Date localDateTimeToDate(LocalDateTime localDate){
        return java.sql.Timestamp.valueOf(localDate);
    }

    public static LocalDateTime dateToLocalDateTime(Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDateTime getNow(){
        LocalDateTime now = LocalDateTime.now();
        now.format(dtf);
        return now;
    }

    public static LocalDateTime getLocalDateTime(Long timestamp){
        return dateToLocalDateTime(new Date(timestamp));
    }

    public static LocalDateTime parseDate(String dateString) {
        return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static LocalDateTime parseGitoliteDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateString, formatter);
        return offsetDateTime.toLocalDateTime();

    }
}
