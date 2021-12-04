package cn.hikyson.godeye.core.helper;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class Test1Activity extends Activity {
	Test1Activity(){}//FDS fix at least one constructor
    @Override
    protected void onCreate(final @Nullable Bundle savedIS) {//FDS fix long var and method arg could be final
        super.onCreate(savedIS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
