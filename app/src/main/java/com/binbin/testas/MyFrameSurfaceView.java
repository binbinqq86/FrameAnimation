package com.binbin.testas;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * 采用surfaceView加载逐帧动画
 */
public class MyFrameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "MyFrameAnimationView";
    private SurfaceHolder holder = null;
    private OnAnimationListener onAnimationListener;
    private int resourceId[];
    private int[] duration;
    private Paint mPaint;
    private Handler mHandler=new Handler(Looper.getMainLooper());
    private BitmapFactory.Options options;
    public MyFrameSurfaceView(Context context, int resourceId[], int[] duration, OnAnimationListener onAnimationListener) {
        super(context);
        this.onAnimationListener=onAnimationListener;
        this.resourceId=resourceId;
        this.duration=duration;
        holder = getHolder();
        setZOrderOnTop(true);//放到最顶层，默认在下面，可能显示不出来
        setZOrderMediaOverlay(true);//解决surfaceView遮挡其他控件的问题（把其他控件提到surfaceView上面来）放在setZOrderOnTop之后
        holder.setFormat(PixelFormat.TRANSLUCENT);//背景透明
        holder.addCallback(this);
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setFilterBitmap(true);//滤波处理
        mPaint.setDither(true);//防抖动
        options=new BitmapFactory.Options();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                anim(0);
            }
        }.start();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed: ====" );
    }

    private void anim(int frameNumber){
        while(frameNumber<resourceId.length-1){
            options.inSampleSize=1;
            options.inMutable=true;
            Bitmap bitmap =  BitmapFactory.decodeResource(getContext().getResources(),resourceId[frameNumber],options);
            options.inBitmap=bitmap;
            Canvas canvas = holder.lockCanvas();
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            if(canvas==null){
                return;
            }
            canvas.drawPaint(mPaint);//清屏
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            canvas.drawBitmap(bitmap,null,new Rect(0,0,getWidth(),getHeight()),mPaint);
            holder.unlockCanvasAndPost(canvas);

            try {
                Thread.sleep(duration[frameNumber]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frameNumber++;
            if(frameNumber==resourceId.length-1){
                frameNumber=0;
            }
        }
        if(options.inBitmap!=null){
            options.inBitmap.recycle();
            options.inBitmap=null;
        }
        options=null;
        if (onAnimationListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onAnimationListener.onAnimationEnd();
                }
            });
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(options!=null&&options.inBitmap!=null){
            options.inBitmap.recycle();
            options.inBitmap=null;
        }
        options=null;
        System.gc();
        Log.e(TAG, "onDetachedFromWindow: ====" );
    }
}
