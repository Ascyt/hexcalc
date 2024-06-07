package at.htlleonding.hexcalc.controller;

public class HexConverter {

    public static String ToHex(double value) {
        final String hexDigits = "0123456789ABCDEF";

        if (value == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();

        // Handle negative values
        if (value < 0) {
            sb.append("-");
            value = -value;
        }

        // Convert integer part
        long integerPart = (long) value;
        double fractionalPart = value - integerPart;

        StringBuilder intPartHex = new StringBuilder();
        while (integerPart > 0) {
            int remainder = (int)(integerPart % 16);
            intPartHex.append(hexDigits.charAt(remainder));
            integerPart /= 16;
        }

        if (intPartHex.length() == 0) {
            intPartHex.append("0");
        }

        sb.append(intPartHex.reverse());

        // Convert fractional part
        if (fractionalPart > 0) {
            sb.append(".");
            int maxFractionDigits = 10; // Limit the number of fractional digits to avoid infinite loop
            while (fractionalPart > 0 && maxFractionDigits-- > 0) {
                fractionalPart *= 16;
                int fractionalDigit = (int) fractionalPart;
                sb.append(hexDigits.charAt(fractionalDigit));
                fractionalPart -= fractionalDigit;
            }
        }

        return sb.toString();
    }

    public static double FromHex(String hexValue) {
        final String hexDigits = "0123456789ABCDEF";
        boolean isNegative = false;
        int startIndex = 0;

        hexValue = hexValue.toUpperCase();

        if (hexValue.charAt(0) == '-') {
            isNegative = true;
            startIndex = 1;
        }

        String[] parts = hexValue.substring(startIndex).split("\\.");
        String intPart = parts[0];
        String fracPart = parts.length > 1 ? parts[1] : "";

        // Convert integer part
        double result = getResult(intPart, hexDigits, fracPart);
        return isNegative ? -result : result;
    }

    private static double getResult(String intPart, String hexDigits, String fracPart) {
        long intValue = 0;
        for (int i = 0; i < intPart.length(); i++) {
            char hexChar = intPart.charAt(i);
            long digitValue = hexDigits.indexOf(hexChar);
            intValue = intValue * 16 + digitValue;
        }

        // Convert fractional part
        double fracValue = 0;
        for (int i = 0; i < fracPart.length(); i++) {
            char hexChar = fracPart.charAt(i);
            long digitValue = hexDigits.indexOf(hexChar);
            fracValue += digitValue / Math.pow(16, i + 1);
        }

        double result = intValue + fracValue;
        return result;
    }
}
