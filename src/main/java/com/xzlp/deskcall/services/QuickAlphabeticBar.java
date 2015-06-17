package com.xzlp.deskcall.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.xzlp.deskcall.R;

/**
 * 字母表
 */
public class QuickAlphabeticBar extends ImageButton {
	private TextView mDialogText;
	private Handler mHandler;
	private ListView mList;
	private float mHight;
	private Activity activity;
	private String[] letter = new String[] { "#", "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" };
	private List<String> letters = Arrays.asList(letter); 
	private HashMap<String, Integer> alphaIndexer;

	public QuickAlphabeticBar(Context context) {
		super(context);
	}

	public QuickAlphabeticBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public QuickAlphabeticBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(Activity ctx) {
		this.activity = ctx;
		mDialogText = (TextView) ctx.findViewById(R.id.fast_position);
		mDialogText.setVisibility(View.INVISIBLE);
		mHandler = new Handler();
	}

	public void setListView(ListView mList) {
		this.mList = mList;
	}

	public void setAlphaIndexer(HashMap<String, Integer> alphaIndexer) {
		this.alphaIndexer = alphaIndexer;
		Set<String> set = alphaIndexer.keySet();
		letters = new ArrayList<String>(set);
		Collections.sort(letters);
		this.invalidate();
	}

	public void setHight(float mHight) {
		this.mHight = mHight;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int act = event.getAction();
		float y = event.getY();
		final int oldChoose = choose;
		if(letters.size() == 0)
			return super.onTouchEvent(event);
		// 计算手指位置，找到对应的段，让mList移动段开头的位置上
		int selectIndex = (int) (y / (getHeight() / letters.size()));
		
		if (selectIndex > -1 && selectIndex < letters.size()) {// 防止越界
			String key = letters.get(selectIndex);
			if (alphaIndexer.containsKey(key)) {
				int pos = alphaIndexer.get(key);
				if (mList.getHeaderViewsCount() > 0) {// 防止ListView有标题栏，本例中没有。
					this.mList.setSelectionFromTop(
							pos + mList.getHeaderViewsCount(), 0);
				} else {
					this.mList.setSelectionFromTop(pos, 0);
				}
				mDialogText.setText(letters.get(selectIndex));
			}
		}

		switch (act) {
		case MotionEvent.ACTION_DOWN:
			showBkg = true;

			if (oldChoose != selectIndex) {
				if (selectIndex > 0 && selectIndex < letters.size()) {
					choose = selectIndex;
					invalidate();
				}
			}

			if (mHandler != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (mDialogText != null
								&& mDialogText.getVisibility() == View.INVISIBLE) {
							mDialogText.setVisibility(VISIBLE);
						}
					}
				});
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != selectIndex) {
				if (selectIndex > 0 && selectIndex < letters.size()) {
					choose = selectIndex;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			showBkg = false;
			choose = -1;
			if (mHandler != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (mDialogText != null
								&& mDialogText.getVisibility() == View.VISIBLE) {
							mDialogText.setVisibility(INVISIBLE);
						}
					}
				});
			}
			break;
		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	//TODO:
	Paint paint = new Paint();
	boolean showBkg = false;
	int choose = -1;

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*if (showBkg) {//设置边栏背景色
			canvas.drawColor(Color.parseColor("#b20264"));
		}*/
		if(letters.size() == 0)
			return;
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / letters.size();
		for (int i = 0; i < letters.size(); i++) {
//			paint.setColor(activity.getResources().getColor(R.color.letter_normal_color));
			paint.setColor(Color.parseColor("#5E5E5E"));
		
			paint.setTextSize(20);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			if (i == choose) {
				paint.setColor(Color.parseColor("#00BFFF"));//滑动时按下的字母颜色
				paint.setFakeBoldText(true);
			}
			float xPos = width / 2 - paint.measureText(letters.get(i)) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(letters.get(i), xPos, yPos, paint);
			paint.reset();
		}

	}

}
