package com.xzlp.deskcall.inter;

import android.database.Cursor;

public interface QueryContactOverListener {
	void onQueryComplete(int token, Object cookie, Cursor cursor);
}
