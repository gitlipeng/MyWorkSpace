package com.xzlp.deskcall.adapter;

import java.io.InputStream;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xzlp.deskcall.MainActivity;
import com.xzlp.deskcall.R;
import com.xzlp.deskcall.ShortcutActivity;
import com.xzlp.deskcall.bean.ContactBean;
import com.xzlp.deskcall.util.UtilTools;

/**
 * 类名：ContactListAdapter 描述：个人通讯录listview适配
 * 
 * @author lipeng
 */
public class ContactListAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	private List<ContactBean> list;

	private Context ctx;

	public ContactListAdapter(Context context, List<ContactBean> list) {
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

	public void remove(int position) {
		list.remove(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact_list_item, null);
			holder = new ViewHolder();
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.qcb = ( Button) convertView.findViewById(R.id.qcb);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.number = (TextView) convertView.findViewById(R.id.number);
			holder.contactlayout = (LinearLayout) convertView.findViewById(R.id.contactlayout);
			holder.detaillayout = (LinearLayout) convertView.findViewById(R.id.details_layout);
			holder.contact_detials_button = (ImageView) convertView.findViewById(R.id.contact_detials_button);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ContactBean cb = list.get(position);
		String name = cb.getDisplayName();
		String number = cb.getPhoneNum();
		holder.name.setText(name);
		holder.number.setText(number); 

		String currentStr = UtilTools.getAlpha(cb.getSortKey());
		String previewStr = (position - 1) >= 0 ? UtilTools.getAlpha(list.get(position - 1).getSortKey()) : " ";
		if (!previewStr.equals(currentStr)) {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		holder.contactlayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ContactBean cb = (ContactBean) getItem(position);
				if (cb.getPhoneNum() != null && !"".equals(cb.getPhoneNum())) {
					UtilTools.callContact(ctx, cb.getPhoneNum());
				}
			}
		});

		if (0 == cb.getPhotoId()) {
			String showName = name.subSequence(name.length()-1,name.length()).toString();
			int resourceId = ctx.getResources().getIdentifier("graffiti_color_" + (position % 4 + 1), "drawable", "com.xzlp.deskcall");
			holder.qcb.setText(showName);
			holder.qcb.setBackgroundResource(resourceId);
		} else {
			Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, cb.getContactId());
			InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(), uri);
			Bitmap contactPhoto = BitmapFactory.decodeStream(input);
			holder.qcb.setText("");
			holder.qcb.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(), contactPhoto));
			cb.setPhoto(contactPhoto);
		}

		holder.contact_detials_button.setVisibility(View.VISIBLE);
		holder.detaillayout.setVisibility(View.VISIBLE);
		holder.detaillayout.setOnClickListener(new MyListener(ctx, cb,position));

		return convertView;
	}

	private static class ViewHolder {

		TextView alpha;

		Button qcb;
		
		LinearLayout contactlayout;

		TextView name;

		TextView number;

		ImageView contact_detials_button;

		LinearLayout detaillayout;
	}

	private class MyListener implements OnClickListener {

		private ContactBean contact;

		private Context context;
		
		private int position;

		public MyListener(Context context, ContactBean contact,int position) {
			this.contact = contact;
			this.context = context;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// 获得一个启动ShortcutActivity的intent
			if(contact.getPhotoId() == 0){
				String name = contact.getDisplayName();
				String showName = name.subSequence(name.length()-1,name.length()).toString();
				int resourceId = ctx.getResources().getIdentifier("graffiti_color_" + (position % 4 + 1), "drawable", "com.xzlp.deskcall");
				Bitmap imgMarker = BitmapFactory.decodeResource(ctx.getResources(),resourceId);
				contact.setPhoto(createDrawable(imgMarker,showName));
			}
			Intent penddingIntent = ShortcutActivity.getIntentCanLaunchThisComponent(contact.getDisplayName(),contact.getPhoneNum(), context, ShortcutActivity.class);
			UtilTools.createShortcut(context, contact.getDisplayName(), contact.getPhoto(), penddingIntent);
		}
	}
	

    // 穿件带字母的标记图片  
    private Bitmap createDrawable(Bitmap photo,String letter) {  
    	int width = 130, hight = 130;
		System.out.println("宽" + width + "高" + hight);
		Bitmap icon = Bitmap
				.createBitmap(width, hight, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap
		Canvas canvas = new Canvas(icon);// 初始化画布绘制的图像到icon上

		Paint photoPaint = new Paint(); // 建立画笔
		photoPaint.setDither(true); // 获取跟清晰的图像采样
		photoPaint.setFilterBitmap(true);// 过滤一些

		Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());// 创建一个指定的新矩形的坐标
		Rect dst = new Rect(0, 0, width, hight);// 创建一个指定的新矩形的坐标
		canvas.drawBitmap(photo, src, dst, photoPaint);// 将photo 缩放或则扩大到
														// dst使用的填充区photoPaint

		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
		textPaint.setTextSize(60.0f);// 字体大小
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
		textPaint.setColor(Color.WHITE);// 采用的颜色
		// textPaint.setShadowLayer(3f, 1,
		// 1,this.getResources().getColor(android.R.color.background_dark));//影音的设置
		FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
		// 转载请注明出处：http://blog.csdn.net/hursing
		int baseline = dst.top
				+ (dst.bottom - dst.top - fontMetrics.bottom + fontMetrics.top)
				/ 2 - fontMetrics.top;
		// 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
		textPaint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(letter, dst.centerX(), baseline, textPaint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return icon;
  
    }  
}
