package uns.ac.rs.uks.util;

import uns.ac.rs.uks.dto.request.search.keywords.Keyword;
import uns.ac.rs.uks.dto.request.search.keywords.Operations;
import uns.ac.rs.uks.model.State;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchUtil {
    public static String parseDateCondition(Keyword keyword, String query, Operations operation,
                                            Map<String, Object> params, String attribute, String paramName) {
        String input = query.replace(keyword.getName(), "");
        String sign = extractSign(input);
        LocalDateTime value = DateUtil.parseDate(extractValue(input));
        params.put(paramName, value);
        return signCondition(operation, attribute, paramName, sign);
    }
    public static String parseNumberCondition(Keyword keyword, String query, Operations operation,
                                              Map<String, Object> params, String attribute, String paramName) {
        String input = query.replace(keyword.getName(), "");
        String sign = extractSign(input);
        String value = extractValue(input);
        params.put(paramName, value);
        return sizeCondition(operation, attribute, paramName, sign);
    }

    public static String parseStringKeyword(Keyword keyword, String query, Operations operation,
                                      Map<String, Object> params, String attributeName, String paramName) {
        String input = query.replace(keyword.getName(), "");
        String sign = input.substring(0, 1);
        String value = input.substring(1).toLowerCase();
        params.put(paramName, value);
        if (sign.equals("=")) {
            return equalsCondition(operation, attributeName, paramName);
        } else if (sign.equals("~")) {
            return likeCondition(operation, attributeName, paramName); // AND r.name LIKE :repoName
        }
        return "";
    }

    public static String likeCondition(Operations operation, String attributeName, String paramName) {
        if(operation.equals(Operations.NOT)) {
            return Operations.AND + " " + attributeName + " NOT LIKE CONCAT('%', :" + paramName + ", '%') ";
        } else {
            return operation + " " + attributeName + " LIKE CONCAT('%', :" + paramName + ", '%') ";
        }
    }

    public static String isNotNullCondition(Operations operation, String attributeName) {
        return operation + " " + attributeName + " IS NOT NULL ";
    }
    public static String isNullCondition(Operations operation, String attributeName) {
        return operation + " " + attributeName + " IS NULL ";
    }
    public static String equalsCondition(Operations operation, String attributeName, String paramName) {
        if(operation.equals(Operations.NOT)){
            return Operations.AND + " " + attributeName + " != :" + paramName + " ";
        } else {
            return operation + " " + attributeName + " = :" + paramName + " ";
        }
    }
    public static String sizeCondition(Operations operation, String attributeName, String paramName, String sign) {
        sign = fixSignIfOperationNOT(operation, sign);
        if(operation.equals(Operations.NOT)){
            operation = Operations.AND;
        }
        return operation + " SIZE(" + attributeName + ") " + sign  + " :" + paramName + " ";
    }
    public static String signCondition(Operations operation, String attributeName, String paramName, String sign) {
        sign = fixSignIfOperationNOT(operation, sign);
        if(operation.equals(Operations.NOT)){
            operation = Operations.AND;
        }
        return operation + " " + attributeName + " " + sign  + " :" + paramName + " ";
    }

    public static String booleanCondition(Operations operation, String attributeName, boolean value) {
        if(operation.equals(Operations.NOT)){
            return Operations.AND + " " + attributeName + " = " + !value + " ";
        }
        return operation + " " + attributeName + " = " + value + " ";
    }
    public static String stateCondition(Operations operation, String attributeName, State state) {
        if(operation.equals(Operations.NOT)){
            if(state == State.OPEN){
                return Operations.AND + " " + attributeName + " = " + State.CLOSE + " ";
            }
            if(state == State.CLOSE){
                return Operations.AND + " " + attributeName + " = " + State.OPEN + " ";
            }
        }
        return operation + " " + attributeName + " = " + state + " ";
    }


    private static String fixSignIfOperationNOT(Operations operation, String sign) {
        if(operation.equals(Operations.NOT)){
            switch (sign) {
                case "=" -> {return "!=";}
                case ">=" -> {return "<=";}
                case "<=" -> {return ">=";}
                case ">" -> {return "<";}
                case "<" -> {return ">";}
                default -> { return sign;}
            }
        }
        return sign;
    }


    public static String extractSign(String input) {
        Pattern pattern = Pattern.compile("^([><=]+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null; // No sign found
        }
    }

    public static String extractValue(String input) {
        Pattern pattern = Pattern.compile("^[><=]+(.+)$");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null; // No value found
        }
    }
}
