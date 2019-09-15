package com.jelly.test.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jelly.tv.R;

public class GlideMainActivity extends Activity {
    private static final String DATA_URI =
            "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD//gA7Q1JFQVRPUjogZ2QtanBlZyB2MS4wICh1c2luZ"
                    + "yBJSkcgSlBFRyB2ODApLCBxdWFsaXR5ID0gNzUK/9sAQwAIBgYHBgUIBwcHCQkICgwUDQwLCwwZEhMPFB0aHx4"
                    + "dGhwcICQuJyAiLCMcHCg3KSwwMTQ0NB8nOT04MjwuMzQy/9sAQwEJCQkMCwwYDQ0YMiEcITIyMjIyMjIyMjIyM"
                    + "jIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIy/8AAEQgAZABkAwEiAAIRAQMRAf/EAB8AAAE"
                    + "FAQEBAQEBAAAAAAAAAAABAgMEBQYHCAkKC//EALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcic"
                    + "RQygZGhCCNCscEVUtHwJDNicoIJChYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN"
                    + "0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2drh4uPk5"
                    + "ebn6Onq8fLz9PX29/j5+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgMEBQYHCAkKC//EALURAAIBAgQEAwQHBQQ"
                    + "EAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1LwFWJy0QoWJDThJfEXGBkaJicoKSo1Njc4OTpDR"
                    + "EVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoKDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i"
                    + "5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5+jp6vLz9PX29/j5+v/aAAwDAQACEQMRAD8A+f6KKKACiiigAoooo"
                    + "AKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAoorU8NabDrPirSNLuGkWC9vYbe"
                    + "RoyAwV3CkjIIzg+hoAy6K6HxJ4VvdH1bVxbWF++kWd/Pax3kkLFCEkKDLgBc8DPvWbpuh6trLMul6Xe3xT7wtb"
                    + "d5dv12g0AUKKmuLO6tLtrS5tpoblG2tDIhV1PoVPOauX3h7W9LtluNQ0fULSBjhZbi2eNSfYkAUAZtFX9O0LV9"
                    + "XSR9N0q+vUi/1jW1u8gT67QcVe8K+GJ/E3iNNK85bRFV5bqeYcQRIMuxHsB09cdKAMKiu9vtC8A3+h6jN4c13U"
                    + "ItRsI/N8nV/KjS7UH5hFjndjkKeT6dxj+B/B9z408RQ6dFIILYFTc3LfdiQsFHXqxJAUdyaAOaorU8S6bDo3ir"
                    + "V9Lt2kaCyvZreNpCCxVHKgnAAzgegrLoAKKKKACug8Cf8lD8Nf9hW1/8ARq1z9WLC+uNM1G2v7OTy7q1lSaF9o"
                    + "O11IKnB4OCB1oA96sNX8R6l8bPEGi6zJcv4cCXaXdq4IgjtdrFHx90E/L83U5PJrnPC0t7pnw/0i51nxjq2laT"
                    + "czzLp1losH76Rlf5y8igH72cKxPHTjiuJv/iR4w1PSbjS7vXbiSzuZHkmjAVd5dizDIAO0kn5c7e2MUzQPiD4r"
                    + "8L6e9ho2sS21q7FjFsRwCepG4Hb+GKAPT/Gt/Novxj8G6pDpF/qlwmkQO1rNFm6mb96pLKoP70DDHA4K1FfXv8"
                    + "Awk3hvxNN4e8aa8yxWbzXuka5AJgIx95VkOVRgemPm4/GvK9Q8Y+IdVu7C7vtVnmu9Pz9muTgSp82774G44PTJ"
                    + "OO1Xtb+JPjDxFpp07VNcnntGxviCJGHwc/NtUFufWgD1O41XQ/DfgPweJb7xjY2s2npKH0B4khknJzJvZuS+7s"
                    + "TjGMDrS6ZrNlqPxU1Oe10TUVur3wxLELXVrZYZb6cAHJVDgh0TqMZ5wBXk/h74heK/Ctk1no2szW1sxJ8ookig"
                    + "nqQHB2/hisqbXtWuNb/ALal1G5bU94kF0ZD5gYdCD2xQB6ZpXiLVfHPhTxXaeKbe3ksdL017i0mW0SE2lwpASN"
                    + "SoGM8jB54xWj4ObwYbTwhpen+MPsd59utby+s/wCzJne8uw6lY2l4UKpyo6jJLHNeb6/8QvFfiiwSx1jWZrm1Q"
                    + "7vK2IisexbaBu/HNYNhfXGmajbX9nJ5d1aypNC+0Ha6kFTg8HBA60AdR8ULTTrT4h6x/Z2qfb/Ou5pbj/R2i+z"
                    + "zGV90XP3tvHzDg5rj6sX99canqNzf3knmXV1K80z7QNzsSWOBwMknpVegAooooAKKKKACiiigAooooAKKKKACi"
                    + "iigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAoo"
                    + "ooA//2Q==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_main);
        ImageView imageView = findViewById(R.id.image_view);

        String url = "http://guolin.tech/book.png";
        String url2 = "http://asgard.image.mucang.cn/asgard/2017/12/28/10/45ab2f68a13546c1914b61d54160b8ae.webp";
        String url3 = "https://img-blog.csdn.net/20161125141410266";
        String url4 = "http://www.etherdream.com/WebP/Test.webp";
        String url5 = "https://p.upyun.com/demo/tmp/cgtoPeG5.webp";
        String url6 = "https://p.upyun.com/demo/tmp/WFnZjYfL.webp";
        String url7 = "https://p.upyun.com/demo/tmp/PRrm49mH.webp";
        Drawable drawable = getResources().getDrawable(R.drawable.guidepage1);
        RequestOptions options = new RequestOptions()
                .error(drawable)//错误图,点击可以继续请求
                .placeholder(drawable);//占位图

        Glide.with(this).load(url2)
                .apply(options)
                .into(imageView);

        GlideApp.with(this).load(url2)
                .apply(options)
                .into(imageView);

        Drawable drawable2 = getDrawable(R.drawable.guidepage1);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable2;
        Bitmap bitmap = bitmapDrawable.getBitmap();



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
        ;

//        GlideUrl glideUrl1 = new GlideUrl(url, new LazyHeaders.Builder().addHeader("Cooke", new LazyHeaderFactory() {
//            @Nullable
//            @Override
//            public String buildHeader() {
//                return "";
//            }
//        }).build());
//
//        GlideUrl glideUrl2 = new GlideUrl(url, new LazyHeaders.Builder().addHeader("Cooke", "").build());
//
//        Glide.with(this).load(glideUrl2)
//                .apply(cropOptions2)
//                .apply(cropOptions)
//                .thumbnail(1.0f)//缩略图，原图大小
//                .into(imageView);
//
//        //只取缓存
//        GlideApp.with(this)
//                .load(url)
//                .onlyRetrieveFromCache(true)
//                .into(imageView);

//        1.原图(SOURCE) :原始图片
//        2.处理图(RESULT) :经过压缩和变形等处理后的图片


//    Glide.with(this)
//        .load(DATA_URI)
//        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//        .into(imageView);
    }
}
