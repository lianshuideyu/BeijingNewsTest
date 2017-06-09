package com.atguigu.beijingnewstest.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.beijingnewslibrary.utils.BitmapCacheUtils;
import com.atguigu.beijingnewslibrary.utils.ConstantUtils;
import com.atguigu.beijingnewslibrary.utils.NetCachUtils;
import com.atguigu.beijingnewstest.R;
import com.atguigu.beijingnewstest.activity.PicassoSampleActivity;
import com.atguigu.beijingnewstest.domain.PhotosMenuDetailPagerBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/6.
 */

public class PhotosMenuDetailPagerAdapater extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapater.MyViewHolder> {

    private final List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas;
    private final Context context;
    private final RecyclerView recyclerview;

    /**
     * 做图片三级缓存
     * 1.内存缓存
     * 2.本地缓存
     * 3.网络缓存
     */
    private BitmapCacheUtils bitmapCacheUtils;

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    protected ImageLoader imageLoader = ImageLoader.getInstance();


    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case NetCachUtils.SUCESS :
                    //获取图片成功
                    Bitmap bitmap = (Bitmap) msg.obj;
                    int position = msg.arg1;
                    Log.e("TAG","请求图片成功=="+position);
                    ImageView imageview = (ImageView) recyclerview.findViewWithTag(position);//根据tag找到相应的图片imageview
                    if(imageview != null && bitmap != null) {
                        imageview.setImageBitmap(bitmap);
                    }


                    break;
                case NetCachUtils.FAIL :
                    position = msg.arg1;
                    Log.e("TAG","请求图片失败=="+position);
                    break;
            }
        }
    };

    public PhotosMenuDetailPagerAdapater(Context context, List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas, RecyclerView recyclerview) {
        this.datas = datas;
        this.context = context;
        //把handler传过去
        bitmapCacheUtils = new BitmapCacheUtils(handler);
        this.recyclerview = recyclerview;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic_item_list_default)
                .showImageForEmptyUri(R.drawable.pic_item_list_default)
                .showImageOnFail(R.drawable.pic_item_list_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
//                .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .displayer(new RoundedBitmapDisplayer(30))
                .build();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.item_photos, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //1.根据位置得到对应的数据
        PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean = datas.get(position);
        //2.绑定数据
        holder.tvTitle.setText(newsBean.getTitle());
        //3.设置点击事件
        String imageUrl = ConstantUtils.BASE_URL + newsBean.getLargeimage();
        //使用Glide请求图片
        /*Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.pic_item_list_default)
                .error(R.drawable.pic_item_list_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivIcon);*/


        //使用自定义方式请求图片
//        Bitmap bitmap = bitmapCacheUtils.getBitmap(imageUrl,position);
//
//        //图片对应的TAG就是位置
//        holder.ivIcon.setTag(position);
//        if(bitmap != null) {//来自内存和本地，不包括网络
//            holder.ivIcon.setImageBitmap(bitmap);
//        }


        //用imageloader请求土图片
        imageLoader.displayImage(imageUrl, holder.ivIcon, options, animateFirstListener);

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_title)
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PicassoSampleActivity.class);
                    intent.setData(Uri.parse(ConstantUtils.BASE_URL + datas.get(getLayoutPosition()).getLargeimage()));
                    context.startActivity(intent);
//                    Log.e("TAG","MyViewHolder--Position" + datas.get(getLayoutPosition()).getListimage());
                }
            });
        }
    }


    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
