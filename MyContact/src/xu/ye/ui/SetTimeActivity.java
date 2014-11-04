/**
 * Project Name:MyContact
 * File Name:PrefsListActivity.java
 * Package Name:xu.ye.view
 * Date:2014-1-24上午11:42:24
 * Copyright (c) 2014, 鸿程系统 All Rights Reserved.
 *
*/

package xu.ye.ui;

import java.util.Calendar;

import xu.ye.R;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * ClassName:PrefsListActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2014-1-24 上午11:42:24 <br/>
 * @author   zja
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class SetTimeActivity extends Activity implements OnClickListener{
	private TextView topbar_title;
	private Button refresh_button;
	private LinearLayout to_form;
	private LinearLayout from_form;
	private TextView from_time;
	private TextView to_time;
	private Button topbar_left_btn;
	private TimePickerDialog tpd;
	private TextView from;
	private TextView to;
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settime);
		initView();

	}
	public void initView(){
		
		
		topbar_title = (TextView) findViewById(R.id.topbar_title);
		topbar_title.setText("有效时间设置");
		from_form = (LinearLayout) findViewById(R.id.from_form);
		from_form.setOnClickListener(this);
		to_form = (LinearLayout) findViewById(R.id.to_form);
		to_form.setOnClickListener(this);
		refresh_button = (Button) findViewById(R.id.refresh_button);
		refresh_button.setOnClickListener(this);
		from_time = (TextView) findViewById(R.id.from_time);
		to_time = (TextView) findViewById(R.id.to_time);
		topbar_left_btn = (Button) findViewById(R.id.topbar_left_btn);
		topbar_left_btn.setOnClickListener(this);
		from = (TextView)findViewById(R.id.from);
		to = (TextView)findViewById(R.id.to);
		
		Intent data = getIntent();
		String fromtime = data.getStringExtra("from_time");
		String totime = data.getStringExtra("to_time");
		from_time.setText(fromtime);
		to_time.setText(totime);
		
		tpd_init(from_time);
		tpd.show();
		
	}
	@Override
	public void onClick(View v) {
		int viewid = v.getId();
		switch (viewid) {
		case R.id.from_form:
			from_form.setBackgroundResource(R.drawable.text_border_up_selected);
			to_form.setBackgroundResource(R.drawable.text_border_down);
			from.setTextColor(getResources().getColor(R.color.white));
			from_time.setTextColor(getResources().getColor(R.color.white));
			to.setTextColor(getResources().getColor(R.color.black));
			to_time.setTextColor(getResources().getColor(R.color.black));
			tpd_init(from_time);
			tpd.show();
			break;
		case R.id.to_form:
			from_form.setBackgroundResource(R.drawable.text_border_up);
			to_form.setBackgroundResource(R.drawable.text_border_down_selected);
			from.setTextColor(getResources().getColor(R.color.black));
			from_time.setTextColor(getResources().getColor(R.color.black));
			to.setTextColor(getResources().getColor(R.color.white));
			to.setTextColor(getResources().getColor(R.color.white));
			tpd_init(to_time);
			tpd.show();
			break;
		case R.id.refresh_button:
			from_time.setText("00:00");
			to_time.setText("23:59");
			break;
		case R.id.topbar_left_btn:
			saveAndBack();
			break;
		default:
			break;
		}
	}
	
    /*tpd初始化*/
    public void tpd_init(final TextView v){
    	TimePickerDialog.OnTimeSetListener otsl=new TimePickerDialog.OnTimeSetListener(){
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				String hourOfDayStr = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
				String minuteStr = minute < 10 ? "0" + minute : minute + "";
				v.setText(hourOfDayStr + ":" + minuteStr);
				tpd.dismiss();
			}
    	};
    	String[] time_text = (v.getText()+"").split(":");
    	tpd=new TimePickerDialog(this,otsl, Integer.parseInt(time_text[0]), Integer.parseInt(time_text[1]), true);
    }
    
    @Override 
    public boolean onKeyDown(int keyCode,KeyEvent event) {  
       // 是否触发按键为back键  
       if (keyCode == KeyEvent.KEYCODE_BACK) {  
    	   saveAndBack();
           return true;  
       } else// 如果不是back键正常响应  
           return super.onKeyDown(keyCode,event);  
    }
    
    public void saveAndBack(){
		Intent data = getIntent();
		data.putExtra("from_time", from_time.getText());
		data.putExtra("to_time", to_time.getText());
		setResult(2, data);
		finish();
    }
}

