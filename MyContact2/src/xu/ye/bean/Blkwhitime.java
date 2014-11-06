/**
 * Project Name:MyContact
 * File Name:Blkwhitime.java
 * Package Name:xu.ye.bean
 * Date:2014-1-26下午4:57:45
 * Copyright (c) 2014, 鸿程系统 All Rights Reserved.
 *
*/

package xu.ye.bean;
/**
 * ClassName:Blkwhitime <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2014-1-26 下午4:57:45 <br/>
 * @author   zja
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class Blkwhitime {
	private String fromtime;
	private String totime;
	private String blkwhi;
	private String sts;
	public String getFromtime() {
		return fromtime;
	}
	public void setFromtime(String fromtime) {
		this.fromtime = fromtime;
	}
	public String getTotime() {
		return totime;
	}
	public void setTotime(String totime) {
		this.totime = totime;
	}
	public String getBlkwhi() {
		return blkwhi;
	}
	public void setBlkwhi(String blkwhi) {
		this.blkwhi = blkwhi;
	}
	public String getSts() {
		return sts;
	}
	public void setSts(String sts) {
		this.sts = sts;
	}
	@Override
	public String toString() {
		return "Blkwhitime [fromtime=" + fromtime + ", totime=" + totime
				+ ", blkwhi=" + blkwhi + ", sts=" + sts + "]";
	}
	public Blkwhitime(String fromtime, String totime, String blkwhi, String sts) {
		super();
		this.fromtime = fromtime;
		this.totime = totime;
		this.blkwhi = blkwhi;
		this.sts = sts;
	}
	public Blkwhitime() {
		super();
	}
}

