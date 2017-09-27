package com.binbin.testas;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifImageView;

/**
 * 我的帧动画方式一
 */
public class MyFrameActivity1 extends AppCompatActivity {

    private ImageView iv;
    private int count=33;
    private int[] res=new int[count];
    private int[] duration=new int[count];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myframe1);
        iv= (ImageView) findViewById(R.id.iv);

        for (int i = 0; i < count; i++) {
            int id = getResources().getIdentifier("yacht" + (i+1), "mipmap", getPackageName());
            res[i]=id;
            duration[i]=150;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MyAnimationDrawable1.getInstance().animateFrameDrawableResourceOneByOne(res, duration, iv, 0, new OnAnimationListener() {
                    @Override
                    public void onDrawableLoaded(Object obj) {
        
                    }
    
                    @Override
                    public void onAnimationStart() {
        
                    }
    
                    @Override
                    public void onAnimationEnd() {
                        MyAnimationDrawable1.getInstance().animateFrameDrawableResourceOneByOne(res, duration, iv, 0,this);
                    }
                });
            }
        },2000);
    }

}
