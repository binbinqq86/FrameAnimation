package com.binbin.testas.opengl;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.binbin.testas.OnAnimationListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 采用surfaceView加载逐帧动画
 */
public class MyFrameAnimationOpenGL extends GLSurfaceView implements GLSurfaceView.Renderer{
    private OnAnimationListener onAnimationListener;
    private int resourceId[];
    private int[] duration;
    private Handler mHandler=new Handler(Looper.getMainLooper());
    private int frameNumber;
    private Screen mScreen;
    private long mLastTime;
    private boolean first=true;
    private BitmapFactory.Options options;
    public MyFrameAnimationOpenGL(Context context, int resourceId[], int[] duration, OnAnimationListener onAnimationListener) {
        super(context);
        this.onAnimationListener=onAnimationListener;
        this.resourceId=resourceId;
        this.duration=duration;
        setZOrderOnTop(true);
        setZOrderMediaOverlay(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);//修改GLSurfaceView颜色模式为有透明度选项的模式
        getHolder().setFormat(PixelFormat.TRANSLUCENT);//背景透明
        setRenderer(this);
        options=new BitmapFactory.Options();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mScreen=new Screen(getContext());
        // Set the background color to black ( rgba ).透明背景
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);  // OpenGL docs.
        // Enable Smooth Shading, default not really needed.
        gl.glShadeModel(GL10.GL_SMOOTH);// OpenGL docs.
        // Depth buffer setup.
        gl.glClearDepthf(1.0f);// OpenGL docs.
        // Enables depth testing.
        gl.glEnable(GL10.GL_DEPTH_TEST);// OpenGL docs.
        // The type of depth testing to do.
        gl.glDepthFunc(GL10.GL_LEQUAL);// OpenGL docs.
        // Really nice perspective calculations.
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, // OpenGL docs.
                GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Sets the current view port to the new size.
        gl.glViewport(0, 0, width, height);// OpenGL docs.
        // Select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);// OpenGL docs.
        // Reset the projection matrix
        gl.glLoadIdentity();// OpenGL docs.
        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f,
                (float) width / (float) height,
                0.1f, 100.0f);
        // Select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);// OpenGL docs.
        // Reset the modelview matrix
        gl.glLoadIdentity();// OpenGL docs.
    }

    @Override
    public void onDrawFrame(GL10 gl) {

//        gl.glColor4f(1f,1f,1f,0f);

        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | // OpenGL docs.
                GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(0, 0, -4);
        Log.e("tianbin","======##################========"+frameNumber);
        anim2(gl);
    }
    private void anim2(GL10 gl){
        try {
            Thread.sleep(duration[frameNumber]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(frameNumber<resourceId.length-1){
            options.inMutable=true;
            options.inSampleSize=1;
            Bitmap bitmap=BitmapFactory.decodeResource(getResources(),resourceId[frameNumber],options);
            options.inBitmap=bitmap;
            mScreen.draw(gl,bitmap);
            frameNumber++;
        }else{
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(options!=null&&options.inBitmap!=null){
                        options.inBitmap.recycle();
                        options.inBitmap=null;
                    }
                    options=null;
                    if (onAnimationListener != null) {
                        onAnimationListener.onAnimationEnd();
                        onAnimationListener=null;//此处会多次进入，所以置空，保证只回调一次
                    }
                }
            });
        }
    }

}
