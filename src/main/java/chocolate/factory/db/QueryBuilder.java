package chocolate.factory.db;

import java.util.Collection;
import java.util.Iterator;

public class QueryBuilder {

    public static String buildProcedureCall(String name, int inParamsCount) {
        String[] nameParts = name.split("\\.");

        StringBuilder builder = new StringBuilder();
        builder.append("{call ");
        for (int i = 0; i < nameParts.length; i++) {
            builder.append('\"');
            builder.append(nameParts[i]);
            builder.append('\"');
            if (i < nameParts.length - 1) {
                builder.append('.');
            }
        }
        builder.append("(");
        buildParamList(builder, inParamsCount);
        builder.append(")");
        builder.append("}");

        return builder.toString();
    }

    public static String buildInsert(String tableName, Collection<? extends String> columnNames) {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO \"");
        builder.append(tableName);
        builder.append("\" (");
        buildStringList(builder, columnNames);
        builder.append(") VALUES (");
        buildParamList(builder, columnNames.size());
        builder.append(")");

        return builder.toString();
    }

    public static String buildUpdate(String tableName, Collection<? extends String> columnNames,
                                     String primaryKeyName) {
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE \"");
        builder.append(tableName);
        builder.append("\" SET ");
        buildNamedParamList(builder, columnNames);
        builder.append(" WHERE ");
        builder.append(primaryKeyName);
        builder.append("=?");

        return builder.toString();
    }

    public static String buildDelete(String tableName, String primaryKeyName) {
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM \"");
        builder.append(tableName);
        builder.append("\" WHERE \"");
        builder.append(primaryKeyName);
        builder.append("\"=?");

        return builder.toString();
    }

    public static String buildSelect(String tableName, Collection<? extends String> columnNames) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        if (columnNames != null) {
            buildStringList(builder, columnNames);
        } else {
            builder.append('*');
        }
        builder.append(" FROM \"");
        builder.append(tableName);
        builder.append('\"');

        return builder.toString();
    }

    public static String buildSelectWithWhereEqual(String tableName, Collection<? extends String> columnNames,
                                                   String columnName) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        if (columnNames != null) {
            buildStringList(builder, columnNames);
        } else {
            builder.append('*');
        }
        builder.append(" FROM \"");
        builder.append(tableName);
        builder.append("\" WHERE \"");
        builder.append(columnName);
        builder.append("\"=?");

        return builder.toString();
    }

    private static void buildStringList(StringBuilder builder, Collection<? extends String> strings) {
        for (Iterator<? extends String> iterator = strings.iterator(); iterator.hasNext();) {
            String str = iterator.next();
            builder.append('\"');
            builder.append(str);
            builder.append('\"');
            if (iterator.hasNext()) {
                builder.append(',');
            }
        }
    }

    private static void buildNamedParamList(StringBuilder builder, Collection<? extends String> names) {
        for (Iterator<? extends String> iterator = names.iterator(); iterator.hasNext();) {
            String str = iterator.next();
            builder.append('\"');
            builder.append(str);
            builder.append("\"=");
            builder.append('?');
            if (iterator.hasNext()) {
                builder.append(',');
            }
        }
    }

    private static void buildParamList(StringBuilder builder, int amount) {
        for (int i = 0; i < amount; i++) {
            builder.append('?');
            if (i < amount - 1) {
                builder.append(',');
            }
        }
    }
}
