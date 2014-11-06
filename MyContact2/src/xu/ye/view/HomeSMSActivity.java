package xu.ye.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xu.ye.R;
import xu.ye.bean.SMSBean;
import xu.ye.uitl.BaseIntentUtil;
import xu.ye.uitl.RexseeSMS;
import xu.ye.view.adapter.HomeSMSAdapter;
import xu.ye.view.sms.MessageBoxList;
import xu.ye.view.sms.NewSMSActivity;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dobmob.doblist.DobList;
import com.dobmob.doblist.events.OnLoadMoreListener;
import com.dobmob.doblist.exceptions.NoEmptyViewException;
import com.dobmob.doblist.exceptions.NoListViewException;

public class HomeSMSActivity extends Activity {

	private ListView listView;
	private HomeSMSAdapter adapter;
	private RexseeSMS rsms;
	private Button newSms;
	private List<SMSBean> list_mmt;
	private static final String TAG = "HomeSMSActivity";
//	private Thread thread;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();

	}

	public void init() {

		setContentView(R.layout.home_sms_page);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

		listView = (ListView) findViewById(R.id.list);
		adapter = new HomeSMSAdapter(HomeSMSActivity.this);
		rsms = new RexseeSMS(HomeSMSActivity.this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, String> map = new HashMap<String, String>();
				SMSBean sb = adapter.getItem(position);
				map.put("displayName", sb.getDisplayName());
				map.put("phoneNumber", sb.getAddress());
				map.put("threadId", sb.getThread_id());
				BaseIntentUtil.intentSysDefault(HomeSMSActivity.this,
						MessageBoxList.class, map);
			}
		});

		newSms = (Button) findViewById(R.id.newSms);
		newSms.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BaseIntentUtil.intentSysDefault(HomeSMSActivity.this,
						NewSMSActivity.class, null);
			}
		});
		
//		setContentShown(false, false);
//
//		thread = new Thread(new Runnable() {
//			private Message msg;
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				msg = new Message();
//				msg.what = 0;
//				doLoadSmsList();
//				mHandler.sendMessage(msg);
//			}
//		});
//		thread.start();
		list_mmt = new ArrayList<SMSBean>();
		initDobList(listView);

	}
	
	private int load_turn_index = 0;
	private final int load_amount = 10;
	private DobList dobList;	

	public int doLoadSmsList() {
		List<SMSBean> list_sms = rsms.getThreadsNum(rsms.getThreads(load_amount, load_amount * load_turn_index));
		list_mmt.addAll(list_sms);
		return list_sms.size();
	}
	
    private void initDobList(ListView listView) {

        // DobList initializing
        dobList = new DobList();
        try {

            // Register ListView
            //
            // NoListViewException will be thrown when
            // there is no ListView
            dobList.register(listView);

            // Add ProgressBar to footers of ListView
            // to be shown in loading more
            dobList.addDefaultLoadingFooterView();

            // Sets the view to show if the adapter is empty
            // see startCentralLoading() method
            View noItems = findViewById(R.id.noItems);
            dobList.setEmptyView(noItems);

            // Callback called when reaching last item in ListView
            dobList.setOnLoadMoreListener(new OnLoadMoreListener() {

                @Override
                public void onLoadMore(final int totalItemCount) {
                    Log.i(TAG, "onStart totalItemCount " + totalItemCount);

                    // Just inserting some dummy data after
                    // period of time to simulate waiting
                    // data from server
                    addDummyData();
                }
            });

        } catch (NoListViewException e) {
            e.printStackTrace();
        }

        try {
            // Show ProgressBar at the center of ListView
            // this can be used while loading data from
            // server at the first time
            //
            // setEmptyView() must be called before
            //
            // NoEmptyViewException will be thrown when
            // there is no EmptyView
            dobList.startCentralLoading();

        } catch (NoEmptyViewException e) {
            e.printStackTrace();
        }
        // Simulate adding data at the first time
        addDummyData();
    }

    protected void addDummyData() {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
//                addItems(adapter.getCount(), adapter.getCount() + itemsCount);
                if(doLoadSmsList() == 0){
                	Toast.makeText(HomeSMSActivity.this, R.string.no_more_sms, Toast.LENGTH_LONG).show();
                	return;
            	} else {
    				adapter.assignment(list_mmt);
    				adapter.notifyDataSetChanged();
                    // We must call finishLoading
                    // when finishing adding data
                    dobList.finishLoading();
                    load_turn_index++;
                }
            }

        });
    }	

//	// 消息处理
//	Handler mHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			if (msg.what == 0) {
//				adapter.assignment(list_mmt);
//				adapter.notifyDataSetChanged();
//				setContentShown(true, true);
//			}
//		}
//	};
//	private LinearLayout progress_container;
//	private LinearLayout mProgressContainer;
//	private ListView mContentContainer;
//	private boolean mContentShown = true;
//
//	/**
//	 * Control whether the content is being displayed. You can make it not
//	 * displayed if you are waiting for the initial data to show in it. During
//	 * this time an indeterminant progress indicator will be shown instead.
//	 * 
//	 * @param shown
//	 *            If true, the content view is shown; if false, the progress
//	 *            indicator. The initial value is true.
//	 * @param animate
//	 *            If true, an animation will be used to transition to the new
//	 *            state.
//	 */
//	private void setContentShown(boolean shown, boolean animate) {
//		ensureContent();
//		if (mContentShown == shown) {
//			return;
//		}
//		mContentShown = shown;
//		if (shown) {
//			if (animate) {
//				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
//						HomeSMSActivity.this, android.R.anim.fade_out));
//				mContentContainer.startAnimation(AnimationUtils.loadAnimation(
//						HomeSMSActivity.this, android.R.anim.fade_in));
//			} else {
//				mProgressContainer.clearAnimation();
//				mContentContainer.clearAnimation();
//			}
//			mProgressContainer.setVisibility(View.GONE);
//			mContentContainer.setVisibility(View.VISIBLE);
//		} else {
//			if (animate) {
//				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
//						HomeSMSActivity.this, android.R.anim.fade_in));
//				mContentContainer.startAnimation(AnimationUtils.loadAnimation(
//						HomeSMSActivity.this, android.R.anim.fade_out));
//			} else {
//				mProgressContainer.clearAnimation();
//				mContentContainer.clearAnimation();
//			}
//			mProgressContainer.setVisibility(View.VISIBLE);
//			mContentContainer.setVisibility(View.GONE);
//		}
//	}
//
//	private void ensureContent() {
//		if (mContentContainer != null && mProgressContainer != null) {
//			return;
//		}
//		mProgressContainer = (LinearLayout) findViewById(R.id.progress_container);
//		mContentContainer = listView;
//
//	}

}
