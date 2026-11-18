package util;

import java.util.StringTokenizer;

/**
 * 獨立字串工具類，替代 commons-lang 的 StringUtils，
 * 避免依賴 IDE 內建函式庫（新版 IDEA 已不再提供 commons-lang）。
 *
 * @author yangchenwen
 */
public final class StringUtil {

    public static final String EMPTY = "";

    private StringUtil() {
    }

    /**
     * 判斷字串是否為 null、空或全空白字元
     */
    public static boolean isBlank(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 依分隔字元切分字串，忽略空 token（與 StringUtils.split 行為一致）
     */
    public static String[] split(String str, String separatorChars) {
        if (str == null) {
            return null;
        }
        StringTokenizer tokenizer = new StringTokenizer(str, separatorChars);
        String[] result = new String[tokenizer.countTokens()];
        for (int i = 0; i < result.length; i++) {
            result[i] = tokenizer.nextToken();
        }
        return result;
    }

    /**
     * 只替换第一次出現的子字串（純文字比對，非正則）
     */
    public static String replaceOnce(String text, String searchString, String replacement) {
        if (text == null || text.isEmpty() || searchString == null || searchString.isEmpty() || replacement == null) {
            return text;
        }
        int index = text.indexOf(searchString);
        if (index < 0) {
            return text;
        }
        return text.substring(0, index) + replacement + text.substring(index + searchString.length());
    }
}