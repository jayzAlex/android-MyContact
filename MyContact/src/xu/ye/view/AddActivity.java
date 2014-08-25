/**
 * Project Name:MyContact
 * File Name:AddActivity.java
 * Package Name:xu.ye.view
 * Date:2014-1-26下午1:51:48
 * Copyright (c) 2014, 鸿程系统 All Rights Reserved.
 *
 */

package xu.ye.view;

import xu.ye.R;
import xu.ye.bean.ContactBean;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * ClassName:AddActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014-1-26 下午1:51:48 <br/>
 * 
 * @author zja
 * @version
 * @since JDK 1.6
 * @see
 */
public class AddActivity extends Activity implements OnClickListener {
	private Button topbar_left_btn;
	private Button topbar_right_btn;
	private EditText contact_name;
	private EditText contact_phonenum;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editlistitem);
		initView();
	}

	public void initView() {
		contact_name = (EditText) findViewById(R.id.contact_name);
		contact_phonenum = (EditText) findViewById(R.id.contact_phonenum);
		topbar_left_btn = (Button) findViewById(R.id.topbar_left_btn);
		topbar_left_btn.setOnClickListener(this);
		topbar_right_btn = (Button) findViewById(R.id.topbar_right_btn);
		topbar_right_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int viewid = v.getId();
		switch (viewid) {
		case R.id.topbar_left_btn:
			finish();
			break;
		case R.id.topbar_right_btn:
			if (TextUtils.isEmpty(contact_name.getText().toString())
					|| TextUtils.isEmpty(contact_phonenum.getText().toString())) {
				Toast.makeText(getApplicationContext(), "请填写联系人姓名和电话",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent data = getIntent();
				ContactBean contactBean = new ContactBean();
				contactBean.setDisplayName(contact_name.getText().toString());
				contactBean.setPhoneNum(contact_phonenum.getText().toString());
				contactBean.setBlkwhi(data.getStringExtra("blkwhi"));
				data.putExtra("ContactBean", contactBean);
				setResult(1, data);
				finish();
			}

			break;
		default:
			break;
		}
	}

}
