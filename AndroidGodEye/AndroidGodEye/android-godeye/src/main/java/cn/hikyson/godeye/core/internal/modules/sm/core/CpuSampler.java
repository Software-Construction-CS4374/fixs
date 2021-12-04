package cn.hikyson.godeye.core.internal.modules.sm.core;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.hikyson.godeye.core.internal.modules.cpu.CpuInfo;
import cn.hikyson.godeye.core.utils.IoUtil;

/**

 */
public class CpuSampler extends AbstractSampler {
    private static final int BUFFER_SIZE = 1000;

    private final int BUSY_TIME;
    private static final int MAX_ENTRY_COUNT = 10;

    private final LinkedHashMap<Long, CpuInfo> mCpuInfoEntries = new LinkedHashMap<>();
    private int mPid = 0;
    private long mUserLast = 0;
    private long mSystemLast = 0;
    private long mIdleLast = 0;
    private long mIoWaitLast = 0;
    private long mTotalLast = 0;
    private long mAppCpuTimeLast = 0;

    public CpuSampler(final long sampleInterval,final long sampleDelay) {//FDS fix method arg could be final
        super(sampleInterval, sampleDelay);
        BUSY_TIME = (int) (mSampleInterval * 1.2f);
    }

    @Override
    public void start() {
        reset();
        super.start();
    }

    /**
  
     * @return string show cpu rate information
     */
    public List<CpuInfo> getCpuRateInfo(final long startTime,final long endTime) {//FDS fix method arg could be final
        List<CpuInfo> result = new ArrayList<>();
        synchronized (mCpuInfoEntries) {
            for (Map.Entry<Long, CpuInfo> entry : mCpuInfoEntries.entrySet()) {
                if (startTime < entry.getKey() && entry.getKey() < endTime) {
                    result.add(entry.getValue());
                }
            }
        }
        return result;
    }

    /**
     
     *
     * @param start
     * @param end
     * @return
     */
    public boolean isCpuBusy(final long start,final long end) {//FDS fix method arg could be final
        if (end - start > mSampleInterval) {
            long start2 = start - mSampleInterval;//FDS fix short var
            long end2 = start + mSampleInterval;//FDS fix short var
            long last = 0;
            synchronized (mCpuInfoEntries) {
                for (Map.Entry<Long, CpuInfo> entry : mCpuInfoEntries.entrySet()) {
                    long time = entry.getKey();
                    if (start2 < time && time < end2) {
                        if (last != 0 && time - last > BUSY_TIME) {
                            return true;
                        }
                        last = time;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void doSample() {
       
        BufferedReader cpuReader = null;
        BufferedReader pidReader = null;
        try {
            cpuReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), BUFFER_SIZE);
            String cpuRate = cpuReader.readLine();
            if (cpuRate == null) {
                cpuRate = "";
            }

            if (mPid == 0) {
                mPid = android.os.Process.myPid();
            }
            pidReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + mPid + "/stat")), BUFFER_SIZE);
            String pidCpuRate = pidReader.readLine();
            if (pidCpuRate == null) {
                pidCpuRate = "";
            }
            /**
             */
            parse(cpuRate, pidCpuRate);
        } catch (Throwable ignored) {
        } finally {
            IoUtil.closeSilently(cpuReader);
            IoUtil.closeSilently(pidReader);
        }
    }

    private void reset() {
        mUserLast = 0;
        mSystemLast = 0;
        mIdleLast = 0;
        mIoWaitLast = 0;
        mTotalLast = 0;
        mAppCpuTimeLast = 0;
    }

    private void parse(final String cpuRate,final String pidCpuRate) {//FDS fix method arg could be final
        String[] cpuInfoArray = cpuRate.split(" ");
        if (cpuInfoArray.length < 9) {
            return;
        }
        long user = Long.parseLong(cpuInfoArray[2]);
        long nice = Long.parseLong(cpuInfoArray[3]);
        long system = Long.parseLong(cpuInfoArray[4]);
        long idle = Long.parseLong(cpuInfoArray[5]);
        long ioWait = Long.parseLong(cpuInfoArray[6]);
        long total = user + nice + system + idle + ioWait
                + Long.parseLong(cpuInfoArray[7])
                + Long.parseLong(cpuInfoArray[8]);
        String[] pidCpuInfoList = pidCpuRate.split(" ");
        if (pidCpuInfoList.length < 17) {
            return;
        }

        long appCpuTime = Long.parseLong(pidCpuInfoList[13])
                + Long.parseLong(pidCpuInfoList[14])
                + Long.parseLong(pidCpuInfoList[15])
                + Long.parseLong(pidCpuInfoList[16]);
        if (mTotalLast != 0) {
            long idleTime = idle - mIdleLast;
            long totalTime = total - mTotalLast;
           
            CpuInfo cpuInfo = new CpuInfo((totalTime - idleTime) * 100.0 / totalTime, (appCpuTime - mAppCpuTimeLast) *
                    100.0 / totalTime,
                    (user - mUserLast) * 100.0 / totalTime, (system - mSystemLast) * 100.0 / totalTime, (ioWait -
                    mIoWaitLast) * 100.0 / totalTime);
            synchronized (mCpuInfoEntries) {
                mCpuInfoEntries.put(System.currentTimeMillis(), cpuInfo);
                if (mCpuInfoEntries.size() > MAX_ENTRY_COUNT) {
                    int overSize = mCpuInfoEntries.size() - MAX_ENTRY_COUNT;
                    List<Long> willRemove = new ArrayList<>();
                    for (Map.Entry<Long, CpuInfo> entry : mCpuInfoEntries.entrySet()) {
                        willRemove.add(entry.getKey());
                        if (willRemove.size() >= overSize) {
                            break;
                        }
                    }
                    for (Long removeKey : willRemove) {
                        mCpuInfoEntries.remove(removeKey);
                    }
                }
            }
        }
        mUserLast = user;
        mSystemLast = system;
        mIdleLast = idle;
        mIoWaitLast = ioWait;
        mTotalLast = total;

        mAppCpuTimeLast = appCpuTime;
    }
}