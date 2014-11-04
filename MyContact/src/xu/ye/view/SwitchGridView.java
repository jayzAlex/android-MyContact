package xu.ye.view;

import xu.ye.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/***
 * 自定义拖拽GridView
 * 
 * @author zhangjia
 * 
 */

public class SwitchGridView extends GridView {

    private WindowManager windowManager;// windows窗口控制类
    private WindowManager.LayoutParams windowParams;// 用于控制拖拽项的显示的参数

    private int scaledTouchSlop;// 判断滑动的一个距离,scroll的时候会用到(24)

    private ImageView dragImageView;// 被拖拽的项(item)，其实就是一个ImageView
    private int dragSrcPosition;// 手指拖动项原始在列表中的位置
    private int dragPosition;// 手指点击准备拖动的时候,当前拖动项在列表中的位置.

    private int dragPointX;// 在当前数据项中的位置
    private int dragPointY;// 在当前数据项中的位置
    private int dragOffsetX;// 当前视图和屏幕的距离(这里只使用了x方向上)
    private int dragOffsetY;// 当前视图和屏幕的距离(这里只使用了y方向上)

    private int upScrollBounce;// 拖动的时候，开始向上滚动的边界
    private int downScrollBounce;// 拖动的时候，开始向下滚动的边界

    private int temChangId;// 临时交换id

    private boolean isDoTouch = true;// touch是否可用

    private boolean isHide = false;// 是否隐藏

    private static final String TAG = "SwitchGridView";

    private OnDeleteDropZoneListener onDeleteDropZoneListener;

    //	private OnLongPressedListener onLongPressedListener;

    private OnDialListener onDialListener;

    //	public OnLongPressedListener getOnLongPressedListener() {
    //		return onLongPressedListener;
    //	}
    //
    //	public void setOnLongPressedListener(
    //			OnLongPressedListener onLongPressedListener) {
    //		this.onLongPressedListener = onLongPressedListener;
    //	}

    public OnDialListener getOnDialListener() {
        return onDialListener;
    }

    public void setOnDialListener(OnDialListener onDialListener) {
        this.onDialListener = onDialListener;
    }

    public void setOnDeleteDropZoneListener(OnDeleteDropZoneListener onDeleteDropZoneListener) {
        this.onDeleteDropZoneListener = onDeleteDropZoneListener;
    }

    public void setDoTouch(boolean isDoTouch) {
        this.isDoTouch = isDoTouch;
    }

    public SwitchGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "super.onInterceptTouchEvent(ev):" + super.onInterceptTouchEvent(ev));
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();

            temChangId = dragSrcPosition = dragPosition = pointToPosition(x, y);

            if (dragPosition == AdapterView.INVALID_POSITION) {
                return super.onInterceptTouchEvent(ev);
            }

            if (SwitchGridAdapter.list.get(dragPosition) == null) {
                return super.onInterceptTouchEvent(ev);
            }

            ViewGroup itemView = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());

            dragPointX = x - itemView.getLeft();
            dragPointY = y - itemView.getTop();
            dragOffsetX = (int) (ev.getRawX() - x);
            dragOffsetY = (int) (ev.getRawY() - y);

            View dragger = itemView.findViewById(R.id.drag_grid_item);

            /***
             * 判断是否选中拖动图标
             */
            if (dragger != null && x > dragger.getLeft() - 20 && x < dragger.getRight() + 20
                    && y > dragger.getTop() - 20 && y < dragger.getBottom() + 20) {

                upScrollBounce = getHeight() / 4;
                downScrollBounce = getHeight() * 3 / 4;

                itemView.setDrawingCacheEnabled(true);
                Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
                startDrag(bm, x, y);// 初始话映像
                // dragger.setVisibility(View.INVISIBLE);// 隐藏该项.
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (dragImageView != null && dragPosition != INVALID_POSITION && isDoTouch) {
            int action = ev.getAction();
            //			if (action == MotionEvent.ACTION_DOWN) {
            //				Log.i(TAG, "handler.postDelayed(mLongPressed, 1000)");
            //				handler.postDelayed(mLongPressed, 1000);
            //			}
            //			if ((action == MotionEvent.ACTION_MOVE)
            //					|| (action == MotionEvent.ACTION_UP)) {
            //				Log.i(TAG, "handler.removeCallbacks(mLongPressed)");
            //				handler.removeCallbacks(mLongPressed);
            //			}

            switch (action) {
            case MotionEvent.ACTION_UP:
                int upX = (int) ev.getX();
                int upY = (int) ev.getY();
                stopDrag();// 删除映像
                onChange(upX, upY);
                // onGestureDetector(ev);
                onDrop(upX, upY);// 松开

                break;
            /***
             * 拖拽item
             */
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                onDrag(moveX, moveY);// 拖拽
                break;
            case MotionEvent.ACTION_DOWN:
                int downX = (int) ev.getX();
                int downY = (int) ev.getY();
                onHide(downX, downY);
                break;
            default:
                break;
            }
            return true;
        }
        Log.i(TAG, "3--onTouchEvent(ev):" + super.onTouchEvent(ev));
        return super.onTouchEvent(ev);
    }

    /**
     * 准备拖动，初始化拖动项的图像
     * 
     * @param bm
     * @param y
     */
    public void startDrag(Bitmap bm, int x, int y) {

        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP | Gravity.LEFT;
        windowParams.x = x - dragPointX + dragOffsetX;
        windowParams.y = y - dragPointY + dragOffsetY;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        windowParams.windowAnimations = 0;

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bm);
        windowManager = (WindowManager) getContext().getSystemService("window");
        windowManager.addView(imageView, windowParams);
        dragImageView = imageView;

    }

    /**
     * 拖动执行，在Move方法中执行
     * 
     * @param y
     */
    public void onDrag(int x, int y) {

        SwitchGridAdapter adapter = (SwitchGridAdapter) getAdapter();
        // 为了避免滑动到分割线的时候，返回-1的问题
        int tempPosition = pointToPosition(x, y);
        if (tempPosition != INVALID_POSITION) {
            dragPosition = tempPosition;
        }
        int drag_top = y - dragPointY;// 拖拽view的top值不能＜0，否则则出界.
        if (dragImageView != null && drag_top >= 0) {
            windowParams.alpha = 0.5f;// 透明度
            windowParams.y = y - dragPointY + dragOffsetY;// 移动y值.//记得要加上dragOffset，windowManager计算的是整个屏幕.(标题栏和状态栏都要算上)
            windowParams.x = x - dragPointX + dragOffsetX;// 移动y值.//记得要加上dragOffset，windowManager计算的是整个屏幕.(标题栏和状态栏都要算上)
            windowManager.updateViewLayout(dragImageView, windowParams);// 时时移动.
        }
        // doScroller(y);
        adapter.setHignlightPosition(dragPosition);
        touchUpInDeleteZoneDrop(x, y);

    }

    /***
     * 隐藏该选项
     */
    private void onHide(int x, int y) {
        // 获取适配器
        SwitchGridAdapter adapter = (SwitchGridAdapter) getAdapter();
        // 为了避免滑动到分割线的时候，返回-1的问题
        int tempPosition = pointToPosition(x, y);
        if (tempPosition != INVALID_POSITION) {
            dragPosition = tempPosition;
        }
        adapter.setIsHidePosition(dragPosition);
        Log.i(TAG, "1--onHide dragPosition:" + dragPosition + " dragSrcPosition:" + dragSrcPosition);
        onDeleteDropZoneListener.createDeleteZone();
    }

    /**
     * 停止拖动，删除影像
     */
    public void stopDrag() {
        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }

    }

    /***
     * 拖动时change
     */
    public void onChange(int x, int y) {
        Log.i(TAG, "1--onChange dragPosition:" + dragPosition + " dragSrcPosition:" + dragSrcPosition);

        // 超出边界处理(如果向上超过第二项Top的话，那么就放置在第一个位置)
        if (y < getChildAt(0).getTop() || x < getChildAt(0).getLeft()) {
            // 超出上边界
            dragPosition = 0;
            // 如果拖动超过最后一项的最下边那么就防止在最下边
        } else if (y > getChildAt(getChildCount() - 1).getBottom()) {
            // 超出下边界
            dragPosition = getAdapter().getCount() - 1;
            return;
        }

        Log.i(TAG, "2--onChange dragPosition:" + dragPosition + " dragSrcPosition:" + dragSrcPosition);
        // 数据交换
        if (dragPosition < getAdapter().getCount()) {
            SwitchGridAdapter adapter = (SwitchGridAdapter) getAdapter();
            adapter.isHidden = false;
            if (dragPosition != temChangId) {
                adapter.replace(temChangId, dragPosition);
                temChangId = dragPosition;
            }

        }
        Log.i(TAG, "3--onChange dragPosition:" + dragPosition + " dragSrcPosition:" + dragSrcPosition);
        // 为了避免滑动到分割线的时候，返回-1的问题
        int tempPosition = pointToPosition(x, y);
        if (tempPosition != INVALID_POSITION) {
            dragPosition = tempPosition;
        }
        Log.i(TAG, "4--onChange dragPosition:" + dragPosition + " dragSrcPosition:" + dragSrcPosition);

    }

    /***
     * 拖动放下的时候
     * 
     * @param x
     * @param y
     */
    public void onDrop(int x, int y) {
        SwitchGridAdapter adapter = (SwitchGridAdapter) getAdapter();
        // Log.i(TAG,
        // "onDrop dragPosition:"+dragPosition+" dragSrcPosition:"+dragSrcPosition);
        // if(dragPosition == dragSrcPosition){
        // adapter.showContactDialog(dragPosition);
        // gestureDetector.onTouchEvent(event);
        // }
        // 判断是否是短点击
        if (dragPosition == dragSrcPosition) {
            //			if (!isLongPressed) {
            onDialListener.onDial(dragPosition);
            //			}
        }
        //		isLongPressed = false;
        if (stopInDeleteZoneDrop(x, y)) {
            adapter.onDelete(dragSrcPosition);
        }
        adapter.setIsHidePosition(-1);// 不进行隐藏
        adapter.notifyDataSetChanged();
        onDeleteDropZoneListener.hideDeleteView();

    }

    // final GestureDetector gestureDetector = new GestureDetector(new
    // GestureDetector.SimpleOnGestureListener() {
    // public void onLongPress(MotionEvent e) {
    // Log.e(TAG, "Longpress detected");
    // }
    // @Override
    // public boolean onSingleTapUp(MotionEvent e) {
    // Log.e(TAG, "onSingleTapUp detected");
    // return super.onSingleTapUp(e);
    // }
    // });
    // public void onGestureDetector(MotionEvent ev){
    // if(dragPosition == dragSrcPosition){
    // SwitchGridAdapter adapter = (SwitchGridAdapter) getAdapter();
    // adapter.showContactDialog(dragPosition);
    // // gestureDetector.onTouchEvent(ev);
    // }
    // }

    final Handler handler = new Handler();

    //	private static boolean isLongPressed = false;
    //	Runnable mLongPressed = new Runnable() {
    //		public void run() {
    //			// SwitchGridAdapter adapter = (SwitchGridAdapter) getAdapter();
    //			// adapter.showContactDialog(dragPosition);
    //			onLongPressedListener.showContactDialog(dragPosition);
    //			isLongPressed = true;
    //		}
    //	};

    private boolean touchUpInDeleteZoneDrop(int x, int y) {
        Rect zone = new Rect();
        View animzone = onDeleteDropZoneListener.getAnimZone();
        animzone.getHitRect(zone);

        if (zone.intersect(x, y, x + 1, y + 1)) {
            onDeleteDropZoneListener.startAnim();
            return true;
        } else {
            onDeleteDropZoneListener.stopAnim();
            onDeleteDropZoneListener.createDeleteZone();
        }
        return false;
    }

    private boolean stopInDeleteZoneDrop(int x, int y) {
        Log.i(TAG, "stopInDeleteZoneDrop");
        Rect zone = new Rect();
        View animzone = onDeleteDropZoneListener.getAnimZone();
        animzone.getHitRect(zone);
        if (zone.intersect(x, y, x + 1, y + 1)) {
            Log.i(TAG, "stopInDeleteZoneDrop true");
            return true;
        } else {
            Log.i(TAG, "stopInDeleteZoneDrop false");
            return false;
        }
    }

}
