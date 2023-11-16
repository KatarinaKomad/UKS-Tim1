package uns.ac.rs.uks.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
}
