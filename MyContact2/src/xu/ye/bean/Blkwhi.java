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
public class Blkwhi implements Parcelable{

	private int contactId;
	private String displayName;
	private String phoneNum;
	private String sortKey;
	private Long photoId;
	private String lookUpKey;
	private int selected = 0;
	private String formattedNumber;
	private String pinyin;
	private String blkwhi;
	private int position;
	
	public Blkwhi(){
		
	}
	
	public Blkwhi(Parcel in){
		readFromParcel(in);
	}
	
	public Blkwhi(String phoneNum, String blkwhi, int position) {
		super();
		this.phoneNum = phoneNum;
		this.blkwhi = blkwhi;
		this.position = position;
	}

	public Blkwhi(int contactId, String displayName, String phoneNum,
			String sortKey, Long photoId, String lookUpKey, int selected,
			String formattedNumber, String pinyin, String blkwhi, int position) {
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
		this.position = position;
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
		dest.writeInt(position);
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
		position = in.readInt();
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Blkwhi>() {

		@Override
		public Blkwhi createFromParcel(Parcel in) {
			return new Blkwhi(in);
		}

		@Override
		public Blkwhi[] newArray(int size) {
			return new Blkwhi[size];
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Blkwhi [contactId=" + contactId + ", displayName="
				+ displayName + ", phoneNum=" + phoneNum + ", sortKey="
				+ sortKey + ", photoId=" + photoId + ", lookUpKey=" + lookUpKey
				+ ", selected=" + selected + ", formattedNumber="
				+ formattedNumber + ", pinyin=" + pinyin + ", blkwhi=" + blkwhi
				+ ", position=" + position + "]";
	}
}
