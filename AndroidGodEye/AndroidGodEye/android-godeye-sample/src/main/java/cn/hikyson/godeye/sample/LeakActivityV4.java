package cn.hikyson.godeye.sample;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.TimeUnit;

import cn.hikyson.android.godeye.sample.R;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class LeakActivityV4 extends FragmentActivity {
	LeakActivityV4(){}//FDS fix at least one constructor
    @Override
    protected void onCreate(final Bundle savedIS) {//FDS fix method arg could be final and long var
        super.onCreate(savedIS);
        setContentView(R.layout.activity_leak_v4);
        LeakFragmentV4 fragmentV4 = new LeakFragmentV4();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragmentV4).commit();
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            try {
                getSupportFragmentManager().beginTransaction().remove(fragmentV4).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 2, TimeUnit.SECONDS);
    }
}
