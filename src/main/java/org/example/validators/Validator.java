package org.example.validators;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean isCodeValid(String code) {
        if (code == null) return false;
        return code.matches("[A-Z]{3}");
    }

    public static boolean isCodePairValid(String code) {
        if (code == null)
            return false;
        Pattern pattern = Pattern.compile("[A-Z]{6}");
        Matcher matcher = pattern.matcher(code);

        return matcher.matches() && !code.substring(0, 3).equals(code.substring(3));
    }

    public static boolean isNotNull(String ...args) {
        return Arrays.stream(args).allMatch(Objects::nonNull);
    }
}