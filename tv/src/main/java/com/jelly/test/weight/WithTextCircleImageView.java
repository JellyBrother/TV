package com.jelly.test.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.jelly.tv.R;


/**
 * 设置圆形图片
 * @author xWX294365
 *
 */
public class WithTextCircleImageView extends ImageView {

  private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

  private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
  private static final int COLORDRAWABLE_DIMENSION = 1;

  private static final int DEFAULT_BORDER_WIDTH = 0;
  private static final int DEFAULT_BORDER_COLOR = Color.WHITE;

  private final RectF mDrawableRect = new RectF();
  private final RectF mBorderRect = new RectF();

  private final Matrix mShaderMatrix = new Matrix();
  private final Paint mBitmapPaint = new Paint();
  private final Paint mBorderPaint = new Paint();

  private int mBorderColor = DEFAULT_BORDER_COLOR;
  private int mBorderWidth = DEFAULT_BORDER_WIDTH;

  private Bitmap mBitmap;
  private BitmapShader mBitmapShader;
  private int mBitmapWidth;
  private int mBitmapHeight;

  private float mDrawableRadius;
  private float mBorderRadius;

  private boolean mReady;
  private boolean mSetupPending;

  public WithTextCircleImageView(Context context) {
    super(context);
  }

  public WithTextCircleImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WithTextCircleImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    super.setScaleType(SCALE_TYPE);

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageViewBase, defStyle, 0);
    mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageViewBase_border_width, DEFAULT_BORDER_WIDTH);
    mBorderColor = a.getColor(R.styleable.CircleImageViewBase_border_color, DEFAULT_BORDER_COLOR);

    a.recycle();

    mReady = true;

    if (mSetupPending) {
      setup();
      mSetupPending = false;
    }
  }

  @Override
  public ScaleType getScaleType() {
    return SCALE_TYPE;
  }
  
  @Override
  public void setScaleType(ScaleType scaleType) {
    if (scaleType != SCALE_TYPE) {
      throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (getDrawable() == null) {
      return;
    }

    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);


//    int viewWidth = getWidth();
//    int viewHeight = getHeight();
//    Paint textPaint = new Paint();
//    textPaint.setColor(Color.BLUE);
//    textPaint.setStrokeWidth(10);
//    textPaint.setTextSize(100);
//    textPaint.setStrokeWidth(2);
//    textPaint.setStyle(Paint.Style.STROKE);
////    canvas.drawArc(0,0,viewWidth,viewHeight,0,270,true,textPaint);
//
//    RectF refct = new RectF(0,0,viewWidth,viewHeight);
//    String text = "好好学习 天天向上";
//    Path path = new Path();
//    path.addArc(refct,120,240);
//    canvas.drawArc(refct,120,300,false,textPaint);
//    canvas.drawTextOnPath(text,path,0,0,textPaint);


     /*因为这种写法对API有要求所以这里加了个if,当然也可以用RectF 这个就对API要求没那么高了
  *解释下四个参数前两个表示左上角的坐标 后两个表示右下角的坐标
  *其实就是在一个矩形里面画圈
  * 第五和第六表示弧度制表示从第五个数值偏转第六个数值
  *boolean值表示是否连接中心点
 */
    //光这么说可能有些蒙 结合图来看看就很清晰了
//    canvas.drawArc(0,0,200,200,0,270,true,paint);

    int viewWidth = getWidth();
    int viewHeight = getHeight();
    Paint textPaint = new Paint();
    textPaint.setColor(Color.RED);
    //设置画笔宽度，线宽
    textPaint.setStrokeWidth(2);
    //设置字体大小
    textPaint.setTextSize(100);
    /**
     * Paint.Style.STROKE 只绘制图形轮廓（描边）
     Paint.Style.FILL 只绘制图形内容
     Paint.Style.FILL_AND_STROKE 既绘制轮廓也绘制内容
     */
    textPaint.setStyle(Paint.Style.STROKE);

    RectF refct = new RectF(viewWidth/4,viewHeight/4,viewWidth*3/4,viewHeight*3/4);
    String text = "好好学习 天天向上";
    Path path = new Path();
    //这里是text的路径：0是起点，180是终点
    path.addArc(refct,0,180);
    //这里是圆弧的路径
//    canvas.drawArc(refct,0,180,false,textPaint);
    canvas.drawTextOnPath(text,path,0,0,textPaint);
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    setup();
  }

  public int getBorderColor() {
    return mBorderColor;
  }

  public void setBorderColor(int borderColor) {
    if (borderColor == mBorderColor) {
      return;
    }

    mBorderColor = borderColor;
    mBorderPaint.setColor(mBorderColor);
    invalidate();
  }

  public int getBorderWidth() {
    return mBorderWidth;
  }

  public void setBorderWidth(int borderWidth) {
    if (borderWidth == mBorderWidth) {
      return;
    }

    mBorderWidth = borderWidth;
    setup();
  }

  @Override
  public void setImageBitmap(Bitmap bm) {
    super.setImageBitmap(bm);
    mBitmap = bm;
    setup();
  }

  @Override
  public void setImageDrawable(Drawable drawable) {
    super.setImageDrawable(drawable);
    mBitmap = getBitmapFromDrawable(drawable);
    setup();
  }

  @Override
  public void setImageResource(int resId) {
    super.setImageResource(resId);
    mBitmap = getBitmapFromDrawable(getDrawable());
    setup();
  }

  private Bitmap getBitmapFromDrawable(Drawable drawable) {
    if (drawable == null) {
      return null;
    }

    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    }

    try {
      Bitmap bitmap;

      if (drawable instanceof ColorDrawable) {
        bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
      } else {
        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
      }

      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      drawable.draw(canvas);
      return bitmap;
    } catch (OutOfMemoryError e) {
      return null;
    }
  }

  private void setup() {
    if (!mReady) {
      mSetupPending = true;
      return;
    }

    if (mBitmap == null) {
      return;
    }

    mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

    mBitmapPaint.setAntiAlias(true);
    mBitmapPaint.setShader(mBitmapShader);

    mBorderPaint.setStyle(Paint.Style.STROKE);
    mBorderPaint.setAntiAlias(true);
    mBorderPaint.setColor(mBorderColor);
    mBorderPaint.setStrokeWidth(mBorderWidth);

    mBitmapHeight = mBitmap.getHeight();
    mBitmapWidth = mBitmap.getWidth();

    mBorderRect.set(0, 0, getWidth(), getHeight());
    mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);

    mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
    mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);

    updateShaderMatrix();
    invalidate();
  }

  private void updateShaderMatrix() {
    float scale;
    float dx = 0;
    float dy = 0;

    mShaderMatrix.set(null);

    if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
    	scale = mDrawableRect.width() / (float) mBitmapWidth;
        dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
    } else {
    	scale = mDrawableRect.height() / (float) mBitmapHeight;
        dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
    }

    mShaderMatrix.setScale(scale, scale);
    mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);

    mBitmapShader.setLocalMatrix(mShaderMatrix);
  }

}