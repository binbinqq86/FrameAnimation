package com.binbin.testas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;

public class AnimView extends SurfaceView implements SurfaceHolder.Callback {
    private BitmapRegionDecoder bitmapRegionDecoder;
    private SurfaceHolder mHolder;
    private boolean isrunning = true;
    private AnimThread thread;
    private Paint mPaint;
    private int WIDTH = 0;
    private int HEIGHT = 0;
    private int state = -1;
    private boolean isstart = false;
    private boolean isblinkfirst = false;
    private int rate = 40;
    private int index = 0;
    private Matrix matrix;
    private Random rand;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            isblinkfirst = true;
        };
    };
    private SparseArray<WeakReference<Bitmap>> weakBitmaps;
    private SparseArray<WeakReference<Bitmap>> cweakBitmaps;

    private BitmapFactory.Options options;

    public AnimView(Context context) {
        super(context);
        init();

    }

    public AnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        weakBitmaps = new SparseArray<WeakReference<Bitmap>>();
        cweakBitmaps = new SparseArray<WeakReference<Bitmap>>();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        setState(FaceBean.BLINK);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        matrix = new Matrix();
        float[] values = { -1f, 0.0f, 0.0f, 0.0f, 1f, 0.0f, 0.0f, 0.0f, 1.0f };
        matrix.setValues(values);
        WindowManager manger = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manger.getDefaultDisplay().getMetrics(displayMetrics);
        WIDTH = displayMetrics.widthPixels / 2;
        HEIGHT = displayMetrics.heightPixels / 2;
        rand = new Random();
        options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        handler.sendEmptyMessageDelayed(0, 1000 * (4 + rand.nextInt(4)));
        thread = new AnimThread();
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (thread != null) {
            thread.stopThread();
        }
    }

    public class AnimThread extends Thread {

        @Override
        public void run() {
            super.run();
            SurfaceHolder holder = mHolder;
//            while (isrunning) {
//                Canvas canvas = holder.lockCanvas();
//                if (canvas == null)
//                    continue;
//                synchronized (AnimThread.class) {
//                    AnimBean.Frames frames;
//                    switch (state) {
//                        case FaceBean.BLINK:
//                            frames = KidbotRobotApplication.animBlink.getFrames()
//                                    .get(index);
//                            if (frames.getFrame().getW() <= 0) {
//                            } else {
//                                Rect rect = new Rect(frames.getFrame().getX(),
//                                        frames.getFrame().getY(), frames.getFrame()
//                                        .getX()
//                                        + frames.getSourceSize().getW(),
//                                        frames.getFrame().getY()
//                                                + frames.getSourceSize().getH());
//                                WeakReference<Bitmap> weakBitmap = weakBitmaps
//                                        .get(index);
//                                Bitmap map = null;
//                                if (weakBitmap == null) {
//                                    map = bitmapRegionDecoder.decodeRegion(rect,
//                                            options);
//                                    weakBitmaps.put(index,
//                                            new WeakReference<Bitmap>(map));
//                                } else {
//                                    map=weakBitmap.get();
//                                    if (map == null) {
//                                        map = bitmapRegionDecoder.decodeRegion(
//                                                rect, options);
//                                        weakBitmaps.put(index,
//                                                new WeakReference<Bitmap>(map));
//                                    }
//                                }
//                                if (map == null) {
//                                    holder.unlockCanvasAndPost(canvas);
//                                    continue;
//                                }
//                                mPaint.setXfermode(new PorterDuffXfermode(
//                                        PorterDuff.Mode.CLEAR));
//                                canvas.drawPaint(mPaint);
//                                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//                                canvas.drawBitmap(map,
//                                        (int) (WIDTH - (map.getWidth() * 1) - 150),
//                                        (int) (HEIGHT - (map.getHeight() / 2)),
//                                        mPaint);
//                                canvas.drawBitmap(map, (int) (WIDTH + 150),
//                                        (int) (HEIGHT - (map.getHeight() / 2)),
//                                        mPaint);
//
//                                if (index == 0) {
//
//                                }
//
//                                if (map.isRecycled()) {
//                                    map.recycle();
//                                }
//
//                            }
//                            if (!isstart) {
//                                if (index < KidbotRobotApplication.animBlink
//                                        .getFrames().size()) {
//                                    index++;
//                                    if (index == KidbotRobotApplication.animBlink
//                                            .getFrames().size()) {
//                                        index--;
//                                        isstart = true;
//                                        if (rand.nextInt(10) <= 2) {
//                                            index = 1;
//                                        }
//                                    }
//                                } else {
//                                    index--;
//                                    isstart = true;
//                                }
//                            } else {
//                                if (index > 0) {
//                                    index--;
//                                    if (index == 0) {
//                                        isstart = false;
//                                    }
//                                } else {
//                                    index++;
//                                    isstart = false;
//                                }
//                            }
//                            if (!isblinkfirst) {
//                                index = 0;
//                            } else {
//                                if (index == KidbotRobotApplication.animBlink
//                                        .getFrames().size() - 1) {
//                                    isblinkfirst = false;
//                                    index = 0;
//                                    handler.sendEmptyMessageDelayed(0,
//                                            1000 * (4 + rand.nextInt(4)));
//                                }
//                            }
//                            break;
//                        case FaceBean.ANGRY:
//                            frames = KidbotRobotApplication.animAngry.getFrames()
//                                    .get(index);
//                            if (frames.getFrame().getW() <= 0) {
//                            } else {
//                                Rect rect = new Rect(frames.getFrame().getX(),
//                                        frames.getFrame().getY(), frames.getFrame()
//                                        .getX() + frames.getFrame().getW(),
//                                        frames.getFrame().getH()
//                                                + frames.getFrame().getX());
//                                WeakReference<Bitmap> weakBitmap = weakBitmaps
//                                        .get(index);
//                                Bitmap map = null;
//                                if (weakBitmap == null) {
//                                    map = bitmapRegionDecoder.decodeRegion(rect,
//                                            options);
//                                    weakBitmaps.put(index,
//                                            new WeakReference<Bitmap>(map));
//                                } else {
//                                    map=weakBitmap.get();
//                                    if (map == null) {
//                                        map = bitmapRegionDecoder.decodeRegion(
//                                                rect, options);
//                                        weakBitmaps.put(index,
//                                                new WeakReference<Bitmap>(map));
//                                    }
//                                }
//                                if (map == null) {
//                                    holder.unlockCanvasAndPost(canvas);
//                                    continue;
//                                }
//                                mPaint.setXfermode(new PorterDuffXfermode(
//                                        PorterDuff.Mode.CLEAR));
//                                canvas.drawPaint(mPaint);
//                                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//                                Bitmap dstbmp =null;
//                                weakBitmap=cweakBitmaps.get(index);
//                                if(weakBitmap==null){
//                                    dstbmp = Bitmap.createBitmap(map, 0, 0,
//                                            map.getWidth(), map.getHeight(),
//                                            matrix, true);
//                                    cweakBitmaps.put(index,
//                                            new WeakReference<Bitmap>(dstbmp));
//                                }else{
//                                    dstbmp=weakBitmap.get();
//                                    if(dstbmp==null){
//                                        dstbmp = Bitmap.createBitmap(map, 0, 0,
//                                                map.getWidth(), map.getHeight(),
//                                                matrix, true);
//                                        cweakBitmaps.put(index,
//                                                new WeakReference<Bitmap>(dstbmp));
//                                    }
//                                }
//                                canvas.drawBitmap(
//                                        map,
//                                        frames.getSpriteSourceSize().getX()
//                                                + (int) (WIDTH
//                                                - (map.getWidth() * 1) - 150),
//                                        frames.getSpriteSourceSize().getY()
//                                                + (int) (HEIGHT - (map.getHeight() / 2)),
//                                        mPaint);
//                                canvas.drawBitmap(dstbmp, frames
//                                                .getSpriteSourceSize().getX()
//                                                + (int) (WIDTH + 150), frames
//                                                .getSpriteSourceSize().getY()
//                                                + (int) (HEIGHT - (map.getHeight() / 2)),
//                                        mPaint);
//                                if (dstbmp.isRecycled()) {
//                                    dstbmp.recycle();
//                                }
//                                if (map.isRecycled()) {
//                                    map.recycle();
//                                }
//                            }
//                            if (!isstart) {
//                                if (index < KidbotRobotApplication.animAngry
//                                        .getFrames().size()) {
//                                    index++;
//                                    if (index == KidbotRobotApplication.animAngry
//                                            .getFrames().size()) {
//                                        index--;
//                                        isstart = true;
//                                    }
//                                } else {
//                                    index--;
//                                    isstart = true;
//                                }
//                            } else {
//                                if (index > 0) {
//                                    index--;
//                                    if (index == 0) {
//                                        isstart = false;
//                                    }
//                                } else {
//                                    index++;
//                                    isstart = false;
//                                }
//                            }
//                            break;
//                        case FaceBean.HAPPY:
//                            frames = KidbotRobotApplication.animHappy.getFrames()
//                                    .get(index);
//                            if (frames.getFrame().getW() <= 0) {
//                            } else {
//                                Rect rect = new Rect(frames.getFrame().getX(),
//                                        frames.getFrame().getY(), frames.getFrame()
//                                        .getX()
//                                        + frames.getSourceSize().getW(),
//                                        frames.getFrame().getY()
//                                                + frames.getSourceSize().getH());
//                                WeakReference<Bitmap> weakBitmap = weakBitmaps
//                                        .get(index);
//                                Bitmap map = null;
//                                if (weakBitmap == null) {
//                                    map = bitmapRegionDecoder.decodeRegion(rect,
//                                            options);
//                                    weakBitmaps.put(index,
//                                            new WeakReference<Bitmap>(map));
//                                } else {
//                                    map=weakBitmap.get();
//                                    if (map == null) {
//                                        map = bitmapRegionDecoder.decodeRegion(
//                                                rect, options);
//                                        weakBitmaps.put(index,
//                                                new WeakReference<Bitmap>(map));
//                                    }
//                                }
//                                if (map == null) {
//                                    holder.unlockCanvasAndPost(canvas);
//                                    continue;
//                                }
//                                mPaint.setXfermode(new PorterDuffXfermode(
//                                        PorterDuff.Mode.CLEAR));
//                                canvas.drawPaint(mPaint);
//                                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//                                Bitmap dstbmp =null;
//                                weakBitmap=cweakBitmaps.get(index);
//                                if(weakBitmap==null){
//                                    dstbmp = Bitmap.createBitmap(map, 0, 0,
//                                            map.getWidth(), map.getHeight(),
//                                            matrix, true);
//                                    cweakBitmaps.put(index,
//                                            new WeakReference<Bitmap>(dstbmp));
//                                }else{
//                                    dstbmp=weakBitmap.get();
//                                    if(dstbmp==null){
//                                        dstbmp = Bitmap.createBitmap(map, 0, 0,
//                                                map.getWidth(), map.getHeight(),
//                                                matrix, true);
//                                        cweakBitmaps.put(index,
//                                                new WeakReference<Bitmap>(dstbmp));
//                                    }
//                                }
//                                canvas.drawBitmap(
//                                        map,
//                                        frames.getSpriteSourceSize().getX()
//                                                + (int) (WIDTH
//                                                - (map.getWidth() * 1) - 150),
//                                        frames.getSpriteSourceSize().getY()
//                                                + (int) (HEIGHT - (map.getHeight() / 2)),
//                                        mPaint);
//                                canvas.drawBitmap(dstbmp, frames
//                                                .getSpriteSourceSize().getX()
//                                                + (int) (WIDTH + 150), frames
//                                                .getSpriteSourceSize().getY()
//                                                + (int) (HEIGHT - (map.getHeight() / 2)),
//                                        mPaint);
//                                // if (dstbmp.isRecycled()) {
//                                // dstbmp.recycle();
//                                // }
//                                // if (map.isRecycled()) {
//                                // map.recycle();
//                                // }
//
//                            }
//                            if (!isstart) {
//                                if (index < KidbotRobotApplication.animHappy
//                                        .getFrames().size()) {
//                                    index++;
//                                    if (index == KidbotRobotApplication.animHappy
//                                            .getFrames().size()) {
//                                        index--;
//                                        isstart = true;
//                                    }
//                                } else {
//                                    index--;
//                                    isstart = true;
//                                }
//                            } else {
//                                if (index > 0) {
//                                    index--;
//                                    if (index == 0) {
//                                        isstart = false;
//                                    }
//                                } else {
//                                    index++;
//                                    isstart = false;
//                                }
//                            }
//                            break;
//                        case FaceBean.RESOLVE:
//                            break;
//                        case FaceBean.RISUS:
//                            break;
//                        case FaceBean.SEERIGHT:
//                            break;
//                        case FaceBean.SAD:
//                            frames = KidbotRobotApplication.animSad.getFrames()
//                                    .get(index);
//                            if (frames.getFrame().getW() <= 0) {
//                            } else {
//                                Rect rect = new Rect(frames.getFrame().getX(),
//                                        frames.getFrame().getY(), frames.getFrame()
//                                        .getX()
//                                        + frames.getSourceSize().getW(),
//                                        frames.getFrame().getY()
//                                                + frames.getSourceSize().getH());
//
//                                WeakReference<Bitmap> weakBitmap = weakBitmaps
//                                        .get(index);
//                                Bitmap map = null;
//                                if (weakBitmap == null) {
//                                    map = bitmapRegionDecoder.decodeRegion(rect,
//                                            options);
//                                    weakBitmaps.put(index,
//                                            new WeakReference<Bitmap>(map));
//                                } else {
//                                    map=weakBitmap.get();
//                                    if (map == null) {
//                                        map = bitmapRegionDecoder.decodeRegion(
//                                                rect, options);
//                                        weakBitmaps.put(index,
//                                                new WeakReference<Bitmap>(map));
//                                    }
//                                }
//                                if (map == null) {
//                                    holder.unlockCanvasAndPost(canvas);
//                                    continue;
//                                }
//                                mPaint.setXfermode(new PorterDuffXfermode(
//                                        PorterDuff.Mode.CLEAR));
//                                canvas.drawPaint(mPaint);
//                                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//                                Bitmap dstbmp =null;
//                                weakBitmap=cweakBitmaps.get(index);
//                                if(weakBitmap==null){
//                                    dstbmp = Bitmap.createBitmap(map, 0, 0,
//                                            map.getWidth(), map.getHeight(),
//                                            matrix, true);
//                                    cweakBitmaps.put(index,
//                                            new WeakReference<Bitmap>(dstbmp));
//                                }else{
//                                    dstbmp=weakBitmap.get();
//                                    if(dstbmp==null){
//                                        dstbmp = Bitmap.createBitmap(map, 0, 0,
//                                                map.getWidth(), map.getHeight(),
//                                                matrix, true);
//                                        cweakBitmaps.put(index,
//                                                new WeakReference<Bitmap>(dstbmp));
//                                    }
//                                }
//                                canvas.drawBitmap(
//                                        map,
//                                        frames.getSpriteSourceSize().getX()
//                                                + (int) (WIDTH
//                                                - (map.getWidth() * 1) - 150),
//                                        frames.getSpriteSourceSize().getY()
//                                                + (int) (HEIGHT - (map.getHeight() / 2)),
//                                        mPaint);
//                                canvas.drawBitmap(dstbmp, frames
//                                                .getSpriteSourceSize().getX()
//                                                + (int) (WIDTH + 150), frames
//                                                .getSpriteSourceSize().getY()
//                                                + (int) (HEIGHT - (map.getHeight() / 2)),
//                                        mPaint);
//                                if (dstbmp.isRecycled()) {
//                                    dstbmp.recycle();
//                                }
//                                if (map.isRecycled()) {
//                                    map.recycle();
//                                }
//                            }
//                            if (!isstart) {
//                                if (index < KidbotRobotApplication.animSad
//                                        .getFrames().size()) {
//                                    index++;
//                                    if (index == KidbotRobotApplication.animSad
//                                            .getFrames().size()) {
//                                        index--;
//                                        isstart = true;
//                                    }
//                                } else {
//                                    index--;
//                                    isstart = true;
//                                }
//                            } else {
//                                if (index > 0) {
//                                    index--;
//                                    if (index == 0) {
//                                        isstart = false;
//                                    }
//                                } else {
//                                    index++;
//                                    isstart = false;
//                                }
//                            }
//                            break;
//                        default:
//                            break;
//                    }
//                }
//                holder.unlockCanvasAndPost(canvas);
//                try {
//                    Thread.sleep(rate);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        }

        public void stopThread() {
            isrunning = false;
            try {
                join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setRate(int rate) {
        this.rate = rate;
    }

    public int getState() {
        return this.state;
    }

    public synchronized void setState(int state) {
        // if (FaceBean.BLINK == this.state) {
        // while ((index != KidbotRobotApplication.animBlink.getFrames()
        // .size() - 1)) {
        // continue;
        // }
        // }
        cweakBitmaps.clear();
        weakBitmaps.clear();
        this.state = state;
        this.index = 0;

//        switch (state) {
//            case FaceBean.BLINK:
//                try {
//                    bitmapRegionDecoder = BitmapRegionDecoder.newInstance(
//                            getContext().getAssets().open("kidbot_blink.png"),
//                            false);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case FaceBean.ANGRY:
//                try {
//                    bitmapRegionDecoder = BitmapRegionDecoder.newInstance(
//                            getContext().getAssets().open("kidbot_angry.png"),
//                            false);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case FaceBean.HAPPY:
//                try {
//                    bitmapRegionDecoder = BitmapRegionDecoder.newInstance(
//                            getContext().getAssets().open("kidbot_happy.png"),
//                            false);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case FaceBean.RESOLVE:
//                try {
//                    bitmapRegionDecoder = BitmapRegionDecoder.newInstance(
//                            getContext().getAssets().open("kidbot_blink.png"),
//                            false);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case FaceBean.RISUS:
//                try {
//                    bitmapRegionDecoder = BitmapRegionDecoder.newInstance(
//                            getContext().getAssets().open("kidbot_blink.png"),
//                            false);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case FaceBean.SEERIGHT:
//                break;
//            case FaceBean.SAD:
//                try {
//                    bitmapRegionDecoder = BitmapRegionDecoder.newInstance(
//                            getContext().getAssets().open("kidbot_sad.png"), false);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
    }

    public synchronized void setRunning(boolean isrunning) {
        this.isrunning = isrunning;
    }

    public synchronized void addIndex() {
        this.index++;
    }

}
