package cn.hikyson.godeye.core.internal.modules.memory;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class NativeHeapInfo implements Serializable {
	NativeHeapInfo(){}//FDS fix at least one constructor
    public long heapFreeSizeKb;
    public long heapSizeKb;
    public long heapAllocatedKb;

    @Override
    public String toString() {
        return "NativeHeapInfo{" +
                "heapFreeSizeKb=" + heapFreeSizeKb +
                ", heapSizeKb=" + heapSizeKb +
                ", heapAllocatedKb=" + heapAllocatedKb +
                '}';
    }
}
