package com.xzlp.deskcall.defineview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.xzlp.deskcall.R;

public class ScaleButton extends Button {

	private int backColor;// 背景色

	private ScaleAnimation animation_start;// 按下后缩放的效果

	private ScaleAnimation animation_end;

	public ScaleButton(Context context) {
		super(context);
	}

	public ScaleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.colorbtn);
		backColor = typedArray.getColor(R.styleable.colorbtn_bgcolor, R.color.blue);
		typedArray.recycle();
		initView();
	}

	private void initView() {
		float start = 1.0f;
		float end = 0.95f;
		animation_start = new ScaleAnimation(start, end, start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation_start.setDuration(200);
		animation_start.setFillAfter(true);
		animation_end = new ScaleAnimation(end, start, end, start, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation_end.setDuration(200);
		animation_end.setFillAfter(true);
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent)
	  {
		switch (paramMotionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			invalidate();
			this.startAnimation(animation_start);
			break;
		case MotionEvent.ACTION_UP:
			invalidate();
			this.startAnimation(animation_end);
//			performClick();
			break;
		// 滑动出去不会调用action_up,调用action_cancel
		case MotionEvent.ACTION_CANCEL:
			invalidate();
			this.startAnimation(animation_end);
			break;
		}
		animation_end.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
			}
		});
		// 不返回true，Action_up就响应不了
		return super.onTouchEvent(paramMotionEvent);
	  }
}
