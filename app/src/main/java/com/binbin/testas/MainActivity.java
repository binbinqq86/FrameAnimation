package com.binbin.testas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView lv;
    private ArrayList<Catalog> list = new ArrayList<>();
    public static int sWidth,sHeight;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list.add(new Catalog("系统原生帧动画", FrameActivity.class));
        list.add(new Catalog("自定义帧动画1", MyFrameActivity1.class));
        list.add(new Catalog("自定义帧动画2", MyFrameActivity2.class));
        list.add(new Catalog("gif实现帧动画", GifActivity.class));
        list.add(new Catalog("surfaceView实现帧动画", SurfaceViewActivity.class));
        list.add(new Catalog("openGL实现帧动画", OpenGLActivity.class));
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<Catalog>(this, android.R.layout.simple_list_item_1, list));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, list.get(position).cls));
            }
        });
        getMaxMemory();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        sWidth=getResources().getDisplayMetrics().widthPixels;
        sHeight=getResources().getDisplayMetrics().heightPixels;
        Log.e(TAG, "onWindowFocusChanged: "+sWidth +"#"+sHeight);
    }
    
    private void getMaxMemory(){
        Log.e(TAG, "getMaxMemory: "+Runtime.getRuntime().maxMemory()/1024/1024+"M");
    }
}
