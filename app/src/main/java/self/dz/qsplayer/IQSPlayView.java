package self.dz.qsplayer;


import android.view.View;

public interface IQSPlayView {

    void setErrorView(View errorView);//设置错误提示页面

    void showErrorMsg(String msg);

    void hideErrorMsg();

    void setUseController(boolean useController); //-- Whether the playback controls can be shown.

    void setControllerHideOnTouch(boolean controllerHideOnTouch);// -- Whether the playback controls are hidden by touch events.

    void setControllerAutoShow(boolean controllerAutoShow);

    void showController();

    void hideController();

    void setControllerHideDuringAds(boolean controllerHideDuringAds);

    void setResizeMode(int resizeMode);

    void setShutterBackgroundColor(int color);
}
