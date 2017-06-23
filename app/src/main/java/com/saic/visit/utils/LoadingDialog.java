package com.saic.visit.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.ContextThemeWrapper;
import android.widget.ImageView;

import com.saic.visit.R;


/**
 * Created by zhaolf on 2015/9/15.
 * 显示 加载中... 动画
 */
public class LoadingDialog extends Dialog {

    private ImageView gifImageView;

    public LoadingDialog(Context context) {
        this(context, R.style.LLLoginSDK_dialog);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        setContentView(R.layout.loading_dialog);
        gifImageView = (ImageView) findViewById(R.id.loadingImg);
        setCancelable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (gifImageView != null) {
            AnimationDrawable anim = (AnimationDrawable) gifImageView.getBackground();
            anim.stop();
        }
    }

    @Override
    public void dismiss() {
        Context context = getContext();
        if (context instanceof ContextThemeWrapper ){
            ContextThemeWrapper wrapper = ( ContextThemeWrapper ) getContext();
            context = wrapper.getBaseContext();
        }

        if (context instanceof Activity) {
            Activity activity = (Activity)context;

            if ( activity.isFinishing() ){
                return;
            }
        }
        super.dismiss();
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public void show() {
        super.show();
        if (gifImageView != null) {
            AnimationDrawable anim = (AnimationDrawable) gifImageView.getBackground();
            anim.start();
        }
    }
}
