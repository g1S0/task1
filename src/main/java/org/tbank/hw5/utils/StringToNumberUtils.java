package org.tbank.hw5.utils;

public class StringToNumberUtils {
    private static final double DEFAULT_VALUE = 500.0;

    public static double getNumberFromString(String value) {
        if (value == null || value.isEmpty()) {
            return DEFAULT_VALUE;
        }

        StringBuilder numberBuilder = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);

            if (Character.isDigit(c) || c == '.' || c == ',') {
                if (c == ',') {
                    c = '.';
                }
                numberBuilder.append(c);
            } else if (!numberBuilder.isEmpty()) {
                break;
            }
        }

        if (!numberBuilder.isEmpty()) {
            try {
                return Double.parseDouble(numberBuilder.toString());
            } catch (NumberFormatException e) {
                return 500;
            }
        }

        return DEFAULT_VALUE;
    }
}
