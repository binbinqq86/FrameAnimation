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
 * 原生帧动画
 */
public class FrameActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_activity);
        iv= (ImageView) findViewById(R.id.iv);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //采用原生frame帧动画
                iv.setBackgroundResource(R.drawable.values);
                AnimationDrawable anim = (AnimationDrawable) iv.getBackground();
                anim.start();
            }
        },2000);
    }

}
