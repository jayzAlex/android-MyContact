package xu.ye.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import xu.ye.R;
import xu.ye.application.MyApplication;
import xu.ye.bean.ContactBean;
import xu.ye.db.DbUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.way.view.CircularImage;
import com.way.view.DragGridView;
import com.way.view.DragGridView.OnRearrangeListener;

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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_fastdial_page);
		mDragGridView = ((DragGridView) findViewById(R.id.vgv));
		context = getApplicationContext();
        DbUtils dbUtils = new DbUtils(context);
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
				quickList.add(cb);
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

	private void setListeners() {
		mDragGridView.setOnRearrangeListener(new OnRearrangeListener() {
			public void onRearrange(int oldIndex, int newIndex) {
				ContactBean cb = quickList.remove(oldIndex);
				quickList.add(newIndex, cb);
			}
		});
		mDragGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i,
					long arg3) {
				if (i < quickList.size()) {
					Toast.makeText(HomeFastDialActivity.this, "点击了"+i, Toast.LENGTH_SHORT).show();
				}
//				if (i == quickList.size()){
//					showContactDialog();
//				}
				
			}
		});
	}
	
	private String[] lianxiren1 = new String[] { "从联系人中添加", "手动添加", "从通话记录中添加" };
	
	// 添加联系人弹出页
	public void showContactDialog() {
		if (this != null) {
			new AlertDialog.Builder(this)
					.setTitle("请选择")
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
				mDragGridView.addView(view, quickList.size());
				quickList.add(resultContactBean);
			}
		}
	}
	
	/**
	 * 
	 * @param cb
	 * @return Bitmap
	 */
	private Bitmap contactBean2Bitmap(ContactBean cb){
		if (0 != cb.getPhotoId()) {
			Uri uri = ContentUris.withAppendedId(
					ContactsContract.Contacts.CONTENT_URI,
					cb.getContactId());
			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(
							context.getContentResolver(), uri);
			return BitmapFactory.decodeStream(input);
		} else {
			String displayName = cb.getDisplayName();
			if (!TextUtils.isEmpty(displayName)) {
				return getThumb(displayName);
			} else {
				return getThumb(cb.getPhoneNum());
			}
		}
	}

	private Bitmap getThumb(String s) {
		Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setColor(Color.rgb(random.nextInt(128), random.nextInt(128),
				random.nextInt(128)));
		paint.setTextSize(24);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawRect(new Rect(0, 0, 150, 150), paint);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(s, 75, 75, paint);
		return bmp;
	}
}