/**
 * Project Name:MyContact
 * File Name:TrashAnim.java
 * Package Name:xu.ye.view.ui
 * Date:2014-1-28下午3:20:25
 * Copyright (c) 2014, 鸿程系统 All Rights Reserved.
 *
*/

package xu.ye.view.ui;

import xu.ye.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * ClassName:TrashAnim <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2014-1-28 下午3:20:25 <br/>
 * @author   zja
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class TrashView extends View{
	private Bitmap trash;
	private Rect bounds;
	private Paint bitmapPaint;

	public TrashView(Context context) {
		super(context);
		bounds = new Rect();
		bitmapPaint = createBaseBitmapPaint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int measuredHeight = getMeasuredHeight();
		int measuredWidth = getMeasuredWidth();
		initTrashIcon();
		canvas.drawBitmap(trash, (measuredWidth / 2) - (bounds.width() / 2) - (trash.getWidth() / 2) - 10, 0, bitmapPaint);
	}
	
	private void initTrashIcon() {
		if (trash == null) {
			trash = getImage(R.drawable.trash0, getMeasuredHeight(), getMeasuredHeight());
		}
	}
	
	private Bitmap getImage (int id, int width, int height) {
	    Bitmap bmp = BitmapFactory.decodeResource( getResources(), id );
	    Bitmap img = Bitmap.createScaledBitmap(bmp, width, height, true);
	    if (bmp != null && !isInEditMode()) {
	        bmp.recycle();
	    }
	    invalidate();
	    return img;
	}
	
	private Paint createBaseBitmapPaint() {
		Paint bitmapPaint = new Paint();
		bitmapPaint.setAntiAlias(true);
		bitmapPaint.setFilterBitmap(true);
		bitmapPaint.setDither(true);
		return bitmapPaint;
	}
	
}

