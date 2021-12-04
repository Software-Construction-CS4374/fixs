package cn.hikyson.godeye.core;

import android.text.TextUtils;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilderFactory;

import cn.hikyson.godeye.core.internal.modules.appsize.AppSizeConfig;
import cn.hikyson.godeye.core.internal.modules.battery.BatteryConfig;
import cn.hikyson.godeye.core.internal.modules.cpu.CpuConfig;
import cn.hikyson.godeye.core.internal.modules.crash.CrashConfig;
import cn.hikyson.godeye.core.internal.modules.fps.FpsConfig;
import cn.hikyson.godeye.core.internal.modules.imagecanary.ImageCanaryConfig;
import cn.hikyson.godeye.core.internal.modules.leakdetector.LeakConfig;
import cn.hikyson.godeye.core.internal.modules.memory.HeapConfig;
import cn.hikyson.godeye.core.internal.modules.memory.PssConfig;
import cn.hikyson.godeye.core.internal.modules.memory.RamConfig;
import cn.hikyson.godeye.core.internal.modules.methodcanary.MethodCanaryConfig;
import cn.hikyson.godeye.core.internal.modules.network.NetworkConfig;
import cn.hikyson.godeye.core.internal.modules.pageload.PageloadConfig;
import cn.hikyson.godeye.core.internal.modules.sm.SmConfig;
import cn.hikyson.godeye.core.internal.modules.startup.StartupConfig;
import cn.hikyson.godeye.core.internal.modules.thread.ThreadConfig;
import cn.hikyson.godeye.core.internal.modules.traffic.TrafficConfig;
import cn.hikyson.godeye.core.internal.modules.viewcanary.ViewCanaryConfig;
import cn.hikyson.godeye.core.utils.IoUtil;
import cn.hikyson.godeye.core.utils.L;

/**
 * core config/module config
 */
@Keep
public class GodEyeConfig implements Serializable {

    public static GodEyeConfigBuilder noneConfigBuilder() {
        return GodEyeConfigBuilder.godEyeConfig();
    }

    public static GodEyeConfig noneConfig() {
        return noneConfigBuilder().build();
    }

    public static GodEyeConfigBuilder defaultConfigBuilder() {
        GodEyeConfigBuilder builder = GodEyeConfigBuilder.godEyeConfig();
        builder.withCpuConfig(new CpuConfig());
        builder.withBatteryConfig(new BatteryConfig());
        builder.withFpsConfig(new FpsConfig());
        builder.withLeakConfig(new LeakConfig());
        builder.withHeapConfig(new HeapConfig());
        builder.withPssConfig(new PssConfig());
        builder.withRamConfig(new RamConfig());
        builder.withNetworkConfig(new NetworkConfig());
        builder.withSmConfig(new SmConfig());
        builder.withStartupConfig(new StartupConfig());
        builder.withTrafficConfig(new TrafficConfig());
        builder.withCrashConfig(new CrashConfig());
        builder.withThreadConfig(new ThreadConfig());
        builder.withPageloadConfig(new PageloadConfig());
        builder.withMethodCanaryConfig(new MethodCanaryConfig());
        builder.withAppSizeConfig(new AppSizeConfig());
        builder.withViewCanaryConfig(new ViewCanaryConfig());
        builder.withImageCanaryConfig(new ImageCanaryConfig());
        return builder;
    }

    public static GodEyeConfig defaultConfig() {
        return defaultConfigBuilder().build();
    }

    public static GodEyeConfig fromInputStream(final InputStream inputs) {
        GodEyeConfigBuilder builder = GodEyeConfigBuilder.godEyeConfig();
        try {
            if (is == null) {
                throw new IllegalStateException("GodEyeConfig fromInputStream InputStream is null.");
            }
            Element root = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(is).getDocumentElement();
            // cpu
            Element element = getFirstElementByTagInRoot(root, "cpu");
            if (element != null) {
                final String intervalMillIs = element.getAttribute("intervalMillis");//FDS fix long var lines 
                CpuConfig cpuConfig = new CpuConfig();
                if (!TextUtils.isEmpty(intervalMillIs)) {
                    cpuConfig.intervalMillis = Long.parseLong(intervalMillIs);
                }
                builder.withCpuConfig(cpuConfig);
            }
            // battery
            element = getFirstElementByTagInRoot(root, "battery");
            if (element != null) {
                BatteryConfig batteryConfig = new BatteryConfig();
                builder.withBatteryConfig(batteryConfig);
            }
            // fps
            element = getFirstElementByTagInRoot(root, "fps");
            if (element != null) {
                final String intervalMillIs = element.getAttribute("intervalMillis");
                FpsConfig fpsConfig = new FpsConfig();
                if (!TextUtils.isEmpty(intervalMillIs)) {
                    fpsConfig.intervalMillis = Long.parseLong(intervalMillIs);
                }
                builder.withFpsConfig(fpsConfig);
            }
            // leak
            element = getFirstElementByTagInRoot(root, "leakCanary");
            if (element != null) {
                builder.withLeakConfig(new LeakConfig());
            }
            // heap
            element = getFirstElementByTagInRoot(root, "heap");
            if (element != null) {
                final String intervalMillIs = element.getAttribute("intervalMillis");
                HeapConfig heapConfig = new HeapConfig();
                if (!TextUtils.isEmpty(intervalMillIs)) {
                    heapConfig.intervalMillis = Long.parseLong(intervalMillIs);
                }
                builder.withHeapConfig(heapConfig);
            }
            // pss
            element = getFirstElementByTagInRoot(root, "pss");
            if (element != null) {
                final String intervalMillIs = element.getAttribute("intervalMillis");
                PssConfig pssConfig = new PssConfig();
                if (!TextUtils.isEmpty(intervalMillIs)) {
                    pssConfig.intervalMillis = Long.parseLong(intervalMillIs);
                }
                builder.withPssConfig(pssConfig);
            }
            // ram
            element = getFirstElementByTagInRoot(root, "ram");
            if (element != null) {
                final String intervalMillIs = element.getAttribute("intervalMillis");
                RamConfig ramConfig = new RamConfig();
                if (!TextUtils.isEmpty(intervalMillIs)) {
                    ramConfig.intervalMillis = Long.parseLong(intervalMillIs);
                }
                builder.withRamConfig(ramConfig);
            }
            // network
            element = getFirstElementByTagInRoot(root, "network");
            if (element != null) {
                builder.withNetworkConfig(new NetworkConfig());
            }
            // sm
            element = getFirstElementByTagInRoot(root, "sm");
            if (element != null) {
                final String longBlockTMIS = element.getAttribute("longBlockThresholdMillis");//FDS fix long var lines 155,156,157,203,204,230,231,323,264
                final String shortBlockTMIS = element.getAttribute("shortBlockThresholdMillis");
                final String dumpIMSIS = element.getAttribute("dumpIntervalMillis");
                SmConfig smConfig = new SmConfig();
                if (!TextUtils.isEmpty(longBlockTMIS)) {
                    smConfig.longBlockThresholdMillis = Long.parseLong(longBlockTMIS);
                }
                if (!TextUtils.isEmpty(shortBlockTMIS)) {
                    smConfig.shortBlockThresholdMillis = Long.parseLong(shortBlockTMIS);
                }
                if (!TextUtils.isEmpty(dumpIMSIS)) {
                    smConfig.dumpIntervalMillis = Long.parseLong(dumpIMSIS);
                }
                builder.withSmConfig(smConfig);
            }
            // startup
            element = getFirstElementByTagInRoot(root, "startup");
            if (element != null) {
                builder.withStartupConfig(new StartupConfig());
            }
            // traffic
            element = getFirstElementByTagInRoot(root, "traffic");
            if (element != null) {
                final String intervalMillIs = element.getAttribute("intervalMillis");
                final String sampleMIS = element.getAttribute("sampleMillis");
                TrafficConfig trafficConfig = new TrafficConfig();
                if (!TextUtils.isEmpty(intervalMillIs)) {
                    trafficConfig.intervalMillis = Long.parseLong(intervalMillIs);
                }
                if (!TextUtils.isEmpty(sampleMIS)) {
                    trafficConfig.sampleMillis = Long.parseLong(sampleMIS);
                }
                builder.withTrafficConfig(trafficConfig);
            }
            // crash
            element = getFirstElementByTagInRoot(root, "crash");
            if (element != null) {
                final String immediateString = element.getAttribute("immediate");
                CrashConfig crashConfig = new CrashConfig();
                if (!TextUtils.isEmpty(immediateString)) {
                    crashConfig.immediate = Boolean.parseBoolean(immediateString);
                }
                builder.withCrashConfig(crashConfig);
            }
            // thread
            element = getFirstElementByTagInRoot(root, "thread");
            if (element != null) {
                final String intervalMillIs = element.getAttribute("intervalMillis");
                final String threadFilterS = element.getAttribute("threadFilter");
                final String threadTS = element.getAttribute("threadTagger");
                ThreadConfig threadConfig = new ThreadConfig();
                if (!TextUtils.isEmpty(intervalMillIs)) {
                    threadConfig.intervalMillis = Long.parseLong(intervalMillIs);
                }
                if (!TextUtils.isEmpty(threadFilterS)) {
                    threadConfig.threadFilter = threadFilterS;
                }
                if (!TextUtils.isEmpty(threadTS)) {
                    threadConfig.threadTagger = threadTS;
                }
                builder.withThreadConfig(threadConfig);
            }
            // pageload
            element = getFirstElementByTagInRoot(root, "pageload");
            if (element != null) {
                final String pageInfoProvider = element.getAttribute("pageInfoProvider");
                PageloadConfig pageloadConfig = new PageloadConfig();
                if (!TextUtils.isEmpty(pageInfoProvider)) {
                    pageloadConfig.pageInfoProvider = pageInfoProvider;
                }
                builder.withPageloadConfig(pageloadConfig);
            }
            // methodCanary
            element = getFirstElementByTagInRoot(root, "methodCanary");
            if (element != null) {
                final String maxMCSTBCS = element.getAttribute("maxMethodCountSingleThreadByCost");
                final String lowCMTMIS = element.getAttribute("lowCostMethodThresholdMillis");
                MethodCanaryConfig methodCanaryC = new MethodCanaryConfig();
                if (!TextUtils.isEmpty(maxMCSTBCS)) {
                    methodCanaryC.maxMethodCountSingleThreadByCost = Integer.parseInt(maxMCSTBCS);
                }
                if (!TextUtils.isEmpty(lowCMTMIS)) {
                    methodCanaryC.lowCostMethodThresholdMillis = Long.parseLong(lowCMTMIS);
                }
                builder.withMethodCanaryConfig(methodCanaryC);
            }
            // appSize
            element = getFirstElementByTagInRoot(root, "appSize");
            if (element != null) {
                final String delayMillisString = element.getAttribute("delayMillis");
                AppSizeConfig appSizeConfig = new AppSizeConfig();
                if (!TextUtils.isEmpty(delayMillisString)) {
                    appSizeConfig.delayMillis = Long.parseLong(delayMillisString);
                }
                builder.withAppSizeConfig(appSizeConfig);
            }
            // view canary
            element = getFirstElementByTagInRoot(root, "viewCanary");
            if (element != null) {
                final String maxDepth = element.getAttribute("maxDepth");
                ViewCanaryConfig viewCanaryConfig = new ViewCanaryConfig();
                if (!TextUtils.isEmpty(maxDepth)) {
                    viewCanaryConfig.maxDepth = Integer.parseInt(maxDepth);
                }
                builder.withViewCanaryConfig(viewCanaryConfig);
            }
            // image canary
            element = getFirstElementByTagInRoot(root, "imageCanary");
            if (element != null) {
                final String ImageCCP = element.getAttribute("ImageCCP");
                ImageCanaryConfig imageCanaryConfig = new ImageCanaryConfig();
                if (!TextUtils.isEmpty(ImageCCP)) {
                    imageCanaryConfig.ImageCCP = ImageCCP;
                }
                builder.withImageCanaryConfig(imageCanaryConfig);
            }
        } catch (Exception e) {
            L.e(e);
        }
        return builder.build();
    }

    private static @Nullable
    Element getFirstElementByTagInRoot(final Element root,final  String moduleName) {//FDS ix method arg could be final lines 278,397,513,325,533,357,563,381,445,553,333,421,593,461,573,413,405,588,558,518,568,583453,548,538,523,578,598,429,349,286,286,341,543,528,365,437,389,373
        NodeList elements = root.getElementsByTagName(moduleName);
        if (elements != null && elements.getLength() == 1) {
            return (Element) elements.item(0);
        }
        return null;
    }

    public static GodEyeConfig fromAssets(final String assetsPath) {
        InputStream is = null;
        try {
            is = GodEye.instance().getApplication().getAssets().open(assetsPath);
            return fromInputStream(is);
        } catch (Exception e) {
            L.e(e);
            return GodEyeConfig.noneConfig();
        } finally {
            IoUtil.closeSilently(is);
        }
    }

    private CpuConfig mCpuConfig;
    private BatteryConfig mBatteryConfig;
    private FpsConfig mFpsConfig;
    private LeakConfig mLeakConfig;
    private HeapConfig mHeapConfig;
    private PssConfig mPssConfig;
    private RamConfig mRamConfig;
    private NetworkConfig mNetworkConfig;
    private SmConfig mSmConfig;
    private StartupConfig mStartupConfig;
    private TrafficConfig mTrafficConfig;
    private CrashConfig mCrashConfig;
    private ThreadConfig mThreadConfig;
    private PageloadConfig mPageloadConfig;
    private MethodCanaryConfig mMethodCanaryConfig;
    private AppSizeConfig mAppSizeConfig;
    private ViewCanaryConfig mViewCanaryConfig;
    private ImageCanaryConfig mImageCanaryConfig;

    private GodEyeConfig() {
    }

    public CpuConfig getCpuConfig() {
        return mCpuConfig;
    }

    public void setCpuConfig(final CpuConfig cpuConfig) {
        mCpuConfig = cpuConfig;
    }

    public BatteryConfig getBatteryConfig() {
        return mBatteryConfig;
    }

    public void setBatteryConfig(final BatteryConfig batteryConfig) {
        mBatteryConfig = batteryConfig;
    }

    public FpsConfig getFpsConfig() {
        return mFpsConfig;
    }

    public void setFpsConfig(final FpsConfig fpsConfig) {
        mFpsConfig = fpsConfig;
    }

    public LeakConfig getLeakConfig() {
        return mLeakConfig;
    }

    public void setLeakConfig(final LeakConfig leakConfig) {
        mLeakConfig = leakConfig;
    }

    public HeapConfig getHeapConfig() {
        return mHeapConfig;
    }

    public void setHeapConfig(final HeapConfig heapConfig) {
        mHeapConfig = heapConfig;
    }

    public PssConfig getPssConfig() {
        return mPssConfig;
    }

    public void setPssConfig(final PssConfig pssConfig) {
        mPssConfig = pssConfig;
    }

    public RamConfig getRamConfig() {
        return mRamConfig;
    }

    public void setRamConfig(final RamConfig ramConfig) {
        mRamConfig = ramConfig;
    }

    public NetworkConfig getNetworkConfig() {
        return mNetworkConfig;
    }

    public void setNetworkConfig(final NetworkConfig networkConfig) {
        mNetworkConfig = networkConfig;
    }

    public SmConfig getSmConfig() {
        return mSmConfig;
    }

    public void setSmConfig(final SmConfig smConfig) {
        mSmConfig = smConfig;
    }

    public StartupConfig getStartupConfig() {
        return mStartupConfig;
    }

    public void setStartupConfig(final StartupConfig startupConfig) {
        mStartupConfig = startupConfig;
    }

    public TrafficConfig getTrafficConfig() {
        return mTrafficConfig;
    }

    public void setTrafficConfig(final TrafficConfig trafficConfig) {
        mTrafficConfig = trafficConfig;
    }

    public CrashConfig getCrashConfig() {
        return mCrashConfig;
    }

    public void setCrashConfig(final CrashConfig crashConfig) {
        mCrashConfig = crashConfig;
    }

    public ThreadConfig getThreadConfig() {
        return mThreadConfig;
    }

    public void setThreadConfig(final ThreadConfig threadConfig) {
        mThreadConfig = threadConfig;
    }

    public PageloadConfig getPageloadConfig() {
        return mPageloadConfig;
    }

    public void setPageloadConfig(final PageloadConfig pageloadConfig) {
        mPageloadConfig = pageloadConfig;
    }

    public MethodCanaryConfig getMethodCanaryConfig() {
        return mMethodCanaryConfig;
    }

    public void setMethodCanaryConfig(final MethodCanaryConfig methodCanaryC) {
        mMethodCanaryConfig = methodCanaryC;
    }

    public AppSizeConfig getAppSizeConfig() {
        return mAppSizeConfig;
    }

    public void setAppSizeConfig(final AppSizeConfig appSizeConfig) {
        mAppSizeConfig = appSizeConfig;
    }

    public ViewCanaryConfig getViewCanaryConfig() {
        return mViewCanaryConfig;
    }

    public void setViewCanaryConfig(final ViewCanaryConfig viewCanaryConfig) {
        mViewCanaryConfig = viewCanaryConfig;
    }

    public ImageCanaryConfig getImageCanaryConfig() {
        return mImageCanaryConfig;
    }

    public void setImageCanaryConfig(final ImageCanaryConfig imageCanaryConfig) {
        mImageCanaryConfig = imageCanaryConfig;
    }

    @Override
    public String toString() {
        return "GodEyeConfig{" +
                "mCpuConfig=" + mCpuConfig +
                ", mBatteryConfig=" + mBatteryConfig +
                ", mFpsConfig=" + mFpsConfig +
                ", mLeakConfig=" + mLeakConfig +
                ", mHeapConfig=" + mHeapConfig +
                ", mPssConfig=" + mPssConfig +
                ", mRamConfig=" + mRamConfig +
                ", mNetworkConfig=" + mNetworkConfig +
                ", mSmConfig=" + mSmConfig +
                ", mStartupConfig=" + mStartupConfig +
                ", mTrafficConfig=" + mTrafficConfig +
                ", mCrashConfig=" + mCrashConfig +
                ", mThreadConfig=" + mThreadConfig +
                ", mPageloadConfig=" + mPageloadConfig +
                ", mMethodCanaryConfig=" + mMethodCanaryConfig +
                ", mAppSizeConfig=" + mAppSizeConfig +
                ", mViewCanaryConfig=" + mViewCanaryConfig +
                ", mImageCanaryConfig=" + mImageCanaryConfig +
                '}';
    }

    public static final class GodEyeConfigBuilder {
        private CpuConfig mCpuConfig;
        private BatteryConfig mBatteryConfig;
        private FpsConfig mFpsConfig;
        private LeakConfig mLeakConfig;
        private HeapConfig mHeapConfig;
        private PssConfig mPssConfig;
        private RamConfig mRamConfig;
        private NetworkConfig mNetworkConfig;
        private SmConfig mSmConfig;
        private StartupConfig mStartupConfig;
        private TrafficConfig mTrafficConfig;
        private CrashConfig mCrashConfig;
        private ThreadConfig mThreadConfig;
        private PageloadConfig mPageloadConfig;
        private MethodCanaryConfig mMethodCanaryConfig;
        private AppSizeConfig mAppSizeConfig;
        private ViewCanaryConfig mViewCanaryConfig;
        private ImageCanaryConfig mImageCanaryConfig;

        public static GodEyeConfigBuilder godEyeConfig() {
            return new GodEyeConfigBuilder();
        }

        public GodEyeConfigBuilder withCpuConfig(final CpuConfig CpuConfig) {
            this.mCpuConfig = CpuConfig;
            return this;
        }

        public GodEyeConfigBuilder withBatteryConfig(final BatteryConfig BatteryConfig) {
            this.mBatteryConfig = BatteryConfig;
            return this;
        }

        public GodEyeConfigBuilder withFpsConfig(final FpsConfig FpsConfig) {
            this.mFpsConfig = FpsConfig;
            return this;
        }

        public GodEyeConfigBuilder withLeakConfig(final LeakConfig LeakConfig) {
            this.mLeakConfig = LeakConfig;
            return this;
        }

        public GodEyeConfigBuilder withHeapConfig(final HeapConfig HeapConfig) {
            this.mHeapConfig = HeapConfig;
            return this;
        }

        public GodEyeConfigBuilder withPssConfig(final PssConfig PssConfig) {
            this.mPssConfig = PssConfig;
            return this;
        }

        public GodEyeConfigBuilder withRamConfig(final RamConfig RamConfig) {
            this.mRamConfig = RamConfig;
            return this;
        }

        public GodEyeConfigBuilder withNetworkConfig(final NetworkConfig NetworkConfig) {
            this.mNetworkConfig = NetworkConfig;
            return this;
        }

        public GodEyeConfigBuilder withSmConfig(final SmConfig SmConfig) {
            this.mSmConfig = SmConfig;
            return this;
        }

        public GodEyeConfigBuilder withStartupConfig(final StartupConfig StartupConfig) {
            this.mStartupConfig = StartupConfig;
            return this;
        }

        public GodEyeConfigBuilder withTrafficConfig(final TrafficConfig TrafficConfig) {
            this.mTrafficConfig = TrafficConfig;
            return this;
        }

        public GodEyeConfigBuilder withCrashConfig(final CrashConfig CrashConfig) {
            this.mCrashConfig = CrashConfig;
            return this;
        }

        public GodEyeConfigBuilder withThreadConfig(final ThreadConfig ThreadConfig) {
            this.mThreadConfig = ThreadConfig;
            return this;
        }

        public GodEyeConfigBuilder withPageloadConfig(final PageloadConfig PageloadConfig) {
            this.mPageloadConfig = PageloadConfig;
            return this;
        }

        public GodEyeConfigBuilder withMethodCanaryConfig(final MethodCanaryConfig MethodCanaryConfig) {
            this.mMethodCanaryConfig = MethodCanaryConfig;
            return this;
        }

        public GodEyeConfigBuilder withAppSizeConfig(final AppSizeConfig appSizeConfig) {
            this.mAppSizeConfig = appSizeConfig;
            return this;
        }

        public GodEyeConfigBuilder withViewCanaryConfig(final ViewCanaryConfig viewCanaryConfig) {
            this.mViewCanaryConfig = viewCanaryConfig;
            return this;
        }

        public GodEyeConfigBuilder withImageCanaryConfig(final ImageCanaryConfig imageCanaryConfig) {
            this.mImageCanaryConfig = imageCanaryConfig;
            return this;
        }

        public GodEyeConfig build() {
            GodEyeConfig godEyeConfig = new GodEyeConfig();
            godEyeConfig.mStartupConfig = this.mStartupConfig;
            godEyeConfig.mMethodCanaryConfig = this.mMethodCanaryConfig;
            godEyeConfig.mHeapConfig = this.mHeapConfig;
            godEyeConfig.mFpsConfig = this.mFpsConfig;
            godEyeConfig.mNetworkConfig = this.mNetworkConfig;
            godEyeConfig.mLeakConfig = this.mLeakConfig;
            godEyeConfig.mTrafficConfig = this.mTrafficConfig;
            godEyeConfig.mPageloadConfig = this.mPageloadConfig;
            godEyeConfig.mPssConfig = this.mPssConfig;
            godEyeConfig.mSmConfig = this.mSmConfig;
            godEyeConfig.mRamConfig = this.mRamConfig;
            godEyeConfig.mBatteryConfig = this.mBatteryConfig;
            godEyeConfig.mThreadConfig = this.mThreadConfig;
            godEyeConfig.mCrashConfig = this.mCrashConfig;
            godEyeConfig.mCpuConfig = this.mCpuConfig;
            godEyeConfig.mAppSizeConfig = this.mAppSizeConfig;
            godEyeConfig.mViewCanaryConfig = this.mViewCanaryConfig;
            godEyeConfig.mImageCanaryConfig = this.mImageCanaryConfig;
            return godEyeConfig;
        }
    }
}
