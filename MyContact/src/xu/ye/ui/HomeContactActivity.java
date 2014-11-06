// @author Bhavya Mehta
package xu.ye.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import xu.ye.R;
import xu.ye.bean.ContactBean;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.listviewfilter.IPinnedListFilter;
import com.example.listviewfilter.PinnedHeaderAdapter;
import com.example.listviewfilter.ui.IndexBarView;
import com.example.listviewfilter.ui.PinnedHeaderListView;

// Activity that display customized list view and search box
public class HomeContactActivity extends Activity implements IPinnedListFilter{

	// unsorted list items
	ArrayList<String> mItems;

	// array list to store section positions
	ArrayList<Integer> mListSectionPos;

	// array list to store listView data
	ArrayList<String> mListItems;

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

	ArrayList<ContactBean> list;

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
		mItems = new ArrayList<String>();
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		init();
		mListSectionPos = new ArrayList<Integer>();
		mListItems = new ArrayList<String>();

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
			if (cursor != null && cursor.getCount() > 0) {
				
				contactIdMap = new HashMap<Integer, ContactBean>();
				
				list = new ArrayList<ContactBean>();
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
						list.add(cb);
						contactIdMap.put(contactId, cb);
						mItems.add(sortKey);
					}
				}
				if (list.size() > 0) {
//					setAdapter(list);
					new Poplulate().execute(mItems);
				} else {
					showEmptyText(mListView, mLoadingView, mEmptyView);
				}
			}
		}

	}

	private void setupViews() {
		setContentView(R.layout.main_act);
		mSearchView = (EditText) findViewById(R.id.search_view);
		mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
		mListView = (PinnedHeaderListView) findViewById(R.id.list_view);
		mEmptyView = (TextView) findViewById(R.id.empty_view);
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
				ArrayList<String> filterItems = new ArrayList<String>();

				synchronized (this) {
					for (String item : mItems) {
						if (item.toLowerCase(Locale.getDefault()).startsWith(constraintStr)) {
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
			ArrayList<String> filtered = (ArrayList<String>) results.values;
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
	private class Poplulate extends AsyncTask<ArrayList<String>, Void, Void> {

		@Override
		protected void onPreExecute() {
//			// show loading indicator
//			showLoading(mListView, mLoadingView, mEmptyView);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(ArrayList<String>... params) {
			mListItems.clear();
			mListSectionPos.clear();
			ArrayList<String> items = params[0];
			if (mItems.size() > 0) {

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
				
				for (String current_item : items) {
					String current_section = current_item.substring(0, 1).toUpperCase(Locale.getDefault());

					if (!prev_section.equals(current_section)) {
						mListItems.add(current_section);
						mListItems.add(current_item);
						// array list of section positions
						mListSectionPos.add(mListItems.indexOf(current_section));
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

	public class SortIgnoreCase implements Comparator<String> {
		public int compare(String s1, String s2) {
			return s1.compareToIgnoreCase(s2);
		}
	}
	


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (mListItems != null && mListItems.size() > 0) {
			outState.putStringArrayList("mListItems", mListItems);
		}
		if (mListSectionPos != null && mListSectionPos.size() > 0) {
			outState.putIntegerArrayList("mListSectionPos", mListSectionPos);
		}
		String searchText = mSearchView.getText().toString();
		if (searchText != null && searchText.length() > 0) {
			outState.putString("constraint", searchText);
		}
		super.onSaveInstanceState(outState);
	}
	
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
