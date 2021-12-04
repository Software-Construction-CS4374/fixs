package cn.hikyson.godeye.sample.methodcanary;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class BaseActivity extends Activity {
	BaseActivity(){}//FDS fix at least one constructor
    @Override
    protected void onCreate(final @Nullable Bundle savedIState) {//FDS fix Method arg could be final and long var
        super.onCreate(savedIState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(final @NonNull Bundle outState) {//FDS fix method arg could be final
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
