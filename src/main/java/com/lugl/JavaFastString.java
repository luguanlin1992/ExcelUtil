package com.lugl;

public interface JavaFastString {
    public static final String TAB = "    ";
    public static final String ENTER = "\n";

    public static String upperFirstChar(String value) {
        String retStr = value.substring(0,1).toUpperCase() + value.substring(1);
        return retStr;
    }

    public static String leftJustify(String s) {
        return leftJustify(s, 50);
    }

    public static String leftJustify(String s, int leftLenght) {
        if (s.length() > leftLenght) {
            return s;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < leftLenght - s.length(); ++i) {
                stringBuilder.append(" ");
            }
            return s + stringBuilder.toString();
        }
    }
}
