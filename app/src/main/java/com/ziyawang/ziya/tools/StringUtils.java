package com.ziyawang.ziya.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 牛海丰 on 2018/5/24.
 */

public class StringUtils {
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
                dest = m.replaceAll("");
            }
            return dest;
        }
}

