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
 * 我的帧动画方式二
 */
public class MyFrameActivity2 extends AppCompatActivity {

    private ImageView iv;
    private int count=33;
    private int[] res=new int[count];
    private int[] duration=new int[count];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myframe2);
        iv= (ImageView) findViewById(R.id.iv);
        for (int i = 0; i < count; i++) {
            int id = getResources().getIdentifier("yacht" + (i+1), "mipmap", getPackageName());
            res[i]=id;
            duration[i]=150;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            MyAnimationDrawable2.getInstance().animateFromDrawableResource(res, duration, iv, new OnAnimationListener() {
                @Override
                public void onDrawableLoaded(Object obj) {
        
                }
    
                @Override
                public void onAnimationStart() {
        
                }
    
                @Override
                public void onAnimationEnd() {
                    MyAnimationDrawable2.getInstance().animateFromDrawableResource(res, duration, iv,this);
                }
            });
            }
        },2000);
    }

}
