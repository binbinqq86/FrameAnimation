package com.binbin.testas;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifImageView;

/**
 * 三种方案，glide,Movie,gifDrawable
 */
public class GifActivity extends AppCompatActivity {
    private static final String TAG = "GifActivity";
    private ImageView iv;
    private GifWithMovie movie;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giflayout);
//        gifTextureView= (GifImageView) findViewById(R.id.gif1);
        iv= (ImageView) findViewById(R.id.iv);
        movie= (GifWithMovie) findViewById(R.id.movie);//需关闭硬件加速
        iv.setVisibility(View.VISIBLE);
//        Glide.with(GifActivity.this).load(R.mipmap.yacht_gif).into(iv);
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iv.setVisibility(View.GONE);
//                playGif(R.mipmap.yacht_gif);
            }
        },2000);
    }
    
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
                    Log.e(TAG,loopNumber+"======="+gifDrawable.getDuration());
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
}
