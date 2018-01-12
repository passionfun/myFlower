package bocai.com.yanghuaji.util;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import bocai.com.yanghuaji.R;


/**
 * 语音播放动画动画util
 * Created by shc on 2018/1/12.
 */

public class VoicePlayingBgUtil {
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask;
    //记录语音动画图片
    private int index = 1;
    AudioAnimationHandler audioAnimationHandler = null;

    /**
     * 播放语音图标动画
     */
    public void playAudioAnimation(final ImageView imageView, final MediaPlayer mediaPlayer) {
        //定时器检查播放状态
        stopTimer();
        mTimer=new Timer();
        //将要关闭的语音图片归位
        if(audioAnimationHandler!=null)
        {
            Message msg=new Message();
            msg.what=3;
            audioAnimationHandler.sendMessage(msg);
        }

        audioAnimationHandler=new AudioAnimationHandler(imageView);
        mTimerTask = new TimerTask() {
            public boolean hasPlayed=false;
            @Override
            public void run() {
                if(mediaPlayer.isPlaying()) {
                    hasPlayed=true;
                    index=(index+1)%3;
                    Message msg=new Message();
                    msg.what=index;
                    audioAnimationHandler.sendMessage(msg);
                }else
                {
                    //当播放完时
                    Message msg=new Message();
                    msg.what=3;
                    audioAnimationHandler.sendMessage(msg);
                    //播放完毕时需要关闭Timer等
                    if(hasPlayed)
                    {
                        stopTimer();
                    }
                }
            }
        };
        //调用频率为500毫秒一次
        mTimer.schedule(mTimerTask, 0, 500);
    }


    static class AudioAnimationHandler extends Handler {
        ImageView imageView;
        AudioAnimationHandler(ImageView imageView)
        {
            this.imageView=imageView;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //根据msg.what来替换图片，达到动画效果
            switch (msg.what) {
                case 0 :
                    imageView.setImageResource(R.mipmap.img_play_video1);
                    break;
                case 1 :
                    imageView.setImageResource(R.mipmap.img_play_video2);
                    break;
                case 2 :
                    imageView.setImageResource(R.mipmap.img_play_video3);
                    break;
                default :
                    imageView.setImageResource(R.mipmap.img_play_video3);
                    break;
            }
        }

    }

    /**
     * 停止
     */
    public void stopTimer(){
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

    }

}
