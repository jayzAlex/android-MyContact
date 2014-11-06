/**
 * Project Name:MyContact
 * File Name:ss.java
 * Package Name:xu.ye.view.ui
 * Date:2014-1-28下午2:38:41
 * Copyright (c) 2014, 鸿程系统 All Rights Reserved.
 *
*/

package xu.ye.view.ui;

import android.view.View;

/**
 * ClassName:ss <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2014-1-28 下午2:38:41 <br/>
 * @author   zja
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public interface OnDeleteDropZoneListener{
	
	public void createDeleteZone();
	
	public void hideDeleteView();
	
	public void startAnim();
	
	public void stopAnim(); 
	
	public View getAnimZone();
	
}

