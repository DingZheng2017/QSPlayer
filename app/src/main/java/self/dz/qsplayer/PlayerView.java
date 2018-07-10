package self.dz.qsplayer;

import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerView extends FrameLayout {
    @BindView(R.id.content_frame)
    FrameLayout contentFrame;

    @BindView(R.id.error_placeholder)
    View errorPlaceHolder;
    //播放器类型
    private static final int TYPE_NONE = 0;//AUDIO
    private static final int TYPE_VIDEO_VIEW = 1;
    private static final int TYPE_SURFACE_VIEW = 2;
    private static final int TYPE_TEXTURE_VIEW = 3;

    private ExpandVideoView playerView;//用于播放的view
    private FrameLayout playerController;//控制器
    private PlayerControlView controller;
    private PlayerTitleView titleView;//控制器
    private View errView;//错误提示页面
    private Context context;

    int surfaceType = TYPE_VIDEO_VIEW;//默认video_view
    int playerLayoutId = R.layout.player_view;

    public PlayerView(Context context) {
        this(context, null);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(playerLayoutId, this);
        ButterKnife.bind(this);
        this.context = context;
        //动态替换播放控件，默认使用videoView
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PlayerView, 0, 0);
            try {
             //   surfaceType = a.getInt(R.styleable.PlayerView_surface_type, surfaceType);
                android.util.Log.d("ysh","surfaceType "+surfaceType);
            } finally {
                a.recycle();
            }
        }

        replacePlayerView();
        replacePlayerControlView(null);
        replaceTitleView();
    }

    private void replaceTitleView() {
        View titlePlaceholder = findViewById(R.id.title_placeholder);
        if (titlePlaceholder != null) {
            this.titleView = new PlayerTitleView(context, null, 0);
            titleView.setLayoutParams(titlePlaceholder.getLayoutParams());
            ViewGroup parent = ((ViewGroup) titlePlaceholder.getParent());
            int titleIndex = parent.indexOfChild(titlePlaceholder);
            parent.removeView(titlePlaceholder);
            parent.addView(titleView, titleIndex);
        } else {
            this.titleView = null;
        }
    }

    private void replacePlayerControlView() {
        View controllerPlaceholder = findViewById(R.id.controller_placeholder);
        if (controllerPlaceholder != null) {

            this.playerController = new PlayerControlView(context, null, 0);
            playerController.setLayoutParams(controllerPlaceholder.getLayoutParams());
            ViewGroup parent = ((ViewGroup) controllerPlaceholder.getParent());
            int controllerIndex = parent.indexOfChild(controllerPlaceholder);
            parent.removeView(controllerPlaceholder);
            parent.addView(playerController, controllerIndex);
        } else {
            this.playerController = null;
        }
    }

    public void replacePlayerControlView(MediaController customController) {
        View controllerPlaceholder = findViewById(R.id.controller_placeholder);
        if (customController == null ) {
            playerController = new PlayerControlView(context, null, 0);
        } else if (controllerPlaceholder != null) {
            playerController = (FrameLayout) customController;
            playerController.setLayoutParams(controllerPlaceholder.getLayoutParams());
            ViewGroup parent = ((ViewGroup) controllerPlaceholder.getParent());
            int controllerIndex = parent.indexOfChild(controllerPlaceholder);
            parent.removeView(controllerPlaceholder);
            parent.addView(playerController, controllerIndex);
        } else {
            this.playerController = null;
        }
    }

    private void replacePlayerView() {
        if (surfaceType == TYPE_VIDEO_VIEW) {
            playerView = new ExpandVideoView(getContext());
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            playerView.setLayoutParams(params);
            contentFrame.addView(playerView, 0);
        }
    }

    public void prepareAndStartPlay(String path){
        playerView.setVideoPath(path);
        playerView.start();
    }

    public void setUseController(boolean useController) {

    }

    public void replaceErrorView(View errorView) {
        this.errView = errorView;
        //动态替换错误提示view
        //TODO 优化errView选取方式，默认布局给一个？
        if (errView != null) {
            ViewGroup parent = ((ViewGroup) errorPlaceHolder.getParent());
            int controllerIndex = parent.indexOfChild(errorPlaceHolder);
            parent.removeView(errorPlaceHolder);
            parent.addView(errView, controllerIndex);
        }
        errView.setVisibility(GONE);
    }


    public void hideErrorMsg() {
        errView.setVisibility(GONE);
    }


    public void showErrorMsg(String msg) {
        errView.setVisibility(VISIBLE);
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        playerView.setOnCompletionListener(listener);
    }

    public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        playerView.setOnErrorListener(listener);
    }

    public void setOnInfoListener(MediaPlayer.OnInfoListener listener) {
        playerView.setOnInfoListener(listener);
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        playerView.setOnPreparedListener(listener);
    }

    public long getDuration() {
        return playerView.getDuration();
    }


    public long getCurrentPosition() {
        return playerView.getCurrentPosition();
    }

    public void setScreenToggleListener(OnClickListener listener) {
     //   playerController.setScreenToggleListener(listener);
        ((MediaController)playerController).screenToggle(listener);
    }

    public void setBackListener(OnClickListener listener) {
        titleView.setBackListener(listener);
    }
}
