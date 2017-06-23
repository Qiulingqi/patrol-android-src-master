package com.saic.visit.widget;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存textwatcher，可以clear所有添加的textwatcher。
 * Created by guizhen on 16/7/25.
 */
public class ClearWatcherEditText extends EditText {

    private List<TextWatcher> textWatchers = null;

    public ClearWatcherEditText(Context context) {
        super(context);
    }

    public ClearWatcherEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearWatcherEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        if (textWatchers == null) {
            textWatchers = new ArrayList<>();
        }
        textWatchers.add(watcher);
        super.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher) {
        if (textWatchers != null) {
            textWatchers.remove(watcher);
        }
        super.removeTextChangedListener(watcher);
    }

    /**
     * 去掉所有添加的textwatcher
     */
    public void clearTextChangedListener() {
        if (textWatchers != null) {
            for (TextWatcher watcher : textWatchers) {
                super.removeTextChangedListener(watcher);
            }
            textWatchers.clear();
            textWatchers = null;
        }
    }

}
