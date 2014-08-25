package xu.ye.view;

import java.util.ArrayList;
import xu.ye.R;
import xu.ye.application.MyApplication;
import xu.ye.bean.ContactBean;
import xu.ye.db.DbUtils;
import xu.ye.view.HomeTabHostAcitivity.OnTabActivityResultListener;
import xu.ye.view.ui.CustomeDialogAlert;
import xu.ye.view.ui.OnDeleteDropZoneListener;
import xu.ye.view.ui.OnDialListener;
import xu.ye.view.ui.SwitchGridAdapter;
import xu.ye.view.ui.SwitchGridView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
//import xu.ye.view.ui.OnLongPressedListener;

public class HomeFastDialActivity extends Activity implements OnTabActivityResultListener, OnClickListener,
        OnDeleteDropZoneListener, OnDialListener
//		, OnLongPressedListener
{

    private MyApplication application;

    private SwitchGridView gridView;

    private OnItemClickListener listener;

    private View allList;

    private View blackList;

    private View whiteList;

    private static ArrayList<ContactBean> list;

    private static final String TAG = "HomeFastDialActivity";

    private static final boolean DEBUG = true;

    private SwitchGridAdapter dragListAdapter;

    private static SharedPreferences sharedPreferences;

    private int srccheckedid;

    private View bottomzone;

    private ImageView bottomtrash;

    private ImageView bottomtrashanim;

    private AnimationDrawable frameAnimation;

    private int contactDialogPosition;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fastdial_page);
        application = (MyApplication) getApplication();

        list = new ArrayList<ContactBean>();

        DbUtils dbUtils = new DbUtils(getApplicationContext());
        ArrayList<ContactBean> allCbList = dbUtils.getQuick();
        for (int i = 0; i < 16; i++) {
            list.add(null);
        }
        for (ContactBean cb : allCbList) {
            if (cb != null && !TextUtils.isEmpty(cb.getPhoneNum()))
                list.set(cb.getPosition(), cb);
        }

        bottomzone = findViewById(R.id.bottomzone);
        bottomtrash = (ImageView) findViewById(R.id.bottomtrash);
        bottomtrashanim = (ImageView) findViewById(R.id.bottomtrashanim);
        frameAnimation = (AnimationDrawable) bottomtrashanim.getBackground();
        dragListAdapter = new SwitchGridAdapter(getApplicationContext(), list, this);
        gridView = (SwitchGridView) findViewById(R.id.grid);
        gridView.setAdapter(dragListAdapter);
        gridView.setOnDeleteDropZoneListener(this);
        //		gridView.setOnLongPressedListener(this);
        gridView.setOnDialListener(this);

        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                ContactBean contactBean = SwitchGridAdapter.list.get(position);
//                Log.i(TAG, "1--position:" + position + " contactBean:" + contactBean);
                if (contactBean == null) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), HomeContactActivity.class);
                    intent.putExtra("flag", HomeContactActivity.INTENT_SELECT_CONTACTBEAN);
                    intent.putExtra("position", position);
                    getParent().startActivityForResult(intent, 1);
                } else {
//                    Log.i(TAG, "2--position:" + position + " contactBean:" + contactBean);
                    //				    showOnSingleClickContactDialog(position);
                }
            }
        };
        gridView.setOnItemClickListener(listener);

        allList = findViewById(R.id.allList);
        blackList = findViewById(R.id.blackList);
        whiteList = findViewById(R.id.whiteList);
        allList.setOnClickListener(this);
        blackList.setOnClickListener(this);
        whiteList.setOnClickListener(this);
        // 根据ID找到RadioGroup实例
        RadioGroup group = (RadioGroup) this.findViewById(R.id.bottom);
        srccheckedid = getPrefList();
        group.check(srccheckedid);
        // 绑定一个匿名监听器
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int radioButtonId = group.getCheckedRadioButtonId();
                switch (radioButtonId) {
                case R.id.blackList:
                    break;
                case R.id.whiteList:
                    break;
                case R.id.allList:
                    break;
                default:
                    break;
                }
                if (getPrefList() != radioButtonId) {
                    setPrefList(radioButtonId);
                }
//                Log.i(TAG, "onCheckedChanged");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 保存一键拨号快捷联系人
        DbUtils dbUtils = new DbUtils(getApplicationContext());
        ArrayList<ContactBean> adapterList = SwitchGridAdapter.list;
        for (int i = 0; i < 16; i++) {
            ContactBean cb = adapterList.get(i);
            if (cb != null) {
                cb.setPosition(i);
                dbUtils.saveQuickByPosition(i, cb);
            } else {
                dbUtils.saveQuickByPosition(i, new ContactBean("", "", i));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 实现子Activity逻辑
    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            if (data.getIntExtra("flag", -1) == HomeContactActivity.INTENT_SELECT_CONTACTBEAN) {
                ContactBean cb = data.getParcelableExtra("ContactBean");
                int position = data.getIntExtra("position", -1);
                if (position != -1) {
                    dragListAdapter.setCb(position, cb);
                }
            }
        }
    }

    public int getPrefList() {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getInt("preflist", R.id.allList);
    }

    public void setPrefList(int prefId) {
        Editor editor = sharedPreferences.edit();
        editor.putInt("preflist", prefId);
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (srccheckedid == viewId && viewId != R.id.allList) {
            Intent intent = new Intent(getApplicationContext(), PrefsListActivity.class);
            intent.putExtra("preflist", viewId);
            startActivityForResult(intent, 1);
        }
        srccheckedid = viewId;
    }

    @Override
    public void createDeleteZone() {
        bottomzone.setVisibility(View.INVISIBLE);
        bottomtrash.setVisibility(View.VISIBLE);
        bottomtrashanim.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideDeleteView() {
        bottomzone.setVisibility(View.VISIBLE);
        bottomtrash.setVisibility(View.INVISIBLE);
        bottomtrashanim.setVisibility(View.INVISIBLE);
        stopAnim();
    }

    @Override
    public void startAnim() {
        // Start the animation (looped playback by default).
        frameAnimation.start();
        bottomtrash.setVisibility(View.INVISIBLE);
        bottomtrashanim.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopAnim() {
        frameAnimation.stop();
        bottomtrashanim.setVisibility(View.INVISIBLE);
    }

    @Override
    public View getAnimZone() {
        return bottomtrashanim;
    }

//    private String[] lianxiren1 = new String[] { "拨打", "发送短信", "取消" };
//
//    // 联系人弹出页
//    public void showOnSingleClickContactDialog(int position) {
//        contactDialogPosition = position;
//        new AlertDialog.Builder(this).setTitle(list.get(position).getDisplayName())
//                .setItems(lianxiren1, dialogueOnSingleClickListener).show();
//    }
    
    /**
     * 提示框
     * @param Text 显示提示
     * @param pContext
     */
    public void showOnSingleClickContactDialog(int position) {
        contactDialogPosition = position;
        final CustomeDialogAlert exitDialog = new CustomeDialogAlert(this,R.style.fullscreendialog);
        exitDialog.setTitleText(list.get(position).getDisplayName())
                .setOption1Button("拨打", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitDialog.dismiss();
                        dragListAdapter.onDial(contactDialogPosition);
                    }
                }).setOption2Button("短信", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitDialog.dismiss();
                        dragListAdapter.onSms(contactDialogPosition);
                    }
                }).setPositiveButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exitDialog.dismiss();
                    }
                }).show();
    }

//    DialogInterface.OnClickListener dialogueOnSingleClickListener = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            switch (which) {
//            case 0:// 拨打
//                dragListAdapter.onDial(contactDialogPosition);
//                break;
//            case 1:// 短信
//                dragListAdapter.onSms(contactDialogPosition);
//                break;
//            case 2:// 取消
//                break;
//            }
//        }
//    };

    @Override
    public void onDial(int position) {
        showOnSingleClickContactDialog(position);
    }

}
