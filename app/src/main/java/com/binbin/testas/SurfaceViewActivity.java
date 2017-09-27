package com.binbin.testas;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

public class SurfaceViewActivity extends AppCompatActivity {
    
    private int count=33;
    private int[] res=new int[count];
    private int[] duration=new int[count];
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyFrameSurfaceView myAnimationView=new MyFrameSurfaceView(SurfaceViewActivity.this, res, duration, new OnAnimationListener() {
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
        setContentView(R.layout.activity_main);
        
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
