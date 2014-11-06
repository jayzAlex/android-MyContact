/**
 * Project Name:MyContact
 * File Name:sss.java
 * Package Name:xu.ye.uitl
 * Date:2014-1-26下午3:45:54
 * Copyright (c) 2014, 鸿程系统 All Rights Reserved.
 *
 */

package xu.ye.uitl;

import android.util.SparseArray;
import android.view.View;

/**
 * ClassName:sss <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014-1-26 下午3:45:54 <br/>
 * 
 * @author zja
 * @version
 * @since JDK 1.6
 * @see
 */
public class ViewHolder {
	// I added a generic return type to reduce the casting noise in client code
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}
