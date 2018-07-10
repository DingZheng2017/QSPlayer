package self.dz.qsplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayerTitleView extends RelativeLayout {
    @BindView(R.id.back_icon)
    ImageView backIcon;

    int layoutId = R.layout.player_title_view;

    public PlayerTitleView(@NonNull Context context) {
        this(context, null);
    }

    public PlayerTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerTitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(layoutId, this);
        ButterKnife.bind(this);
    }

    public void setBackListener(OnClickListener listener){
        backIcon.setOnClickListener(listener);
    }

}
