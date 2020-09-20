package com.jelly.tv.weight;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.jelly.tv.R;

/**
 * 设置圆形图片
 */
public class RepairProgressImageView1 extends ImageView {
    private Resources resources;
    //受理的画笔
    private Paint acceptPaint = new Paint();
    //检测的画笔
    private Paint checkPaint = new Paint();
    //维修的画笔
    private Paint servicePaint = new Paint();
    //取机的画笔
    private Paint getPaint = new Paint();

    public RepairProgressImageView1(Context context) {
        super(context);
    }

    public RepairProgressImageView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RepairProgressImageView1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        resources = context.getResources();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //先画图片
        super.onDraw(canvas);
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        //画文字路径：RectF是文字线路，第二个参数是起点，第三个参数是终点
        RectF acceptRefct = new RectF(viewWidth / 4, viewHeight / 4, viewWidth * 3 / 4, viewHeight * 3 / 4);
        Path acceptPath = new Path();
        acceptPath.addArc(acceptRefct, 270, 90);

        RectF checkRefct = new RectF(viewWidth / 4, viewHeight / 4, viewWidth * 3 / 4, viewHeight * 3 / 4);
        Path checkPath = new Path();
        checkPath.addArc(checkRefct, 90, -90);

        RectF serviceRefct = new RectF(viewWidth / 4, viewHeight / 4, viewWidth * 3 / 4, viewHeight * 3 / 4);
        Path servicePath = new Path();
        servicePath.addArc(serviceRefct, 180, -90);

        RectF getRefct = new RectF(viewWidth / 4, viewHeight / 4, viewWidth * 3 / 4, viewHeight * 3 / 4);
        Path getPath = new Path();
        getPath.addArc(getRefct, 180, 90);

        //进度提示文字
        String acceptText = resources.getString(R.string.main_repair_accept);//受理
        String checkText = resources.getString(R.string.main_repair_check);//检测
        String serviceText = resources.getString(R.string.main_repair_service);//维修
        String getText = resources.getString(R.string.main_repair_get);//取机

        //把文字画上去
//        canvas.drawTextOnPath(acceptText, acceptPath, 0, 0, acceptPaint);
        canvas.drawTextOnPath(checkText, checkPath, 40, 0, checkPaint);
        canvas.drawTextOnPath(serviceText, servicePath, 40, 0, servicePaint);
//        canvas.drawTextOnPath(getText, getPath, 0, 0, getPaint);
    }

    /**
     * 设置维修进度,供外部调用
     *
     * @param type 1、受理；2、检测；3、维修；4、取机；默认为受理
     */
    public void setRepairSchedule(int type) {
        //画笔颜色
//        int blueColor = resources.getColor(R.color.blue_1294f6);
        int blueColor = resources.getColor(R.color.black);
        acceptPaint.setTextSize(30);
        checkPaint.setTextSize(30);
        servicePaint.setTextSize(30);
        getPaint.setTextSize(30);
        acceptPaint.setColor(blueColor);
        checkPaint.setColor(blueColor);
        servicePaint.setColor(blueColor);
        getPaint.setColor(blueColor);
        switch (type) {
            case 2://检测
                Drawable checkDrawable = resources.getDrawable(R.mipmap.main_sr_detail_check);//受理
                setUp(checkPaint, checkDrawable);
                break;
            case 3://维修
                Drawable serviceDrawable = resources.getDrawable(R.mipmap.main_sr_detail_service);//维修
                setUp(servicePaint, serviceDrawable);
                break;
            case 4://取机
                Drawable getDrawable = resources.getDrawable(R.mipmap.main_sr_detail_get);//取机
                setUp(getPaint, getDrawable);
                break;
            default: //默认受理
            case 1://受理
                Drawable accpetDrawable = resources.getDrawable(R.mipmap.main_sr_detail_accpet);//受理
                setUp(acceptPaint, accpetDrawable);
                break;
        }
    }

    private void setUp(Paint paint, Drawable drawable) {
        setImageDrawable(drawable);
//        int whiteColor = resources.getColor(R.color.white);
        int whiteColor = resources.getColor(R.color.black);
        //设置画笔颜色
        paint.setColor(whiteColor);
        //设置画笔宽度，线宽
        paint.setStrokeWidth(2);
        //设置字体大小
//        paint.setTextSize(50);
        /**
         * Paint.Style.STROKE 只绘制图形轮廓（描边）
         Paint.Style.FILL 只绘制图形内容
         Paint.Style.FILL_AND_STROKE 既绘制轮廓也绘制内容
         */
        acceptPaint.setStyle(Paint.Style.STROKE);
        invalidate();
    }
}