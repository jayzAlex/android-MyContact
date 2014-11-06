/**
 * Project Name:MyContact
 * File Name:PrefsListActivity.java
 * Package Name:xu.ye.view
 * Date:2014-1-24上午11:42:24
 * Copyright (c) 2014, 鸿程系统 All Rights Reserved.
 *
 */

package xu.ye.view;

import java.util.ArrayList;
import xu.ye.R;
import xu.ye.bean.Blkwhitime;
import xu.ye.bean.ContactBean;
import xu.ye.db.DbUtils;
import xu.ye.view.adapter.PrefsListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName:PrefsListActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014-1-24 上午11:42:24 <br/>
 * 
 * @author zja
 * @version
 * @since JDK 1.6
 * @see
 */
public class PrefsListActivity extends Activity implements OnClickListener {
	private TextView topbar_title;
	private RelativeLayout time_set;
	private TextView from_time;
	private TextView to_time;
	private Button topbar_left_btn;
	private Button topbar_right_btn;
	private String blkwhi;
	private PrefsListAdapter adapter;
	private static final String TAG = "PrefsListActivity";
	private ArrayList<ContactBean> allCbList;
	private ListView mListView;
//	private LoadBlackAndWhiteListTask mLoadBlackAndWhiteListTask;
	private TextView loading;
	private Thread thread;
	private String fromtime;
	private String totime;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prefslist);
		initView();

	}

	public void initView() {

		topbar_title = (TextView) findViewById(R.id.topbar_title);
		int preflist = getIntent().getIntExtra("preflist", R.id.allList);
		switch (preflist) {
		case R.id.blackList:
			topbar_title.setText("不想接名单");
			blkwhi = "B";
			break;
		case R.id.whiteList:
			topbar_title.setText("只想接名单");
			blkwhi = "W";
			break;
		default:
			break;
		}

		time_set = (RelativeLayout) findViewById(R.id.time_set);
		time_set.setOnClickListener(this);
		from_time = (TextView) findViewById(R.id.from_time);
		to_time = (TextView) findViewById(R.id.to_time);
		topbar_left_btn = (Button) findViewById(R.id.topbar_left_btn);
		topbar_left_btn.setOnClickListener(this);
		topbar_right_btn = (Button) findViewById(R.id.topbar_right_btn);
		topbar_right_btn.setOnClickListener(this);

		loading = (TextView) findViewById(R.id.loading);

		// 这里数据加载任务
		// mLoadBlackAndWhiteListTask = new LoadBlackAndWhiteListTask();
		// mLoadBlackAndWhiteListTask.execute();
		loading.setVisibility(View.VISIBLE);

		thread = new Thread(new Runnable() {
			private Message msg;

			@Override
			public void run() {
				// TODO Auto-generated method stub
				msg = new Message();
				msg.what = 0;
				doLoadBlackAndWhiteList();
				mHandler.sendMessage(msg);
			}
		});
		thread.start();

	}

	// 消息处理
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				loading.setVisibility(View.GONE);
				from_time.setText(fromtime);
				to_time.setText(totime);
				adapter = new PrefsListAdapter(getApplicationContext(), allCbList);
				mListView = (ListView) findViewById(R.id.list);
				mListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}
	};


	// 多线程加载列表：TODO
	public void doLoadBlackAndWhiteList() {
		// db读取从..至..时间段
		DbUtils dbUtils = new DbUtils(getApplicationContext());
		Blkwhitime blkwhitime = dbUtils.getBlkwhitime(blkwhi);
		fromtime = blkwhitime.getFromtime();
		totime = blkwhitime.getTotime();
		allCbList = dbUtils.getBlkwhi(blkwhi);
		

	}

	// // 多线程加载列表：TODO
	// class LoadBlackAndWhiteListTask extends AsyncTask<Void, Integer, Void>{
	//
	// @Override
	// protected Void doInBackground(Void... arg0) {
	// // db读取从..至..时间段
	// DbUtils dbUtils = new DbUtils(getApplicationContext());
	// Blkwhitime blkwhitime = dbUtils.getBlkwhitime(blkwhi);
	// String fromtime = blkwhitime.getFromtime();
	// String totime = blkwhitime.getTotime();
	// from_time.setText(fromtime);
	// to_time.setText(totime);
	//
	// allCbList = dbUtils.getBlkwhi(blkwhi);
	// adapter = new PrefsListAdapter(getApplicationContext(), allCbList);
	// mListView = (ListView) findViewById(R.id.list);
	// mListView.setAdapter(adapter);
	//
	// return null;
	//
	// }
	//
	// //当在上面方法中调用publishProgress时，该方法触发,该方法在UI线程中被执行
	// protected void onProgressUpdate(Integer...progress){
	//
	// }
	//
	// protected void onPostExecute(Void result){
	// loading.setVisibility(View.GONE);
	// }
	//
	// protected void onPreExecute(){
	// loading.setVisibility(View.VISIBLE);
	// }
	//
	// }

	@Override
	public void onClick(View v) {
		int viewid = v.getId();
		switch (viewid) {
		case R.id.time_set:
			Intent intent = new Intent(getApplication(), SetTimeActivity.class);
			intent.putExtra("from_time", from_time.getText().toString());
			intent.putExtra("to_time", to_time.getText().toString());
			startActivityForResult(intent, 2);
			break;
		case R.id.topbar_left_btn:
			finish();
			break;
		case R.id.topbar_right_btn:
			showContactDialog();
			break;
		default:
			break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			ContactBean resultContactBean = data
					.getParcelableExtra("ContactBean");
			if (resultContactBean != null) {
				allCbList.add(resultContactBean);
			}
			adapter.notifyDataSetChanged();
			DbUtils dbUtils = new DbUtils(getApplicationContext());
			dbUtils.saveBlkwhi(resultContactBean);

		}
		if (resultCode == 2) {
			String fromtime = data.getStringExtra("from_time");
			String totime = data.getStringExtra("to_time");
			from_time.setText(fromtime);
			to_time.setText(totime);
			// 保存到db
			Blkwhitime blkwhitime = new Blkwhitime(fromtime, totime, blkwhi,
					"Y");
			DbUtils dbutils = new DbUtils(this);
			dbutils.saveBlkwhitime(blkwhitime);

		}

	}

	private String[] lianxiren1 = new String[] { "手动添加", "从通话记录中添加", "从联系人中添加" };

	// 群组联系人弹出页
	public void showContactDialog() {
		if (this != null) {
			new AlertDialog.Builder(this)
					.setTitle("请选择")
					.setItems(lianxiren1,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										onAdd();
										break;
									case 1:
										onAddRecord();
										break;
									case 2:
										onAddList();
										break;
									}
								}
							}).show();
		}
	}

	// // 删除
	// public void onDelete() {
	// adapter.showDeleteOption();
	// }

	// 手动添加
	public void onAdd() {
		Intent intent = new Intent(getApplicationContext(), AddActivity.class);
		intent.putExtra("blkwhi", blkwhi);
		startActivityForResult(intent, 1);
	}

	// 从通话记录中添加
	public void onAddRecord() {
		Intent intent = new Intent(getApplicationContext(),
				HomeDialActivity.class);
		intent.putExtra("blkwhi", blkwhi);
		intent.putExtra("flag", HomeContactActivity2.INTENT_SELECT_CONTACTBEAN);
		startActivityForResult(intent, 1);
	}

	// 从联系人中添加
	public void onAddList() {
		Intent intent = new Intent(getApplicationContext(),
				HomeContactActivity2.class);
		intent.putExtra("blkwhi", blkwhi);
		intent.putExtra("flag", HomeContactActivity2.INTENT_SELECT_CONTACTBEAN);
		startActivityForResult(intent, 1);
	}

}
