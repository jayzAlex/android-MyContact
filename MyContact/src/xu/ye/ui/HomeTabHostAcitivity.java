package xu.ye.ui;

import xu.ye.R;
import xu.ye.view.AnimationTabHost;
import xu.ye.view.other.SystemScreenInfo;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class HomeTabHostAcitivity extends TabActivity {

	private AnimationTabHost mTabHost;
	private TabWidget mTabWidget;
	private ImageView cursor;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private ImageView[] views;
	private static final String TAG = "HomeTabHostAcitivity";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_hometabhost);

		SystemScreenInfo.getSystemInfo(HomeTabHostAcitivity.this);

		InitImageView();


		mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
		mTabHost = (AnimationTabHost) findViewById(android.R.id.tabhost);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				initBottomMenu();
			}
		}, 300);

		init();
	}

	private int getImageId(int index, boolean isSelect){
		int result = -1;
		switch (index) {
		case 0: 
			result = isSelect ? R.drawable.tab_fastdial_selected : R.drawable.tab_fastdial_normal;
			break;
		case 1: 
			result = isSelect ? R.drawable.tab_dial_selected : R.drawable.tab_dial_normal;
			break;
		case 2:
			result = isSelect ? R.drawable.tab_contact_selected : R.drawable.tab_contact_normal;
			break;
		case 3:
			result = isSelect ? R.drawable.tab_sms_selected : R.drawable.tab_sms_normal;
			break;
		case 4:
			result = isSelect ? R.drawable.tab_settings_selected : R.drawable.tab_settings_normal;
			break;
		}
		return result;
	}

	private void initBottomMenu() {
		int viewCount = mTabWidget.getChildCount();
		views = new ImageView[viewCount];
		for (int i = 0; i < views.length; i++) {
			View v = (LinearLayout) mTabWidget.getChildAt(i);
			views[i] = (ImageView) v.findViewById(R.id.main_activity_tab_image);
		}
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				int tabID = Integer.valueOf(tabId);
				views[currIndex].setImageResource(getImageId(currIndex, false));
				views[tabID].setImageResource(getImageId(tabID, true));
				onPageSelected(tabID);
			}
		});
	}

	private void init() {
		setIndicator("一键拨号", 0, new Intent(this, HomeFastDialActivity.class), R.drawable.tab_fastdial_selected);
		setIndicator("拨号", 1, new Intent(this, HomeDialActivity.class), R.drawable.tab_dial_normal);
		setIndicator("联系人", 2, new Intent(this, HomeContactActivity.class), R.drawable.tab_contact_normal);
		setIndicator("信息", 3, new Intent(this, HomeSMSActivity.class), R.drawable.tab_sms_normal);
		setIndicator("设置", 4, new Intent(this, HomeSettintActivity.class), R.drawable.tab_settings_normal);
		mTabHost.setOpenAnimation(true);
//		onPageSelected(1);
	}

	private void setIndicator(String ss, int tabId, Intent intent, int image_id) {

		View localView = LayoutInflater.from(this.mTabHost.getContext()).inflate(R.layout.tab_widget_view, null);
		((ImageView) localView.findViewById(R.id.main_activity_tab_image)).setImageResource(image_id);
		((TextView) localView.findViewById(R.id.main_activity_tab_text)).setText(ss);
		String str = String.valueOf(tabId);
		TabHost.TabSpec localTabSpec = mTabHost.newTabSpec(str).setIndicator(localView).setContent(intent);
		mTabHost.addTab(localTabSpec);
	}

	private void InitImageView() {

		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.main_tab_anim_light).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 5 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}

	public void onPageSelected(int arg0) {

		int one = offset * 2 + bmpW;
		Animation animation = null;
		animation = new TranslateAnimation(one * currIndex, one * arg0 , 0, 0);
		currIndex = arg0;
		animation.setFillAfter(true);
		animation.setDuration(300);
		cursor.startAnimation(animation);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		}
		return false;
	}

	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取当前活动的Activity实例
        Activity subActivity = getLocalActivityManager().getCurrentActivity();
        Log.i(TAG, "onActivityResult");
        //判断是否实现返回值接口
        if (subActivity instanceof OnTabActivityResultListener) {
            //获取返回值接口实例
            OnTabActivityResultListener listener = (OnTabActivityResultListener) subActivity;
            //转发请求到子Activity
            listener.onTabActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
	
	/**
	 * 解决子Activity无法接收Activity回调的问题
	 * @author Administrator
	 *
	 */
	public interface OnTabActivityResultListener {
	    public void onTabActivityResult(int requestCode, int resultCode, Intent data);
	}

}