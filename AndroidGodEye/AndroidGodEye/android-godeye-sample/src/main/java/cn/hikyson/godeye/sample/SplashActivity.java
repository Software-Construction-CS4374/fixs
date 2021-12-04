package cn.hikyson.godeye.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.hikyson.android.godeye.sample.R;

public class SplashActivity extends Activity {
	SplashActivity(){}//FDS fix at least one constructor

    @Override
    protected void onCreate(final Bundle savedIS) {//FDS fix long var and method arg could be final
        super.onCreate(savedIS);
        setContentView(R.layout.activity_splash);
        StartupTracer.get().onSplashCreate();
        Intent intent = new Intent(SplashActivity.this, Main2Activity.class);
        startActivity(intent);
        finish();
    }
}
