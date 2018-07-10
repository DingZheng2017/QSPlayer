package self.dz.qsplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CustomPlayerControlView extends FrameLayout implements self.dz.qsplayer.MediaController{
    @BindView(R.id.screen_toggle)
    ImageView screenToggle;

    int controllerLayoutId = R.layout.player_control_view;

    public CustomPlayerControlView(@NonNull Context context) {
        this(context, null);
    }

    public CustomPlayerControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPlayerControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(controllerLayoutId, this);
        ButterKnife.bind(this);
    }


//    public void setScreenToggleListener(OnClickListener listener){
//        screenToggle.setOnClickListener(listener);
//    }

    @Override
    public void screenToggle(OnClickListener listener) {
        screenToggle.setOnClickListener(listener);
    }
}
