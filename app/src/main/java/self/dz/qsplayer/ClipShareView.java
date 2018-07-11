package self.dz.qsplayer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ClipShareView extends RelativeLayout {

    @BindView(R.id.clip_share_container)
    RelativeLayout mClipContainer;

    @BindView(R.id.clip_share_cancel_btn)
    TextView mClipCancelBtn;

    @BindView(R.id.clipping_btn_red_bg)
    ImageView mClippingBtnBg;

    @BindView(R.id.clip_completed_btn)
    ImageView mClipCompletedBtn;

    @BindView(R.id.clip_hint_container)
    RelativeLayout mClipHintContainer;

    int layoutId = R.layout.clip_share_view;
    private AnimatorSet animatorSet;

    private static int MSG_WHAT_START_CLIP_BTN_DELAY = -1;
    public static int MIN_CLIP_TIME = 3 * 1000;//片段时长不少于3s

    public ClipShareView(@NonNull Context context) {
        this(context, null);
    }

    public ClipShareView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipShareView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(layoutId, this);
        ButterKnife.bind(this);
        clipStart();
    }

    private void clipStart() {
        showClipContainerView();
        showClipStartBtnStatus();
    }

    private void showClipStartBtnStatus() {
        mClipCompletedBtn.setEnabled(false);
        handler.sendEmptyMessageDelayed(MSG_WHAT_START_CLIP_BTN_DELAY, MIN_CLIP_TIME);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_WHAT_START_CLIP_BTN_DELAY) {
                mClipCompletedBtn.setImageDrawable(
                        getResources().getDrawable(R.drawable.btn_clip_record_red));
                mClipCompletedBtn.setEnabled(true);

                startClippingBtnAnimation();
                return;
            }
//            if (msg.what < ShareDataUtil.longToSecond(maxClipTime)) {
//                showClipHintTime(msg.what);
//            } else {
//                completeClip();
//            }
        }
    };

    private void showClipContainerView() {
        mClipContainer.setVisibility(View.VISIBLE);
        mClipCancelBtn.setVisibility(View.VISIBLE);
        mClipHintContainer.setVisibility(View.VISIBLE);
        mClipCompletedBtn.setVisibility(View.VISIBLE);
    }


    //按钮状态 - 录制过程中 - 呼吸灯效果
    private void startClippingBtnAnimation() {
        mClippingBtnBg.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mClippingBtnBg, "scaleX", 0.8f, 1f);
        objectAnimatorX.setRepeatCount(ObjectAnimator.INFINITE);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mClippingBtnBg, "scaleY", 0.8f, 1f);
        objectAnimatorY.setRepeatCount(ObjectAnimator.INFINITE);

        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
        }
        animatorSet.setDuration(1000);
        animatorSet.playTogether(objectAnimatorX, objectAnimatorY);
        animatorSet.start();
    }

}
