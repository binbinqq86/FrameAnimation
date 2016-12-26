package com.binbin.testas;

import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by -- on 2016/10/12.
 * 三种方式加载逐帧动画，原理一样
 */

public class MyAnimationDrawable {
    private static final String TAG = "MyAnimationDrawable";
    private static MyAnimationDrawable instance;
    private static final Handler handler=new Handler(Looper.getMainLooper());
    private MyAnimationDrawable(){}
    public static synchronized MyAnimationDrawable getInstance(){
        if(instance==null){
            instance=new MyAnimationDrawable();
        }
        return instance;
    }

    private int times;

    public void initAndPlayAnimation(final ImageView mImageView,final int[] mResIds,final int[] mDurations, int times,final boolean canRepeat) {
        this.times=times-1;
        realPlay(mImageView,mResIds,mDurations,0,canRepeat);
    }
    private void realPlay(final ImageView mImageView,final int[] mResIds,final int[] mDurations,final int currFrameNo,final boolean canRepeat) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mImageView.setBackgroundResource(mResIds[currFrameNo]);
                if (currFrameNo == mResIds.length-1) {
                    if(canRepeat){
                        realPlay(mImageView,mResIds,mDurations,0,canRepeat);
                    }else{
                        if(times>0){
                            times--;
                            realPlay(mImageView,mResIds,mDurations,0,canRepeat);
                        }else{
                            return;
                        }
                    }
                }else {
                    realPlay(mImageView,mResIds,mDurations,currFrameNo + 1,canRepeat);
                }
            }
        }, mDurations[currFrameNo]);
    }

    /**
     * 从xml文件中加载并播放逐帧动画
     * @param resourceId
     * @param duration
     * @param imageView
     * @param onAnimationListener
     */
    public void animateFromXMLResource(final int resourceId, final int[] duration,final ImageView imageView, final OnAnimationListener onAnimationListener) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                final ArrayList<MyFrame> myFrames = new ArrayList<MyFrame>();
                XmlResourceParser parser = imageView.getContext().getApplicationContext().getResources().getXml(resourceId);

                try {
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {

                        } else if (eventType == XmlPullParser.START_TAG) {

                            if (parser.getName().equals("item")) {
                                byte[] bytes = null;
                                int duration = 0;
                                for (int i = 0; i < parser.getAttributeCount(); i++) {
                                    if (parser.getAttributeName(i).equals("drawable")) {
                                        int resId = Integer.parseInt(parser.getAttributeValue(i).substring(1));
                                        bytes = toByteArray(imageView.getContext().getApplicationContext().getResources().openRawResource(resId));
                                    } else if (parser.getAttributeName(i) .equals("duration")) {
                                        duration = parser.getAttributeIntValue(i, 0);
                                    }
                                }

                                MyFrame myFrame = new MyFrame();
                                myFrame.bytes = bytes;
                                myFrame.duration = duration;
                                myFrames.add(myFrame);
                            }

                        } else if (eventType == XmlPullParser.END_TAG) {

                        } else if (eventType == XmlPullParser.TEXT) {

                        }

                        eventType = parser.next();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onAnimationListener != null) {
                            onAnimationListener.onDrawableLoaded(myFrames);
                            animateDrawableManually(myFrames,imageView,onAnimationListener,0);
                            onAnimationListener.onAnimationStart();
                        }
                    }
                });

            }
        }.start();
    }

    /**
     * 从drawable文件中加载并播发逐帧动画
     * @param resourceId
     * @param duration
     * @param imageView
     * @param onAnimationListener
     */
    public void animateFromDrawableResource(final int resourceId[], final int[] duration,final ImageView imageView, final OnAnimationListener onAnimationListener) {
//        new Thread(){//加载资源属于耗时操作，放在子线程
//            @Override
//            public void run() {
//                super.run();
//                final ArrayList<MyFrame> myFrames=new ArrayList<MyFrame>();
//
//                byte[] bytes = null;
//
//                for (int i = 0; i < resourceId.length; i++) {
//                    try {
//                        bytes = OtherUtils.toByteArray(imageView.getContext().getApplicationContext().getResources().openRawResource(resourceId[i]));
//                        MyFrame myFrame = new MyFrame();
//                        myFrame.bytes = bytes;
//                        myFrame.duration = duration[i];
//                        myFrames.add(myFrame);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (onAnimationListener != null) {
//                            onAnimationListener.onDrawableLoaded(myFrames);
//                            animateDrawableManually(myFrames,imageView,onAnimationListener,0);
//                            onAnimationListener.onAnimationStart();
//                        }
//                    }
//                });
//
//            }
//        }.start();

        //新方案================================================
//        if (onAnimationListener != null) {
//            onAnimationListener.onDrawableLoaded(null);
//            animateFrameDrawableResourceOneByOne(resourceId,duration,imageView,0,onAnimationListener);
//            onAnimationListener.onAnimationStart();
//        }

        //================每次只加载一个，新方案，原来是一次性加载到内存中
        animateDrawableManually(new BitmapFactory.Options(),resourceId,duration,new ArrayList<MyFrame>(),imageView,onAnimationListener,0);
    }

    private void animateFrameDrawableResourceOneByOne(final int resIds[], final int durations[], final ImageView imageView,final int frameNumber, final OnAnimationListener onAnimationListener){
//        int width = Constants.getInt(Constants.SCREENWIDTH, 0);
//        int height = Constants.getInt(Constants.SCREENHEIGHT, 0) - Constants.getInt(Constants.STATUSBARHEIGHT, 0);
        if(frameNumber!=0){
            Bitmap bit=imageView.getDrawingCache();
            if(bit!=null){
                bit.recycle();
                bit=null;
            }
        }
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
    private void animateDrawableManually2(final int resIds[], final int durations[], final List<MyFrame> myFrame,final ImageView imageView, final OnAnimationListener onAnimationListener,final int frameNumber){
        MyFrame thisFrame=null;
        if(frameNumber!=0){
            thisFrame=myFrame.get(1);
            //回收之前的bitmap
            MyFrame previousFrame = myFrame.get(0);
            Bitmap pBit=((BitmapDrawable)previousFrame.drawable).getBitmap();
            if(pBit!=null){
                pBit.recycle();
                pBit=null;
            }
            previousFrame.drawable=null;
            previousFrame.bytes=null;
            myFrame.remove(previousFrame);
        }else{
            thisFrame = new MyFrame();
            try {
                byte[] bytes = toByteArray(imageView.getContext().getApplicationContext().getResources().openRawResource(resIds[0]));
                thisFrame.bytes = bytes;
                thisFrame.duration = durations[0];
                thisFrame.drawable=new BitmapDrawable(imageView.getContext().getApplicationContext().getResources(),BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length));
                myFrame.add(thisFrame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageView.setImageDrawable(thisFrame.drawable);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (frameNumber < resIds.length-1) {
                    //准备并播放下一帧
                    try {
                        byte[] bytes = toByteArray(imageView.getContext().getApplicationContext().getApplicationContext().getResources().openRawResource(resIds[frameNumber+1]));
                        MyFrame nextFrame = new MyFrame();
                        nextFrame.bytes = bytes;
                        nextFrame.duration = durations[frameNumber+1];
                        nextFrame.drawable=new BitmapDrawable(imageView.getContext().getApplicationContext().getResources(),BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        myFrame.add(nextFrame);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    animateDrawableManually2(resIds,durations,myFrame, imageView, onAnimationListener, frameNumber + 1);
                } else {
                    if (onAnimationListener != null) {
                        onAnimationListener.onAnimationEnd();
                    }
                }
            }
        }, thisFrame.duration);
    }

    /**
     * 不进行回收，重用之前的bitmap
     * @param resIds
     * @param durations
     * @param myFrame
     * @param imageView
     * @param onAnimationListener
     * @param frameNumber
     */
    private void animateDrawableManually(final BitmapFactory.Options options,final int resIds[], final int durations[], final List<MyFrame> myFrame,final ImageView imageView, final OnAnimationListener onAnimationListener,final int frameNumber){
        MyFrame thisFrame=null;
        if(frameNumber==0){
            thisFrame = new MyFrame();
//            try {
//                byte[] bytes = toByteArray(imageView.getContext().getApplicationContext().getResources().openRawResource(resIds[0]));
//                thisFrame.bytes = bytes;
                thisFrame.duration = durations[0];
//                thisFrame.bitmap=BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                thisFrame.bitmap=BitmapFactory.decodeResource(imageView.getContext().getApplicationContext().getResources(),resIds[0],options);
                myFrame.add(thisFrame);
//                boolean can1=Utils.canUseForInBitmap(thisFrame.bitmap,options);
//                Log.e(TAG, "run: "+can+"#"+can1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }else{
            thisFrame=myFrame.get(1);
            myFrame.remove(0);
        }

        options.inMutable=true;
        options.inSampleSize=1;
        options.inBitmap=thisFrame.bitmap;
        imageView.setImageBitmap(thisFrame.bitmap);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (frameNumber < resIds.length-1) {
                    //准备并播放下一帧
//                    try {
//                        byte[] bytes = toByteArray(imageView.getContext().getApplicationContext().getApplicationContext().getResources().openRawResource(resIds[frameNumber+1]));
                        MyFrame nextFrame = new MyFrame();
//                        nextFrame.bytes = bytes;
                        nextFrame.duration = durations[frameNumber+1];
//                        nextFrame.bitmap=BitmapFactory.decodeByteArray(bytes, 0, bytes.length,options);
                        nextFrame.bitmap=BitmapFactory.decodeResource(imageView.getContext().getApplicationContext().getResources(),resIds[frameNumber+1],options);
//                        boolean can1=Utils.canUseForInBitmap(nextFrame.bitmap,options);
//                        Log.e(TAG, "run: "+can1+"$$$"+can1);
                        myFrame.add(nextFrame);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    animateDrawableManually(options,resIds,durations,myFrame, imageView, onAnimationListener, frameNumber + 1);
                } else {
                    if (onAnimationListener != null) {
                        onAnimationListener.onAnimationEnd();
                        options.inBitmap.recycle();
                    }
                }
            }
        }, thisFrame.duration);
    }

    /**
     * 负责动画的播放
     * @param myFrames
     * @param imageView
     * @param onAnimationListener
     * @param frameNumber
     */
    private void animateDrawableManually(final List<MyFrame> myFrames, final ImageView imageView, final OnAnimationListener onAnimationListener, final int frameNumber) {
        final MyFrame thisFrame = myFrames.get(frameNumber);

        if (frameNumber == 0) {
            thisFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(), BitmapFactory.decodeByteArray(thisFrame.bytes, 0, thisFrame.bytes.length));
        } else {
            //回收之前的bitmap
            MyFrame previousFrame = myFrames.get(frameNumber - 1);
            Bitmap pBit=((BitmapDrawable) previousFrame.drawable).getBitmap();
            if(pBit!=null){
                pBit.recycle();
                pBit=null;
            }
            previousFrame.drawable = null;
            previousFrame.bytes=null;
            previousFrame.isReady = false;
        }

        imageView.setImageDrawable(thisFrame.drawable);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Make sure ImageView hasn't been changed to a different Image
                // in this time
                if (imageView.getDrawable() == thisFrame.drawable) {
                    if (frameNumber < myFrames.size()-1) {
                        MyFrame nextFrame = myFrames.get(frameNumber + 1);
                        if (nextFrame.isReady) {
                            //播放下一帧
                            animateDrawableManually(myFrames, imageView, onAnimationListener, frameNumber + 1);
                        } else {
                            nextFrame.isReady = true;
                        }
                    } else {
                        if (onAnimationListener != null) {
                            onAnimationListener.onAnimationEnd();
                        }
                    }
                }
            }
        }, thisFrame.duration);

        // 加载（准备）下一帧数据
        if (frameNumber < myFrames.size()-1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyFrame nextFrame = myFrames.get(frameNumber + 1);
                    nextFrame.drawable = new BitmapDrawable(imageView
                            .getContext().getApplicationContext().getResources(),
                            BitmapFactory.decodeByteArray(nextFrame.bytes, 0,
                                    nextFrame.bytes.length));
                    if (nextFrame.isReady) {
                        //播放下一帧
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                animateDrawableManually(myFrames, imageView, onAnimationListener,frameNumber + 1);
                            }
                        });
                    } else {
                        nextFrame.isReady = true;
                    }

                }
            }).start();
        }
    }

    public static class MyFrame {
        byte[] bytes;
        int duration;
        Drawable drawable;
        boolean isReady = false;
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
