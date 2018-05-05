package cn.yejy.util;

import java.util.Arrays;

public class TextUtil {

    public static boolean isEmpty(String text) {
        if (text == null || text.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static boolean isAllNotEmpty(String... texts) {
        return Arrays.stream(texts).allMatch(TextUtil::isNotEmpty);
    }
}
