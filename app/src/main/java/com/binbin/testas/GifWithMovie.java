package com.binbin.testas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

/**
 * Created by -- on 2016/10/21.
 * //需关闭硬件加速android:hardwareAccelerated="false"
 */

public class GifWithMovie extends View {

    private Movie mMovie;
    private long mMovieStart = 0;

    public GifWithMovie(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

        mMovie=null;
        mMovieStart=0;

        //从描述文件中读出gif的值，创建出Movie实例
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.GIFView, defStyle, 0);

        int srcID=a.getResourceId(R.styleable.GIFView_gif, 0);
        if(srcID>0){
            InputStream is = context.getResources().openRawResource(srcID);
            mMovie = Movie.decodeStream(is);
        }

        a.recycle();
    }

    @Override
    public void onDraw(Canvas canvas) {

        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        if (mMovie != null) {
            int dur = mMovie.duration();

            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int) ((now - mMovieStart) % dur);
            mMovie.setTime(relTime);
            mMovie.draw(canvas, 0, 0);

            invalidate();
        }
    }
}
