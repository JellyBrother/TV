//package com.china.jelly.mytv.mycode.test.weight;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.database.DataSetObserver;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//import com.china.jelly.mytv.R;
//
///**
// * @author: xiaolijuan
// * @description: 流式布局-标签流容器
// * @projectName: FlowLayoutDome
// * @date: 2016-06-16
// * @time: 16:21
// */
//public class FlowLayout extends ViewGroup{
//    /**
//     * 每个item纵向间距
//     */
//    private int mVerticalSpacing;
//    /**
//     * 每个item横向间距
//     */
//    private int mHorizontalSpacing;
//    private BaseAdapter mAdapter;
//    private TagItemClickListener mListener;
//    private DataChangeObserver mObserver;
//
//    public FlowLayout(Context context) {
//        this(context, null);
//    }
//
//    public FlowLayout(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        /**
//         * 获得我们所定义的自定义样式属性
//         */
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlowLayout, defStyle, 0);
//        for (int i = 0; i < a.getIndexCount(); i++) {
//            int attr = a.getIndex(i);
//            switch (attr) {
//                case R.styleable.FlowLayout_verticalSpacing:
//                    mVerticalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, 5);
//                    break;
//                case R.styleable.FlowLayout_horizontalSpacing:
//                    mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, 10);
//                    break;
//            }
//        }
//        a.recycle();
//    }
//
//    /**
//     * 负责设置子控件的测量模式和大小 根据所有子控件设置自己的宽和高
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        /**
//         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
//         */
//        int heighMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heighSize = MeasureSpec.getSize(heightMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        /**
//         * 高
//         */
//        int height = 0;
//        /**
//         * 每一行的高度，累加至height
//         */
//        int lineHeight = 0;
//        /**
//         * 在warp_content情况下，记录当前childView的左边的一个位置
//         */
//        int childLeft = getPaddingLeft();
//        /**
//         * 在warp_content情况下，记录当前childView的上边的一个位置
//         */
//        int childTop = getPaddingTop();
//        // getChildCount得到子view的数目,遍历循环出每个子View
//        for (int i = 0; i < getChildCount(); i++) {
//            //拿到index上的子view
//            View childView = getChildAt(i);
//            // 测量每一个child的宽和高
//            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
//            //当前子空间实际占据的高度
//            int childHeight = childView.getMeasuredHeight();
//            //当前子空间实际占据的宽度
//            int childWidth = childView.getMeasuredWidth();
//            lineHeight = Math.max(childHeight, lineHeight);// 取最大值
//            //如果加入当前childView，超出最大宽度，则将目前最大宽度给width，类加height 然后开启新行
//            if (childWidth + childLeft + getPaddingRight() > widthSize) {
//                childLeft = getPaddingLeft();// 重新开启新行，开始记录childLeft
//                childTop += mVerticalSpacing + childHeight;// 叠加当前的高度
//                lineHeight = childHeight;// 开启记录下一行的高度
//            }else{
//                //否则累加当前childView的宽度
//                childLeft += childWidth + mHorizontalSpacing;
//            }
//        }
//        height += childTop + lineHeight + getPaddingBottom();
//        setMeasuredDimension(widthSize, heighMode == MeasureSpec.EXACTLY ? heighSize : height);
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int width = r - l;
//        int childLeft = getPaddingLeft();
//        int childTop = getPaddingTop();
//        int lineHeight = 0;
//        //遍历所有childView根据其宽和高，计算子控件应该出现的位置
//        for (int i = 0; i < getChildCount(); i++) {
//            final View childView = getChildAt(i);
//            if (childView.getVisibility() == View.GONE) {
//                continue;
//            }
//            int childWidth = childView.getMeasuredWidth();
//            int childHeight = childView.getMeasuredHeight();
//            lineHeight = Math.max(childHeight, lineHeight);
//            // 如果已经需要换行
//            if (childLeft + childWidth + getPaddingRight() > width) {
//                childLeft = getPaddingLeft();
//                childTop += mVerticalSpacing + lineHeight;
//                lineHeight = childHeight;
//            }
//            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
//            childLeft += childWidth + mHorizontalSpacing;
//        }
//    }
//
//    private void drawLayout() {
//        if (mAdapter == null || mAdapter.getCount() == 0) {
//            return;
//        }
//        removeAllViews();
//        for (int i = 0; i < mAdapter.getCount(); i++) {
//            View view = mAdapter.getView(i, null, null);
//            final int position = i;
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        mListener.itemClick(position);
//                    }
//                }
//            });
//            addView(view);
//        }
//    }
//
//    public void setAdapter(BaseAdapter adapter) {
//        if (mAdapter == null) {
//            mAdapter = adapter;
//            if (mObserver == null) {
//                mObserver = new DataChangeObserver();
//                mAdapter.registerDataSetObserver(mObserver);
//            }
//            drawLayout();
//        }
//    }
//
//    public void setItemClickListener(TagItemClickListener mListener) {
//        this.mListener = mListener;
//    }
//
//    public interface TagItemClickListener {
//        void itemClick(int position);
//    }
//
//    class DataChangeObserver extends DataSetObserver {
//        @Override
//        public void onChanged() {
//            drawLayout();
//        }
//
//        @Override
//        public void onInvalidated() {
//            super.onInvalidated();
//        }
//    }
//}