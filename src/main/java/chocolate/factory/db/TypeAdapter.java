package chocolate.factory.db;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;

public class TypeAdapter {

    public static Object sqlToObject(Field field, Object value) {
        Class<?> type = field.getType();

        if (Enum.class.isAssignableFrom(type)) {
            value = type.getEnumConstants()[(Integer) value];
        } else if (LocalDate.class.isAssignableFrom(type)) {
            value = ((Date)value).toLocalDate();
        } else if (LocalTime.class.isAssignableFrom(type)) {
            value = LocalTime.ofSecondOfDay((Long)value);
        }  else if (LocalDateTime.class.isAssignableFrom(type)) {
            long millis = ((Timestamp)value).getTime();
            value = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("UTC"));
        }

        return value;
    }

    public static Object objectToSql(Field field, Object value) {
        Class<?> type = field.getType();

        if (Enum.class.isAssignableFrom(type)) {
            value = ((Enum<?>)value).ordinal();
        } else if (LocalDate.class.isAssignableFrom(type)) {
            value = Date.valueOf((LocalDate)value);
        } else if (LocalTime.class.isAssignableFrom(type)) {
            value = ((LocalTime)value).toSecondOfDay();
        } else if (LocalDateTime.class.isAssignableFrom(type)) {
            value = Timestamp.valueOf(((LocalDateTime)value));
        }

        return value;
    }

    public static Class<?> adaptTypeToSql(Class<?> type) {
        if (Enum.class.isAssignableFrom(type)) {
            return Integer.class;
        } else if (LocalDate.class.isAssignableFrom(type)) {
            return Date.class;
        } else if (LocalTime.class.isAssignableFrom(type)) {
            return Long.class;
        }  else if (LocalDateTime.class.isAssignableFrom(type)) {
            return Timestamp.class;
        }

        return type;
    }
}
