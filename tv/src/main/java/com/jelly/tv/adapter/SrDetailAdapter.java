package com.jelly.tv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.jelly.tv.R;
import com.jelly.tv.bean.RepairInfo;
import com.jelly.tv.utils.Common;
import com.jelly.tv.utils.SharedPreferencesUtil;
import com.jelly.tv.weight.RepairProgressImageView1;

import java.util.ArrayList;
import java.util.List;

/**
 * v1.1开发
 * Created by lwx334725 on 2017/07/04.
 */
public class SrDetailAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<RepairInfo> mSrDetailList = new ArrayList<>();

    public SrDetailAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mSrDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSrDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_sr_detail, null);
            viewHolder = new ViewHolder();
            viewHolder.rpivw_sr_detail = (RepairProgressImageView1) convertView.findViewById(R.id.rpivw_sr_detail);
            viewHolder.ivw_sr_detail = (ImageView) convertView.findViewById(R.id.ivw_sr_detail);
            viewHolder.ivw_sr_default = (ImageView) convertView.findViewById(R.id.ivw_sr_default);
            viewHolder.tvw_queueing_no = (TextView) convertView.findViewById(R.id.tvw_queueing_no);
            viewHolder.tvw_tatstart_time = (TextView) convertView.findViewById(R.id.tvw_tatstart_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RepairInfo repairInfo = mSrDetailList.get(position);
        viewHolder.tvw_queueing_no.setText(repairInfo.getQueueingNo());
        viewHolder.tvw_tatstart_time.setText(repairInfo.getTatStartTime());
        viewHolder.rpivw_sr_detail.setRepairSchedule(repairInfo.getRepairStatusNum());
        if (repairInfo.isRepair()) {
            viewHolder.ivw_sr_default.setVisibility(View.VISIBLE);
            viewHolder.rpivw_sr_detail.setVisibility(View.GONE);
            viewHolder.ivw_sr_detail.setVisibility(View.GONE);
            viewHolder.tvw_queueing_no.setVisibility(View.GONE);
            viewHolder.tvw_tatstart_time.setVisibility(View.GONE);
        } else {
            viewHolder.ivw_sr_default.setVisibility(View.GONE);
            viewHolder.rpivw_sr_detail.setVisibility(View.VISIBLE);
            viewHolder.ivw_sr_detail.setVisibility(View.VISIBLE);
            viewHolder.tvw_queueing_no.setVisibility(View.VISIBLE);
            viewHolder.tvw_tatstart_time.setVisibility(View.VISIBLE);
        }

        //根据父控件宽高来动态设置。
        int horizontalSpacing = (int) mContext.getResources().getDimension(R.dimen.px30);
        int verticalSpacing = (int) mContext.getResources().getDimension(R.dimen.px18);
        int width = (SharedPreferencesUtil.getInt(mContext, Common.FLT_NODATA_WIDTH, 1124)-horizontalSpacing)/2;
        int height = (SharedPreferencesUtil.getInt(mContext, Common.FLT_NODATA_HEIGHT, 899)-verticalSpacing)/2;
        convertView.setLayoutParams(new GridView.LayoutParams(width, height));
        return convertView;
    }

    public void updata(List<RepairInfo> srDetailList) {
        this.mSrDetailList.clear();
        int size = srDetailList.size();
        if (size > 4) {
            mSrDetailList.addAll(srDetailList.subList(0, 4));
        } else {
            mSrDetailList.addAll(srDetailList);
            for (int i = size; i < 4; i++) {
                RepairInfo repairInfo = new RepairInfo();
                repairInfo.setRepair(true);
                mSrDetailList.add(repairInfo);
            }
        }
        notifyDataSetChanged();
    }

    class ViewHolder {
        //状态图
        private RepairProgressImageView1 rpivw_sr_detail;
        //状态图中间的提示点
        private ImageView ivw_sr_detail;
        //默认状态图
        private ImageView ivw_sr_default;
        //工单号
        private TextView tvw_queueing_no;
        //受理时间
        private TextView tvw_tatstart_time;
    }
}
