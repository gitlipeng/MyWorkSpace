package com.xzlp.deskcall.services;


import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.xzlp.deskcall.inter.QueryContactOverListener;
/**
 * 类名：MyAsyncQueryHandler
 * 
 * 描述：异步查询类
 * 
 * @author lipeng
 *
 */
public class MyAsyncQueryHandler extends AsyncQueryHandler {
	private QueryContactOverListener listener;
	public MyAsyncQueryHandler(ContentResolver cr,QueryContactOverListener listener) {
		super(cr);
		this.listener = listener;
	}
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		listener.onQueryComplete(token, cookie, cursor);
	}
}
