package com.log.loggerlib;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Created by cong on 19-5-24.
 */

public class MyToast {

    private static MyToast mToastUtil;
    private static TextView textView;
    private static Toast mToast;
    private View.OnAttachStateChangeListener listener;
    //是否已经隐藏toast，隐藏上一条toast才可以显示当前toast
    private boolean mHasHidden = true;
    //是否可以显示toast
    private boolean mIsCanShow = true;

    /**
     * 单例对象
     */
    public synchronized static MyToast getInstance() {
        if (mToastUtil == null) {
            synchronized (MyToast.class) {
                if (mToastUtil == null) {
                    mToastUtil = new MyToast();
//                    mToast = new Toast(FutureCrApplication.Companion.getContext());
//                    mToast.setGravity(Gravity.CENTER, 0, 0);
//
//                    textView = new TextView(FutureCrApplication.Companion.getContext());
//                    textView.setBackgroundResource(R.drawable.bg_toast_frame_shape);
//                    textView.setGravity(Gravity.CENTER);
//                    textView.setAlpha(0.9f);
//                    textView.setPadding(ScreenUtil.dip2px(15),ScreenUtil.dip2px(10),ScreenUtil.dip2px(15),ScreenUtil.dip2px(10));
//                    // 这里14的单位是默认sp，详情请见方法源代码
//                    textView.setTextSize(14);
//                    textView.setTextColor(FutureCrApplication.Companion.getContext().getResources().getColor(R.color.colorToastText));
                }
            }
        }
        return mToastUtil;
    }

    //短时间吐司
    public void show(int resourceID) {
        show(resourceID, Toast.LENGTH_SHORT);
    }

    //短时间吐司
    public void show(String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    //自定义时长吐司
    public void show(@StringRes int resourceID, int duration) {
        // 用于显示的文字
//        String text = FutureCrApplication.Companion.getContext().getString(resourceID);
//        show(text, duration);
    }


    /**
     * 展示吐司
     * @param text
     * @param duration
     */
    public void show(String text, int duration){
        if (!mIsCanShow) return;

        textView.setText(text);
        mToast.setView(textView);
        mToast.setDuration(duration);

        //监听toast弹窗的view显示隐藏生命周期状态
        if (listener == null) {
            listener = new View.OnAttachStateChangeListener() {
                //添加窗口
                @Override
                public void onViewAttachedToWindow(View v) {
                }

                //移除view的显示回调
                @Override
                public void onViewDetachedFromWindow(View v) {
                    mHasHidden = true;
                }
            };
            mToast.getView().addOnAttachStateChangeListener(listener);
        }

        if (mHasHidden) {
            mToast.show();
            mHasHidden = false;
        }
    }

    public void setIsCanShow(boolean mIsCanShow) {
        this.mIsCanShow = mIsCanShow;
    }

}
