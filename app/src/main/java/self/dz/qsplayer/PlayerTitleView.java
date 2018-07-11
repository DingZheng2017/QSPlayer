package self.dz.qsplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import self.dz.qsplayer.collection.PlayerStatusCollection;
import self.dz.qsplayer.enums.TitleFunctions;


public class PlayerTitleView extends RelativeLayout implements PlayerStatusCollection.StatusListener {
    @BindView(R.id.back_icon)
    ImageView backIcon;

    @BindView(R.id.share_icon)
    ImageView shareIcon;

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
        PlayerStatusCollection.getInstance().subscribeListener(this);
    }

    public void setBackListener(OnClickListener listener){
        backIcon.setOnClickListener(listener);
    }

    @OnClick({R.id.back_icon,R.id.share_icon,R.id.catalog_icon,R.id.clip_icon})
    public void onViewClick(View view){
        switch (view.getId()) {
            case R.id.share_icon:
                PlayerStatusCollection.getInstance().statusUpdate(TitleFunctions.SHARE);
                break;
            case R.id.clip_icon:
                PlayerStatusCollection.getInstance().statusUpdate(TitleFunctions.CLICP);
                break;
            case R.id.catalog_icon:
                PlayerStatusCollection.getInstance().statusUpdate(TitleFunctions.CATALOG);
                break;
        }
    }

    @Override
    public void statusUpdate(TitleFunctions function) {

    }
}
