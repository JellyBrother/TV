package com.jelly.tv.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.jelly.tv.R;
import com.jelly.tv.adapter.SrDetailAdapter;
import com.jelly.tv.bean.RepairInfo;
import com.jelly.tv.utils.Common;
import com.jelly.tv.utils.SharedPreferencesUtil;

import java.util.ArrayList;

/**
 * v1.1开发
 */
public class TVMainActivity extends BaseActivity {
    //左边图片展示
    private ViewFlipper vfr_image_left;
    //时间日期
    private TextView tvw_data;
    //sr单详情
    private GridView gvw_sr_detail;
    //温馨提示
    private TextView tvw_warm_prompt;
    //可取机用户
    private ListView llw_can_receive;
    private FrameLayout flt_nodata;
    private FrameLayout flt_measure;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tv_main_activity);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        vfr_image_left = (ViewFlipper) findViewById(R.id.vfr_image_left);
        tvw_data = (TextView) findViewById(R.id.tvw_data);
        llw_can_receive = (ListView) findViewById(R.id.llw_can_receive);
        gvw_sr_detail = (GridView) findViewById(R.id.gvw_sr_detail);
        flt_nodata = (FrameLayout) findViewById(R.id.flt_nodata);
        flt_measure = (FrameLayout) findViewById(R.id.flt_measure);
        tvw_warm_prompt = (TextView) findViewById(R.id.tvw_warm_prompt);
        flt_nodata.setVisibility(View.VISIBLE);
        gvw_sr_detail.setVisibility(View.GONE);
    }

    private void initData() {
        //图片轮播
        int[] imageIds = {R.mipmap.default_main_nodata, R.mipmap.login_logo, R.mipmap.default_main_left};
        int length = imageIds.length;
        if (length == 0) {
            vfr_image_left.addView(getImageView(R.mipmap.default_main_left));
        } else {
            for (int i = 0; i < length; i++) {
                vfr_image_left.addView(getImageView(imageIds[i]));
            }
        }
//            vfr_image_left.removeAllViews();
        //设置轮播时长
        vfr_image_left.setFlipInterval(3000);
        vfr_image_left.startFlipping();

        //可取机用户列表
        String[] can_receives = {"A001", "B002", "A003", "B004", "A005", "B006", "A007", "B008",
                "B002", "A003", "B004", "A005", "B006", "A007", "B008",
                "A003", "B004", "A005", "B006", "A007", "B008"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.main_a_can_receive, R.id.tvw_spinner, can_receives);
        llw_can_receive.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        //sr单详情
        ArrayList<RepairInfo> repairInfoArrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            RepairInfo repairInfo = new RepairInfo();
            repairInfo.setRepairStatusNum(2);
            repairInfo.setRepairStatus("受理" + i);
            repairInfo.setTatStartTime("受理时间" + i);
            repairInfo.setLight("绿灯" + i);
            repairInfo.setQueueingNo("C00" + i);
            repairInfo.setRepairNo("工单描述" + i);
            repairInfoArrayList.add(repairInfo);
        }

        log(flt_measure);
        if (repairInfoArrayList != null && !repairInfoArrayList.isEmpty()) {
            flt_nodata.setVisibility(View.GONE);
            gvw_sr_detail.setVisibility(View.VISIBLE);
            //sr单详情
            SrDetailAdapter mSrDetailAdapter = new SrDetailAdapter(this);
            gvw_sr_detail.setAdapter(mSrDetailAdapter);
            mSrDetailAdapter.updata(repairInfoArrayList);
        } else {
            flt_nodata.setVisibility(View.VISIBLE);
            gvw_sr_detail.setVisibility(View.GONE);
        }
    }

    private void setListener() {

    }

    private void log(final View view) {
        //在视图的 layout 改变时调用该事件，会被多次调用，因此需要在获取到视图的宽度和高度后执行 remove 方法移除该监听事件。
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //移除监听
                view.removeOnLayoutChangeListener(this);
                int width = view.getWidth(); // 获取宽度
                int height = view.getHeight(); // 获取高度
                Log.e("TVMainLayoutChange:", "width:" + width + "**height:" + height);
                SharedPreferencesUtil.putInt(TVMainActivity.this, Common.FLT_NODATA_WIDTH, width);
                SharedPreferencesUtil.putInt(TVMainActivity.this, Common.FLT_NODATA_HEIGHT, height);
            }
        });

        //Runnable 对象中的方法会在 View 的 measure、layout 等事件完成后触发。
//        view.post(new Runnable() {
//            @Override
//            public void run() {
//                int width = view.getWidth(); // 获取宽度
//                int height = view.getHeight(); // 获取高度
//                Log.e("TVMainRunnable:", "width:" + width + "**height:" + height);
//                SharedPreferencesUtil.putInt(TVMainActivity.this, Common.FLT_NODATA_WIDTH, width);
//                SharedPreferencesUtil.putInt(TVMainActivity.this, Common.FLT_NODATA_HEIGHT, height);
//            }
//        });
    }

    private ImageView getImageView(int imageId) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(imageId);
        return imageView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
