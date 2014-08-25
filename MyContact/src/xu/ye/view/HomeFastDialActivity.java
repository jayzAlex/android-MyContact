package com.way.app;

import java.util.ArrayList;
import java.util.Random;

import xu.ye.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.way.view.CircularImage;
import com.way.view.DragGridView;
import com.way.view.DragGridView.OnRearrangeListener;

/**
 * MainActivity
 * 
 * @author way
 * 
 */
public class HomeFastDialActivity extends Activity {
	static Random random = new Random();
	static String[] words = "我aaa 是aa 一aa 只aa 大aa 笨aa 猪aa".split(" ");
	DragGridView mDragGridView;
	Button mAddBtn, mViewBtn;
	ArrayList<String> poem = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mDragGridView = ((DragGridView) findViewById(R.id.vgv));
		mAddBtn = ((Button) findViewById(R.id.add_item_btn));
		mViewBtn = ((Button) findViewById(R.id.view_poem_item));

		setListeners();
	}

	private void setListeners() {
		mDragGridView.setOnRearrangeListener(new OnRearrangeListener() {
			public void onRearrange(int oldIndex, int newIndex) {
				String word = poem.remove(oldIndex);
				if (oldIndex < newIndex)
					poem.add(newIndex, word);
				else
					poem.add(newIndex, word);
			}
		});
		mDragGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mDragGridView.removeViewAt(arg2);
				poem.remove(arg2);
			}
		});
		mAddBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String word = words[random.nextInt(words.length)];
				CircularImage view = new CircularImage(HomeFastDialActivity.this);
//				view.setImageResource(R.drawable.head);
				view.setImageBitmap(getThumb(word));
				mDragGridView.addView(view);
				poem.add(word);
			}
		});
		mViewBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String finishedPoem = "";
				for (String s : poem)
					finishedPoem += s + " ";
				new AlertDialog.Builder(HomeFastDialActivity.this).setTitle("这是你选择的")
						.setMessage(finishedPoem).show();
			}
		});
	}

	private Bitmap getThumb(String s) {
		Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();

		paint.setColor(Color.rgb(random.nextInt(128), random.nextInt(128),
				random.nextInt(128)));
		paint.setTextSize(24);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawRect(new Rect(0, 0, 150, 150), paint);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(s, 75, 75, paint);

		return bmp;
	}
}