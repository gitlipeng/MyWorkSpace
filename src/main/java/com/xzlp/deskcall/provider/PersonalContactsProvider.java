package com.xzlp.deskcall.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.Groups;
import android.util.Log;

import com.xzlp.deskcall.bean.ContactBean;
import com.xzlp.deskcall.bean.GroupBean;
import com.xzlp.deskcall.inter.QueryContactOverListener;
import com.xzlp.deskcall.services.MyAsyncQueryHandler;
import com.xzlp.deskcall.util.Constant;
import com.xzlp.deskcall.util.ToPinYin;
import com.xzlp.deskcall.util.UtilTools;

/**
 * 类名：PersonalContactsProvider
 * 
 * 描述：个人通讯录数据提供类
 * 
 * @author lipeng
 *
 */
public class PersonalContactsProvider implements QueryContactOverListener{
	private static final String TAG = "ContactsProvider";
	private static PersonalContactsProvider contactsProvider;
	private AsyncQueryHandler asyncQuery;
	private ArrayList<ContactBean> contactList = new ArrayList<ContactBean>();
	private ArrayList<GroupBean> groupList;
	private Handler handler;
	private Context context;
	private long start;
	private long end;
	public synchronized static PersonalContactsProvider getInstance(){
		if(contactsProvider == null){
			contactsProvider = new PersonalContactsProvider();
		}
		return contactsProvider;
	}
	private PersonalContactsProvider(){};
	
	/**
	 * 方法名：queryGroup
	 * 
	 * 描述：查询系统联系人分组
	 * 
	 * 
	 */
	private synchronized void queryGroup(Context context){
		groupList=new ArrayList<GroupBean>();
		Cursor cur = context.getContentResolver().query(Groups.CONTENT_URI, null, null, null, null); 
		for (cur.moveToFirst();!(cur.isAfterLast());cur.moveToNext()) {
			if(null!=cur.getString(cur.getColumnIndex(Groups.TITLE))&&(!"".equals(cur.getString(cur.getColumnIndex(Groups.TITLE))))){
				GroupBean bean=new GroupBean();
				String gTitle = cur.getString(cur.getColumnIndex(Groups.TITLE));
				if (gTitle.contains("Group:")) {
                    gTitle = gTitle.substring(gTitle.indexOf("Group:") + 6).trim();
				}
				if (gTitle.contains("Favorite_")) {
				    gTitle = "Favorites";
				}
				if (gTitle.contains("Starred in Android")) {
					gTitle = "关注";
				}
				bean.setId(cur.getInt(cur.getColumnIndex(Groups._ID)));
				bean.setName(gTitle);
				groupList.add(bean);
			}
		}   
		cur.close();
	}
	/**
	 * 方法名：queryGroupMember
	 * 
	 * 描述：找到某个分组下的联系人
	 * 
	 * 
	 * @param context
	 * @param gb
	 */
	public synchronized List<ContactBean> queryGroupMember(Context context,GroupBean gb){
		String[] RAW_PROJECTION = new String[]{ContactsContract.Data.RAW_CONTACT_ID};  
		Cursor cur = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,RAW_PROJECTION,  
				ContactsContract.Data.MIMETYPE+" = '"+GroupMembership.CONTENT_ITEM_TYPE  
				+"' AND "+ContactsContract.Data.DATA1+"="+ gb.getId(),     
				null,"data1 asc"); 
		StringBuilder inSelectionBff = new StringBuilder().append(ContactsContract.RawContacts._ID + " IN ( 0");
		while(cur.moveToNext()){
			inSelectionBff.append(',').append(cur.getLong(0));
		}
		cur.close();	
		inSelectionBff.append(')');
		Cursor contactIdCursor =  context.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI,  
				new String[] { ContactsContract.RawContacts.CONTACT_ID }, inSelectionBff.toString(), null, ContactsContract.Contacts.DISPLAY_NAME+"  COLLATE LOCALIZED asc "); 
		//存放本组所有的联系人ID
		List<ContactBean> list = new ArrayList<ContactBean>();
		while (contactIdCursor.moveToNext()) {  
			int contactId = contactIdCursor.getInt(0);
			for(ContactBean bean :contactList){
				if(bean.getContactId() == contactId){
					list.add(bean);
				}
			}
		}  
		return list;
	}
	
	/**
	 * 方法名：querySystemContacts
	 * 
	 * 描述：查询系统联系人
	 * 
	 * 
	 * @param context
	 * @param handler
	 */
	public void querySystemContacts(Context context,Handler handler){
		Log.i(TAG, "-----------querySystemContacts---------------");
		this.handler = handler;
		this.context = context;
		start = System.currentTimeMillis();
		//查询分组
		queryGroup(context);
		asyncQuery = new MyAsyncQueryHandler(context.getContentResolver(),this);
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
								ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
								ContactsContract.CommonDataKinds.Phone.DATA1,
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
								ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
								"sort_key"
							  };
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");
	}
	
	
	
	/**
	 * 方法名：onQueryComplete
	 * 
	 * 描述：   实现QueryContactOverListener接口的方法
	 *		查询结束后的回调方法，此方法中对查询的数据进行整理
	 * 
	 * 
	 */
	@Override
	public void onQueryComplete(final int token,final Object cookie, final Cursor cursor) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				treatData(token,cookie,cursor);
			}
		}).start();
	}

	/**
	 * 方法名：treatData
	 * 
	 * 描述：   此方法中对查询的数据进行整理
	 * 
	 * 
	 */
	private synchronized void treatData(int token, Object cookie, Cursor cursor){
		ArrayList<ContactBean> list = new ArrayList<ContactBean>();
		Map<Integer,String> map = new HashMap<Integer, String>();
		if (cursor != null && cursor.getCount() > 0) {
			try {
				for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
					String _id = cursor.getString(0);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					int contactId = cursor.getInt(3);
					Long photoId = cursor.getLong(4);
					String sortKey = cursor.getString(5);
					if(map.get(contactId) != null){
						continue;
					}else{
						map.put(contactId, name);
					}
					ContactBean cb = new ContactBean();
					cb.setDisplayName(name); 
					number = number.replaceAll(" ", "");
					number = number.replaceAll("-", "");
					cb.setPhoneNum(number);
					cb.setContactId(contactId);
					cb.setPhotoId(photoId);
					if (cb.getDisplayName() == null) {
						cb.setDisplayName(cb.getPhoneNum());
					}
					if(UtilTools.getSDKVersionNumber() >= 18){
						//当前为android4.3以上系统，联系人数据库已经改变，不能用sortkey来取首字母
						String[] array = ToPinYin.getNameIndex(cb.getDisplayName());
						cb.setNameIndex(array[0]);
						cb.setNameLetter(array[1]);
						cb.setSortKey(cb.getNameIndex());
					}else{
						cb.setSortKey(sortKey);
					}
					list.add(cb);
				}
				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map = null;
		Message msg = myHandler.obtainMessage();
		msg.what = Constant.FLAG_PROCESSOVER;
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("list", list);
		msg.setData(bundle);
		myHandler.sendMessage(msg);
	}
	
	//子线程组装完数据之后，传递给主线程，以防数据源在子线程中被更改造成BUG
	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.FLAG_PROCESSOVER:
				Bundle bundle = msg.getData();
				contactList = bundle.getParcelableArrayList("list");
				//通知监听的handler数据已经加载完毕,可以获取数据
				end = System.currentTimeMillis();
				if(handler != null){
					handler.sendMessage(handler.obtainMessage(Constant.FLAG_1));
				}
				break;
			default:
				break;
			}
		};
	};
	
	/**
	 * 方法名：searchContacts
	 * 
	 * 描述：   搜索联系人,多种匹配
	 * 
	 * @param searchContent
	 * @return
	 */
	public List<ContactBean> searchContacts(String searchContent){
		List<ContactBean> searchList = new ArrayList<ContactBean>();
		try {
			start = System.currentTimeMillis();
			for(ContactBean cb : contactList){
				if(cb.getNameIndex() == null || cb.getNameLetter() == null){
					//将联系人的名称换成字母拼音
					String[] array = ToPinYin.getNameIndex(cb.getDisplayName());
					cb.setNameIndex(array[0]);
					cb.setNameLetter(array[1]);
				}
				searchContent = searchContent.toLowerCase(Locale.CHINA);
				//搜索框搜索联系人
				if(cb.getPhoneNum().toLowerCase(Locale.CHINA).indexOf(searchContent) >= 0 
						|| cb.getDisplayName().toLowerCase(Locale.CHINA).indexOf(searchContent) >= 0 
						|| cb.getNameLetter().toLowerCase(Locale.CHINA).indexOf(searchContent) >= 0
						|| cb.getNameIndex().toLowerCase(Locale.CHINA).indexOf(searchContent) >= 0){
					searchList.add(cb);
				}
				
			}
			end = System.currentTimeMillis();
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchList;
	}
	
	public ArrayList<ContactBean> getContactList() {
		return contactList;
	}
	public ArrayList<GroupBean> getGroupList() {
		return groupList;
	}
	
}
