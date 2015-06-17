package com.xzlp.deskcall.util;


public class Constant {
	public static final String SP_NAME = "icontact";
	public static final String CONTACT_UPDATE = "com.pansoft.contactupdate";//联系人变化MyBroadcastReceiver过滤
	public static final String CALLHISTORY_UPDATE = "com.pansoft.callhistoryupdate";//联系人变化MyBroadcastReceiver过滤
	public static final int FLAG_1 = 1;//handler判断是否有联系人发生变化
	public static final int FLAG_2 = 2;//handler判断是否有通话记录发生变化
	public static final int FLAG_PROCESSOVER = 3;//handler判断是否有通话记录发生变化
	public static final String ISFIRSTLOGIN = "isfirstlogin";
	public static final String PHOTO_TEMPORARY = "_1";//临时头像存储，
	public static final int UPDATEGROUP_SUCCESS = 1;//通知handler同步组织结构完毕
	public static final int UPDATEGROUP_FAIL = 2;//通知handler同步组织结构完毕出错
	public static final int UPDATECONTACT_SUCCESS = 3;//通知handler同步联系人完毕
	public static final int UPDATECONTACT_FAIL = 4;//通知handler同步联系人完毕出错
	public static final int LOADDATA_OVER = 5;//onCreate是加载数据成功后通知页面
	public static final String CONTACT_ID = "contactId";
	/** 正在加载本地数据或同步状态 */
	public static final int LOADING = 1;
	/** 正常状态 即当前用户可以下拉刷新或点击右上角的同步按钮 */
	public static final int NORMOL = 0;
	
	public static final String MALE = "M";//性别男
	public static final String FEMALE = "F";//性别女
	
	public static final String SHOWPHOTO_FLAG = "showphotodefault";//性别女

	public static final String DEFAULTPHOTO_YES = "true";//使用标准的头像
	public static final String DEFAULTPHOTO_NO = "false";//使用标准的头像
	
	public static final String SHOWCALLFRAME_YES = "0";//来电显示悬浮窗
	public static final String SHOWCALLFRAME_NO = "1";//来电不显示悬浮窗
	
	
//	public static final String CLOUD_BACKUP_FILE_NAME =  FileInAndOutPut.mainPath + FileInAndOutPut.sign + "contacts_backup_temp.vcf";
	//public static final String LOCAL_BACKUP_FILE_NAME = FileInAndOutPut.pPath +FileInAndOutPut.sign + "backup.vcf";
	
	
}