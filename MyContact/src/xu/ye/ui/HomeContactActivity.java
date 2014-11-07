// @author Bhavya Mehta
package xu.ye.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import xu.ye.R;
import xu.ye.bean.ContactBean;
import xu.ye.uitl.BaseIntentUtil;
import xu.ye.view.sms.MessageBoxList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listviewfilter.IPinnedListFilter;
import com.example.listviewfilter.PinnedHeaderAdapter;
import com.example.listviewfilter.ui.IndexBarView;
import com.example.listviewfilter.ui.PinnedHeaderListView;

// Activity that display customized list view and search box
public class HomeContactActivity extends Activity implements IPinnedListFilter{

	// unsorted list items
	ArrayList<ContactBean> mItems;

	// array list to store section positions
	ArrayList<Integer> mListSectionPos;

	// array list to store listView data
	ArrayList<ContactBean> mListItems;

	// custom list view with pinned header
	PinnedHeaderListView mListView;

	// custom adapter
	PinnedHeaderAdapter mAdaptor;

	// search box
	EditText mSearchView;

	// loading view
	ProgressBar mLoadingView;

	// empty view
	TextView mEmptyView;

	HashMap<Integer, ContactBean> contactIdMap;

//	ArrayList<ContactBean> list;

	HashMap<String, ContactBean> contactMap;

	private MyAsyncQueryHandler asyncQuery;
	
	public static final int INTENT_SELECT_CONTACTBEAN = 1;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// UI elements
		setupViews();

//		// Array to ArrayList
//		mItems = new ArrayList<String>(Arrays.asList(ITEMS));
		mItems = new ArrayList<ContactBean>();
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		init();
		mListSectionPos = new ArrayList<Integer>();
		mListItems = new ArrayList<ContactBean>();

//		// for handling configuration change
//		if (savedInstanceState != null) {
//			mListItems = savedInstanceState.getStringArrayList("mListItems");
//			mListSectionPos = savedInstanceState.getIntegerArrayList("mListSectionPos");
//
//			if (mListItems != null && mListItems.size() > 0 && mListSectionPos != null && mListSectionPos.size() > 0) {
//				setListAdaptor();
//			}
//
//			String constraint = savedInstanceState.getString("constraint");
//			if (constraint != null && constraint.length() > 0) {
//				mSearchView.setText(constraint);
//				setIndexBarViewVisibility(constraint);
//			}
//		} else {
//			new Poplulate().execute(mItems);
//		}
	}
	
	private void init(){
		showLoading(mListView, mLoadingView, mEmptyView);
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
		String[] projection = { 
				ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1,
				"sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
		}; // 查询的列
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
	}
	
	/**
	 * 数据库异步查询类AsyncQueryHandler
	 * 
	 * @author administrator
	 * 
	 */
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		/**
		 * 查询结束的回调函数
		 */
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			mItems.clear();
			if (cursor != null && cursor.getCount() > 0) {
				
				contactIdMap = new HashMap<Integer, ContactBean>();
				
//				list = new ArrayList<ContactBean>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					int contactId = cursor.getInt(4);
					Long photoId = cursor.getLong(5);
					String lookUpKey = cursor.getString(6);

					if (contactIdMap.containsKey(contactId)) {
						
					}else{
						
						ContactBean cb = new ContactBean();
						cb.setDisplayName(name);
//					if (number.startsWith("+86")) {// 去除多余的中国地区号码标志，对这个程序没有影响。
//						cb.setPhoneNum(number.substring(3));
//					} else {
						cb.setPhoneNum(number);
//					}
						cb.setSortKey(sortKey);
						cb.setContactId(contactId);
						cb.setPhotoId(photoId);
						cb.setLookUpKey(lookUpKey);
//						list.add(cb);
						contactIdMap.put(contactId, cb);
						mItems.add(cb);
					}
				}
				if (mItems.size() > 0) {
//					setAdapter(list);
					new Poplulate().execute(mItems);
				} else {
					showEmptyText(mListView, mLoadingView, mEmptyView);
				}
			}
		}

	}

	private void setupViews() {
		setContentView(R.layout.home_contact_page);
		mSearchView = (EditText) findViewById(R.id.search_view);
		mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
		mListView = (PinnedHeaderListView) findViewById(R.id.list_view);
		mEmptyView = (TextView) findViewById(R.id.empty_view);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ContactBean cb = (ContactBean) mListItems.get(position);
				cb.setBlkwhi(getIntent().getStringExtra("blkwhi"));
				if (getIntent().getIntExtra("flag", -1) == INTENT_SELECT_CONTACTBEAN) {
					Intent intent = getIntent();
					intent.putExtra("ContactBean", cb);
					setResult(1, intent);
					finish();
					return;
				}
				showContactDialog(dialogItems, cb, position);
			}
		});
	}
	private String[] dialogItems = new String[] { "拨打电话", "发送短信", "查看","删除" };

	// 联系人操作Dialog
	private void showContactDialog(final String[] arg, final ContactBean cb,
			final int position) {
		new AlertDialog.Builder(this).setTitle(cb.getDisplayName())
				.setItems(arg, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Uri uri = null;
						switch (which) {
						case 0:// 打电话
							String toPhone = cb.getPhoneNum();
							uri = Uri.parse("tel:" + toPhone);
							Intent it = new Intent(Intent.ACTION_CALL, uri);
							startActivity(it);
							break;
						case 1:// 发短息
							String threadId = getSMSThreadId(cb.getPhoneNum());

							Map<String, String> map = new HashMap<String, String>();
							map.put("phoneNumber", cb.getPhoneNum());
							map.put("threadId", threadId);
							BaseIntentUtil.intentSysDefault(
									HomeContactActivity.this,
									MessageBoxList.class, map);
							break;
						case 2:// 查看详细 修改联系人资料
							uri = ContactsContract.Contacts.CONTENT_URI;
							Uri personUri = ContentUris.withAppendedId(uri,
									cb.getContactId());
							Intent intent2 = new Intent();
							intent2.setAction(Intent.ACTION_VIEW);
							intent2.setData(personUri);
							startActivity(intent2);
							break;
						case 3:// 删除
							showDelete(cb.getContactId(), position);
							break;
						}
					}
				}).show();
	}
	
	// 删除联系人方法
	private void showDelete(final int contactsID, final int position) {
		new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher).setTitle("是否删除此联系人")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				//源码删除
				Uri deleteUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactsID);
				Uri lookupUri = ContactsContract.Contacts.getLookupUri(HomeContactActivity.this.getContentResolver(), deleteUri);
				if(lookupUri != Uri.EMPTY){
					HomeContactActivity.this.getContentResolver().delete(deleteUri, null, null);
				}
				// TODO
				Toast.makeText(HomeContactActivity.this, "该联系人已经被删除.", Toast.LENGTH_SHORT).show();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
			}
		}).show();
	}
	
	public static String[] SMS_COLUMNS = new String[]{ 
		"thread_id"
	};
	private String getSMSThreadId(String adddress){
		Cursor cursor = null;
		ContentResolver contentResolver = getContentResolver();
		cursor = contentResolver.query(Uri.parse("content://sms"), SMS_COLUMNS, " address like '%" + adddress + "%' ", null, null); 
		String threadId = "";
		if (cursor == null || cursor.getCount() > 0){
			cursor.moveToFirst();
			threadId = cursor.getString(0);
			cursor.close();
			return threadId;
		}else{
			cursor.close();
			return threadId;
		}
	}

	// I encountered an interesting problem with a TextWatcher listening for
	// changes in an EditText.
	// The afterTextChanged method was called, each time, the device orientation
	// changed.
	// An answer on Stackoverflow let me understand what was happening: Android
	// recreates the activity, and
	// the automatic restoration of the state of the input fields, is happening
	// after onCreate had finished,
	// where the TextWatcher was added as a TextChangedListener.The solution to
	// the problem consisted in adding
	// the TextWatcher in onPostCreate, which is called after restoration has
	// taken place
	//
	// http://stackoverflow.com/questions/6028218/android-retain-callback-state-after-configuration-change/6029070#6029070
	// http://stackoverflow.com/questions/5151095/textwatcher-called-even-if-text-is-set-before-adding-the-watcher
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		mSearchView.addTextChangedListener(filterTextWatcher);
		super.onPostCreate(savedInstanceState);
	}

	private void setListAdaptor() {
		// create instance of PinnedHeaderAdapter and set adapter to list view
		mAdaptor = new PinnedHeaderAdapter(this, this, mListItems, mListSectionPos);
		mListView.setAdapter(mAdaptor);

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		// set header view
		View pinnedHeaderView = inflater.inflate(R.layout.section_row_view, mListView, false);
		mListView.setPinnedHeaderView(pinnedHeaderView);

		// set index bar view
		IndexBarView indexBarView = (IndexBarView) inflater.inflate(R.layout.index_bar_view, mListView, false);
		indexBarView.setData(mListView, mListItems, mListSectionPos);
		mListView.setIndexBarView(indexBarView);

		// set preview text view
		View previewTextView = inflater.inflate(R.layout.preview_view, mListView, false);
		mListView.setPreviewView(previewTextView);

		// for configure pinned header view on scroll change
		mListView.setOnScrollListener(mAdaptor);
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) {
			String str = s.toString();
			if (mAdaptor != null && str != null)
				mAdaptor.getFilter().filter(str);
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}
	};

	public class ListFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// NOTE: this function is *always* called from a background thread,
			// and
			// not the UI thread.
			String constraintStr = constraint.toString().toLowerCase(Locale.getDefault());
			FilterResults result = new FilterResults();

			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<ContactBean> filterItems = new ArrayList<ContactBean>();

				synchronized (this) {
					for (ContactBean item : mItems) {
						if (item.getSortKey().toLowerCase(Locale.getDefault()).startsWith(constraintStr)) {
							filterItems.add(item);
						}
					}
					result.count = filterItems.size();
					result.values = filterItems;
				}
			} else {
				synchronized (this) {
					result.count = mItems.size();
					result.values = mItems;
				}
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			ArrayList<ContactBean> filtered = (ArrayList<ContactBean>) results.values;
			setIndexBarViewVisibility(constraint.toString());
			// sort array and extract sections in background Thread
			new Poplulate().execute(filtered);
		}

	}
	
	@Override
	public Filter getListFilter(){
		return new ListFilter();
	}

	private void setIndexBarViewVisibility(String constraint) {
		// hide index bar for search results
		if (constraint != null && constraint.length() > 0) {
			mListView.setIndexBarVisibility(false);
		} else {
			mListView.setIndexBarVisibility(true);
		}
	}

	// sort array and extract sections in background Thread here we use
	// AsyncTask
	private class Poplulate extends AsyncTask<ArrayList<ContactBean>, Void, Void> {

		@Override
		protected void onPreExecute() {
//			// show loading indicator
//			showLoading(mListView, mLoadingView, mEmptyView);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(ArrayList<ContactBean>... params) {
			mListItems.clear();
			mListSectionPos.clear();
			ArrayList<ContactBean> items = params[0];
			if (items.size() > 0) {
				// NOT forget to sort array
				Collections.sort(items, new SortIgnoreCase());
				String prev_section = "";
//				for (ContactBean current_item : list){
//					String current_section = current_item.getSortKey().substring(0, 1).toUpperCase(Locale.getDefault());
//					if (!prev_section.equals(current_section)) {
//						mListItems.add(current_section);
//						mListItems.add(current_item.getDisplayName());
//						// array list of section positions
//						mListSectionPos.add(mListItems.indexOf(current_section));
//						prev_section = current_section;
//					} else {
//						mListItems.add(current_item.getDisplayName());
//					}
//				}
				
				for (ContactBean current_item : items) {
					
					String current_section = current_item.getSortKey().substring(0, 1).toUpperCase(Locale.getDefault());

					if (!prev_section.equals(current_section)) {
						ContactBean section_title = new ContactBean();
						section_title.setDisplayName(current_section);
						mListItems.add(section_title);
						mListItems.add(current_item);
						// array list of section positions
						mListSectionPos.add(mListItems.indexOf(section_title));
						prev_section = current_section;
					} else {
						mListItems.add(current_item);
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!isCancelled()) {
				if (mListItems.size() <= 0) {
					showEmptyText(mListView, mLoadingView, mEmptyView);
				} else {
					setListAdaptor();
					showContent(mListView, mLoadingView, mEmptyView);
				}
			}
			super.onPostExecute(result);
		}
	}

	public class SortIgnoreCase implements Comparator<ContactBean> {
		public int compare(ContactBean s1, ContactBean s2) {
			return s1.getSortKey().compareToIgnoreCase(s2.getSortKey());
		}
	}
	


//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		if (mListItems != null && mListItems.size() > 0) {
//			outState.putStringArrayList("mListItems", mListItems);
//		}
//		if (mListSectionPos != null && mListSectionPos.size() > 0) {
//			outState.putIntegerArrayList("mListSectionPos", mListSectionPos);
//		}
//		String searchText = mSearchView.getText().toString();
//		if (searchText != null && searchText.length() > 0) {
//			outState.putString("constraint", searchText);
//		}
//		super.onSaveInstanceState(outState);
//	}
	
	private void showLoading(View contentView, View loadingView, View emptyView) {
		contentView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);
	}

	private void showContent(View contentView, View loadingView, View emptyView) {
		contentView.setVisibility(View.VISIBLE);
		loadingView.setVisibility(View.GONE);
		emptyView.setVisibility(View.GONE);
	}

	private void showEmptyText(View contentView, View loadingView, View emptyView) {
		contentView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		emptyView.setVisibility(View.VISIBLE);
	}
}
