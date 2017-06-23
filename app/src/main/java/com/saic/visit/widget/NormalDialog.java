package com.saic.visit.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.saic.visit.R;


public class NormalDialog extends Dialog implements OnClickListener{

	private Context mContext;
	private LinearLayout mLinearDialog;
	private TextView mTxtTitle, mTxtContent;
	private ImageView imgDelete;
	private String content;
	private String title;
	
	public NormalDialog(Context context, String content,String title) {
		super(context, R.style.Dialog);
		mContext = context;
		this.content = content;
		this.title = title;
		setContentView(R.layout.normaldialog);
		initViews();
		initEvents();
		setCancelable(false);
		setCanceledOnTouchOutside(false);
	}

	private void initViews() {
		mLinearDialog = (LinearLayout) findViewById(R.id.linear_main_dialog);
		mTxtContent = (TextView) findViewById(R.id.txt_content);
		mTxtTitle = (TextView) findViewById(R.id.txt_title);
		imgDelete = (ImageView) findViewById(R.id.img_delete);
		mTxtContent.setText(content);
		mTxtTitle.setText(title);
	}
	
	private void initEvents() {
		imgDelete.setOnClickListener(this);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams layoutParams = new LayoutParams(dm.widthPixels * 4/ 5,
				dm.heightPixels / 2);
		mLinearDialog.setLayoutParams(layoutParams);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.img_delete:
			dismiss();
			break;
		}
		
	}
}
