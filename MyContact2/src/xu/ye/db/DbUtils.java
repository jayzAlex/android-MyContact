/**
 * Project Name:MyContact
 * File Name:DbUtils.java
 * Package Name:xu.ye.db
 * Date:2014-1-23上午11:27:37
 * Copyright (c) 2014, 鸿程系统 All Rights Reserved.
 *
 */

package xu.ye.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import xu.ye.R;
import xu.ye.bean.Blkwhitime;
import xu.ye.bean.ContactBean;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * ClassName:DbUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014-1-23 上午11:27:37 <br/>
 * 
 * @author jayz
 * @version
 * @since JDK 1.6
 * @see
 */
public class DbUtils {
	private Context context;
	private static String filepath;
	private static String filename;
	private static SQLiteDatabase database;

//	static {
////		FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath()
////				+ "/MyContact/DB";
//		getDiskCacheDir(context, "DB");
//		FILENAME = "contact_dict";
//	}
	
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	
	public DbUtils(Context context){
		try {
			setContext(context);
			filepath = getDiskCacheDir(context, "DB").getAbsolutePath();
			filename = "contact_dict";
			copydatabase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public SQLiteDatabase openDatabase() {

		database = null;
		try {
			File file = new File(filepath);
			if (!file.exists()) {
				file.mkdirs();
			}
			String databaseFilename = filepath + File.separator + filename;
			database = SQLiteDatabase.openOrCreateDatabase(databaseFilename,
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return database;

	}

	public SQLiteDatabase getDbInstance() {
		if (database == null) {
			syncInit();
		}
		return database;
	}

	public synchronized void syncInit() {
		if (database == null) {
			database = openDatabase();
		}
	}

	public void copydatabase() throws IOException {

		File path = new File(filepath);
		if (!path.exists()) {
			path.mkdirs();
		}
		File file = new File(filepath + File.separator + filename);
		if (!file.exists()) {
			file.createNewFile();
			OutputStream out = new FileOutputStream(file);
			InputStream inputStream = context.getResources().openRawResource(
					R.raw.contact_dict);
			byte[] buffer = new byte[1024];
			int readLen = 0;
			while ((readLen = inputStream.read(buffer)) != -1) {
				out.write(buffer, 0, readLen);
			}
			out.flush();
			inputStream.close();
			out.close();
		}
	}

	/**
	 * save:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @param phonenum
	 * @param blkwhi
	 * @param position
	 * @return
	 */
	public boolean save(String phonenum, ContactBean blkwhi) {
		try {
			
			SQLiteDatabase sdb = openDatabase();
			String sql = "select * from BLKWHI where PHONENUM = '" + phonenum + "'";
			Cursor c = sdb.rawQuery(sql, null);
//			Log.i("save", "sql" + sql);
			ContentValues values = new ContentValues();
			values.put("CONTACTID", blkwhi.getContactId());
			values.put("DISPLAYNAME", blkwhi.getDisplayName());
			values.put("PHONENUM", blkwhi.getPhoneNum());
			values.put("SORTKEY", blkwhi.getSortKey());
			values.put("PHOTOID", blkwhi.getPhotoId());
			values.put("LOOKUPKEY", blkwhi.getLookUpKey());
			values.put("SELECTED", blkwhi.getSelected());
			values.put("FORMATTEDNUMBER", blkwhi.getFormattedNumber());
			values.put("PINYIN", blkwhi.getPinyin());
			values.put("BLKWHI", blkwhi.getBlkwhi());
			values.put("POSITION", blkwhi.getId());
			values.put("BACKGROUNDCOLOR", blkwhi.getBackgroundColor());
			if (c.getCount() > 0) {
				String args[] = { phonenum };
				sdb.update("BLKWHI", values, "PHONENUM = ?", args);
//				Log.i("save", "update "+phonenum);
			} else {
				sdb.insert("BLKWHI", null, values);
//				Log.i("save", "insert "+phonenum);
			}
			c.close();
			if (sdb != null)
				sdb.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
//	public boolean updatePositionnull() {
//		try {
//			
//			SQLiteDatabase sdb = openDatabase();
//			String sql = "select * from BLKWHI";
//			Cursor c = sdb.rawQuery(sql, null);
//			Log.i("save", "sql" + sql);
//			ContentValues values = new ContentValues();
//			values.put("POSITION", "");
//			if (c.getCount() > 0) {
//				sdb.update("BLKWHI", values, null, null);
//				Log.i("save", "updatePositionnull");
//			}
//			c.close();
//			if (sdb != null)
//				sdb.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
	
	/**
	 * save:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @param phonenum
	 * @param blkwhi
	 * @param position
	 * @return
	 */
	public boolean saveQuickByPosition(int _id, ContactBean blkwhi) {
		try {
			SQLiteDatabase sdb = openDatabase();
			String sql = "select * from QUICK where _ID = '" + _id + "'";
			Cursor c = sdb.rawQuery(sql, null);
//			Log.i("save QUICK", "sql" + sql);
			ContentValues values = new ContentValues();
			values.put("CONTACTID", blkwhi.getContactId());
			values.put("DISPLAYNAME", blkwhi.getDisplayName());
			values.put("PHONENUM", blkwhi.getPhoneNum());
			values.put("SORTKEY", blkwhi.getSortKey());
			values.put("PHOTOID", blkwhi.getPhotoId());
			values.put("LOOKUPKEY", blkwhi.getLookUpKey());
			values.put("SELECTED", blkwhi.getSelected());
			values.put("FORMATTEDNUMBER", blkwhi.getFormattedNumber());
			values.put("PINYIN", blkwhi.getPinyin());
			values.put("_ID", _id);
			values.put("BACKGROUNDCOLOR", blkwhi.getBackgroundColor());
//			if (c.getCount() > 0) {
//				String args[] = { position+"" };
//				sdb.update("QUICK", values, "ID = ?", args);
////				Log.i("save", "update QUICK"+position);
//			}
			sdb.insert("QUICK", null, values);
			c.close();
			if (sdb != null)
				sdb.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public ArrayList<ContactBean> getBlkwhi(String blkwhi) {
		ContactBean bean = null;
		SQLiteDatabase sdb = openDatabase();
		ArrayList<ContactBean> list = new ArrayList<ContactBean>();
		String sql = "select * from BLKWHI where 1 = 1";
		String whereclause = "";
		if(blkwhi != null && !blkwhi.isEmpty()){
			whereclause = " and BLKWHI = '" + blkwhi + "'";
		}
		sql += whereclause;
		Cursor c = sdb.rawQuery( sql, null);
		while (c.moveToNext()) {
			bean = new ContactBean(c.getInt(c.getColumnIndex("CONTACTID")),
					c.getString(c.getColumnIndex("DISPLAYNAME")),
					c.getString(c.getColumnIndex("PHONENUM")),
					c.getString(c.getColumnIndex("SORTKEY")),
					c.getLong(c.getColumnIndex("PHOTOID")),
					c.getString(c.getColumnIndex("LOOKUPKEY")),
					c.getInt(c.getColumnIndex("SELECTED")),
					c.getString(c.getColumnIndex("FORMATTEDNUMBER")),
					c.getString(c.getColumnIndex("PINYIN")),
					c.getString(c.getColumnIndex("BLKWHI")), 
					-1);
			list.add(bean);
		}
		c.close();
		sdb.close();
		return list;
	}
	
	public boolean saveBlkwhi(ContactBean blkwhi) {
		try {
			
			SQLiteDatabase sdb = openDatabase();
			String sql = "select * from BLKWHI where PHONENUM = '" + blkwhi.getPhoneNum() + "'";
			Cursor c = sdb.rawQuery(sql, null);
			ContentValues values = new ContentValues();
			values.put("CONTACTID", blkwhi.getContactId());
			values.put("DISPLAYNAME", blkwhi.getDisplayName());
			values.put("PHONENUM", blkwhi.getPhoneNum());
			values.put("SORTKEY", blkwhi.getSortKey());
			values.put("PHOTOID", blkwhi.getPhotoId());
			values.put("LOOKUPKEY", blkwhi.getLookUpKey());
			values.put("SELECTED", blkwhi.getSelected());
			values.put("FORMATTEDNUMBER", blkwhi.getFormattedNumber());
			values.put("PINYIN", blkwhi.getPinyin());
			values.put("BLKWHI", blkwhi.getBlkwhi());
			if (c.getCount() > 0) {
				String args[] = { blkwhi.getPhoneNum()+"" };
				sdb.update("BLKWHI", values, "PHONENUM = ?", args);
			}else{
				sdb.insert("BLKWHI", null, values);
			}
			c.close();
			if (sdb != null)
				sdb.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteBlkwhi(String phonenum, String blkwhi) {
		try {
			SQLiteDatabase sdb = openDatabase();
			String sql = "select * from BLKWHI where PHONENUM = '" + phonenum + "' and BLKWHI = '" + blkwhi + "'";
			Cursor c = sdb.rawQuery(sql, null);
			if (c.getCount() > 0) {
				sdb.delete("BLKWHI", "PHONENUM = '" + phonenum + "' and BLKWHI = '" + blkwhi + "'", null);
			}
			c.close();
			if (sdb != null)
				sdb.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void deleteQuick(){
		SQLiteDatabase sdb = openDatabase();
		sdb.delete("QUICK", null, null);
		sdb.close();
	}
	
	public ArrayList<ContactBean> getQuick() {
		ContactBean bean = null;
		SQLiteDatabase sdb = openDatabase();
		ArrayList<ContactBean> list = new ArrayList<ContactBean>();
		String sql = "select * from QUICK where phonenum is not null and phonenum != '' order by _id";
		Cursor c = sdb.rawQuery( sql, null);
		while (c.moveToNext()) {
			bean = new ContactBean(c.getInt(c.getColumnIndex("CONTACTID")),
					c.getString(c.getColumnIndex("DISPLAYNAME")),
					c.getString(c.getColumnIndex("PHONENUM")),
					c.getString(c.getColumnIndex("SORTKEY")),
					c.getLong(c.getColumnIndex("PHOTOID")),
					c.getString(c.getColumnIndex("LOOKUPKEY")),
					c.getInt(c.getColumnIndex("SELECTED")),
					c.getString(c.getColumnIndex("FORMATTEDNUMBER")),
					c.getString(c.getColumnIndex("PINYIN")),
					"",
					c.getInt(c.getColumnIndex("_ID")),
					c.getInt(c.getColumnIndex("BACKGROUNDCOLOR")));
			list.add(bean);
		}
		c.close();
		sdb.close();
		return list;
	}
	
	/**
	 * save:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @param phonenum
	 * @param blkwhi
	 * @param position
	 * @return
	 */
	public boolean saveBlkwhitime(Blkwhitime blkwhitime) {
		try {
			
			SQLiteDatabase sdb = openDatabase();
			String sql = "select * from BLKWHITIME where BLKWHI = '" + blkwhitime.getBlkwhi() + "' and STS = 'Y'";
			Cursor c = sdb.rawQuery(sql, null);
			ContentValues values = new ContentValues();
			values.put("FROMTIME", blkwhitime.getFromtime());
			values.put("TOTIME", blkwhitime.getTotime());
			values.put("BLKWHI", blkwhitime.getBlkwhi());
			values.put("STS", blkwhitime.getSts());
			if (c.getCount() > 0) {
				String args[] = { blkwhitime.getBlkwhi()+"" };
				sdb.update("BLKWHITIME", values, "BLKWHI = ?", args);
			}
			c.close();
			if (sdb != null)
				sdb.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Blkwhitime getBlkwhitime(String blkwhi) {
		Blkwhitime b = null;
		SQLiteDatabase sdb = openDatabase();
		String sql = "select * from BLKWHITIME where BLKWHI = '" + blkwhi + "' and STS = 'Y'";
		Cursor c = sdb.rawQuery(sql, null);
		while (c.moveToNext()) {
			b = new Blkwhitime(c.getString(c.getColumnIndex("FROMTIME")),
					c.getString(c.getColumnIndex("TOTIME")),
					c.getString(c.getColumnIndex("BLKWHI")),
					c.getString(c.getColumnIndex("STS")));
		}
		c.close();
		sdb.close();
		return b;
	}
	
}
