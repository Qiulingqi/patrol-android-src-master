package com.saic.visit.utils;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;

public class ViewUtil {

    public static <T extends View> T findViewById(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }

    public static <T extends View> T findViewById(View parentView, int id) {
        return (T) parentView.findViewById(id);
    }

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
