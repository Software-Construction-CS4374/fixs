package cn.hikyson.godeye.sample;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;

import cn.hikyson.android.godeye.sample.R;

public class ComplexLayoutActivity extends Activity {
	ComplexLayoutActivity(){}//FDS fix at least one constructor

    @Override
    protected void onCreate(final @Nullable Bundle savedIState) {//FDS fix method arg could be final and long var
        super.onCreate(savedIState);
        setContentView(R.layout.activity_complex_layout);
    }
}
