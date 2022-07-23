package net.deechael.dddouga.utils;

public final class StringUtils {

    /**
     * To prevent a single line string too long
     *
     * @param string the string to be separated
     * @param everyLength the maximum length of each line
     * @return separated string
     */
    public static String lineByLength(String string, int everyLength) {
        if (string.length() < everyLength)
            return string;
        int dividedTimes = string.length() % everyLength == 0 ? string.length() / everyLength : string.length() / everyLength + 1;
        StringBuilder base = new StringBuilder();
        for (int i = 0; i < dividedTimes; i++) {
            base.append(string, i * everyLength, Math.min((i + 1) * everyLength, string.length()));
            if (i < dividedTimes - 1)
                base.append("\n");
        }
        return base.toString();
    }

    private StringUtils() {}

}
