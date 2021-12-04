package cn.hikyson.godeye.core.internal.modules.appsize;

import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;


class AppSizeUtil {

    static void getAppSize(final Context context,final OnGetSizeListener listener) {//FDS Fix method arg could be fine
        if (listener == null) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getAppSizeAboveO(context, listener);
            } else {
                getAppSizeLowerO(context, listener);
            }
        }finally{//FDS fix generic catch
        	System.exit(0);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void getAppSizeAboveO(final Context context,final @NonNull OnGetSizeListener listener) {//FDS fix method arg could be final
        StorageStatsManager sStatsManager = (StorageStatsManager) context
                .getSystemService(Context.STORAGE_STATS_SERVICE);
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);

        List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
        for (StorageVolume item : storageVolumes) {
            String uuidStr = item.getUuid();
            UUID uuid;
            if (uuidStr == null) {
                uuid = StorageManager.UUID_DEFAULT;
            } else {
                uuid = UUID.fromString(uuidStr);
            }
            int uid = getUid(context, context.getPackageName());

            StorageStats storageStats;
            try {
                storageStats = sStatsManager.queryStatsForUid(uuid, uid);
                AppSizeInfo ctAppSizeInfo = new AppSizeInfo();
                ctAppSizeInfo.cacheSize = storageStats.getCacheBytes();
                ctAppSizeInfo.dataSize = storageStats.getDataBytes();
                ctAppSizeInfo.codeSize = storageStats.getAppBytes();
                listener.onGetSize(ctAppSizeInfo);
            } catch (IOException e) {
                listener.onError(e);
            }
        }
    }


    private static int getUid(final Context context,final String pakName) {//FDS fix method arg could be final
        try {
            return context.getPackageManager().getApplicationInfo(pakName, PackageManager.GET_META_DATA).uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }


    @SuppressWarnings("JavaReflectionMemberAccess")
    private static void getAppSizeLowerO(final Context context, @NonNull final OnGetSizeListener listener) {//fix method arg could be final
        try {
            Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class,
                    IPackageStatsObserver.class);
            
            method.invoke(context.getPackageManager(), context.getPackageName(), new IPackageStatsObserver.Stub() {
                @Override
                public void onGetStatsCompleted(final PackageStats pStats,final boolean succeeded) {//FDS fix Method arg could be final
                    AppSizeInfo ctAppSizeInfo = new AppSizeInfo();
                    ctAppSizeInfo.cacheSize = pStats.cacheSize;
                    ctAppSizeInfo.dataSize = pStats.dataSize;
                    ctAppSizeInfo.codeSize = pStats.codeSize;
                    listener.onGetSize(ctAppSizeInfo);
                }
            });
        } catch (Throwable e) {
            listener.onError(e);
        }
    }

    public interface OnGetSizeListener {
        void onGetSize(AppSizeInfo ctAppSizeInfo);

        void onError(Throwable throwable);
    }

    static String formatSize(final long size) {//FDS fix method arg could be final
        if (size / (1024 * 1024 * 1024) > 0) {
            float tmpSize = (float) (size) / (float) (1024 * 1024 * 1024);
            DecimalFormat dFormat = new DecimalFormat("#.##");//FDS fix short var
            return dFormat.format(tmpSize) + "GB";
        } else if (size / (1024 * 1024) > 0) {
            float tmpSize = (float) (size) / (float) (1024 * 1024);
            DecimalFormat dFormat = new DecimalFormat("#.##");//FDS Fix short var
            return dFormat.format(tmpSize) + "MB";
        } else if (size / 1024 > 0) {
            return (size / (1024)) + "KB";
        } else {
            return size + "B";
        }
    }
}

