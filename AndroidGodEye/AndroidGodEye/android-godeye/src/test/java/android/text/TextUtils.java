package android.text;

public class TextUtils {
    public static boolean isEmpty(final CharSequence str) {//FDS fix method arg could be final
        if (str == null || str.equals("")) {
            return true;
        }
        return false;
    }
}
