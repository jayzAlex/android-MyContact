package xu.ye.ui;
//// @author Bhavya Mehta
//package xu.ye.ui;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Locale;
//
//import xu.ye.R;
//import xu.ye.bean.ContactBean;
//import xu.ye.view.ContactIndexBarView2;
//import xu.ye.view.adapter.ContactPinnedHeaderAdapter2;
//import android.app.Activity;
//import android.content.AsyncQueryHandler;
//import android.content.ContentResolver;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Filter;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.example.listviewfilter.ui.PinnedHeaderListView;
//
//// Activity that display customized list view and search box
//public class HomeContactActivity4 extends Activity {
//
////	// an array of countries to display in the list
////	static final String[] ITEMS = new String[] { "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
////			"Eritrea", "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland", "Afghanistan",
////			"Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica",
////			"Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain",
////			"Bangladesh", "Barbados", "Belarus", "Belgium", "Monaco", "Mongolia", "Montserrat", "Morocco",
////			"Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
////			"New Caledonia", "New Zealand", "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras",
////			"Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy",
////			"Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos", "Latvia",
////			"Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Nicaragua", "Niger",
////			"Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Marianas", "Norway", "Oman", "Pakistan",
////			"Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland",
////			"Portugal", "Puerto Rico", "Qatar", "French Southern Territories", "Gabon", "Georgia", "Germany", "Ghana",
////			"Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea",
////			"Guinea-Bissau", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova",
////			"Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory",
////			"Saint Vincent and the Grenadines", "Samoa", "San Marino", "Saudi Arabia", "Senegal", "Seychelles",
////			"Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
////			"South Georgia and the South Sandwich Islands", "South Korea", "Spain", "Sri Lanka", "Sudan", "Suriname",
////			"Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan",
////			"Tanzania", "Thailand", "The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago",
////			"Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
////			"United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands",
////			"Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Virgin Islands",
////			"Wallis and Futuna", "Western Sahara", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso",
////			"Burundi", "Cote d'Ivoire", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
////			"Central African Republic", "Chad", "Chile", "China", "Reunion", "Romania", "Russia", "Rwanda",
////			"Sqo Tome and Principe", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia",
////			"Saint Pierre and Miquelon", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Christmas Island",
////			"Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia", "Cuba",
////			"Cyprus", "Czech Republic", "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica",
////			"Dominican Republic", "Former Yugoslav Republic of Macedonia", "France", "French Guiana",
////			"French Polynesia", "Macau", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
////			"Marshall Islands", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe" };
////
//	public static final int INTENT_SELECT_CONTACTBEAN = 1;
//	
//	// unsorted list items
//	ArrayList<ContactBean> mItems;
//
//	// array list to store section positions
//	ArrayList<Integer> mListSectionPos;
//
//	// array list to store listView data
//	ArrayList<ContactBean> mListItems;
//
//	// custom list view with pinned header
//	PinnedHeaderListView mListView;
//
//	// custom adapter
//	ContactPinnedHeaderAdapter2 mAdaptor;
//
//	// search box
//	EditText mSearchView;
//
//	// loading view
//	ProgressBar mLoadingView;
//
//	// empty view
//	TextView mEmptyView;
//
//	private MyAsyncQueryHandler asyncQuery;
//
//	public HashMap<Integer, ContactBean> contactIdMap = null;
//	
//	Poplulate poplulate;
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		// UI elements
//		poplulate = new Poplulate();
//		
//		setupViews();
//
//		// Array to ArrayList
////		mItems = new ArrayList<String>(Arrays.asList(ITEMS));
//		mListSectionPos = new ArrayList<Integer>();
//		mListItems = new ArrayList<ContactBean>();
//		
//
//		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
//		init();
////		// for handling configuration change
////		if (savedInstanceState != null) {
////			mListItems = savedInstanceState.getParcelableArrayList("mListItems");
////			mListSectionPos = savedInstanceState.getIntegerArrayList("mListSectionPos");
////
////			if (mListItems != null && mListItems.size() > 0 && mListSectionPos != null && mListSectionPos.size() > 0) {
////				setListAdaptor();
////			}
////
////			String constraint = savedInstanceState.getString("constraint");
////			if (constraint != null && constraint.length() > 0) {
////				mSearchView.setText(constraint);
////				setIndexBarViewVisibility(constraint);
////			}
////		} else {
////			new Poplulate().execute(mItems);
////		}
//	}
//	
//	private void init(){
//		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人的Uri
//		String[] projection = { 
//				ContactsContract.CommonDataKinds.Phone._ID,
//				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//				ContactsContract.CommonDataKinds.Phone.DATA1,
//				"sort_key",
//				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
//				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
//				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
//		}; // 查询的列
//		asyncQuery.startQuery(0, null, uri, projection, null, null,
//				"sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询
//	}
//	
//	/**
//	 * 数据库异步查询类AsyncQueryHandler
//	 * 
//	 * @author administrator
//	 * 
//	 */
//	private class MyAsyncQueryHandler extends AsyncQueryHandler {
//
//		public MyAsyncQueryHandler(ContentResolver cr) {
//			super(cr);
//		}
//
//		/**
//		 * 查询结束的回调函数
//		 */
//		@Override
//		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
//			if (cursor != null && cursor.getCount() > 0) {
//				
//				contactIdMap = new HashMap<Integer, ContactBean>();
//				
//				mItems = new ArrayList<ContactBean>();
//				cursor.moveToFirst();
//				for (int i = 0; i < cursor.getCount(); i++) {
//					cursor.moveToPosition(i);
//					String name = cursor.getString(1);
//					String number = cursor.getString(2);
//					String sortKey = cursor.getString(3);
//					int contactId = cursor.getInt(4);
//					Long photoId = cursor.getLong(5);
//					String lookUpKey = cursor.getString(6);
//
//					if (contactIdMap.containsKey(contactId)) {
//						
//					}else{
//						
//						ContactBean cb = new ContactBean();
//						cb.setDisplayName(name);
////					if (number.startsWith("+86")) {// 去除多余的中国地区号码标志，对这个程序没有影响。
////						cb.setPhoneNum(number.substring(3));
////					} else {
//						cb.setPhoneNum(number);
////					}
//						cb.setSortKey(sortKey);
//						cb.setContactId(contactId);
//						cb.setPhotoId(photoId);
//						cb.setLookUpKey(lookUpKey);
//						mItems.add(cb);
//						
//						contactIdMap.put(contactId, cb);
//						
//					}
//				}
//				if (mItems.size() > 0) {
//					new Poplulate().execute();
//				} else {
//					poplulate.showEmptyText(mListView, mLoadingView, mEmptyView);
//				}
//			}
//		}
//
//	}
//
//	private void setupViews() {
//		setContentView(R.layout.main_act);
//		mSearchView = (EditText) findViewById(R.id.search_view);
//		mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
//		mListView = (PinnedHeaderListView) findViewById(R.id.list_view);
//		mEmptyView = (TextView) findViewById(R.id.empty_view);
//		poplulate.showLoading(mListView, mLoadingView, mEmptyView);
//	}
//
//	// I encountered an interesting problem with a TextWatcher listening for
//	// changes in an EditText.
//	// The afterTextChanged method was called, each time, the device orientation
//	// changed.
//	// An answer on Stackoverflow let me understand what was happening: Android
//	// recreates the activity, and
//	// the automatic restoration of the state of the input fields, is happening
//	// after onCreate had finished,
//	// where the TextWatcher was added as a TextChangedListener.The solution to
//	// the problem consisted in adding
//	// the TextWatcher in onPostCreate, which is called after restoration has
//	// taken place
//	//
//	// http://stackoverflow.com/questions/6028218/android-retain-callback-state-after-configuration-change/6029070#6029070
//	// http://stackoverflow.com/questions/5151095/textwatcher-called-even-if-text-is-set-before-adding-the-watcher
//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		mSearchView.addTextChangedListener(filterTextWatcher);
//		super.onPostCreate(savedInstanceState);
//	}
//
//	private void setListAdaptor() {
//		// create instance of PinnedHeaderAdapter and set adapter to list view
//		mAdaptor = new ContactPinnedHeaderAdapter2(this, mListItems, mListSectionPos);
//		mListView.setAdapter(mAdaptor);
//
//		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//
//		// set header view
//		View pinnedHeaderView = inflater.inflate(R.layout.section_row_view, mListView, false);
//		mListView.setPinnedHeaderView(pinnedHeaderView);
//
//		// set index bar view
//		ContactIndexBarView2 indexBarView = (ContactIndexBarView2) inflater.inflate(R.layout.contact_index_bar_view2, mListView, false);
//		indexBarView.setData(mListView, mListItems, mListSectionPos);
//		mListView.setIndexBarView(indexBarView);
//
//		// set preview text view
//		View previewTextView = inflater.inflate(R.layout.preview_view, mListView, false);
//		mListView.setPreviewView(previewTextView);
//
//		// for configure pinned header view on scroll change
//		mListView.setOnScrollListener(mAdaptor);
//	}
//
//	private TextWatcher filterTextWatcher = new TextWatcher() {
//		public void afterTextChanged(Editable s) {
//			String str = s.toString();
//			if (mAdaptor != null && str != null)
//				mAdaptor.getFilter().filter(str);
//		}
//
//		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//		}
//
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//		}
//	};
//
//	public class ListFilter extends Filter {
//		@Override
//		protected FilterResults performFiltering(CharSequence constraint) {
////			// NOTE: this function is *always* called from a background thread,
////			// and
////			// not the UI thread.
////			String constraintStr = constraint.toString().toLowerCase(Locale.getDefault());
////			FilterResults result = new FilterResults();
////
////			if (constraint != null && constraint.toString().length() > 0) {
////				ArrayList<String> filterItems = new ArrayList<String>();
////
////				synchronized (this) {
////					for (String item : mItems) {
////						if (item.toLowerCase(Locale.getDefault()).startsWith(constraintStr)) {
////							filterItems.add(item);
////						}
////					}
////					result.count = filterItems.size();
////					result.values = filterItems;
////				}
////			} else {
////				synchronized (this) {
////					result.count = mItems.size();
////					result.values = mItems;
////				}
////			}
////			return result;
//			String str = constraint.toString();
//			FilterResults results = new FilterResults();
//			ArrayList<ContactBean> contactList = new ArrayList<ContactBean>();
//			if (mItems != null && mItems.size() != 0) {
//				for(ContactBean cb : mItems){
//					if(cb.getFormattedNumber().indexOf(str)>=0 || cb.getPhoneNum().indexOf(str)>-1){
//						contactList.add(cb);
//					}
//				}
//			}
//			results.values = contactList;
//			results.count = contactList.size();
//			return results;
//		}
//
//		@SuppressWarnings("unchecked")
//		@Override
//		protected void publishResults(CharSequence constraint, FilterResults results) {
//			ArrayList<String> filtered = (ArrayList<String>) results.values;
//			setIndexBarViewVisibility(constraint.toString());
//			// sort array and extract sections in background Thread
//			new Poplulate().execute(filtered);
//		}
//
//	}
//
//	private void setIndexBarViewVisibility(String constraint) {
//		// hide index bar for search results
//		if (constraint != null && constraint.length() > 0) {
//			mListView.setIndexBarVisibility(false);
//		} else {
//			mListView.setIndexBarVisibility(true);
//		}
//	}
//
//	// sort array and extract sections in background Thread here we use
//	// AsyncTask
//	private class Poplulate extends AsyncTask<ArrayList<String>, Void, Void> {
//
//		public void showLoading(View contentView, View loadingView, View emptyView) {
//			contentView.setVisibility(View.GONE);
//			loadingView.setVisibility(View.VISIBLE);
//			emptyView.setVisibility(View.GONE);
//		}
//
//		public void showContent(View contentView, View loadingView, View emptyView) {
//			contentView.setVisibility(View.VISIBLE);
//			loadingView.setVisibility(View.GONE);
//			emptyView.setVisibility(View.GONE);
//		}
//
//		private void showEmptyText(View contentView, View loadingView, View emptyView) {
//			contentView.setVisibility(View.GONE);
//			loadingView.setVisibility(View.GONE);
//			emptyView.setVisibility(View.VISIBLE);
//		}
//
//		@Override
//		protected void onPreExecute() {
//			// show loading indicator
////			showLoading(mListView, mLoadingView, mEmptyView);
//			super.onPreExecute();
//		}
//
//		@Override
//		protected Void doInBackground(ArrayList<String>... params) {
//			mListItems.clear();
//			mListSectionPos.clear();
////			ArrayList<String> items = params[0];
//			ArrayList<ContactBean> items = mItems;
//			if (mItems.size() > 0) {
//				// NOT forget to sort array
//				Collections.sort(items, new SortIgnoreCase());
//
//				String prev_section = "";
//				for (ContactBean current_item : items) {
//					String current_section = current_item.getSortKey().substring(0, 1).toUpperCase(Locale.getDefault());
//					ContactBean current_contactBean = new ContactBean();
//					current_contactBean.setDisplayName(current_section);
//					if (!prev_section.equals(current_section)) {
//						mListItems.add(current_contactBean);
//						mListItems.add(current_item);
//						// array list of section positions
//						mListSectionPos.add(mListItems.indexOf(current_section));
//						prev_section = current_section;
//					} else {
//						mListItems.add(current_item);
//					}
//				}
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			if (!isCancelled()) {
//				if (mListItems.size() <= 0) {
//					showEmptyText(mListView, mLoadingView, mEmptyView);
//				} else {
//					setListAdaptor();
//					showContent(mListView, mLoadingView, mEmptyView);
//				}
//			}
//			super.onPostExecute(result);
//		}
//	}
//
//	public class SortIgnoreCase implements Comparator<ContactBean> {
//		public int compare(ContactBean s1, ContactBean s2) {
//			return s1.getSortKey().compareToIgnoreCase(s2.getSortKey());
//		}
//	}
//
////	@Override
////	protected void onSaveInstanceState(Bundle outState) {
////		if (mListItems != null && mListItems.size() > 0) {
////			outState.putStringArrayList("mListItems", mListItems);
////		}
////		if (mListSectionPos != null && mListSectionPos.size() > 0) {
////			outState.putIntegerArrayList("mListSectionPos", mListSectionPos);
////		}
////		String searchText = mSearchView.getText().toString();
////		if (searchText != null && searchText.length() > 0) {
////			outState.putString("constraint", searchText);
////		}
////		super.onSaveInstanceState(outState);
////	}
//}
