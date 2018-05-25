package self.dz.qsplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.VideoView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QSPlayView extends FrameLayout implements IQSPlayView {
    @BindView(R.id.qs_content_frame)
    FrameLayout contentFrame;

    @BindView(R.id.qs_error_placeholder)
    View errorPlaceHolder;
    //播放器类型
    private static final int TYPE_NONE = 0;//AUDIO
    private static final int TYPE_VIDEO_VIEW = 1;
    private static final int TYPE_SURFACE_VIEW = 2;
    private static final int TYPE_TEXTURE_VIEW = 3;

    private View titleView;//标题view
    private VideoView contentView;//用于播放的view
    private View controlView;//控制器
    private View errView;//错误提示页面

    private boolean useController;
    private boolean controllerAutoShow;
    private boolean controllerHideDuringAds;
    private boolean controllerHideOnTouch;

    int contentViewType = TYPE_VIDEO_VIEW;//默认video_view
    int playerLayoutId = R.layout.qs_player_view;

    public QSPlayView(Context context) {
        this(context, null);
    }

    public QSPlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QSPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(playerLayoutId, this);
        ButterKnife.bind(this);
        //动态替换播放控件，默认使用videoView
        if (contentViewType == TYPE_VIDEO_VIEW) {
             contentView = new VideoView(getContext());
            ViewGroup.LayoutParams params =
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            contentView.setLayoutParams(params);
            contentFrame.addView(contentView,0);
        }


        //播放
        File file = new File(Environment.getExternalStorageDirectory(), "/Android/screen.mp4");
        contentView.setVideoPath(file.getPath());
        contentView.start();
    }

    @Override
    public void setUseController(boolean useController) {

    }

    @Override
    public void setErrorView(View errorView){
        this.errView = errorView;
        //动态替换错误提示view
        //TODO 优化errView选取方式，默认布局给一个？
        if(errView != null) {
            ViewGroup parent = ((ViewGroup) errorPlaceHolder.getParent());
            int controllerIndex = parent.indexOfChild(errorPlaceHolder);
            parent.removeView(errorPlaceHolder);
            parent.addView(errView, controllerIndex);
        }
        errView.setVisibility(GONE);
    }

    @Override
    public void hideErrorMsg() {
        errView.setVisibility(GONE);
    }

    @Override
    public void showErrorMsg(String msg) {
        errView.setVisibility(VISIBLE);
    }

    @Override
    public void setControllerHideOnTouch(boolean controllerHideOnTouch) {

    }

    @Override
    public void setControllerAutoShow(boolean controllerAutoShow) {

    }

    @Override
    public void showController() {

    }

    @Override
    public void hideController() {

    }

    @Override
    public void setControllerHideDuringAds(boolean controllerHideDuringAds) {

    }

    @Override
    public void setResizeMode(int resizeMode) {

    }

    @Override
    public void setShutterBackgroundColor(int color) {

    }

}
