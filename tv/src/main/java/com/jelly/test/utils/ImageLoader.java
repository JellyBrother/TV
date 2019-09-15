package com.jelly.test.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class ImageLoader {
    private RequestManager requestManager;
    private final RequestOptions defaultRequestOptions;
    private RequestBuilder<Drawable> request;

    /**
     * 这种单例模式既实现了线程安全，又避免了同步带来的性能问题
     */
    private static class LazyHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();
    }

    public ImageLoader() {
        defaultRequestOptions = new RequestOptions();
    }

    public static final ImageLoader getInstance() {
        return LazyHolder.INSTANCE;
    }

    public ImageLoader with(Context context) {
        requestManager = Glide.with(context);
        return this;
    }

    public ImageLoader with(Activity activity) {
        requestManager = Glide.with(activity);
        return this;
    }

    public ImageLoader with(Fragment fragment) {
        requestManager = Glide.with(fragment);
        return this;
    }

    public ImageLoader with(View view) {
        requestManager = Glide.with(view);
        return this;
    }

    //加载图片的地址
    public ImageLoader load(Object object) {
        if (object instanceof String) {

        }
        request = requestManager.load(object);
        request = request.apply(defaultRequestOptions);
        return this;
    }

    //占位图
    public ImageLoader setPlaceholderDrawable(Drawable drawable) {
        RequestOptions options = new RequestOptions().placeholder(drawable);
        request = request.apply(options);
        return this;
    }

    //请求失败图
    public ImageLoader setFallbackDrawable(Drawable drawable) {
        RequestOptions options = new RequestOptions().fallback(drawable);
        request = request.apply(options);
        return this;
    }

    //错误图
    public ImageLoader setErrorDrawable(Drawable drawable) {
        RequestOptions options = new RequestOptions().error(drawable);
        request = request.apply(options);
        return this;
    }

    //没有磁盘缓存
    public ImageLoader setNoDiskCache() {
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
        request = request.apply(options);
        return this;
    }

    //开始加载图片
    public ViewTarget<ImageView, Drawable> into(ImageView imageView) {

        return request.into(imageView);
    }

    //开始加载图片
    public SimpleTarget into(SimpleTarget simpleTarget) {
        return request.into(simpleTarget);
    }

    //开始加载图片
    public ViewTarget into(ViewTarget viewTarget) {
        return request.into(viewTarget);
    }

    public class SimpleTarget extends SimpleTarget {

        @Override
        public void onResourceReady(Object o, Transition transition) {

        }
    }

    public class SimpleTarget2<Bitmap> extends SimpleTarget<Bitmap> {

        @Override
        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {

        }
    }

    public class ViewTarget extends ViewTarget {

        public ViewTarget(View view) {
            super(view);
        }

        @Override
        public void onResourceReady(Object o, Transition transition) {

        }
    }

    public class ViewTarget2 extends ViewTarget<ImageView, Bitmap> {

        public ViewTarget2(ImageView view) {
            super(view);
        }

        @Override
        public void onLoadStarted(@Nullable Drawable placeholder) {
            super.onLoadStarted(placeholder);
        }

        @Override
        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {

        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            super.onLoadFailed(errorDrawable);
        }
    }

    public interface ViewTargetInterface {
        public void onLoadStarted(@Nullable Drawable placeholder);

        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition);

        public void onLoadFailed(@Nullable Drawable errorDrawable);
    }


//        RequestOptions cropOptions = new RequestOptions()
//                .dontAnimate()//没有动画
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//设置磁盘缓存方式，方便预加载
//                .dontTransform();//没有图片变换
//        RequestOptions cropOptions2 = new RequestOptions()
//                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)//如果想要图片变换可以调用
//                .centerCrop()//图片变换--对原图的中心区域进行裁剪后适配屏幕
//                .centerCrop()//图片变换--变圆
//                .override(200,200)//图片变换--变换区域
//                .skipMemoryCache(true)//跳过内存缓存，默认false使用内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//跳过磁盘缓存
//                .transform(withCrossFade())//图片变换--第三方库图行变换
//                .onlyRetrieveFromCache(true)//省流模式
//                .fallback()//失败图
//                .error()//错误图,点击可以继续请求
//                .placeholder()//占位图
//                .set(MyCustomModelLoader.TIMEOUT_MS, 1000L)//自定义超时
}
