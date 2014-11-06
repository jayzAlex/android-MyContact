//package xu.ye.bean;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
///**
// * ClassName: ContactBean <br/>
// * Function: A basic object that can be parcelled to transfer between objects <br/>
// * Reason: TODO ADD REASON(可选). <br/>
// * date: 2014-1-17 上午9:59:12 <br/>
// *
// * @author jayz
// * @version 
// */
//public class ContactBean implements Parcelable{
//
//	private int contactId;
//	private String displayName;
//	private String phoneNum;
//	private String sortKey;
//	private Long photoId;
//	private String lookUpKey;
//	private int selected = 0;
//	private String formattedNumber;
//	private String pinyin;
//	
//	public ContactBean(){
//		
//	}
//	
//	public ContactBean(Parcel in){
//		readFromParcel(in);
//	}
//	
//	@Override
//	public int describeContents() {
//		
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flag) {
//		dest.writeInt(contactId); 
//		dest.writeString(displayName); 
//		dest.writeString(phoneNum); 
//		dest.writeString(sortKey); 
//		dest.writeLong(photoId); 
//		dest.writeString(lookUpKey); 
//		dest.writeInt(selected); 
//		dest.writeString(formattedNumber);
//		dest.writeString(pinyin);
//	}
//	
//	private void readFromParcel(Parcel in){
//		contactId = in.readInt();
//		displayName = in.readString();
//		phoneNum = in.readString();
//		sortKey = in.readString();
//		photoId = in.readLong();
//		lookUpKey = in.readString();
//		selected = in.readInt();
//		formattedNumber = in.readString();
//		pinyin = in.readString();
//	}
//	
//	public static final Parcelable.Creator CREATOR = new Parcelable.Creator<ContactBean>() {
//
//		@Override
//		public ContactBean createFromParcel(Parcel in) {
//			return new ContactBean(in);
//		}
//
//		@Override
//		public ContactBean[] newArray(int size) {
//			return new ContactBean[size];
//		}
//		
//	};
//
//	public int getContactId() {
//		return contactId;
//	}
//	public void setContactId(int contactId) {
//		this.contactId = contactId;
//	}
//	public String getDisplayName() {
//		return displayName;
//	}
//	public void setDisplayName(String displayName) {
//		this.displayName = displayName;
//	}
//	public String getPhoneNum() {
//		return phoneNum;
//	}
//	public void setPhoneNum(String phoneNum) {
//		this.phoneNum = phoneNum;
//	}
//	public String getSortKey() {
//		return sortKey;
//	}
//	public void setSortKey(String sortKey) {
//		this.sortKey = sortKey;
//	}
//	public Long getPhotoId() {
//		return photoId;
//	}
//	public void setPhotoId(Long photoId) {
//		this.photoId = photoId;
//	}
//	public String getLookUpKey() {
//		return lookUpKey;
//	}
//	public void setLookUpKey(String lookUpKey) {
//		this.lookUpKey = lookUpKey;
//	}
//	public int getSelected() {
//		return selected;
//	}
//	public void setSelected(int selected) {
//		this.selected = selected;
//	}
//	public String getFormattedNumber() {
//		return formattedNumber;
//	}
//	public void setFormattedNumber(String formattedNumber) {
//		this.formattedNumber = formattedNumber;
//	}
//	public String getPinyin() {
//		return pinyin;
//	}
//	public void setPinyin(String pinyin) {
//		this.pinyin = pinyin;
//	}
//	@Override
//	public String toString() {
//		return "ContactBean [contactId=" + contactId + ", displayName="
//				+ displayName + ", phoneNum=" + phoneNum + ", sortKey="
//				+ sortKey + ", photoId=" + photoId + ", lookUpKey=" + lookUpKey
//				+ ", selected=" + selected + ", formattedNumber="
//				+ formattedNumber + ", pinyin=" + pinyin + "]";
//	}
//}
package xu.ye.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ClassName: ContactBean <br/>
 * Function: A basic object that can be parcelled to transfer between objects <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2014-1-17 上午9:59:12 <br/>
 *
 * @author jayz
 * @version 
 */
public class ContactBean implements Parcelable{

	private int contactId;
	private String displayName;
	private String phoneNum;
	private String sortKey;
	private Long photoId = 0l;
	private String lookUpKey;
	private int selected = 0;
	private String formattedNumber;
	private String pinyin;
	private String blkwhi;
	private int _id = -1;
	private int backgroundColor;
	
	public ContactBean(){
		
	}
	
	public ContactBean(Parcel in){
		readFromParcel(in);
	}
	
	public ContactBean(String phoneNum, String blkwhi, int _id) {
		super();
		this.phoneNum = phoneNum;
		this.blkwhi = blkwhi;
		this._id = _id;
	}

	public ContactBean(int contactId, String displayName, String phoneNum,
			String sortKey, Long photoId, String lookUpKey, int selected,
			String formattedNumber, String pinyin, String blkwhi, int _id, int backgroundColor) {
		super();
		this.contactId = contactId;
		this.displayName = displayName;
		this.phoneNum = phoneNum;
		this.sortKey = sortKey;
		this.photoId = photoId;
		this.lookUpKey = lookUpKey;
		this.selected = selected;
		this.formattedNumber = formattedNumber;
		this.pinyin = pinyin;
		this.blkwhi = blkwhi;
		this._id = _id;
		this.backgroundColor = backgroundColor;
	}
	
	public ContactBean(int contactId, String displayName, String phoneNum,
			String sortKey, Long photoId, String lookUpKey, int selected,
			String formattedNumber, String pinyin, String blkwhi, int _id) {
		super();
		this.contactId = contactId;
		this.displayName = displayName;
		this.phoneNum = phoneNum;
		this.sortKey = sortKey;
		this.photoId = photoId;
		this.lookUpKey = lookUpKey;
		this.selected = selected;
		this.formattedNumber = formattedNumber;
		this.pinyin = pinyin;
		this.blkwhi = blkwhi;
		this._id = _id;
	}	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		dest.writeInt(contactId); 
		dest.writeString(displayName); 
		dest.writeString(phoneNum); 
		dest.writeString(sortKey); 
		dest.writeLong(photoId); 
		dest.writeString(lookUpKey); 
		dest.writeInt(selected); 
		dest.writeString(formattedNumber);
		dest.writeString(pinyin);
		dest.writeString(blkwhi);
		dest.writeInt(_id);
		dest.writeInt(backgroundColor);
	}
	
	private void readFromParcel(Parcel in){
		contactId = in.readInt();
		displayName = in.readString();
		phoneNum = in.readString();
		sortKey = in.readString();
		photoId = in.readLong();
		lookUpKey = in.readString();
		selected = in.readInt();
		formattedNumber = in.readString();
		pinyin = in.readString();
		blkwhi = in.readString();
		_id = in.readInt();
		backgroundColor = in.readInt();
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator<ContactBean>() {

		@Override
		public ContactBean createFromParcel(Parcel in) {
			return new ContactBean(in);
		}

		@Override
		public ContactBean[] newArray(int size) {
			return new ContactBean[size];
		}
		
	};

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
	public String getSortKey() {
		return sortKey;
	}
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	public Long getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}
	public String getLookUpKey() {
		return lookUpKey;
	}
	public void setLookUpKey(String lookUpKey) {
		this.lookUpKey = lookUpKey;
	}
	public int getSelected() {
		return selected;
	}
	public void setSelected(int selected) {
		this.selected = selected;
	}
	public String getFormattedNumber() {
		return formattedNumber;
	}
	public void setFormattedNumber(String formattedNumber) {
		this.formattedNumber = formattedNumber;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	public String getBlkwhi() {
		return blkwhi;
	}

	public void setBlkwhi(String blkwhi) {
		this.blkwhi = blkwhi;
	}

	public int getId() {
		return _id;
	}

	public void setId(int _id) {
		this._id = _id;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@Override
	public String toString() {
		return "Blkwhi [contactId=" + contactId + ", displayName="
				+ displayName + ", phoneNum=" + phoneNum + ", sortKey="
				+ sortKey + ", photoId=" + photoId + ", lookUpKey=" + lookUpKey
				+ ", selected=" + selected + ", formattedNumber="
				+ formattedNumber + ", pinyin=" + pinyin + ", blkwhi=" + blkwhi
				+ ", _id=" + _id + ", backgroundColor=" + backgroundColor +"]";
	}
}

