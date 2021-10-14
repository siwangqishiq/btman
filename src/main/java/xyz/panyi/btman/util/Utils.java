package xyz.panyi.btman.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String replaceLineBlanks(String str) {
        String result = "";
        if (str != null) {
            Pattern p = Pattern.compile("(\r?\n(\\s*\r?\n)+)");
            Matcher m = p.matcher(str);
            result = m.replaceAll("\r\n");
        }
        return result;
    }
}
