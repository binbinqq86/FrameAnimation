package com.binbin.testas;

import android.os.Handler;
import android.widget.ImageView;

public class SceneAnimation {
    private ImageView mImageView;
    private int[] mFrameRess;// 图片
    private int[] mDurations;
    private Handler handler = new Handler();
    Runnable mRunnable;
    private int mLastFrameNo;
    private int times;//循环次数
    private boolean canRepeat=false;//是否可以无限循环
    public interface AnimationListener{
        void onAnimationStart();
        void onAnimationEnd();
    }

    public SceneAnimation(ImageView pImageView, int[] pFrameRess, int[] pDurations,int times,boolean canRepeat) {
        mImageView = pImageView;
        mFrameRess = pFrameRess;
        mDurations = pDurations;
        mLastFrameNo = pFrameRess.length - 1;
        mImageView.setBackgroundResource(mFrameRess[0]);
        this.times=times-1;
        this.canRepeat=canRepeat;
        play(1);
    }

    private void play(final int pFrameNo) {
        mRunnable = new Runnable() {
            public void run() {
                mImageView.setBackgroundResource(mFrameRess[pFrameNo]);
                if (pFrameNo == mLastFrameNo) {
                    if(canRepeat){
                        play(0);
                    }else{
                        if(times>0){
                            times--;
                            play(0);
                        }else{
                            return;
                        }
                    }
                }else {
                    play(pFrameNo + 1);
                }
            }
        };
        handler.postDelayed(mRunnable, mDurations[pFrameNo]);
    }
};
