package com.xzlp.deskcall.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ContactBean implements Parcelable {
	/** Parcelable.Creator needs by Android. */
	public static final Creator<ContactBean> CREATOR = new Creator<ContactBean>() {
		@Override
		public ContactBean createFromParcel(Parcel source) {
			return new ContactBean(source);
		}

		@Override
		public ContactBean[] newArray(int size) {
			return new ContactBean[size];
		}
	};

	private int contactId;// 联系人ID
	private String displayName;// 联系人姓名
	private String phoneNum;// 联系人电话
	private Long photoId;// 头像ID
	private String formattedNumber;// 汉语名字对应的12键盘上的数字，如白洁：对应的是25
	private String nameLetter;// 名称的汉语拼音
	private String nameIndex;// 名称的首字母
	private String sortKey;//联系人数据库中存放的排序字段 陈强：CHEN 陈 QIANG 强
	
	
	private String address;
	private String email;
	private Bitmap photo;
	
    public Bitmap getPhoto() {
    	return photo;
    }
	
    public void setPhoto(Bitmap photo) {
    	this.photo = photo;
    }
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ContactBean() {
		// TODO Auto-generated constructor stub
	}
	public ContactBean(final Parcel source) {
		 source.readInt();
		 source.readString();
		 source.readString();
		 source.readLong();
		 source.readString();
		 source.readString();
		 source.readString();
		 source.readString();
	 }
	 
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(contactId);
		dest.writeString(displayName);
		dest.writeString(phoneNum);
		dest.writeLong(photoId);
		dest.writeString(formattedNumber);
		dest.writeString(nameLetter);
		dest.writeString(nameIndex);
		dest.writeString(sortKey);
	}

	public String getNameIndex() {
		return nameIndex;
	}

	public void setNameIndex(String nameIndex) {
		this.nameIndex = nameIndex;
	}

	public String getSortKey() {
		return sortKey;
	}
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Long getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public String getFormattedNumber() {
		return formattedNumber;
	}

	public void setFormattedNumber(String formattedNumber) {
		this.formattedNumber = formattedNumber;
	}

	public String getNameLetter() {
		return nameLetter;
	}

	public void setNameLetter(String nameLetter) {
		this.nameLetter = nameLetter;
	}

}
