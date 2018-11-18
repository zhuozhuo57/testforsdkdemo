package com.openailab.sdkdemotest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.InputFilter;
import android.util.Log;

import com.openailab.facelibrary.FaceAPP;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.constraint.Constraints.TAG;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Before
    public void setup() {
        int i =0;
        Log.d(TAG, "setup: "+i);
        i++;
    }
    @Test
    public void test_get_version() {
        // Context of the app under test.
        int i=0;
        if(i<200) {
             {
                FaceAPP faceAPP = FaceAPP.GetInstance();
                String libversion = faceAPP.GetFacelibVersion();
                String sdkversion = faceAPP.GetVersion();
                Log.d(TAG, "zhangjian: testing libversiong" + faceAPP.GetFacelibVersion() + "sdkversion" + faceAPP.GetVersion());
                assertEquals("V0.3.1", sdkversion);
                assertEquals("@0x8093114e482519878d226621dc5e4e91f1ba7467@", libversion);
                i++;
            }
        }
    }

    @Test
    public void test_get_useAppContext() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.openailab.sdkdemotest", appContext.getPackageName());
        String  getpackagename = appContext.getPackageName();
        Log.d(TAG, "test_get_useAppContext: " + getpackagename);
    }
   // @Test
    public void test_get_filelistname() {


    }
    @After
    public void setdown(){
        FaceAPP faceAPP =FaceAPP.getInstance();
        faceAPP.Destroy();
        int i=0;
        Log.d(TAG, "setdown: "+i);
        i++;
    }
}
