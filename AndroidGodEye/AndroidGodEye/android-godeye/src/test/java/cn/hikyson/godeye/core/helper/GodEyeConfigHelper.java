package cn.hikyson.godeye.core.helper;

import java.io.InputStream;

import cn.hikyson.godeye.core.GodEyeConfig;
import cn.hikyson.godeye.core.utils.IoUtil;

public class GodEyeConfigHelper {
    public static GodEyeConfig createFromResource() {
        InputStream InputStream = null;
        try {
            InputStream = GodEyeConfigHelper.class.getClassLoader().getResourceAsStream("install.config");
            return GodEyeConfig.fromInputStream(InputStream);
        } finally {
            IoUtil.closeSilently(InputStream);
        }
    }

    public static GodEyeConfig createFromResource2() {
        InputStream InputStream = null;
        try {
            InputStream = GodEyeConfigHelper.class.getClassLoader().getResourceAsStream("install2.config");
            return GodEyeConfig.fromInputStream(InputStream);
        } finally {
            IoUtil.closeSilently(InputStream);
        }
    }
}
