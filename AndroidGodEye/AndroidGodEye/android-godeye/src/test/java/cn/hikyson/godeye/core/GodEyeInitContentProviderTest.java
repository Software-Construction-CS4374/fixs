package cn.hikyson.godeye.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class GodEyeInitContentProviderTest {
	GodEyeInitContentProviderTest(){}//FDS fix at least one constructor

    @Test
    public void action() {
        GodEyeInitContentProvider godEyeInitCP = new GodEyeInitContentProvider();//FDS fix long var
        try {
            godEyeInitCP.delete(null, null, null);
            fail();
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            godEyeInitCP.getType(null);
            fail();
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            godEyeInitCP.insert(null, null);
            fail();
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            godEyeInitCP.query(null, null, null, null, null);
            fail();
        } catch (UnsupportedOperationException ignore) {
        }
        try {
            godEyeInitCP.update(null, null, null, null);
            fail();
        } catch (UnsupportedOperationException ignore) {
        }
    }
}