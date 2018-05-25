package self.dz.qsplayer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import self.dz.qsplayer.network.NetworkChangeReceiver;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.qs_play_view)
    QSPlayView qsPlayView;
    private NetworkChangeReceiver networkChangeReceiver;
    private IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        qsPlayView.setErrorView(new QSErrorView(this));
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    qsPlayView.hideErrorMsg();
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
                    qsPlayView.showErrorMsg("当前无网络连接");
                }


            }
        };
        registerReceiver(networkChangeReceiver, intentFilter);
    }
}
