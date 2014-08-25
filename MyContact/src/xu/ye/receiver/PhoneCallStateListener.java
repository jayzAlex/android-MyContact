package xu.ye.receiver;

import java.lang.reflect.Method;
import java.util.ArrayList;

import xu.ye.R;
import xu.ye.bean.ContactBean;
import xu.ye.db.DbUtils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

public class PhoneCallStateListener extends PhoneStateListener {

	private Context context;
	private Intent intent;
	private static final String TAG = "Phone call";
	private static SharedPreferences sharedPreferences;
	private ArrayList<ContactBean> allCbList;

	public PhoneCallStateListener(Context context, Intent intent) {
		this.context = context;
		this.intent = intent;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		if ("android.intent.action.PHONE_STATE".equals(intent.getAction())) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// Bundle b = intent.getExtras();
				// String incommingNumber =
				// b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
				// String incommingNumber = b.getString("incoming_number");
				String incommingNumber = intent
						.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				Log.e("Incomming number========>", incommingNumber + "");
				if (onBlock(incommingNumber, getPrefList(context), context)) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
						// 如果当前版本小于HONEYCOMB版本，即2.3版本
						Log.d(TAG, "InFirst Method Ans Call");
						TelephonyManager telephony = (TelephonyManager) context
								.getSystemService(Context.TELEPHONY_SERVICE);
						try {
							Class<?> c = Class.forName(telephony.getClass()
									.getName());
							Method m = c.getDeclaredMethod("getITelephony");
							m.setAccessible(true);
							ITelephony telephonyService = (ITelephony) m
									.invoke(telephony);
							// telephonyService.silenceRinger();
							// 必须有权限 android.permission.CALL_PHONE
							telephonyService.endCall();
							// iTelephony.answerRingingCall();//自动接通电话
							// 必须有权限 android.permission.CALL_PHONE
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
						Log.d(TAG, "InSecond Method Ans Call");
						// froyo and beyond trigger on buttonUp instead of
						// buttonDown
						Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
						buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(
								KeyEvent.ACTION_UP,
								KeyEvent.KEYCODE_HEADSETHOOK));
						context.sendOrderedBroadcast(buttonUp,
								"android.permission.CALL_PRIVILEGED");

						Intent headSetUnPluggedintent = new Intent(
								Intent.ACTION_HEADSET_PLUG);
						headSetUnPluggedintent
								.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
						headSetUnPluggedintent.putExtra("state", 0);
						headSetUnPluggedintent.putExtra("name", "Headset");
						try {
							context.sendOrderedBroadcast(
									headSetUnPluggedintent, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			case PhoneStateListener.LISTEN_CALL_STATE:
			}
		}
		super.onCallStateChanged(state, incomingNumber);
	}

	public int getPrefList(Context context) {
		if (sharedPreferences == null)
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
		return sharedPreferences.getInt("preflist", R.id.allList);
	}

	public boolean onBlock(String incommingNumber, int preflist, Context context) {
		if (TextUtils.isEmpty(incommingNumber))
			return false;
		if (preflist == R.id.allList)
			return false;
		String blkwhi = null;
		switch (preflist) {
		case R.id.blackList:
			blkwhi = "B";
			break;
		case R.id.whiteList:
			blkwhi = "W";
			break;
		default:
			break;
		}
		DbUtils dbUtils = new DbUtils(context);
		allCbList = dbUtils.getBlkwhi(blkwhi);
		if (preflist == R.id.blackList) {
			for (ContactBean contactbean : allCbList) {
				if (incommingNumber.equals(contactbean.getPhoneNum())) {
					return true;
				}
			}
			return false;
		}

		if (preflist == R.id.whiteList) {
			for (ContactBean contactbean : allCbList) {
				if (incommingNumber.equals(contactbean.getPhoneNum())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}