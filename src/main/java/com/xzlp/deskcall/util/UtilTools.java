package com.xzlp.deskcall.util;

import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class UtilTools {

	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}

	public static void hideSoft(Context context, View v) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/**
	 * 拨打电话
	 * 
	 * @param phoneNum
	 *            电话号码
	 */
	public static void callContact(Context context, String phoneNum) {
		// 判断是否存在sim卡
		if (!isSimOk(context)) {
			Log.i("xxxx", "sim卡不可使用");
			Toast.makeText(context, "SIM卡不可用", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!hasCallPermission(context)) {
			Log.i("xxxx", "未获得拨打电话的权限");
			Toast.makeText(context, "未获得拨打电话的权限", Toast.LENGTH_SHORT).show();
			return;
		}
		Log.i("xxxx", "开始拨打电话");
		if (phoneNum != null && !"".equals(phoneNum.trim())) {
			try {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.CALL");
				intent.setData(Uri.parse("tel:" + phoneNum));
				context.startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(context, "该设备无法拨打电话", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 判断sim卡是否可用
	 */
	public static boolean isSimOk(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = tm.getSimState();
		if (simState == TelephonyManager.SIM_STATE_READY) {
			return true;
		}

		return false;
	}

	/**
	 * 判断是否具有拨打电话的权限
	 */
	public static boolean hasCallPermission(Context context) {
		int result = context.getPackageManager().checkPermission("android.permission.CALL_PHONE", context.getPackageName());
		if (result == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		return false;
	}

	public static String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // ��д���
		} else {
			return "#";
		}
	}

	/**
	 * 发送短信
	 * 
	 * @param phoneNum
	 *            电话号码
	 */
	public static void sendSMS(Context context, String phoneNum) {
		// 判断是否存在sim卡
		if (!isSimOk(context)) {
			Toast.makeText(context, "SIM卡不可用", Toast.LENGTH_SHORT).show();
			return;
		}
		if (phoneNum != null && !"".equals(phoneNum.trim())) {
			try {
				Uri smsToUri = Uri.parse("smsto:" + phoneNum);
				Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
				context.startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(context, "该设备无法发送短信", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 创建快捷方式
	 * 
	 * @param context
	 * @param shortcutName
	 * @param shortcutIcon
	 * @param penddingIntent
	 */
	public static void createShortcut(Context context, String shortcutName, Bitmap shortcutIcon, Intent penddingIntent) {

		Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName); // 快捷方式名称
		shortcutIntent.putExtra("duplicate", false); // 允许重复创建
		shortcutIntent.putExtra("android.intent.extra.shortcut.ICON", shortcutIcon); // 快捷方式图标

		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, penddingIntent);

		context.sendBroadcast(shortcutIntent);
	}
}
