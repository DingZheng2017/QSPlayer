package self.dz.qsplayer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import self.dz.qsplayer.network.NetworkChangeReceiver;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.player_container)
    RelativeLayout playerContainer;

    private NetworkChangeReceiver networkChangeReceiver;
    private IntentFilter intentFilter;
    private CustomPlayerView playView;

    private boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
          //          qsPlayView.hideErrorMsg();
                    switch (networkInfo.getType()) {
                        case TYPE_MOBILE:
                            Toast.makeText(context, "正在使用2G/3G/4G网络", Toast.LENGTH_SHORT).show();
                            break;
                        case TYPE_WIFI:
                            Toast.makeText(context, "正在使用wifi上网", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(context, "当前无网络连接", Toast.LENGTH_SHORT).show();
            //        qsPlayView.showErrorMsg("当前无网络连接");
                }


            }
        };
        registerReceiver(networkChangeReceiver, intentFilter);
        initView();
    }

    private void initView() {
        if (playView == null) {
            playView = new CustomPlayerView(this);
            playerContainer.addView(playView,
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT));
            playView.setScreenToggleListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    screenToFull();
                }
            });

            playView.setBackListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFullScreen) {
                        screenToSmall();
                    } else {
                        finish();
                    }
                }
            });
            String videoPath = "http://ql.qsxt.io/live_record_905_1526615963878.mp4";
            playView.prepareAndStartPlay(videoPath);
            playView.replacePlayerControlView(new CustomPlayerControlView(this));

        }
    }

    private void screenToSmall() {
        isFullScreen = false;
        RelativeLayout.LayoutParams linearParams =
                (RelativeLayout.LayoutParams) playerContainer.getLayoutParams();
        linearParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        linearParams.height = (int) getResources().getDimension(R.dimen.dp_value_200);
        linearParams.setMargins(0, 0, 0, 0);
        playerContainer.setLayoutParams(linearParams);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void screenToFull() {
        isFullScreen = true;
        RelativeLayout.LayoutParams linearParams =
                (RelativeLayout.LayoutParams) playerContainer.getLayoutParams();
        linearParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        linearParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        linearParams.setMargins(0, 0, 0, 0);
        playerContainer.setLayoutParams(linearParams);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
