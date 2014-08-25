package xu.ye.view.adapter;

import java.util.List;

import xu.ye.R;
import xu.ye.bean.CallLogBean;
import xu.ye.bean.ContactBean;
import xu.ye.db.DbUtils;
import xu.ye.uitl.ViewHolder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PrefsListAdapter extends BaseAdapter implements View.OnClickListener{

	private Context context;
	private List<ContactBean> list;
	private LayoutInflater inflater;
//	public boolean box_option_delete_show;
	
//	public boolean isBox_option_delete_show() {
//		return box_option_delete_show;
//	}

	public PrefsListAdapter(Context context, List<ContactBean> list) {

		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

	    if (convertView == null) {
	        convertView = inflater.inflate(R.layout.prefslist_item, parent, false);
	    }
	    TextView displayname = ViewHolder.get(convertView, R.id.displayname);
	    TextView phoneNum = ViewHolder.get(convertView, R.id.phonenum);
	    ImageView box_option_delete = ViewHolder.get(convertView, R.id.box_option_delete);
	    ContactBean contactBean = list.get(position);
	    displayname.setText(contactBean.getDisplayName());
	    phoneNum.setText(contactBean.getPhoneNum());
	    box_option_delete.setTag(position);
	    box_option_delete.setOnClickListener(this);
		return convertView;
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		ContactBean contactBean = list.get(position);
		DbUtils dbUtils = new DbUtils(context);
		dbUtils.deleteBlkwhi(contactBean.getPhoneNum(), contactBean.getBlkwhi());
		list.remove(position);
		notifyDataSetChanged();
	}
	

}
