package cn.hikyson.godeye.core.helper;

import android.view.Choreographer;

public class ChoreographerInjecor {
    public interface ChoreographerProvider {
        public Choreographer getChoreographer();
    }

    private static ChoreographerProvider sChoreographerP = new ChoreographerProvider() {//FDS fix long var
        @Override
        public Choreographer getChoreographer() {
            return Choreographer.getInstance();
        }
    };

    public static void setChoreographerProvider(final ChoreographerProvider choreographerP) {//FDS fix long var and method arg could be final
        sChoreographerProvider = choreographerP;
    }

    public static ChoreographerProvider getChoreographerProvider() {
        return sChoreographerProvider;
    }
}
