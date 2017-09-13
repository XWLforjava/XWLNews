package com.oushangfeng.ounews.module.news.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oushangfeng.ounews.R;
import com.oushangfeng.ounews.annotation.ActivityFragmentInject;
import com.oushangfeng.ounews.base.BaseActivity;
import com.oushangfeng.ounews.bean.NeteastNewsDetail;
import com.oushangfeng.ounews.bean.SinaPhotoDetail;
import com.oushangfeng.ounews.bean.TsinghuaNewsDetail;
import com.oushangfeng.ounews.bean.TsinghuaNewsSummary;
import com.oushangfeng.ounews.collection.CollectionManager;
import com.oushangfeng.ounews.module.news.presenter.INewsDetailPresenter;
import com.oushangfeng.ounews.module.news.presenter.INewsDetailPresenterImpl;
import com.oushangfeng.ounews.module.news.view.INewsDetailView;
import com.oushangfeng.ounews.module.photo.ui.PhotoDetailActivity;
import com.oushangfeng.ounews.module.video.ui.VideoPlayActivity;
import com.oushangfeng.ounews.share.ShareUtil;
import com.oushangfeng.ounews.utils.GlideUtils;
import com.oushangfeng.ounews.utils.MeasureUtil;
import com.oushangfeng.ounews.utils.ViewUtil;
import com.oushangfeng.ounews.widget.ThreePointLoadingView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


import zhou.widget.RichText;

/**
 * ClassName: NewsDetailActivity<p>
 * Author: oubowu<p>
 * Fuction: 新闻详情界面<p>
 * CreateDate: 2016/2/20 2:12<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
@ActivityFragmentInject(contentViewId = R.layout.activity_news_detail,
        //menuId = R.menu.menu_settings,
        menuId = R.menu.menu_news_details,
        enableSlidr = true)
public class NewsDetailActivity extends BaseActivity<INewsDetailPresenter> implements INewsDetailView {

    private ThreePointLoadingView mLoadingView;
    private ImageView mNewsImageView;
    private TextView mTitleTv;
    private TextView mFromTv;
    private RichText mBodyTv;
    private Toolbar mToolbar;
    private Menu mMenu;

    private FloatingActionButton mFabPic, mFabRead;

    private String mNewsListSrc;
    private SinaPhotoDetail mSinaPhotoDetail;

    private TsinghuaNewsSummary mSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            // 设置全屏，并且不会Activity的布局让出状态栏的空间
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            ViewUtil.showStatusBar(this);
        }

        getWindow().setBackgroundDrawable(null);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            // 4.4设置全屏并动态修改Toolbar的位置实现类5.0效果，确保布局延伸到状态栏的效果
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) mToolbar.getLayoutParams();
            mlp.topMargin = MeasureUtil.getStatusBarHeight(this);
        }

        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(getString(R.string.news_detail));
        toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.accent));
        toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.material_white));

        mNewsImageView = (ImageView) findViewById(R.id.iv_news_detail_photo);

        mLoadingView = (ThreePointLoadingView) findViewById(R.id.tpl_view);
        mLoadingView.setOnClickListener(this);

        mTitleTv = (TextView) findViewById(R.id.tv_news_detail_title);

        mFromTv = (TextView) findViewById(R.id.tv_news_detail_from);

        mBodyTv = (RichText) findViewById(R.id.tv_news_detail_body);

        mFabPic = (FloatingActionButton) findViewById(R.id.fab_pic);
        mFabPic.setOnClickListener(this);
        mFabRead = (FloatingActionButton) findViewById(R.id.fab_reading);
        mFabRead.setOnClickListener(this);

        mNewsListSrc = getIntent().getStringExtra("imgsrc");

        mPresenter = new INewsDetailPresenterImpl(this, getIntent().getStringExtra("postid"));

        ObjectMapper mapper = new ObjectMapper();
        try {
            mSummary = mapper.readValue(getIntent().getStringExtra("summary"), TsinghuaNewsSummary.class);
        }catch (IOException e){

        }

    }

    @Override
    public void initNewsDetail(final TsinghuaNewsDetail data) {
        //lcy: remove videos
        /*
        if (data.video != null && data.video.size() > 0) {
            final NeteastNewsDetail.VideoEntity video = data.video.get(0);
            final String mp4HdUrl = video.mp4HdUrl;
            final String mp4Url = video.mp4Url;
            if (!TextUtils.isEmpty(mp4HdUrl)) {
                mFabPic.setImageResource(R.drawable.ic_play_normal);
                mFabPic.setTag(mp4HdUrl);
            } else if (!TextUtils.isEmpty(mp4Url)) {
                mFabPic.setImageResource(R.drawable.ic_play_normal);
                mFabPic.setTag(mp4Url);
            }

        }
        */


        final String pics[] = data.pictures.split(";| ");
        if (data.pictures != null && data.pictures.length() > 0 && pics.length > 0) {
            // 设置tag用于点击跳转浏览图片列表的时候判断是否有图片可供浏览
            mNewsImageView.setTag(R.id.img_tag, true);
            // 显示第一张图片，通过pixel字符串分割得到图片的分辨率
            String[] pixel = null;

            try
            {
                URL picture = new URL(pics[0]);
                InputStream istream = picture.openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(istream);
                pixel = new String[2];
                pixel[0] = Integer.toString(bitmap.getWidth());
                pixel[1] = Integer.toString(bitmap.getHeight());
            }
            catch(Exception e){}

            /*if (!TextUtils.isEmpty(data.img.get(0).pixel)) {
                // pixel可能为空
                pixel = data.img.get(0).pixel.split("\\*");
            }*/
            // 图片高清显示，按屏幕宽度为准缩放
            if (pixel != null && pixel.length == 2) {

                // KLog.e(pixel[0] + ";" + pixel[1]);

                final int w = MeasureUtil.getScreenSize(this).x;
                final int h = Integer.parseInt(pixel[1]) * w / Integer.parseInt(pixel[0]);

                // KLog.e(w + ";" + h);

                if (pics[0].contains(".gif")) {
                    mNewsImageView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GlideUtils.loadDefaultOverrideNoAnim(pics[0], mNewsImageView, w, h, true, null, DiskCacheStrategy.SOURCE);
                            //                            Glide.with(mNewsImageView.getContext()).load(data.img.get(0).src).asGif().animate(R.anim.image_load).placeholder(R.drawable.ic_loading).error(R.drawable.ic_fail)
                            //                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).override(w, h).into(mNewsImageView);
                        }
                    }, 500);
                } else {
                    GlideUtils.loadDefaultOverrideNoAnim(pics[0], mNewsImageView, w, h, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.ALL);
                    //                    Glide.with(this).load(data.img.get(0).src).asBitmap().animate(R.anim.image_load).placeholder(R.drawable.ic_loading)
                    //                            .format(DecodeFormat.PREFER_ARGB_8888).error(R.drawable.ic_fail).diskCacheStrategy(DiskCacheStrategy.ALL).override(w, h).into(mNewsImageView);
                }
            } else {
                GlideUtils.loadDefaultNoAnim(pics[0], mNewsImageView, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.ALL);
                //                Glide.with(this).load(data.img.get(0).src).asBitmap().animate(R.anim.image_load).placeholder(R.drawable.ic_loading).format(DecodeFormat.PREFER_ARGB_8888)
                //                        .diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ic_fail).into(mNewsImageView);
            }

            if (mFabPic.getTag() == null) {
                // 以下将数据封装成新浪需要的格式，用于点击跳转到图片浏览
                mSinaPhotoDetail = new SinaPhotoDetail();
                mSinaPhotoDetail.data = new SinaPhotoDetail.SinaPhotoDetailDataEntity();
                mSinaPhotoDetail.data.title = data.title;
                //mSinaPhotoDetail.data.content = data.digest;//detail没有intro
                mSinaPhotoDetail.data.pics = new ArrayList<>();
                for (String x : pics) {
                    SinaPhotoDetail.SinaPhotoDetailPicsEntity sinaPicsEntity = new SinaPhotoDetail.SinaPhotoDetailPicsEntity();
                    sinaPicsEntity.pic = x;
                    //sinaPicsEntity.alt = entiity.alt;
                    sinaPicsEntity.kpic = x;
                    if (pixel != null && pixel.length == 2) {
                        // 新浪分辨率是按100x100这种形式的
                        sinaPicsEntity.size = pixel[0] + "x" + pixel[1];
                    }
                    mSinaPhotoDetail.data.pics.add(sinaPicsEntity);
                }
            }

        } else {
            // 图片详情列表没有数据的时候，取图片列表页面传送过来的图片显示
            mNewsImageView.setTag(R.id.img_tag, false);
            GlideUtils.loadDefault(mNewsListSrc, mNewsImageView, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.ALL);
            //            Glide.with(this).load(mNewsListSrc).asBitmap().animate(R.anim.image_load).placeholder(R.drawable.ic_loading).diskCacheStrategy(DiskCacheStrategy.ALL)
            //                    .format(DecodeFormat.PREFER_ARGB_8888).error(R.drawable.ic_fail).into(mNewsImageView);
        }


        mTitleTv.setText(data.title);
        // 设置新闻来源和发布时间
        mFromTv.setText(getString(R.string.from, data.source, data.time));
        // 新闻内容可能为空
        if (!TextUtils.isEmpty(data.content)) {
            mBodyTv.setRichText(data.content);
        }

    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
    }*/

    @Override
    public void showProgress() {
        mLoadingView.play();
    }

    @Override
    public void hideProgress() {
        mLoadingView.stop();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_pic) {
            //显示图片新闻
            if (mFabPic.getTag() != null && mFabPic.getTag() instanceof String) {
                Intent intent = new Intent(this, VideoPlayActivity.class);
                intent.putExtra("videoUrl", (String) mFabPic.getTag());
                intent.putExtra("videoName", mTitleTv.getText().toString());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                ActivityCompat.startActivity(this, intent, options.toBundle());
            } else {
                if (mNewsImageView.getTag(R.id.img_tag) != null && !(boolean) mNewsImageView.getTag(R.id.img_tag) || mSinaPhotoDetail == null) {
                    toast("没有图片供浏览哎o(╥﹏╥)o");
                } else {
                    Intent intent = new Intent(this, PhotoDetailActivity.class);
                    intent.putExtra("neteast", mSinaPhotoDetail);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                    ActivityCompat.startActivity(this, intent, options.toBundle());
                }
            }
        }
        else if(v.getId() == R.id.fab_reading){
            //...语音读出新闻
            toast(mBodyTv.getText().toString());
            //toast("语音读出新闻，尚未完成");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_to_collection) {
            if(CollectionManager.getInstance().checkInCollections(mSummary)){
                CollectionManager.getInstance().deleteCollection(mSummary);
                toast("已从收藏夹移除");
                mMenu.getItem(0).setTitle(R.string.add_to_collection);
            }
            else{
                CollectionManager.getInstance().addCollection(mSummary);
                toast("已加入收藏夹");
                mMenu.getItem(0).setTitle(R.string.delete_from_collection);
            }
        }
        else if(item.getItemId() == R.id.menu_share){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

            String str = "";
            str = "来自XWLNews:\n" + mSummary.title + "\n" + mSummary.intro + "\n" + "原网址: " + mSummary.url;
            Bitmap pic = null;
            /*
            if(mSummary.pictures.equals(""))
                pic = BitmapFactory.decodeResource(getResources(), R.drawable.ic_fail);
            else{
                try {
                    pic = Glide.with(this).load(mSummary.pictures).asBitmap().centerCrop().into(500, 500).get();
                }catch (Exception e){
                    pic = BitmapFactory.decodeResource(getResources(), R.drawable.ic_fail);
                }
            }
            */
            //mNewsImageView.setImageBitmap(pic);
            mNewsImageView.setDrawingCacheEnabled(true);
            pic = Bitmap.createBitmap(mNewsImageView.getDrawingCache());
            mNewsImageView.setDrawingCacheEnabled(false);
            //mNewsImageView.setImageBitmap(pic);
            Uri picuri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), pic, "", ""));
            ShareUtil shareUtil = new ShareUtil(this);
            shareUtil.shareToWXCircle(str, picuri);
            //toast("分享功能尚未完成");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getmMenuId(), menu);
        mMenu = mToolbar.getMenu();
        if(CollectionManager.getInstance().checkInCollections(mSummary)){
            mMenu.getItem(0).setTitle(R.string.delete_from_collection);
        }
        else{
            mMenu.getItem(0).setTitle(R.string.add_to_collection);
        }
        return true;
    }

}
