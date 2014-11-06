//// @author jayzAlex
//package xu.ye.view.adapter;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Locale;
//
//import xu.ye.R;
//import xu.ye.bean.ContactBean;
//import android.content.ContentUris;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.provider.ContactsContract;
//import android.provider.ContactsContract.Contacts;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.BaseAdapter;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.QuickContactBadge;
//import android.widget.TextView;
//
//import com.example.listviewfilter.IPinnedHeader;
//import com.example.listviewfilter.ui.MainActivity;
//import com.example.listviewfilter.ui.PinnedHeaderListView;
//
//// Customized adaptor to populate data in PinnedHeaderListView
//public class ContactPinnedHeaderAdapter2 extends BaseAdapter implements OnScrollListener, IPinnedHeader, Filterable {
//
//	private static final int TYPE_ITEM = 0;
//	private static final int TYPE_SECTION = 1;
//	private static final int TYPE_MAX_COUNT = TYPE_SECTION + 1;
//
//	LayoutInflater mLayoutInflater;
//	int mCurrentSectionPosition = 0, mNextSectionPostion = 0;
//
//	// array list to store section positions
//	ArrayList<Integer> mListSectionPos;
//
//	// array list to store list view data
//	ArrayList<ContactBean> mListItems;
//
//	// context object
//	Context mContext;
//
//	public ContactPinnedHeaderAdapter2(Context context, ArrayList<ContactBean> listItems,ArrayList<Integer> listSectionPos) {
//		this.mContext = context;
//		this.mListItems = listItems;
//		this.mListSectionPos = listSectionPos;
//
//		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
//
//	@Override
//	public int getCount() {
//		return mListItems.size();
//	}
//
//	@Override
//	public boolean areAllItemsEnabled() {
//		return false;
//	}
//
//	@Override
//	public boolean isEnabled(int position) {
//		return !mListSectionPos.contains(position);
//	}
//
//	@Override
//	public int getViewTypeCount() {
//		return TYPE_MAX_COUNT;
//	}
//
//	@Override
//	public int getItemViewType(int position) {
//		return mListSectionPos.contains(position) ? TYPE_SECTION : TYPE_ITEM;
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return mListItems.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return mListItems.get(position).hashCode();
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder = null;
//		int type = getItemViewType(position);
//		if (convertView == null) {
//			holder = new ViewHolder();
//			switch (type) {
//			case TYPE_ITEM:
//				convertView = mLayoutInflater.inflate(R.layout.contact_home_list_item, null);
//				holder.qcb = (QuickContactBadge) convertView.findViewById(R.id.qcb);
//				holder.name = (TextView) convertView.findViewById(R.id.name);
//				holder.number = (TextView) convertView
//						.findViewById(R.id.number);
//				break;
//			case TYPE_SECTION:
//				convertView = mLayoutInflater.inflate(R.layout.section_row_view, null);
//				holder.name = (TextView) convertView.findViewById(R.id.row_title);
//				break;
//			}
//			
//			
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//
//		switch (type) {
//		case TYPE_ITEM:
//			ContactBean cb = mListItems.get(position);
//			String name = cb.getDisplayName();
//			String number = cb.getPhoneNum();
//			holder.name.setText(name);
//			holder.number.setText(number);
//			holder.qcb.assignContactUri(Contacts.getLookupUri(cb.getContactId(), cb.getLookUpKey()));
//			if(0 == cb.getPhotoId()){
//				holder.qcb.setImageResource(R.drawable.touxiang);
//			}else{
//				Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, cb.getContactId());
//				InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(mContext.getContentResolver(), uri); 
//				Bitmap contactPhoto = BitmapFactory.decodeStream(input);
//				holder.qcb.setImageBitmap(contactPhoto);
//			}
//			break;
//		case TYPE_SECTION:
//			convertView = mLayoutInflater.inflate(R.layout.section_row_view, null);
//			holder.name.setText(mListItems.get(position).getDisplayName());
//			break;
//		}		
//		
////		holder.textView.setText(mListItems.get(position).toString());
//		
//		return convertView;
//	}
//
//	@Override
//	public int getPinnedHeaderState(int position) {
//		// hide pinned header when items count is zero OR position is less than
//		// zero OR
//		// there is already a header in list view
//		if (getCount() == 0 || position < 0 || mListSectionPos.indexOf(position) != -1) {
//			return PINNED_HEADER_GONE;
//		}
//
//		// the header should get pushed up if the top item shown
//		// is the last item in a section for a particular letter.
//		mCurrentSectionPosition = getCurrentSectionPosition(position);
//		mNextSectionPostion = getNextSectionPosition(mCurrentSectionPosition);
//		if (mNextSectionPostion != -1 && position == mNextSectionPostion - 1) {
//			return PINNED_HEADER_PUSHED_UP;
//		}
//
//		return PINNED_HEADER_VISIBLE;
//	}
//
//	public int getCurrentSectionPosition(int position) {
//		String listChar = mListItems.get(position).toString().substring(0, 1).toUpperCase(Locale.getDefault());
//		return mListItems.indexOf(listChar);
//	}
//
//	public int getNextSectionPosition(int currentSectionPosition) {
//		int index = mListSectionPos.indexOf(currentSectionPosition);
//		if ((index + 1) < mListSectionPos.size()) {
//			return mListSectionPos.get(index + 1);
//		}
//		return mListSectionPos.get(index);
//	}
//
//	@Override
//	public void configurePinnedHeader(View v, int position) {
//		// set text in pinned header
//		TextView header = (TextView) v;
//		mCurrentSectionPosition = getCurrentSectionPosition(position);
//		header.setText(mListItems.get(mCurrentSectionPosition).getDisplayName());
//	}
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
//		if (view instanceof PinnedHeaderListView) {
//			((PinnedHeaderListView) view).configureHeaderView(firstVisibleItem);
//		}
//	}
//
//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public Filter getFilter() {
//		return ((MainActivity) mContext).new ListFilter();
//	}
//
////	public static class ViewHolder {
////		public TextView textView;
////	}
//	private static class ViewHolder {
//		QuickContactBadge qcb;
//		TextView name;
//		TextView number;
//	}
//}
