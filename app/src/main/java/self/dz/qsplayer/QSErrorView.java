package self.dz.qsplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

public class QSErrorView extends FrameLayout {


    int playerLayoutId = R.layout.qs_player_error_view;
    public QSErrorView(Context context) {
        this(context, null);
    }

    public QSErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QSErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(playerLayoutId,this);
        ButterKnife.bind(this);
    }
}
