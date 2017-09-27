package com.binbin.testas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by -- on 2016/10/12.
 */

public class MyAnimationDrawable2 {
    private static final String TAG = "MyAnimationDrawable2";
    private static MyAnimationDrawable2 instance;
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private BitmapFactory.Options options;
    private MyAnimationDrawable2() {
    }
    
    public static synchronized MyAnimationDrawable2 getInstance() {
        if (instance == null) {
            instance = new MyAnimationDrawable2();
        }
        return instance;
    }
    
    /**
     * 从drawable文件中加载并播发逐帧动画
     *
     * @param resourceId
     * @param duration
     * @param imageView
     * @param onAnimationListener
     */
    public void animateFromDrawableResource(final int resourceId[], final int[] duration, final ImageView imageView, final OnAnimationListener onAnimationListener) {
        //每次只加载一个，新方案，原来是一次性加载到内存中
        animateDrawableManually(new BitmapFactory.Options(), resourceId, duration, new ArrayList<MyFrame>(), imageView, onAnimationListener, 0);
    }
    
    /**
     * 不进行回收，重用之前的bitmap
     *
     * @param resIds
     * @param durations
     * @param myFrame
     * @param imageView
     * @param onAnimationListener
     * @param frameNumber
     */
    private void animateDrawableManually(final BitmapFactory.Options options, final int resIds[], final int durations[], final List<MyFrame> myFrame, final ImageView imageView, final OnAnimationListener onAnimationListener, final int frameNumber) {
        MyFrame thisFrame = null;
        if (frameNumber == 0) {
            thisFrame = new MyFrame();
            thisFrame.duration = durations[0];
            thisFrame.bitmap = BitmapFactory.decodeResource(imageView.getContext().getApplicationContext().getResources(), resIds[0], options);
            myFrame.add(thisFrame);
        } else {
            thisFrame = myFrame.get(1);
            myFrame.remove(0);
        }
        
        options.inMutable = true;//true 这样返回的bitmap 才是mutable 也就是可重用的，否则是不能重用的
        options.inSampleSize=1;
        options.inBitmap = thisFrame.bitmap;
        imageView.setImageBitmap(thisFrame.bitmap);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (frameNumber < resIds.length - 1) {
                    //准备并播放下一帧
                    MyFrame nextFrame = new MyFrame();
                    nextFrame.duration = durations[frameNumber + 1];
                    nextFrame.bitmap = BitmapFactory.decodeResource(imageView.getContext().getApplicationContext().getResources(), resIds[frameNumber + 1], options);
                    boolean can1 = Utils.canUseForInBitmap(nextFrame.bitmap, options);
                    Log.e(TAG, "run: " + "$$$" + can1+"$"+nextFrame.bitmap.getHeight());
                    myFrame.add(nextFrame);
                    animateDrawableManually(options, resIds, durations, myFrame, imageView, onAnimationListener, frameNumber + 1);
                } else {
                    options.inBitmap.recycle();
                    myFrame.clear();
                    if (onAnimationListener != null) {
                        onAnimationListener.onAnimationEnd();
                    }
                }
            }
        }, thisFrame.duration);
    }
    
    public static class MyFrame {
        int duration;
        Bitmap bitmap;
    }
}
