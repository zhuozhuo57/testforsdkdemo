package com.openailab.sdkdemotest;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.openailab.facelibrary.FaceAPP;

import java.io.File;

public class facesdktest extends AppCompatActivity {
    //v1.0.0aar
    public final static int MAX_REGISTER = 5000; // set max number of faces ,could be modify
    private final static int FEATURE_LEN = 128;
    private static final int Msg_detect = 10001;
    private static final int Msg_getFeature = 10002;
    private static final int Msg_recognition = 10003;
    private final static int FACENUM = 1;
    public static float[][] flist = new float[MAX_REGISTER][FEATURE_LEN];
    private static int fIndex = 0;
    public byte[] tmpPos = new byte[1024 * FACENUM];
    private ImageView imageView;
    private HandlerThread monitorThread = null;// 用于监控摄像头是否有图像或检查容量，检测各种变量是否正常，以及初始化
    private Handler monitorHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facesdktest);
        //imageView = (ImageView) findViewById(R.id.imageView);
        OpenAiLabFaceSDK.getInstance().initOpenAiLabAction(this);
        initHandler();
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory() + "/resident");
                if (file.exists() && file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File imageFile : files) {
                        String path = imageFile.getPath();
                        if (path.endsWith(".jpg")) {
                            printE("imagePath:" + path);
                            FaceAPP.Image image = OpenAiLabFaceSDK.rgbaToImage(path);
                            //TODO 注册特征值
                            float[] feature = OpenAiLabFaceSDK.getInstance().getFeatureAction(image);
                            if (feature != null) {
                                flist[fIndex] = feature.clone();
                                fIndex++;
                                printD("feature  " + feature + " \nfIndex= " + fIndex);
                            }

                        }

                    }

                }

            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File snapFile = new File(Environment.getExternalStorageDirectory() + "/snap");
                if (snapFile.exists() && snapFile.isDirectory()) {
                    File[] files = snapFile.listFiles();
                    for (File file : files) {
                        String path = file.getPath();
                        if (file.isDirectory()) {
                            File[] filesList = file.listFiles();
                            for (File file1 : filesList) {
                                String path1 = file1.getPath();
                                if (path1.endsWith(".jpg")) {
                                    printE("imagePath:" + path1);
                                    FaceAPP.Image image = OpenAiLabFaceSDK.rgbaToImage(path1);
                                    float[] scores = new float[1];
                                    int res = OpenAiLabFaceSDK.getInstance().recognitionAction(image, flist, fIndex, scores);
                                }
                            }
                        } else {
                            if (path.endsWith(".jpg")) {
                                printE("imagePath:" + path);
                                FaceAPP.Image image = OpenAiLabFaceSDK.rgbaToImage(path);
                                float[] scores = new float[1];
                                int res = OpenAiLabFaceSDK.getInstance().recognitionAction(image, flist, fIndex, scores);
                            }
                        }

                    }

                }
            }
        });

       /* findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testCompareAction();
                //testMoreFaceAction();
               // getRecognizetime();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initHandler() {
        monitorThread = new HandlerThread("monitorThread:"
                + System.currentTimeMillis());
        monitorThread.start();
        monitorHandler = new Handler(monitorThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Msg_detect:
                        FaceAPP.Image image = (FaceAPP.Image) msg.obj;
                        OpenAiLabFaceSDK.getInstance().detectAction(image);
                        break;
                    case Msg_getFeature:
                        FaceAPP.Image image1 = (FaceAPP.Image) msg.obj;
                        OpenAiLabFaceSDK.getInstance().getFeatureAction(image1);
                        break;
                    case Msg_recognition:
                        FaceAPP.Image recognition = (FaceAPP.Image) msg.obj;
                        float[] scores = new float[1];
                        OpenAiLabFaceSDK.getInstance().recognitionAction(recognition,flist,fIndex,scores);
                        break;
                }
            }
        };
    }

    private void testCompareAction() {
        //TODO 测试比对
        String path = Environment.getExternalStorageDirectory() + "/faceValidate" + "/3.jpg";
        printE("imagePath:" + path);
        FaceAPP.Image image = OpenAiLabFaceSDK.rgbaToImage(path);
        float[] orign = OpenAiLabFaceSDK.getInstance().getFeatureAction(image);
        float score = OpenAiLabFaceSDK.getInstance().compareAction(orign, orign);
        printD("compareResult  " + score);
    }

    private void testMoreFaceAction() {
        String path = Environment.getExternalStorageDirectory() + "/persons.png";
        FaceAPP.Image image = OpenAiLabFaceSDK.rgbaToImage(path);
        Message msg = new Message();
        msg.what = Msg_detect;
        msg.obj = image;
        monitorHandler.sendMessage(msg);
    }

    public void getRecognizetime() {
        long liveness_t1;
        long liveness_t2;
        FaceAPP face = FaceAPP.GetInstance();
        FaceAPP.Image image = FaceAPP.GetInstance().new Image();
        liveness_t1 = System.currentTimeMillis();
        // face.Recognize(image,FileOperator.getflist(),FileOperator.getfIndex(),tmpPos,FaceAPP.class.GetFeature(int[] var4);
        liveness_t2 = System.currentTimeMillis();
        Log.d("zheng", "Recognize time:" + (liveness_t2 - liveness_t1));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OpenAiLabFaceSDK.getInstance().releaseFaceApp();
    }

    private void printD(String msg) {
        Log.d("FaceAPP", msg);
    }

    private void printE(String msg) {
        Log.e("FaceAPP", msg);
    }
}

