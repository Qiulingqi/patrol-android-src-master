package com.saic.visit.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saic.visit.R;


/**
 * Created by ywih on 15/8/31.
 */
public class DialogUtil {
    public static int sRefCount = 0;
    public static Dialog mProgressDialog;
    public static AlertDialog.Builder mDialogBuilder;

    /**
     * @param context
     * @Description: 显示加载提示框
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:39:21
     */
    public static void showLoading(Context context) {
//        showLoading(context, context.getResources().getString(R.string.llloginsdk_loading));
        showLoadingDialog(context);
    }

    /**
     * @param context
     * @param text    提示内容
     * @Description: 显示加载提示框
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:39:31
     */
    public static void showLoading(Context context, String text) {
        try {
            sRefCount++;
            if (mProgressDialog == null) {
                mProgressDialog = buildLoadingDialog(context, text);
            }
            mProgressDialog.show();
            if (image != null) {
                AnimationDrawable anim = (AnimationDrawable) image.getBackground();
                anim.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 关闭加载提示框
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:39:44
     */
    public static void dismisLoading() {
//        dismisLoading(0);
        dismissLoadingDialog();
    }


    /**
     * @Description: 关闭加载提示框
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:39:44
     */
    public static void dismisLoading(int errCode) {
        try {
            if (sRefCount > 0 && --sRefCount > 0) {
                sRefCount = sRefCount < 0 ? 0 : sRefCount;
                return;
            }

            if (errCode != 0) {
                if (mProgressDialog != null) {
                    if (image != null) {
                        AnimationDrawable anim = (AnimationDrawable) image.getBackground();
                        anim.stop();
                    }
                    ToastUtil.show(mProgressDialog.getContext(), mProgressDialog.getContext().getString(R.string.llloginsdk_neterror));
                }
            }

            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                if (image != null) {
                    AnimationDrawable anim = (AnimationDrawable) image.getBackground();
                    anim.stop();
                }
                mProgressDialog.dismiss();
                mProgressDialog = null;

            }
        } catch (Exception e) {
        }

    }

    /**
     * @Description: 创建加载提示框
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:40:02
     * @param context
     * @param text
     * @return
     */
    private static ImageView image;

    public static Dialog buildLoadingDialog(Context context, String text) {
        View v = View.inflate(context, R.layout.llloginsdk_loading_dialog, null);// 得到加载view
        v.setVisibility(View.VISIBLE);
        image = (ImageView) v.findViewById(R.id.loadingImg);
        Dialog loadingDialog = new Dialog(context, R.style.LLLoginSDK_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        return loadingDialog;
    }

    private static LoadingDialog loadingDialog;

    public static void showLoadingDialog(Context con) {
        if(con == null)
            return;
        if (loadingDialog == null)
            loadingDialog = new LoadingDialog(con);
        loadingDialog.show();
    }

    public static void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    /**
     * @param context
     * @param title
     * @param content
     * @Description: 显示提示信息对话框
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:40:52
     */
    public static void showHintDialog(Context context, String title,
                                      String content) {
        showHintDialog(context, title, content, false);
    }

    /**
     * @param context
     * @param content
     * @Description: 显示提示信息对话框
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:40:47
     */
    public static void showHintDialog(Context context, String content) {
        showHintDialog(context, context.getResources().getString(R.string.llloginsdk_tip),
                content, false);
    }

    /**
     * @param context
     * @param content
     * @param isCancelFinish
     * @Description: 显示提示信息对话框
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:40:39
     */
    public static void showHintDialog(Context context, String content,
                                      boolean isCancelFinish) {
        try {
            showHintDialog(context,
                    context.getResources().getString(R.string.llloginsdk_tip), content,
                    isCancelFinish);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param title
     * @param content
     * @param isCancelFinish
     * @Description: 显示提示信息对话框
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:40:35
     */
    public static void showHintDialog(final Context context, String title,
                                      String content, final boolean isCancelFinish) {
        if (context == null)
            return;
        // if (mDialogBuilder == null) {
        mDialogBuilder = new AlertDialog.Builder(context);
        // }
        mDialogBuilder
                .setTitle(title)
                .setMessage(content)
                .setCancelable(true)
                .setNegativeButton(
                        context.getResources().getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    dialog.cancel();
                                    if (isCancelFinish) {
                                        ((Activity) context).finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
        if (isCancelFinish) {
            mDialogBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getRepeatCount() == 0) {
                        try {
                            dialog.cancel();
                            ((Activity) context).finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
        mDialogBuilder.setCancelable(false);
        mDialogBuilder.show();
    }

    /**
     * @param context
     * @param title
     * @param content
     * @param buttonID
     * @Description: 显示提示信息对话框，有一个按钮
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:40:28
     */
    public static void showHintDialog(final Context context, String title,
                                      String content, String buttonID, DialogInterface.OnClickListener clickListener) {
        if (mDialogBuilder == null) {
            mDialogBuilder = new AlertDialog.Builder(context);
        }
        mDialogBuilder
                .setTitle(title)
                .setMessage(content)
                .setCancelable(true)
                .setPositiveButton(
                        buttonID,
                        clickListener);
        mDialogBuilder.setCancelable(false);
        mDialogBuilder.show();
    }

    /**
     * @param context
     * @param title
     * @param content
     * @param buttonID
     * @Description: 显示提示信息对话框，有一个按钮
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:40:28
     */
    public static void showGrayOrangeHintDialog(final Context context, String title,
                                                String content, String buttonID, final View.OnClickListener onClickListener) {

        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        View view = View.inflate((Activity) context, R.layout.dialog_grayorange, null);
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.content)).setText(content);
        ((Button) view.findViewById(R.id.okBtn)).setText(buttonID);
        ((Button) view.findViewById(R.id.okBtn)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
                onClickListener.onClick(v);
            }
        });
        window.setContentView(view);

    }

    public static void showHintDialog(final Context context, String title,
                                      String content, DialogInterface.OnClickListener clickListener) {
        showHintDialog(context, title,
                content, context.getResources().getString(android.R.string.ok), clickListener);
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param onClickListener
     * @Description: 显示提示信息对话框，有两个按钮
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:40:23
     */
    public static void showHintDoubleDialog(final Activity context,
                                            String title, String message, DialogInterface.OnClickListener onClickListener) {
        showHintDoubleDialog(context, title, message, onClickListener, null);
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param onClickListener
     * @Description: 显示提示信息对话框，有两个按钮
     * @author Ming Fanglin
     * @date 2014-12-3 上午11:40:23
     */
    public static void showHintDoubleDialog(final Activity context,
                                            final String title, final String message, final DialogInterface.OnClickListener onClickListener, final DialogInterface.OnClickListener cancelListener) {
        if (mDialogBuilder == null || ((Activity) ((ContextThemeWrapper) mDialogBuilder.getContext()).getBaseContext()).isFinishing()) {
            mDialogBuilder = new AlertDialog.Builder(context);
        }
        mDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (onClickListener != null) {
                            onClickListener.onClick(dialog, id);
                        }
                    }
                })
                .setNegativeButton(context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (cancelListener != null) {
                            cancelListener.onClick(dialog, id);
                        }
                    }
                });
        mDialogBuilder.setCancelable(false);
        mDialogBuilder.show();
    }

}