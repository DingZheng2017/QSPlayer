package self.dz.qsplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PlayerControlView extends FrameLayout {
    @BindView(R.id.screen_toggle)
    ImageView screenToggle;

    int controllerLayoutId = R.layout.player_control_view;

    public PlayerControlView(@NonNull Context context) {
        this(context, null);
    }

    public PlayerControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(controllerLayoutId, this);
        ButterKnife.bind(this);
    }


    public void setScreenToggleListener(OnClickListener listener){
        screenToggle.setOnClickListener(listener);
    }

}
