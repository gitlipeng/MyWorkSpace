package com.xzlp.deskcall.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xzlp.deskcall.R;
import com.xzlp.deskcall.bean.GroupBean;

public class GroupListAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<GroupBean> list;
	private Context ctx;
	public GroupListAdapter(Context context, List<GroupBean> list) {
		this.ctx = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list; 
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void remove(int position){
		list.remove(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		convertView = inflater.inflate(R.layout.grouplist_item, null);
//		TextView mGroupName = (TextView)convertView.findViewById(R.id.groupname);
//		mGroupName.setText(list.get(position).getName());
		return convertView;
	}
	
}
