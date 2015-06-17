package com.xzlp.deskcall;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.xzlp.deskcall.adapter.ContactListAdapter;
import com.xzlp.deskcall.adapter.GroupListAdapter;
import com.xzlp.deskcall.bean.ContactBean;
import com.xzlp.deskcall.bean.GroupBean;
import com.xzlp.deskcall.provider.PersonalContactsProvider;
import com.xzlp.deskcall.services.QuickAlphabeticBar;
import com.xzlp.deskcall.util.Constant;
import com.xzlp.deskcall.util.LogUtil;
import com.xzlp.deskcall.util.UtilTools;

import java.util.HashMap;
import java.util.List;

/**
 * 类名：PersonalContactActivity 描述：个人通讯录页面
 * 
 * @author lipeng
 */
public class MainActivity extends SuperActivity implements TextWatcher, OnClickListener {

	private TextView mTopCenterTxt;

	private ListView mContactList;

	private EditText mSearchContact;

	private ImageView mClearBtn;

	private QuickAlphabeticBar alpha;

	private PopupWindow mPopupWindow;

	private List<ContactBean> contactList;

	private List<GroupBean> groupList;

	private ContactListAdapter adapter;

	private GroupListAdapter groupListAdapter;

	private PersonalContactsProvider contactsProvider;

	// 记录最近一次ListView滑到的位置
	private int listIndex = 0;

	// 此handler用来接收数据加载完毕的通知
	@SuppressLint("HandlerLeak")
	private Handler myHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case Constant.FLAG_1:
					LogUtil.getInstance().i("接收到整理结束的通知");
					contactList = contactsProvider.getContactList();
					groupList = contactsProvider.getGroupList();
					setAdapter(contactList);
					GroupBean b = new GroupBean();
					b.setId(-1);
					b.setName(getString(R.string.contactgroupall));
					groupList.add(0, b);// 增加“全部”选项
					initPopupWindow(groupList);
					stopProgressDialog();
					break;
				default:
					break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.getInstance().i("onCreate");
		setContentView(R.layout.activity_main);

		initView();
		contactsProvider = PersonalContactsProvider.getInstance();
		contactList = contactsProvider.getContactList();
		groupList = contactsProvider.getGroupList();
		if (contactList == null || contactList.size() == 0) {
			contactsProvider.querySystemContacts(MainActivity.this, myHandler);
			showProgressDialog(R.string.lodingheadermsg, R.string.lodingmsg);
		} else {
			setAdapter(contactList);
			initPopupWindow(groupList);
		}

	}

	/**
	 * 方法名：onResume 描述：每次进入页面时，都显示最初的列表状态
	 */
	@Override
	protected void onResume() {
		LogUtil.getInstance().i("onResume");
		if (adapter != null && contactList != null) {
			mSearchContact.setText("");
			setAdapter(contactsProvider.getContactList());
			mContactList.setSelection(listIndex > 0 ? listIndex : 0);
		}
		super.onResume();
        MobclickAgent.onResume(this);
	}

	/**
	 * 方法名：initView 描述：初始化页面控件
	 */
	private void initView() {
		// mTopCenterTxt = (TextView) findViewById(R.id.top_title);
		// mTopCenterTxt.setText(R.string.personalcontact_title);
		// mTopCenterTxt.setCompoundDrawablesWithIntrinsicBounds(null, null,
		// getResources().getDrawable(R.drawable.arrow_down),null);

		mSearchContact = (EditText) findViewById(R.id.searchContact);
		mSearchContact.addTextChangedListener(this);

		mClearBtn = (ImageView) findViewById(R.id.btn_clearInput);
		mClearBtn.setOnClickListener(this);

		alpha = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);

		mContactList = (ListView) findViewById(R.id.contactlist);
		mContactList.setFastScrollEnabled(false);
		mContactList.setSelection(listIndex > 0 ? listIndex : 0);
		mContactList.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				UtilTools.hideSoft(MainActivity.this, view);// 滑动时隐藏软键盘
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					listIndex = mContactList.getFirstVisiblePosition();
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		mContactList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ContactBean cb = (ContactBean) adapter.getItem(position);
				if (cb.getPhoneNum() != null && !"".equals(cb.getPhoneNum())) {
					UtilTools.callContact(MainActivity.this, cb.getPhoneNum());
				}
			}
		});
	}

	@Override
	protected void onPause() {
		LogUtil.getInstance().i("onPause");
		super.onPause();
        MobclickAgent.onPause(this);
	}

	/**
	 * 方法名：setAdapter 描述：设置listview的adapter
	 */
	private void setAdapter(List<ContactBean> list) {
		if (list == null)
			return;
		/* 设置右侧字母下拉列表 start */
		HashMap<String, Integer> alphaIndexer;
		alphaIndexer = new HashMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			String name = UtilTools.getAlpha(list.get(i).getSortKey());
			if (!alphaIndexer.containsKey(name)) {
				alphaIndexer.put(name, i);
			}
		}
		alpha.setAlphaIndexer(alphaIndexer);
		/* 设置右侧字母下拉列表 end */

		adapter = new ContactListAdapter(this, list);
		mContactList.setAdapter(adapter);

		alpha.init(this);
		alpha.setListView(mContactList);
		alpha.setHight(alpha.getHeight());
		alpha.setVisibility(View.VISIBLE);
	}

	/**
	 * 方法名：onTextChanged 描述：搜索框发生变化之后，触发此方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (contactList != null && contactList.size() > 0) {
			String searchContent = s.toString().toLowerCase();
			if (searchContent.length() > 0) {
				List<ContactBean> searchList = contactsProvider.searchContacts(searchContent);
				setAdapter(searchList);
			} else {
				setAdapter(contactList);
			}
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	public void afterTextChanged(Editable s) {
	}

	/**
	 * 方法名：initPopupWindow 描述：初始化popupwindow,显示联系人分组
	 */
	private void initPopupWindow(List<GroupBean> list) {
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int height = getWindowManager().getDefaultDisplay().getHeight();
		LogUtil.getInstance().i("屏幕宽度：" + width);
		// View view =
		// this.getLayoutInflater().inflate(R.layout.personalcontact_popup_window,
		// null);
		//
		// ListView mListView = (ListView) view.findViewById(R.id.grouplist);
		// groupListAdapter = new GroupListAdapter(this, list);
		// mListView.setAdapter(groupListAdapter);
		//
		// mPopupWindow = new PopupWindow(view, width*2/5,height/2);
		// mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		// mPopupWindow.setOutsideTouchable(true);
		// mPopupWindow.setFocusable(true);
		//
		// mListView.setOnItemClickListener(new
		// AdapterView.OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// mPopupWindow.dismiss();
		// GroupBean bean = (GroupBean)groupListAdapter.getItem(position);
		// if(bean.getId() == -1){
		// setAdapter(contactList);
		// mTopCenterTxt.setText(R.string.personalcontact_title);
		// }else{
		// List<ContactBean> list =
		// contactsProvider.queryGroupMember(MainActivity.this,bean);
		// setAdapter(list);
		// mTopCenterTxt.setText(bean.getName());
		// }
		// }
		// });
		//
		// mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
		// {
		// @Override
		// public void onDismiss() {
		// mTopCenterTxt.setCompoundDrawablesWithIntrinsicBounds(null, null,
		// getResources().getDrawable(R.drawable.arrow_down),null);
		// }
		// });
		//
		// view.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// mPopupWindow.dismiss();
		// }
		// });
		//
		// mTopCenterTxt.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		//
		// if(!mPopupWindow.isShowing()){
		// int x = mTopCenterTxt.getWidth()/2 - mPopupWindow.getWidth()/2;
		// mPopupWindow.showAsDropDown(mTopCenterTxt,x,0);
		// mTopCenterTxt.setCompoundDrawablesWithIntrinsicBounds(null, null,
		// getResources().getDrawable(R.drawable.arrow_up),null);
		// }else {
		// mPopupWindow.dismiss();
		// }
		// }
		// });
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mClearBtn)) {
			if (mSearchContact.getText().toString().length() > 0)
				mSearchContact.setText("");
		}
	}


	@Override
	protected void onStart() {
		LogUtil.getInstance().i("onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		LogUtil.getInstance().i("onStop");
		super.onStop();
	};

	@Override
	protected void onDestroy() {
		LogUtil.getInstance().i("onDestroy");
		super.onDestroy();
	}
}
