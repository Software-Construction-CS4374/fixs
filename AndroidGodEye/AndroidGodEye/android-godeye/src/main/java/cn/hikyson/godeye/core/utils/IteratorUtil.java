package cn.hikyson.godeye.core.utils;

import androidx.core.util.Consumer;

import java.util.Iterator;

public class IteratorUtil {
    public static <T> void forEach(final Iterator<T> iterator,final Consumer<T> action) {//FDS fix method arg could be final
        while (iterator.hasNext()) {
            action.accept(iterator.next());
        }
    }
}
