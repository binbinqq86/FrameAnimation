package com.binbin.testas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by : tb on 2017/9/27 下午2:50.
 * Description :
 */
public class MyAnimationDrawable1 {
    private static final String TAG = "MyAnimationDrawable";
    private static MyAnimationDrawable1 instance;
    private static final Handler handler=new Handler(Looper.getMainLooper());
    private MyAnimationDrawable1(){}
    public static synchronized MyAnimationDrawable1 getInstance(){
        if(instance==null){
            instance=new MyAnimationDrawable1();
        }
        return instance;
    }
    
    public void animateFrameDrawableResourceOneByOne(final int resIds[], final int durations[], final ImageView imageView,final int frameNumber, final OnAnimationListener onAnimationListener){
        imageView.setImageResource(resIds[frameNumber]);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(frameNumber<resIds.length-1){
                    animateFrameDrawableResourceOneByOne(resIds,durations,imageView,frameNumber+1,onAnimationListener);
                }else{
                    if (onAnimationListener != null) {
                        onAnimationListener.onAnimationEnd();
                    }
                }
            }
        },durations[frameNumber]);
    }
}
