package com.binbin.testas;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.binbin.testas.opengl.MyFrameAnimationOpenGL;

public class OpenGLActivity extends AppCompatActivity {
    private static final String TAG = "OpenGLActivity";
    private int count=33;
    private int[] res=new int[count];
    private int[] duration=new int[count];
    
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyFrameAnimationOpenGL myAnimationView=new MyFrameAnimationOpenGL(OpenGLActivity.this, res, duration, new OnAnimationListener() {
                @Override
                public void onDrawableLoaded(Object obj) {

                }

                @Override
                public void onAnimationStart() {

                }

                @Override
                public void onAnimationEnd() {
                    ((RelativeLayout)findViewById(R.id.activity_main)).removeAllViews();
                    handler.sendEmptyMessageDelayed(0,0);
                }
            });
            ((RelativeLayout)findViewById(R.id.activity_main)).addView(myAnimationView);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opengllayout);
        
//        for (int i = 0; i < count; i++) {
//            String ids=(i+48)<100?("0"+(i+48)):(i+48+"");
//            int id = getResources().getIdentifier("fudong1_00" + ids, "drawable", getPackageName());
//            res[i]=id;
//            duration[i]=50;
//        }
    
        for (int i = 0; i < count; i++) {
            int id = getResources().getIdentifier("yacht" + (i+1), "mipmap", getPackageName());
            res[i]=id;
            duration[i]=50;
        }
        
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },2000);
    }
    
}
