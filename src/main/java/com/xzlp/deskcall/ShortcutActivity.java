package com.xzlp.deskcall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xzlp.deskcall.util.UtilTools;

public class ShortcutActivity extends Activity implements OnClickListener {

	private static final String ACTION = "com.xzlp.deskcall.SHORT_CUT"; // 当前Activity可以响应的action

	public static final String INTENT_KEY_PHONE = "phone";

	public static final String INTENT_KEY_NAME = "name";

	private Button dialButton;

	private Button smsButton;

	private Button cancelButton;

	private String phone;

	private TextView mCallName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除标题栏
		setContentView(R.layout.shoutcut_activity_layout);
		DisplayMetrics localDisplayMetrics = getResources().getDisplayMetrics();
		float scrrenW = localDisplayMetrics.widthPixels;
		float screenH = localDisplayMetrics.heightPixels;
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		params.width = (int) (scrrenW * 0.9);
		params.height = params.height;
		this.getWindow().setAttributes(params);

		Intent intent = getIntent();
		phone = intent.getStringExtra(INTENT_KEY_PHONE);
		String name = intent.getStringExtra(INTENT_KEY_NAME);
		
		mCallName = (TextView) findViewById(R.id.callname);
		mCallName.setText(name);
		
		dialButton = (Button) findViewById(R.id.shortcut_dail);
		smsButton = (Button) findViewById(R.id.shortcut_sms);
		cancelButton = (Button) findViewById(R.id.shortcut_cancel);

		dialButton.setOnClickListener(this);
		smsButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);

		Log.i("xxxx", "ShortcutActivity onCreate");
		Log.i("xxxx", "action:" + intent.getAction());
		Log.i("xxxx", "phone:" + intent.getStringExtra("phone"));
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.i("xxxx", "ShortcutActivity onNewIntent");
		Log.i("xxxx", "action:" + intent.getAction());

		Log.i("xxxx", "phone:" + intent.getStringExtra("phone"));
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.shortcut_dail: // 拨打电话
				UtilTools.callContact(this, phone);
				finish();
				break;
			case R.id.shortcut_sms: // 发送短信
				UtilTools.sendSMS(this, phone);
				finish();
				break;
			case R.id.shortcut_cancel: // 取消
				finish();
				break;
			default:
				break;
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("xxxx", "ShortcutActivity onStart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("xxxx", "ShortcutActivity onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.i("xxxx", "ShortcutActivity onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		Log.i("xxxx", "ShortcutActivity onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("xxxx", "ShortcutActivity onDestroy");
	}

	public static Intent getIntentCanLaunchThisComponent(String name, String phone, Context context, Class classs) {
		Intent pendingIntent = new Intent(ACTION);
		pendingIntent.setClassName(context, classs.getName());
		// ComponentName comp = new ComponentName(PACKAGE, ".ShortcutActivity");
		// pendingIntent.setComponent(comp);
		pendingIntent.putExtra(INTENT_KEY_PHONE, phone);
		pendingIntent.putExtra(INTENT_KEY_NAME, name);

		return pendingIntent;
	}

}
