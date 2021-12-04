package cn.hikyson.godeye.core.internal.modules.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import cn.hikyson.godeye.core.internal.Engine;
import cn.hikyson.godeye.core.internal.Producer;
import cn.hikyson.godeye.core.utils.L;

/**
 * Created by kysonchao on 2017/11/23.
 */
public class BatteryEngine implements Engine {
    private final Context mContext;//FDS fix immutable field
    private final Producer<BatteryInfo> mProducer;//FDS fix immutable field
    private final BatteryChangeReceiver mBatteryCR;//FDS fix long var and immutable field

    public BatteryEngine(final Context context,final Producer<BatteryInfo> producer) {//FDS fix method arg could be final
        this.mContext = context;
        mProducer = producer;
    }

    @Override
    public void work() {
        if (mBatteryCR == null) {
            mBatteryCR = new BatteryChangeReceiver();
            mBatteryCR.setBatteryInfoProducer(mProducer);
            mContext.registerReceiver(mBatteryCR, BatteryIntentFilterHolder.BATTERY_INTENT_FILTER);
        }
    }

    
    @Override
    public void shutdown() {
        if (mBatteryChangeReceiver != null) {
            try {
                mContext.unregisterReceiver(mBatteryChangeReceiver);
            } catch (Throwable e) {
                L.d("Battery shutdown warning: " + e);
            }
            mBatteryChangeReceiver = null;
        }
    }

    private static final class BatteryIntentFilterHolder {
        private static final IntentFilter BATTERY_INTENTF = new IntentFilter();//FDS fix long variable

        static {
            BATTERY_INTENTF.addAction(Intent.ACTION_BATTERY_CHANGED);
            BATTERY_INTENTF.addAction(Intent.ACTION_BATTERY_LOW);
            BATTERY_INTENTF.addAction(Intent.ACTION_BATTERY_OKAY);
        }
    }
}
