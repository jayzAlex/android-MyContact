package xu.ye.view.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import xu.ye.R;
import xu.ye.bean.ContactBean;
import xu.ye.uitl.BaseIntentUtil;
import xu.ye.view.sms.MessageBoxList;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * 自定义适配器
 * 
 * @author zhangjia
 * 
 */
public class SwitchGridAdapter extends BaseAdapter {

	private static Activity activity;
	public static ArrayList<ContactBean> list;
	private Context context;
	private View view;
	private static int isHidePosition = -1;
	public boolean isHidden;
//	private static int highlightPosition = -1;
	public static boolean isHighlight;
	private ViewHolder holder;

	public static final Random random;
	private static final String colors[] = { "#82b6cc", "#a3c9dc", "#b6d7e0",
			"#ccdeea", "#5e92aa", "#82b6cc", "#a4c9db", "#b6d7e0", "#23547f",
			"#5c92aa", "#85b5cc", "#a5cdd5", "#2f4f4f", "#23547f", "#5c92aa",
			"#85b5cc" };

	static {
		random = new Random();
	}

	public SwitchGridAdapter(Context context, ArrayList<ContactBean> list, Activity activity) {
		this.context = context;
		this.list = list;
		this.activity = activity;
	}
	
	private static class ViewHolder {
		TextView num;
		// QuickContactBadge lxr;
		ImageView lxr;
	}

	private Bitmap getThumb(String s, int color) {
		Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setTextSize(24);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawRect(new Rect(0, 0, 150, 150), paint);
		paint.setColor(Color.BLACK);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(s, 75, 75, paint);
		return bmp;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		view = convertView;
		/***
		 * 在这里尽可能每次都进行实例化新的，这样在拖拽ListView的时候不会出现错乱.
		 * 具体原因不明，不过这样经过测试，目前没有发现错乱。虽说效率不高，但是做拖拽LisView足够了。
		 */
		view = LayoutInflater.from(context).inflate(R.layout.drag_grid_item,
				null);
		holder = new ViewHolder();
		holder.num = (TextView) view.findViewById(R.id.num);
		// holder.lxr = (QuickContactBadge) view.findViewById(R.id.lxr);
		holder.lxr = (ImageView) view.findViewById(R.id.lxr);
		ContactBean cb = null;
		if (list != null && list.size() != 0) {
			cb = list.get(position);
		}
		if (cb != null) {
			holder.num.setVisibility(View.GONE);
			holder.lxr.setVisibility(View.VISIBLE);
			if (0 == cb.getPhotoId()) {
				holder.lxr.setImageBitmap(getThumb(cb.getDisplayName(),
						Color.parseColor(colors[position])));
			} else {
				Uri uri = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI,
						cb.getContactId());
				InputStream input = ContactsContract.Contacts
						.openContactPhotoInputStream(
								context.getContentResolver(), uri);
				Bitmap contactPhoto = BitmapFactory.decodeStream(input);
				holder.lxr.setImageBitmap(contactPhoto);
			}
		} else {
			holder.num.setVisibility(View.VISIBLE);
			holder.lxr.setVisibility(View.GONE);
			holder.num.setText(position + 1 + "");
			view.setBackgroundColor(Color.parseColor(colors[position]));
		}

		if (position == isHidePosition && isHidden && isHidePosition != -1) {
			view.setVisibility(View.GONE);
		} else {
			view.setVisibility(View.VISIBLE);
		}

		return view;
	}

	/***
	 * 动态修改GridView的方位.
	 * 
	 * @param start
	 *            点击移动的position
	 * @param down
	 *            松开时候的position
	 */
	public void update(int start, int down) {

		// 获取删除的东东.
		ContactBean contactBean = list.get(start);
		list.remove(start);// 删除该项
		list.add(down, contactBean);// 添加删除项
		notifyDataSetChanged();// 刷新ListView

	}

	/***
	 * 动态交换GridView的方位.
	 * 
	 * @param start
	 *            点击移动的position
	 * @param down
	 *            松开时候的position
	 */
	public void replace(int start, int down) {

		// 获取删除的东东.
		ContactBean contactBean = list.get(start);
		list.set(start, list.get(down));// 删除该项
		list.set(down, contactBean);// 添加删除项
		notifyDataSetChanged();// 刷新GridView

	}

	/***
	 * 删除GridView的指定项.
	 * 
	 * @param position
	 *            点击移动的position
	 */
	public void onDelete(int position) {

		// 获取删除的东东.
		list.set(position, null);
		Log.i("onDelete", "onDelete:" + position);
		notifyDataSetChanged();// 刷新GridView

	}

	/***
	 * 设置ContactBean的值.
	 * 
	 * @param position
	 *            修改的index
	 * @param cb
	 *            设置的参数值
	 */
	public void setCb(int position, ContactBean cb) {

		list.set(position, cb);// 设置该项
		notifyDataSetChanged();// 刷新GridView

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

	public void setIsHidePosition(int position) {
		isHidePosition = position;
		isHidden = true;
	}

	public void setHignlightPosition(int position) {
//		highlightPosition = position;
		isHighlight = true;
		notifyDataSetChanged();
	}

	public void onDial(int position) {
		ContactBean contactBean = list.get(position);
		String toPhone = contactBean.getPhoneNum();
		Uri uri = Uri.parse("tel:" + toPhone);
		Intent it = new Intent(Intent.ACTION_CALL, uri);
		activity.startActivity(it);
	}

	public void onSms(int position) {
		ContactBean contactBean = list.get(position);
		String threadId = getSMSThreadId(contactBean.getPhoneNum());
		Map<String, String> map = new HashMap<String, String>();
		map.put("phoneNumber", contactBean.getPhoneNum());
		map.put("threadId", threadId);
		BaseIntentUtil.intentSysDefault(activity, MessageBoxList.class, map);
	}

	public static String[] SMS_COLUMNS = new String[] { "thread_id" };

	private String getSMSThreadId(String adddress) {
		Cursor cursor = null;
		ContentResolver contentResolver = context.getContentResolver();
		cursor = contentResolver.query(Uri.parse("content://sms"), SMS_COLUMNS,
				" address like '%" + adddress + "%' ", null, null);
		String threadId = "";
		if (cursor == null || cursor.getCount() > 0) {
			cursor.moveToFirst();
			threadId = cursor.getString(0);
			cursor.close();
			return threadId;
		} else {
			cursor.close();
			return threadId;
		}
	}
	

}