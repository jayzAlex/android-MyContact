package xu.ye.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import xu.ye.R;
import xu.ye.bean.ContactBean;
import xu.ye.db.DbUtils;
import xu.ye.uitl.BaseIntentUtil;
import xu.ye.view.sms.MessageBoxList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.u1aryz.android.lib.newpopupmenu.MenuItem;
import com.u1aryz.android.lib.newpopupmenu.PopupMenu;
import com.u1aryz.android.lib.newpopupmenu.PopupMenu.OnItemSelectedListener;
import com.way.view.CircularImage;
import com.way.view.DragGridView;
import com.way.view.DragGridView.OnRearrangeListener;
import com.zf.iosdialog.widget.ActionSheetDialog;
import com.zf.iosdialog.widget.ActionSheetDialog.OnSheetItemClickListener;
import com.zf.iosdialog.widget.ActionSheetDialog.SheetItemColor;

/**
 * MainActivity
 * 
 * @author way
 * 
 */
public class HomeFastDialActivity extends Activity {
	private Random random = new Random();
	DragGridView mDragGridView;
	private Context context;
	private ArrayList<ContactBean> quickList;
	private Button addContactBtn;
	private DbUtils dbUtils;
	private int selectedContactBeanIndex;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_fastdial_page);
		mDragGridView = ((DragGridView) findViewById(R.id.vgv));
		context = getApplicationContext();
		addContactBtn = (Button) findViewById(R.id.addContactBtn);
		dbUtils = new DbUtils(context);
		quickList = dbUtils.getQuick();
		initQuickViews();
		setListeners();
	}
	
	/**
	 * 初始化一键拨号联系人列表
	 */	
	public void initQuickViews(){
		for (int i = 0; i < quickList.size(); i++){
			ContactBean cb = quickList.get(i);
			if (cb != null) {
				CircularImage view = new CircularImage(this);
				view.setImageBitmap(contactBean2Bitmap(cb));
				mDragGridView.addView(view);
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void setListeners() {
		mDragGridView.setOnRearrangeListener(new OnRearrangeListener() {
			public void onRearrange(int oldIndex, int newIndex) {
				ContactBean cb = quickList.remove(oldIndex);
				quickList.add(newIndex, cb);
			}
		});
		mDragGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i,
					long arg3) {
				if(i >= quickList.size())
					return;
				selectedContactBeanIndex = i;
//				new ActionSheetDialog(HomeFastDialActivity.this)
//						.builder()
//						.setCancelable(false)
//						.setCanceledOnTouchOutside(false)
//						.addSheetItem("拨打电话", SheetItemColor.Blue,
//								new OnSheetItemClickListener() {
//									@Override
//									public void onClick(int which) {
//										onDial(i);
//									}
//								})
//						.addSheetItem("发送短信", SheetItemColor.Blue,
//								new OnSheetItemClickListener() {
//									@Override
//									public void onClick(int which) {
//										onSms(i);
//									}
//								})
//						.addSheetItem("删除联系人", SheetItemColor.Blue,
//								new OnSheetItemClickListener() {
//									@Override
//									public void onClick(int which) {
//										onDelete(i);
//									}
//								}).show();
		        // Create Instance
		        PopupMenu menu = new PopupMenu(HomeFastDialActivity.this);
		        menu.setHeaderTitle(quickList.get(i).getDisplayName()+":");
		        // Set Listener
		        menu.setOnItemSelectedListener(mOnItemSelectedListener);
		        // Add Menu (Android menu like style)
		        menu.add(DIAL, R.string.dial).setIcon(
		                getResources().getDrawable(R.drawable.ic_action_call));
		        menu.add(SMS, R.string.sms).setIcon(
		                getResources().getDrawable(R.drawable.ic_action_sms));
		        menu.add(DELETE, R.string.delete).setIcon(
		                getResources().getDrawable(R.drawable.ic_action_discard));
		        menu.show(view);
			}
		});
		addContactBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showContactDialog();
			}
		});
	}
	
    private final static int DIAL = 0;
    private final static int SMS = 1;
    private final static int DELETE = 2;	
	
	OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case DIAL:
	        	onDial(selectedContactBeanIndex);
	            break;
	        case SMS:
	        	onSms(selectedContactBeanIndex);
	            break;
	        case DELETE:
	        	onDelete(selectedContactBeanIndex);
	            break;
	        }
		}
		
	};
	
	private String[] lianxiren1 = new String[] { "从联系人中添加", "手动添加", "从通话记录中添加"};
	
	/**
	 * 添加联系人弹出页
	 */
	public void showContactDialog() {
		if (this != null) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.please_choose)
					.setItems(lianxiren1,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0: onAddList();
										break;
									case 1: onManualAdd();
										break;
									case 2: onAddRecord();
										break;
									}
								}
							}).show();
		}
	}
	
	// 从联系人中添加
	public void onAddList() {
		Intent intent = new Intent(getApplicationContext(),
				HomeContactActivity.class);
		intent.putExtra("flag", HomeContactActivity.INTENT_SELECT_CONTACTBEAN);
		startActivityForResult(intent, 1);
	}
	
	// 手动添加
	public void onManualAdd() {
		Intent intent = new Intent(getApplicationContext(), AddActivity.class);
		startActivityForResult(intent, 1);
	}

	// 从通话记录中添加
	public void onAddRecord() {
		Intent intent = new Intent(getApplicationContext(),
				HomeDialActivity.class);
		intent.putExtra("flag", HomeContactActivity.INTENT_SELECT_CONTACTBEAN);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			ContactBean resultContactBean = data
					.getParcelableExtra("ContactBean");
			if (resultContactBean != null) {
				CircularImage view = new CircularImage(HomeFastDialActivity.this);
				view.setImageBitmap(contactBean2Bitmap(resultContactBean));
				mDragGridView.addView(view);
				quickList.add(resultContactBean);
			}
		}
	}
	
	/**
	 * 
	 * @param cb
	 * @return Bitmap
	 */
	private Bitmap contactBean2Bitmap(ContactBean cb) {
		if (0 != cb.getContactId()) {
			String displayName = cb.getDisplayName();
			if (TextUtils.isEmpty(displayName)) {
				displayName = "";
			}
//			return getThumb(displayName, contactid2bitmap(cb.getContactId()));
			return getThumb(displayName, cb);
		} else {
			String displayName = cb.getDisplayName();
			if (!TextUtils.isEmpty(displayName)) {
				return getThumb(displayName);
			} else {
				return getThumb(cb.getPhoneNum());
			}
		}
	}
	
	public Bitmap contactid2bitmap(int contactid) {
		Uri uri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, contactid);
		InputStream input = ContactsContract.Contacts
				.openContactPhotoInputStream(context.getContentResolver(), uri);
		return BitmapFactory.decodeStream(input);
	}

	/**
	 * 获取联系人图标
	 * @param s 联系人昵称
	 * @return
	 */	
	private Bitmap getThumb(String s) {
		return getThumb(s, null);
	}
	
	/**
	 * 获取联系人图标
	 * @param s 联系人昵称
	 * @param bitmap 头像
	 * @return
	 */
	private Bitmap getThumb(String s, ContactBean cb) {
		int canvas_width = 150;
		int canvas_height = 150;
		Bitmap bmp = null;
		bmp = Bitmap.createBitmap(canvas_width, canvas_height, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bmp);
		//如果有头像，就显示头像；没有头像就显示纯色背景
		Bitmap bitmap = null;
		if(cb != null)
			bitmap = contactid2bitmap(cb.getContactId());
		if (bitmap != null) {
			float scaleWidth =  (float) canvas_width / bitmap.getWidth();
			float scaleHeight =  (float) canvas_height / bitmap.getHeight();
			Matrix matrix=new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            canvas.drawBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), 0, 0, null);
		} else {
			Paint paint = new Paint();
			if (cb.getBackgroundColor() == 0) {
				int color = Color.rgb(random.nextInt(128), random.nextInt(128),
						random.nextInt(128));
				paint.setColor(color);
				cb.setBackgroundColor(color);
			} else {
				paint.setColor(cb.getBackgroundColor());
			}
			canvas.drawRect(new Rect(0, 0, 150, 150), paint);
		}
		drawPhoneText(s, canvas);
		return bmp;
	}
	
	/**
	 * 拨号
	 * @param position
	 */
	public void onDial(int position) {
		ContactBean contactBean = quickList.get(position);
		String toPhone = contactBean.getPhoneNum();
		Uri uri = Uri.parse("tel:" + toPhone);
		Intent it = new Intent(Intent.ACTION_CALL, uri);
		startActivity(it);
	}

	/**
	 * 短信
	 * @param position
	 */
	public void onSms(int position) {
		ContactBean contactBean = quickList.get(position);
		String threadId = getSMSThreadId(contactBean.getPhoneNum());
		Map<String, String> map = new HashMap<String, String>();
		map.put("phoneNumber", contactBean.getPhoneNum());
		map.put("threadId", threadId);
		BaseIntentUtil.intentSysDefault(this, MessageBoxList.class, map);
	}
	
	/**
	 * 删除联系人
	 * @param position
	 */
	public void onDelete(int position) {
		mDragGridView.removeViewAt(position);
		quickList.remove(position);
	}
	
	private static String[] SMS_COLUMNS = new String[] { "thread_id" };
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
	
	/**
	 * Draw text on the canvas, if the text's length is more than 5 chars, we
	 * draw multi-line on the canvas, else draw single line. As a result, it
	 * is more good-looking.
	 * 
	 * @param s
	 * @param canvas
	 */
	public void drawPhoneText(String s, Canvas canvas) {
		if (TextUtils.isEmpty(s))
			return;
		if (s.length() <= 5) {
			Paint paint = new Paint();
			paint.setTextSize(24);
			paint.setColor(Color.WHITE);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setAntiAlias(true);
			canvas.drawText(s, 75, 75, paint);
		} else {
			TextPaint textPaint = new TextPaint();
			textPaint.setARGB(0xFF, 0xFF, 0xFF, 0xFF);
			textPaint.setTextSize(24.0F);
			textPaint.setAntiAlias(true);
			StaticLayout layout = new StaticLayout(s, textPaint, 110,
					Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
			canvas.save();
			canvas.translate(20, 30);
			layout.draw(canvas);
			canvas.restore();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbUtils != null) {
			dbUtils.deleteQuick();
			for (int i = 0; i < quickList.size(); i++) {
				ContactBean cb = quickList.get(i);
				if (cb != null) {
					dbUtils.saveQuickByPosition(i, cb);
				}
			}
		}
	}
}