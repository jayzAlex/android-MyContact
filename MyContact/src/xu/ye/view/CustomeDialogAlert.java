package xu.ye.view;

import xu.ye.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class CustomeDialogAlert extends Dialog {

	public CustomeDialogAlert(Context context) {
		super(context);
		show();
	}

	public CustomeDialogAlert(Context context,int theme){
		super(context, theme);
		show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_alert);
		getWindow().setBackgroundDrawableResource(R.drawable.dialog_alert_bg);
		LayoutParams _LayoutParams = getWindow().getAttributes();
		_LayoutParams.x = 0;
		_LayoutParams.y = 0;
		_LayoutParams.gravity = Gravity.CENTER;
		getWindow().setAttributes(_LayoutParams);

	}
	
	public final CustomeDialogAlert setTitleText(String pString){
		 ((TextView)findViewById(R.id.alert_title)).setText(pString);
		return this ;
	}
	
	/**
	 * 设置提示内容信息
	 * @param paramCharSequence
	 * @return
	 */
	 public final CustomeDialogAlert setMessageText(CharSequence paramCharSequence)
	  {
	    if (paramCharSequence == null){
	    	paramCharSequence = "" ;
	    }
	    ((TextView)findViewById(R.id.alert_message)).setText(paramCharSequence);
	    return this;
	  }

	/**
	 * 第一个按钮事件
	 * @param paramString
	 * @param paramOnClickListener
	 * @return
	 */
	public final CustomeDialogAlert setOption1Button(CharSequence paramString,
			View.OnClickListener paramOnClickListener) {
		Button option1 = (Button) findViewById(R.id.option1);
		option1.setVisibility(View.VISIBLE);
		option1.setText(paramString);
		option1.setOnClickListener(paramOnClickListener);
		return this;
	}
	   /**
     * 第二按钮事件
     * @param paramString
     * @param paramOnClickListener
     * @return
     */
    public final CustomeDialogAlert setOption2Button(CharSequence paramString,
            View.OnClickListener paramOnClickListener) {
        Button option2 = (Button) findViewById(R.id.option2);
        option2.setVisibility(View.VISIBLE);
        option2.setText(paramString);
        option2.setOnClickListener(paramOnClickListener);
        return this;
    }
	/**
	 * 最后按钮事件
	 * @param pString
	 * @param pOnClickListener
	 * @return
	 */
	public final CustomeDialogAlert setPositiveButton(CharSequence paramCharSequence,View.OnClickListener pOnClickListener){
		Button _Button = (Button) findViewById(R.id.cancel);
		_Button.setVisibility(View.VISIBLE);
		_Button.setText(paramCharSequence);
		_Button.setOnClickListener(pOnClickListener);
		return this ;
	}
	
	

}
