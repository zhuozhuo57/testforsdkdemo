package com.openailab.sdkdemotest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.openailab.facelibrary.FaceAPP;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.StringTokenizer;

public class OpenAiLabFaceSDK {
    private static OpenAiLabFaceSDK mInstance;
    public float[] feature = new float[128];
    public byte[] tmpPos = new byte[1024];
    private FaceAPP face = FaceAPP.GetInstance(); //face作为成员变量

    private OpenAiLabFaceSDK() {
    }

    public static OpenAiLabFaceSDK getInstance() {
        if (mInstance == null) {
            mInstance = new OpenAiLabFaceSDK();
        }
        return mInstance;
    }

    public static FaceAPP.Image rgbaToImage(String imagePath) {
        long startTimes = System.currentTimeMillis();
        FaceAPP.Image image = FaceAPP.GetInstance().new Image(); //初始化
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        long consumeTimes = System.currentTimeMillis() - startTimes;
        startTimes = System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Mat mRgba = new Mat(height, width, CvType.CV_8U, new Scalar(4));
        Utils.bitmapToMat(bitmap, mRgba);
        consumeTimes = System.currentTimeMillis() - startTimes;
        startTimes = System.currentTimeMillis();
        image.matAddrframe = mRgba.getNativeObjAddr();//image赋值
        try {
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        consumeTimes = System.currentTimeMillis() - startTimes;
        return image;
    }

    public static FaceAPP.Image rgbaToImage(Bitmap bitmap) {
        long startTimes = System.currentTimeMillis();
        FaceAPP.Image image = FaceAPP.GetInstance().new Image(); //初始化
        long consumeTimes = System.currentTimeMillis() - startTimes;
        startTimes = System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Mat mRgba = new Mat(height, width, CvType.CV_8U, new Scalar(4));
        Utils.bitmapToMat(bitmap, mRgba);
        consumeTimes = System.currentTimeMillis() - startTimes;
        startTimes = System.currentTimeMillis();
        image.matAddrframe = mRgba.getNativeObjAddr();//image赋值
        consumeTimes = System.currentTimeMillis() - startTimes;
        return image;
    }

    public void detectAction(FaceAPP.Image image) {
        printD("detectAction.start");
        long startTimes = System.currentTimeMillis();
        int[] ret = new int[1];

        int res = face.Detect(image, tmpPos, ret);
        long consumeTimes = System.currentTimeMillis() - startTimes;
        printE("Detect  耗时：" + consumeTimes);
        if (FaceAPP.SUCCESS == res) {
            //TODO
            String[] StrList = new String[160];
            String tmpStr = new String(tmpPos);
            //faces: <1>; [box, x0: <118.36>, y0: <49.7445>, x1: <278.951>, y1: <247.08>, title: <10001 no>, markx0: <4.73871e-38>, marky0: <0>, markx1: <0>, marky1: <2.64989e-27>, markx2: <-1.26299e-32>, marky2: <0>, markx3: <3.5593e-43>, marky3: <5.25744e+29>, markx4: <0>, marky4: <0>] �����
            printD("detectAction.tmpStr  " + tmpStr);
            StringTokenizer st = new StringTokenizer(tmpStr, " <>");
            int icount = 0;
            while (st.hasMoreElements()) {
                StrList[icount] = st.nextToken();
                icount++;
            }
            int faceNum = Integer.parseInt(StrList[1]);
            printE("detectAction  faceNum:" + faceNum);
            int shift = 1;
            float[] pos2 = new float[50];
            for (icount = 0; icount < Integer.parseInt(StrList[1]); icount++) {
                for (int j = 1; j <= 5; j++) {
                    try {
                        pos2[shift] = Float.parseFloat(StrList[2 + icount * 62 + j * 3]);
                    } catch (NumberFormatException e) {
                    }
                    shift++;
                }
            }
            if (icount == 0) {
                printE("detectAction  icount =" + icount);
            }
        } else {
            printE("detectAction  error ." + res);
        }
    }

    public float compareAction(float[] origin, float[] chose) {
        float[] score = new float[1];
        if (origin == null || chose == null) {
            return 0;
        }
        long startTimes = System.currentTimeMillis();
        int res = face.Compare(origin, chose, score);
        long consumeTimes = System.currentTimeMillis() - startTimes;
        printE("compareAction  耗时：" + consumeTimes);
        if (FaceAPP.SUCCESS == res) {
            printD("compareAction SUCCESS ");
        } else {
            printE("compareAction error " + res);
        }
        return score[0];
    }

    public float[] getFeatureAction(FaceAPP.Image image) {
        printD("getFeatureAction.start");
        int[] ret = new int[1];
        long startTimes = System.currentTimeMillis();
        for (int i = 0; i < feature.length; i++) {
            feature[i] = 0;
        }
        int res = face.GetFeature(image, feature, tmpPos, ret);
        long consumeTimes = System.currentTimeMillis() - startTimes;
        printE("GetFeature  耗时：" + consumeTimes);
        if (res == FaceAPP.SUCCESS) {
            //TODO 成功获取到人脸特征值
            String[] StrList = new String[160];
            String tmpStr = new String(tmpPos);
            //faces: <1>; [box, x0: <118.36>, y0: <49.7445>, x1: <278.951>, y1: <247.08>, title: <10001 no>, markx0: <4.73871e-38>, marky0: <0>, markx1: <0>, marky1: <2.64989e-27>, markx2: <-1.26299e-32>, marky2: <0>, markx3: <3.5593e-43>, marky3: <5.25744e+29>, markx4: <0>, marky4: <0>] �����
            printD("getFeatureAction.tmpStr  " + tmpStr);
            StringTokenizer st = new StringTokenizer(tmpStr, " <>");
            int icount = 0;
            while (st.hasMoreElements()) {
                StrList[icount] = st.nextToken();
                icount++;
            }
            StringBuilder builder = new StringBuilder("feature:");
            for (int i = 0; i < feature.length; i++) {
                builder.append(feature[i] + ";");
            }
            printD(builder.toString());
        } else {
            printE("getFeatureAction  error ." + res);
            return null;
        }
        return feature;
    }

    public int recognitionAction(FaceAPP.Image image, float[][] featurelist, int size, float[] scores) {
        int[] ret = new int[1];
        long startTimes = System.currentTimeMillis();
        int res = face.Recognize(image, featurelist, size, tmpPos, scores, ret);
        long consumeTimes = System.currentTimeMillis() - startTimes;
        printE("recognitionAction  耗时：" + consumeTimes);
        switch (res) {
            case FaceAPP.ERROR_INVALID_PARAM:
                printE("recognitionAction 未发现人脸");
                break;
            case FaceAPP.ERROR_NOT_EXIST:
                printE("recognitionAction 未提取到特征");
                break;
        }
        if (res >= 0) {
            printE("recognitionAction 识别成功：res=" + res + "   ret=" + ret[0] + " score:" + scores[0]);
            return ret[0];
        }
        return -1001;
    }

    public void releaseFaceApp() {
        face.Destroy();
    }

    public void initOpenAiLabAction(Context context) {
        copyFilesFassets(context, "openailab", "/sdcard/openailab");
        setParamAction();
    }

    public void setParamAction() {
        String[] name = {"a", "b", "c", "d", "factor", "min_size", "clarity", "perfoptimize", "livenessdetect", "gray2colorScale"};
        double[] value = {0.9, 0.9, 0.9, 0.6, 0.6, 64, 400, 0, 0, 0.5};
        face.SetParameter(name, value);
        face.SetParameter(name, value);
    }

    public void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
        }
    }

    private void printD(String msg) {
        Log.d("FaceAPP", msg);
    }

    private void printE(String msg) {
        Log.e("FaceAPP", msg);
    }
}
