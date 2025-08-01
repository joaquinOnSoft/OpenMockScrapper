package com.openmock.util;

import java.util.regex.Pattern;

public class NumberUtil {

    public static boolean isPositiveInt(String strNum) {
        return match("\\d+", strNum);
    }

    public static boolean isNumeric(String strNum) {
        return match("-?\\d+(\\.\\d+)?", strNum);
    }

    /**
     * Check If a String match a given regular expression
     *
     * @param strPatter - Regular expression to validate the string.
     * @param strNum    - String to be validated
     * @return true if match, false in other case
     * @see <a href="https://www.baeldung.com/java-check-string-number">
     * Check If a String Is Numeric in Java
     * </a>
     */
    private static boolean match(String strPatter, String strNum) {
        Pattern pattern = Pattern.compile(strPatter);
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }
}
