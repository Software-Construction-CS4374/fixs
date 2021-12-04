package cn.hikyson.godeye.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Created by kysonchao on 2017/11/22.
 */

public final class JsonUtil {//FDS fix class with only private constructors should be final
    public static Gson sGson = new GsonBuilder().create();

    private JsonUtil() {
    }

    public static String toJson(final Object object) {//FDS fix method arg could be final and short var
        return sGson.toJson(object);
    }
}
