package com.binbin.testas;

import android.graphics.drawable.AnimationDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import com.binbin.testas.opengl.MyFrameAnimationView2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifImageView;
import pl.droidsonroids.gif.GifTextureView;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    private AnimationDrawable animationDrawable;
    private SceneAnimation sceneAnimation;
    private int count=33;
    private int[] res=new int[count];
    private int[] duration=new int[count];
    public static int sWidth,sHeight;

    private void playGif(int resId){
        try {
//            GifDrawable gifFromResource = new GifDrawable( getResources(), R.mipmap.yacht_gif);
            GifImageView gifImageView=new GifImageView(this);
            gifImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            gifImageView.setImageResource(resId);
            final MediaController mc = new MediaController(this);
            mc.setVisibility(View.GONE);
            final pl.droidsonroids.gif.GifDrawable gifDrawable=(pl.droidsonroids.gif.GifDrawable)gifImageView.getDrawable();
            gifDrawable.setLoopCount(0);
            gifDrawable.addAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationCompleted(int loopNumber) {
                    Log.e("tianbin",loopNumber+"======="+gifDrawable.getDuration());
                }
            });
            mc.setMediaPlayer(gifDrawable);
            mc.setAnchorView(gifImageView);
            ((RelativeLayout)findViewById(R.id.activity_main)).addView(gifImageView,new RelativeLayout.LayoutParams(-1,-1));
            mc.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
playGif(R.mipmap.yacht_gif);
//            MyFrameAnimationView2 myAnimationView=new MyFrameAnimationView2(MainActivity.this, res, duration, new OnAnimationListener() {
//                @Override
//                public void onDrawableLoaded(Object obj) {
//
//                }
//
//                @Override
//                public void onAnimationStart() {
//
//                }
//
//                @Override
//                public void onAnimationEnd() {
//                    ((RelativeLayout)findViewById(R.id.activity_main)).removeAllViews();
////                    handler.sendEmptyMessageDelayed(0,1000);
//                }
//            });
//            ((RelativeLayout)findViewById(R.id.activity_main)).addView(myAnimationView);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv= (ImageView) findViewById(R.id.iv);
//        gifTextureView= (GifImageView) findViewById(R.id.gif1);
//        Glide.with(MainActivity.this).load(R.mipmap.yacht_gif).into(iv);

        animationDrawable = new AnimationDrawable();
        for (int i = 0; i < count; i++) {
            int id = getResources().getIdentifier("yacht" + (i+1), "mipmap", getPackageName());
            res[i]=id;
            duration[i]=150;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                sWidth=getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT).getWidth();
                sHeight=getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
//                handler.sendEmptyMessage(0);
//                MyFrameAnimation.getInstance().initAndPlayAnimation(iv,res,duration,2,true);
//                MyFrameAnimation.getInstance().animateFromDrawableResource(res, duration, iv, new OnAnimationListener() {
//                    @Override
//                    public void onDrawableLoaded(Object obj) {
//                        Log.e("tianbin","==========onDrawableLoaded============");
//                    }
//
//                    @Override
//                    public void onAnimationStart() {
//                        Log.e("tianbin","==========onAnimationStart============");
//                    }
//
//                    @Override
//                    public void onAnimationEnd() {
//                        Log.e("tianbin","==========onAnimationEnd============");
//                    }
//                });
//                animationDrawable.start();
//                sceneAnimation=new SceneAnimation(iv,res,duration,1,false);
//                MyAnimationDrawable.animateRawManuallyFromXML(R.anim.values,iv,null,null);

//                MyAnimationDrawable.getInstance().animateFromDrawableResource(res, duration, iv, new OnAnimationListener() {
//                    @Override
//                    public void onDrawableLoaded(Object obj) {
//
//                    }
//
//                    @Override
//                    public void onAnimationStart() {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd() {
//
//                    }
//                });


                //采用原生frame帧动画
                iv.setBackgroundResource(R.drawable.values);
                AnimationDrawable anim = (AnimationDrawable) iv.getBackground();
                anim.start();
            }
        },1000);
    }

}
