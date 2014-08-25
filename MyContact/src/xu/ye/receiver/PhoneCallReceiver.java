package xu.ye.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		PhoneCallStateListener customPhoneListener = new PhoneCallStateListener(
				context, intent);
		telephony.listen(customPhoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);
	}

}