package self.dz.qsplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomPlayerView extends LinearLayout implements
        MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener,MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener {

    @BindView(R.id.vitamio_parent)
    RelativeLayout mVideoViewParent;

    @BindView(R.id.error_container)
    LinearLayout mErrorContainer;
    @BindView(R.id.icon)
    ImageView mErrorIcon;
    @BindView(R.id.error_content)
    TextView mErrorText;
    @BindView(R.id.btn)
    TextView mErrorBtn;
    @BindView(R.id.video_loading_container)
    LinearLayout mLoadingContainer;
    @BindView(R.id.videoView)
    PlayerView playerView;
    @BindView(R.id.change_progress)
    TextView mChangeProgressMsg;
    @BindView(R.id.coverView)
    public ImageView mCoverView;

    DisplayMetrics dm;
    protected String mVideoUrl;
    protected String mName;
    int layoutId = R.layout.custom_play_view;
    private float brightness;
    private int currentVolume;
    private int maxVolume;
    private int hasCharge;//-1:本次竖向滑动； 0:还没有判断滑动方向 ； 1:本次横向滑动
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private int fillX;
    private int fillY;
    private int maxX = 0;
    private int maxY = 0;
    private AudioManager audioManager;
    private boolean isLeftPressed;
    private float changeNO; //横向滑动百分比
    protected static final int HIDE_MSG = 1;
    protected FragmentActivity activity;

    public CustomPlayerView(FragmentActivity context) {
        super(context);
        this.activity = context;
        LayoutInflater.from(context).inflate(layoutId, this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        //TODO coverView
        // playerView.setCoverView(mCoverViewr);
        mLoadingContainer.setVisibility(View.VISIBLE);


        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        fillX = dm.widthPixels;
        fillY = dm.heightPixels;
        addListener();
    }

    private void addListener() {
        playerView.setOnCompletionListener(this);
        playerView.setOnErrorListener(this);
        playerView.setOnInfoListener(this);
        playerView.setOnPreparedListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                mCoverView.setVisibility(GONE);
                mLoadingContainer.setVisibility(GONE);
                break;
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    public void setScreenToggleListener(OnClickListener screenToggleListener) {
        playerView.setScreenToggleListener(screenToggleListener);
    }

    public void setBackListener(OnClickListener backListener) {
        playerView.setBackListener(backListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getPressedInfo(event);
                hasCharge = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                setActionPoint(event);

                if (hasCharge == 0)// 如果还没有判断滑动方向
                {
                    chargeSlideOrientation();
                } else {
                    slideAction();
                }
                break;
            case MotionEvent.ACTION_UP:
                setActionPoint(event);
                doActionUp();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 判断本次动作是点击操作还是滑动操作
     */
    protected void doActionUp() {
        // 滑动区域小：视为点击；以锁住：视为无效操作；否则更新播放进度、消失动画
        if (maxX < 50 && maxY < 50) {
            clicked();
        } else {
            // 快进
            if (hasCharge == 1) {
                // 更新播放进度
                fastForward((long) changeNO);
            }
        }
        disMiss();
        maxX = 0;
        maxY = 0;
    }

    /**
     * 点击事件
     * <p/>
     * true-继续传递点击事件；false-不再传递点击事件
     */
    protected void clicked() {

    }

    /**
     * 更新当前播放进度
     */
    protected void fastForward(long timePoint) {

    }

    /**
     * 隐藏亮度、音量改变信息
     */
    protected void disMiss() {
        handler.removeMessages(HIDE_MSG);
      //  mHandler.removeMessages(HIDE_MSG);
        // 延迟两秒隐藏提示信息
        handler.sendEmptyMessage(HIDE_MSG);
   //     mHandler.sendEmptyMessage(HIDE_MSG);
    }

    /**
     * 设置 ACTION_MOVE 以及 ACTION_UP 点
     */
    private void setActionPoint(MotionEvent event) {
        endY = (int) event.getY();
        endX = (int) event.getX();

        maxX = Math.max(maxX, Math.abs(endX - startX));
        maxY = Math.max(maxY, Math.abs(endY - startY));
    }

    /**
     * 判断滑动方向 :hasCharge
     */
    private void chargeSlideOrientation() {
        int dis = Math.abs(endX - startX) - Math.abs(endY - startY);
        if (dis > 0) {
            hasCharge = 1;
        } else if (dis < 0) {
            hasCharge = -1;
        }
    }

    /**
     * 做滑动响应
     */
    private void slideAction() {
        if (hasCharge == -1) // 竖向滑动响应
        {
            slideVertical();
        } else if (hasCharge == 1)// 横向滑动响应
        {
            slideHorizontal();
        }
    }


    /**
     * 纵向滑动（改变亮度或者音量）
     */
    private void slideVertical() {
        float changeNO;
        if (maxY >= 50) {
            if (isLeftPressed) {
                changeNO = (float) (startY - endY) / (float) fillY;
                if (brightness == -1f) {
                    brightness = 1f;
                }
                changeAppBrightness(brightness + changeNO);
            } else {
                changeNO = (startY - endY) * maxVolume / fillY;
                changeVoice((int) (currentVolume + changeNO));
            }
        }
    }

    /**
     * 获取按下时坐标，声音大小，屏幕亮度
     */
    private void getPressedInfo(MotionEvent event) {
        startX = (int) event.getX();
        startY = (int) event.getY();

        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        brightness = activity.getWindow().getAttributes().screenBrightness;

        isLeftPressed = startX <= fillX / 2;
    }

    /**
     * 改变播放进度
     */
    protected void slideHorizontal() {
        if (maxX >= 50) {
            changeNO = getCurPosition()
                    + ((endX - startX) * 50000 / (float) fillX);

            showProgressChanging(changeNO);
        }
    }

    /**
     * 获取当前的播放进度
     */
    public long getCurPosition() {
        return playerView.getCurrentPosition();
    }

    /**
     * 改变亮度
     */
    private void changeAppBrightness(float brightness) {
        if (brightness >= 1) {
            brightness = 1f;
        } else if (brightness <= 0.2f) {
            brightness = 0.2f;
        }
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = brightness;
        activity.getWindow().setAttributes(lp);

        // 显示亮度变化
        showBrightnessChanging(brightness);
    }

    /**
     * 改变音量
     */
    private void changeVoice(int voice) {
        if (voice > maxVolume) {
            voice = maxVolume;
        } else if (voice < 0) {
            voice = 0;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, voice, 0);
        showVolumesChanging(voice * 100 / maxVolume);
    }

    /**
     * 显示亮度变化
     */
    protected void showBrightnessChanging(float brightness) {
        showChangeMsg((int) (brightness * 100) + "%", R.drawable.brightness);
    }

    /**
     * 显示音量变化
     */
    protected void showVolumesChanging(int voice) {
        showChangeMsg(voice + "%", R.drawable.icon_sound);
    }
    /**
     * 显示快进、快退
     */
    protected void showProgressChanging(float changeNO) {
        long target = resetTagTime((long) changeNO);
        showChangeMsg(formatTime(target, playerView.getDuration()), 0);
    }

    private String formatTime(long target, long duration) {
        return formatTime(target) + "/" + formatTime(duration);
    }

    private String formatTime(long time) {
        time = time / 1000;
        StringBuffer buffer = new StringBuffer();
        // 计算分钟
        long min = time / 60;
        append(buffer, min);

        buffer.append(":");
        // 计算秒数
        long sec = time - min * 60;
        append(buffer, sec);

        return buffer.toString();
    }

    private void append(StringBuffer buffer, long time) {
        if (time < 10) {
            buffer.append(0);
        }
        buffer.append(time);
    }

    private void showChangeMsg(final String msg, final int resId) {
       // mHandler.removeMessages(HIDE_MSG);
        handler.removeMessages(HIDE_MSG);
        mChangeProgressMsg.setText(msg);
        mChangeProgressMsg.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0,
                0);
        mChangeProgressMsg.setVisibility(View.VISIBLE);
    }

    private ProgressHandler handler = new ProgressHandler(this);

    private static class ProgressHandler extends Handler{
        private final WeakReference<CustomPlayerView> mView;

        public ProgressHandler(CustomPlayerView view) {
            mView = new WeakReference<>(view);
        }

        @Override
        public void dispatchMessage(Message msg) {
            CustomPlayerView view = mView.get();
            if (view != null) {
                switch (msg.what) {
                    case HIDE_MSG:
                        view.hideProgress();
                        break;
                    default:
                        break;
                }
            }
        }
    }
    private void hideProgress() {
        mChangeProgressMsg.setVisibility(View.GONE);
    }

    protected long resetTagTime(long target) {
        long duration = playerView.getDuration();
        if (target > duration) {
            target = duration;
        } else if (target < 0) {
            target = 0;
        }
        return target;
    }

    public void prepareAndStartPlay(String videoPath){
        playerView.prepareAndStartPlay(videoPath);
    }

    public void replacePlayerControlView(MediaController customController) {
        playerView.replacePlayerControlView(customController);
    }
}
