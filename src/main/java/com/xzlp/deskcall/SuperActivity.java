package com.xzlp.deskcall;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.xzlp.deskcall.util.LogUtil;

public abstract class SuperActivity extends Activity {
	public static final String TAG = "SuperActivity";
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtil.getInstance().i("onCreate");
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initProgressDialog();
	}
	
	
	protected void showProgressDialog(int titleId, int msgId){
		String title = getResources().getString(titleId);
		String msg =  getResources().getString(msgId);
		if(pd != null){
			pd.setTitle(title);
			pd.setMessage(msg);
			pd.show();
		}
	}
	protected void stopProgressDialog(){
		if(pd != null && pd.isShowing()){
			pd.dismiss();
		}
	}
	
	private void initProgressDialog() {
		pd = new ProgressDialog(this);//初始化进度条对话框
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
}
