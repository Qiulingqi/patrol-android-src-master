package com.saic.visit.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saic.visit.R;
import com.saic.visit.constant.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * ListView下拉刷新和加载更�?
 * <p>
 * 
 * <strong>变更说明:</strong>
 * <p>
 * 默认如果设置了OnRefreshListener接口和OnLoadMoreListener接口
 * <p>
 * 剩余三个Flag�?<br>
 * mIsAutoLoadMore(是否自动加载更多) <br>
 * mIsMoveToFirstItemAfterRefresh(下拉刷新后是否显示第�?��Item) <br>
 * mIsDoRefreshOnWindowFocused(当该ListView�?��的控件显示到屏幕上时，是否直接显示正在刷�?..)
 * 
 * @author lee
 */
public class MongoPullToRefreshOrLoadMoreListView extends ListView implements OnScrollListener{

	/** 显示格式化日期模�?*/
	private final static String DATE_FORMAT_STR = "yyyy年MM月dd日HH:mm";

	/** 实际的padding的距离与界面上偏移距离的比例 */
	private final static int RATIO = 3;

	// ===========================以下4个常量为
	// 下拉刷新的状态标�?==============================
	/** 松开刷新 */
	private final static int RELEASE_TO_REFRESH = 0;
	/** 下拉刷新 */
	private final static int PULL_TO_REFRESH = 1;
	/** 正在刷新 */
	private final static int REFRESHING = 2;
	/** 刷新完成 or �?��都没做，恢复原状态�? */
	private final static int DONE = 3;

	// ===========================以下3个常量为
	// 加载更多的状态标�?==============================
	/** 加载�?*/
	private final static int ENDINT_LOADING = 1;
	/** 手动完成刷新 */
	private final static int ENDINT_MANUAL_LOAD_DONE = 2;
	/** 自动完成刷新 */
	private final static int ENDINT_AUTO_LOAD_DONE = 3;
	
	/** 滚动的监听器 */
	private OnScrollListener mScrollListener;
	
	private OnFocusIndexListener mFocusIndexListener;

	public OnFocusIndexListener getmFocusIndexListener() {
		return mFocusIndexListener;
	}

	public void setmFocusIndexListener(OnFocusIndexListener mFocusIndexListener) {
		this.mFocusIndexListener = mFocusIndexListener;
	}

	/**
	 * <strong>下拉刷新HeadView的实时状态flag</strong>
	 * 
	 * <p>
	 * 0 : RELEASE_TO_REFRESH;
	 * <p>
	 * 1 : PULL_To_REFRESH;
	 * <p>
	 * 2 : REFRESHING;
	 * <p>
	 * 3 : DONE;
	 * 
	 */
	private int mHeadState;

	/**
	 * <strong>加载更多FootView（EndView）的实时状�?flag</strong>
	 * 
	 * <p>
	 * 0 : 完成/等待刷新 ;
	 * <p>
	 * 1 : 加载�?
	 */
	private int mEndState;

	/** 可以加载更多吗?*/
	private boolean mCanLoadMore = false;

	/** 可以下拉刷新吗?*/
	private boolean mCanRefresh = false;

	/** 可以自动加载更多吗？（注意，先判断是否有加载更多，如果没有，这个flag也没有意义） */
	private boolean mIsAutoLoadMore = false;

	/** 下拉刷新后是否显示第一个Item? */
	private boolean mIsMoveToFirstItemAfterRefresh = false;

	/** 当该ListView�?��的控件显示到屏幕上时，是否直接显示正在刷�?.. */
	private boolean mIsDoRefreshOnUIChanged = false;

	public boolean isCanLoadMore() {
		return mCanLoadMore;
	}

	public void setCanLoadMore(boolean pCanLoadMore) {
		mCanLoadMore = pCanLoadMore;
		if (mCanLoadMore && getFooterViewsCount() == 0) {
			addFooterView();
		} else if (!mCanLoadMore) {
			if (null != mEndRootView) {
				removeFooterView(mEndRootView);
//				mEndLoadTipsTextView.setText("加载完毕");
			}
		}
	}

	public boolean isCanRefresh() {
		return mCanRefresh;
	}

	public void setCanRefresh(boolean pCanRefresh) {
		mCanRefresh = pCanRefresh;
	}

	public boolean isAutoLoadMore() {
		return mIsAutoLoadMore;
	}

	public void setAutoLoadMore(boolean pIsAutoLoadMore) {
		mIsAutoLoadMore = pIsAutoLoadMore;
	}

	public boolean isMoveToFirstItemAfterRefresh() {
		return mIsMoveToFirstItemAfterRefresh;
	}

	public void setMoveToFirstItemAfterRefresh(boolean pIsMoveToFirstItemAfterRefresh) {
		mIsMoveToFirstItemAfterRefresh = pIsMoveToFirstItemAfterRefresh;
	}

	public boolean isDoRefreshOnUIChanged() {
		return mIsDoRefreshOnUIChanged;
	}

	public void setDoRefreshOnUIChanged(boolean pIsDoRefreshOnWindowFocused) {
		mIsDoRefreshOnUIChanged = pIsDoRefreshOnWindowFocused;
	}

	// ============================================================================

	private LayoutInflater mInflater;

	private LinearLayout mHeadRootView;
	private LinearLayout mLinearPointer;
	private TextView mTipsTextView;
	private TextView mLastUpdatedTextView;
	private ImageView mArrowImageView;
	private ImageView mImgCarRunning;
	private AnimationDrawable animationDrawable;

	private View mEndRootView;
	private ProgressBar mEndLoadProgressBar;
	private TextView mEndLoadTipsTextView;

	/** headView动画 */
	private RotateAnimation mArrowAnim;

	/** headView反转动画 */
	private RotateAnimation mArrowReverseAnim;

	/** 用于保证startY的�?在一个完整的touch事件中只被记录一�?*/
	private boolean mIsRecored;

	private int mHeadViewHeight;

	private int mStartY;
	private boolean mIsBack;

	private int mFirstItemIndex;
	private int mLastItemIndex;
	private int mCount;

	@SuppressWarnings("unused")
	private boolean mEnoughCount;// 足够数量充满屏幕�?

	private OnRefreshListener mRefreshListener;
	private OnLoadMoreListener mLoadMoreListener;

	private Handler mHandler;
	
	public Handler getmHandler() {
		return mHandler;
	}

	public void setmHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	private String mLabel;

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String pLabel) {
		mLabel = pLabel;
	}

	public MongoPullToRefreshOrLoadMoreListView(Context pContext) {
		super(pContext);
		init(pContext);
	}

	public MongoPullToRefreshOrLoadMoreListView(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
		init(pContext);
	}

	public MongoPullToRefreshOrLoadMoreListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
		super(pContext, pAttrs, pDefStyle);
		init(pContext);
	}

	/**
	 * 初始化操�?
	 * nn
	 * @param pContext
	 */
	private void init(Context pContext) {

		setCacheColorHint(pContext.getResources().getColor(R.color.tran));
		setOnLongClickListener(null);
		mInflater = LayoutInflater.from(pContext);

		addHeadView();

		this.setOnScrollListener(this);

		initPullImageAnimation(0);
	}

	/**
	 * 添加下拉刷新的HeadView
	 */
	private void addHeadView() {

		mHeadRootView = (LinearLayout) mInflater.inflate(R.layout.pull_to_refresh_head2, null);
		mLinearPointer = (LinearLayout) mHeadRootView.findViewById(R.id.linear_pointer);
		mArrowImageView = (ImageView) mHeadRootView.findViewById(R.id.head_arrowImageView);
		mImgCarRunning = (ImageView) mHeadRootView.findViewById(R.id.loading_progressBar);
		mTipsTextView = (TextView) mHeadRootView.findViewById(R.id.head_tipsTextView);
		mLastUpdatedTextView = (TextView) mHeadRootView.findViewById(R.id.head_lastUpdatedTextView);
		measureView(mHeadRootView);
		mHeadViewHeight = mHeadRootView.getMeasuredHeight();

		mHeadRootView.invalidate();

		addHeaderView(mHeadRootView, null, false);

		mHeadState = DONE;
		changeHeadViewByState();
	}

	/**
	 * 添加加载更多FootView
	 */
	private void addFooterView() {
		mEndRootView = mInflater.inflate(R.layout.pull_to_refresh_load_more, null);
		mEndRootView.setVisibility(View.VISIBLE);
		mEndLoadProgressBar = (ProgressBar) mEndRootView.findViewById(R.id.pull_to_refresh_progress);
		mEndLoadTipsTextView = (TextView) mEndRootView.findViewById(R.id.load_more);
		mEndRootView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCanLoadMore) {
					if (mCanRefresh) {
						if (mEndState != ENDINT_LOADING && mHeadState != REFRESHING) {
							mEndState = ENDINT_LOADING;
							onLoadMore();
						}
					} else if (mEndState != ENDINT_LOADING) {
						mEndState = ENDINT_LOADING;
						onLoadMore();
					}
				}
			}
		});

		addFooterView(mEndRootView);

		if (mIsAutoLoadMore) {
			mEndState = ENDINT_AUTO_LOAD_DONE;
		} else {
			mEndState = ENDINT_MANUAL_LOAD_DONE;
		}
	}

	/**
	 * 实例化下拉刷新的箭头的动画效�?
	 */
	private void initPullImageAnimation(final int pAnimDuration) {

		int _Duration;

		if (pAnimDuration > 0) {
			_Duration = pAnimDuration;
		} else {
			_Duration = 250;
		}

		Interpolator _Interpolator = new LinearInterpolator();

		mArrowAnim = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 1.0f);
		mArrowAnim.setInterpolator(_Interpolator);
		mArrowAnim.setDuration(_Duration);
		mArrowAnim.setFillAfter(true);

		mArrowReverseAnim = new RotateAnimation(360, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 1.0f);
		mArrowReverseAnim.setInterpolator(_Interpolator);
		mArrowReverseAnim.setDuration(_Duration);
		mArrowReverseAnim.setFillAfter(true);
	}

	/**
	 * 测量HeadView宽高(此方法仅适用于LinearLayout)
	 * 
	 * @param pChild
	 */
	private void measureView(View pChild) {
		ViewGroup.LayoutParams p = pChild.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;

		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		pChild.measure(childWidthSpec, childHeightSpec);
	}


	@Override
	public void onScroll(AbsListView pView, int pFirstVisibleItem, int pVisibleItemCount, int pTotalItemCount) {
		Log.d("kyson", "pview"+pView+"pFirstVisibleItem"+pFirstVisibleItem+"pVisibleItemCount"+pVisibleItemCount+"pTotalItemCount"+pTotalItemCount);
		mFirstItemIndex = pFirstVisibleItem;
		mLastItemIndex = pFirstVisibleItem + pVisibleItemCount - 2;
		mCount = pTotalItemCount - 2;
		if (pTotalItemCount > pVisibleItemCount) {
			mEnoughCount = true;
		} else {
			mEnoughCount = false;
		}
	}

	/**
	 * 加载判断�?
	 */
	@Override
	public void onScrollStateChanged(AbsListView pView, int pScrollState) {
		if (mCanLoadMore) {
			if (mLastItemIndex == mCount && pScrollState == SCROLL_STATE_IDLE) {
				if (mEndState != ENDINT_LOADING) {
					if (mIsAutoLoadMore) {
						if (mCanRefresh) {
							if (mHeadState != REFRESHING) {
								mEndState = ENDINT_LOADING;
								onLoadMore();
								changeEndViewByState();
							}
						} else {
							mEndState = ENDINT_LOADING;
							onLoadMore();
							changeEndViewByState();
						}
					} else {
						mEndState = ENDINT_MANUAL_LOAD_DONE;
						changeEndViewByState();
					}
				}
			}
		} else if (mEndRootView != null && mEndRootView.getVisibility() == VISIBLE) {
			mEndRootView.setVisibility(View.GONE);
			this.removeFooterView(mEndRootView);
		}
		
		Log.i("pScrollState", "pScrollState = " + pScrollState);
		if (pScrollState != 0) {
			Constants.isScrolling = true;
		} else {
			Constants.isScrolling = false;
			if (getmHandler() != null) {
				getmHandler().sendEmptyMessage(0);
			}
		}
		
		// 不滚动时保存当前滚动到的位置  
        if (pScrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//        	getmFocusIndexListener().focusIndex(this.getFirstVisiblePosition());
//        	if (currentIndex == 0) {
//        		indexOne = mListView.getFirstVisiblePosition();
//        	} else if (currentIndex == 1) {
//        		indexTwo = mListView.getFirstVisiblePosition();
//        	} else if (currentIndex == 2) {
//        		indexThree = mListView.getFirstVisiblePosition();
//        	}
        } 
		
	}

	public interface OnFocusIndexListener {
		abstract void focusIndex(int visiblePosition);
	}
	
	/**
	 * 改变加载更多状�?
	 */
	private void changeEndViewByState() {
		Log.i("pulltorefresh", "mCanLoadMore = " + mCanLoadMore + " , and mEndState = " + mEndState);
		if (mCanLoadMore) {
			switch (mEndState) {
			case ENDINT_LOADING: // 刷新�?
				if (mEndLoadTipsTextView.getText().equals(R.string.p2refresh_doing_end_refresh)) {
					break;
				}
				mEndLoadTipsTextView.setText(R.string.p2refresh_doing_end_refresh);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.VISIBLE);
				break;
			case ENDINT_MANUAL_LOAD_DONE:// 手动刷新完成

				mEndLoadTipsTextView.setText(R.string.p2refresh_end_click_load_more);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.GONE);

				mEndRootView.setVisibility(View.VISIBLE);
				break;
			case ENDINT_AUTO_LOAD_DONE:// 自动刷新完成

				mEndLoadTipsTextView.setText(R.string.p2refresh_end_load_more);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.GONE);

				mEndRootView.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 注意事项�?此方法不适用于ViewPager中， 方法为：直接调用pull2RefreshManually();
	 */
	@Override
	public void onWindowFocusChanged(boolean pHasWindowFocus) {
		super.onWindowFocusChanged(pHasWindowFocus);
		if (mIsDoRefreshOnUIChanged) {
			if (pHasWindowFocus) {
				pull2RefreshManually();
			}
		}
	}

	/**
	 * 当该ListView�?��的控件显示到屏幕上时，直接显示正在刷�?..
	 */
	public void pull2RefreshManually() {
		mHeadState = REFRESHING;
		changeHeadViewByState();
		onRefresh();

		mIsRecored = false;
		mIsBack = false;
	}

	/**
	 */
	public boolean onTouchEvent(MotionEvent event) {

		if (mCanRefresh) {
			if (mCanLoadMore && mEndState == ENDINT_LOADING) {
				return super.onTouchEvent(event);
			}

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:

				if (mFirstItemIndex == 0 && !mIsRecored) {
					mIsRecored = true;
					mStartY = (int) event.getY();
				} else if (mFirstItemIndex == 0 && mIsRecored) {
					mStartY = (int) event.getY();
				}

				break;

			case MotionEvent.ACTION_UP:

				if (mHeadState != REFRESHING) {

					if (mHeadState == DONE) {

					}
					if (mHeadState == PULL_TO_REFRESH) {
						mHeadState = DONE;
						changeHeadViewByState();
					}
					if (mHeadState == RELEASE_TO_REFRESH) {
						mHeadState = REFRESHING;
						changeHeadViewByState();
						onRefresh();
					}
				}

				mIsRecored = false;
				mIsBack = false;

				break;

			case MotionEvent.ACTION_MOVE:

				int _TempY = (int) event.getY();

				if (!mIsRecored && mFirstItemIndex == 0) {
					mIsRecored = true;
					mStartY = _TempY;
				}

				if (mHeadState != REFRESHING && mIsRecored) {

					Log.i("uuuuuuu", "mHeadState = " + mHeadState);
					if (mHeadState == RELEASE_TO_REFRESH) {

						setSelection(0);

						if (((_TempY - mStartY) / RATIO < mHeadViewHeight) && (_TempY - mStartY) > 0) {
							mHeadState = PULL_TO_REFRESH;
							changeHeadViewByState();
						} else if (_TempY - mStartY <= 0) {
							mHeadState = DONE;
							changeHeadViewByState();
						}
					}
					if (mHeadState == PULL_TO_REFRESH) {

						setSelection(0);

						if ((_TempY - mStartY) / RATIO >= mHeadViewHeight) {
							mHeadState = RELEASE_TO_REFRESH;
							mIsBack = true;
							changeHeadViewByState();
						} else if (_TempY - mStartY <= 0) {
							mHeadState = DONE;
							changeHeadViewByState();
						}
					}

					if (mHeadState == DONE) {
						if (_TempY - mStartY > 0) {
							mHeadState = PULL_TO_REFRESH;
							changeHeadViewByState();
						}
					}

					if (mHeadState == PULL_TO_REFRESH) {
						mHeadRootView.setPadding(0, -1 * mHeadViewHeight + (_TempY - mStartY) / RATIO, 0, 0);

					}

					if (mHeadState == RELEASE_TO_REFRESH) {
						mHeadRootView.setPadding(0, (_TempY - mStartY) / RATIO - mHeadViewHeight, 0, 0);
					}
				}
				break;
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 当HeadView状�?改变时�?，调用该方法，以更新界面
	 */
	private void changeHeadViewByState() {
		switch (mHeadState) {
		case RELEASE_TO_REFRESH:
			mArrowImageView.setVisibility(View.VISIBLE);
			mTipsTextView.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.startAnimation(mArrowAnim);
			mTipsTextView.setText(R.string.p2refresh_release_refresh);

			break;
		case PULL_TO_REFRESH:
			mTipsTextView.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.VISIBLE);
			if (mIsBack) {
				mIsBack = false;
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mArrowReverseAnim);
				mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			} else {
				mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			}
			break;

		case REFRESHING:

			changeHeaderViewRefreshState();
			break;
		case DONE:

			mHeadRootView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			mArrowImageView.clearAnimation();
			mArrowImageView.setImageResource(R.drawable.pointer);
			mTipsTextView.setText(R.string.p2refresh_pull_to_refresh);
			mLastUpdatedTextView.setVisibility(View.GONE);
			mLinearPointer.setVisibility(View.VISIBLE);
			mImgCarRunning.setVisibility(View.GONE);
			if (animationDrawable != null) {
				animationDrawable.stop();
			}
			break;
		}
	}

	/**
	 * 改变HeadView在刷新状态下的显�?
	 */
	private void changeHeaderViewRefreshState() {
		Log.i("uuuuuuu","changeHeaderViewRefreshState on the top");
		mHeadRootView.setPadding(0, 10, 0, 0);
		mArrowImageView.clearAnimation();
		// mArrowImageView.setVisibility(View.GONE);
		mTipsTextView.setText(R.string.p2refresh_doing_head_refresh);
		mLastUpdatedTextView.setVisibility(View.VISIBLE);
		mLinearPointer.setVisibility(View.GONE);
		mImgCarRunning.setVisibility(View.VISIBLE);
		animationDrawable = (AnimationDrawable) mImgCarRunning.getBackground();
		animationDrawable.start();
	}

	/**
	 * 下拉刷新监听接口
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/**
	 * 加载更多监听接口
	 */
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

	public void setOnRefreshListener(OnRefreshListener pRefreshListener) {
		if (pRefreshListener != null) {
			mRefreshListener = pRefreshListener;
			mCanRefresh = true;
		}
	}

	public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
		if (pLoadMoreListener != null) {
			mLoadMoreListener = pLoadMoreListener;
			mCanLoadMore = true;
			if (mCanLoadMore && getFooterViewsCount() == 0) {
				addFooterView();
			}
		}
	}

	/**
	 * 正在下拉刷新
	 */
	private void onRefresh() {
		if (mRefreshListener != null) {
			mRefreshListener.onRefresh();
		}
	}

	/**
	 * 下拉刷新完成
	 */
	public void onRefreshComplete() {

		mHeadState = DONE;
		mLastUpdatedTextView.setText(getResources().getString(R.string.p2refresh_refresh_lasttime) + new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
		changeHeadViewByState();

		if (mIsMoveToFirstItemAfterRefresh) {
			mFirstItemIndex = 0;
//			setSelection(0);
		}
	}

	/**
	 * 正在加载更多，FootView显示 �?加载�?..
	 */
	private void onLoadMore() {
		if (mLoadMoreListener != null) {
			mEndLoadTipsTextView.setText(R.string.p2refresh_doing_end_refresh);
			mEndLoadTipsTextView.setVisibility(View.VISIBLE);
			mEndLoadProgressBar.setVisibility(View.VISIBLE);
			mLoadMoreListener.onLoadMore();
		}
	}

	/**
	 * 加载更多完成
	 */
	public void onLoadMoreComplete() {
		if (mIsAutoLoadMore) {
			mEndState = ENDINT_AUTO_LOAD_DONE;
		} else {
			mEndState = ENDINT_MANUAL_LOAD_DONE;
		}
		changeEndViewByState();
	}

	/**
	 * 主要更新�?��刷新时间啦！
	 */
	public void setAdapter(BaseAdapter adapter) {
		mLastUpdatedTextView.setText(getResources().getString(R.string.p2refresh_refresh_lasttime) + new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
		super.setAdapter(adapter);
	}

	public void setEndRootViewVisibility(boolean b) {

		if (mEndRootView == null) {
			return;
		}

		if (getFooterViewsCount() == 0 && b) {
			addFooterView();
		} else {
			removeFooterView(mEndRootView);
		}
	}

	public View getFooterView() {
		return mEndRootView;
	}

}
