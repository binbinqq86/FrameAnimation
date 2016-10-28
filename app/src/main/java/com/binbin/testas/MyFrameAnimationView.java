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
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * 采用surfaceView加载逐帧动画
 */
public class MyFrameAnimationView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder = null;
    private List<MyFrame> myFrames=new ArrayList<>();
    private OnAnimationListener onAnimationListener;
    private int resourceId[];
    private int[] duration;
    private Paint mPaint;
    private Handler mHandler=new Handler(Looper.getMainLooper());
    public MyFrameAnimationView(Context context, int resourceId[], int[] duration, OnAnimationListener onAnimationListener) {
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
                loadResources();
                anim(0);
            }
        }.start();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private void anim(int frameNumber){
        while(frameNumber<myFrames.size()-1){
            if(frameNumber!=0){
                Bitmap bit=myFrames.get(frameNumber-1).bitmap;
                if(bit!=null){
                    bit.recycle();
                    bit=null;
                }
            }
            final MyFrame thisFrame = myFrames.get(frameNumber);
            thisFrame.bitmap =  BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length);
            Canvas canvas = holder.lockCanvas();
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPaint(mPaint);//清屏
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            canvas.drawBitmap(thisFrame.bitmap,null,new Rect(0,0,getWidth(),getHeight()),mPaint);
            holder.unlockCanvasAndPost(canvas);

            try {
                Thread.sleep(thisFrame.duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            frameNumber++;
        }
        if (onAnimationListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onAnimationListener.onAnimationEnd();
                }
            });
        }
    }

    private void loadResources(){
        byte[] bytes = null;

        for (int i = 0; i < resourceId.length; i++) {
            try {
                bytes = toByteArray(getContext().getApplicationContext().getResources().openRawResource(resourceId[i]));
                MyFrame myFrame = new MyFrame();
                myFrame.bytes = bytes;
                myFrame.duration = duration[i];
                myFrames.add(myFrame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (onAnimationListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onAnimationListener.onDrawableLoaded(myFrames);
                    onAnimationListener.onAnimationStart();
                }
            });
        }
    }

    public static class MyFrame {
        byte[] bytes;
        int duration;
        Bitmap bitmap;
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        if (count > Integer.MAX_VALUE) {
//            return -1;
        }
        return output.toByteArray();
    }
}
