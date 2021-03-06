package com.java.two.module.settings.ui;

import android.content.res.ColorStateList;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.CheckedTextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.java.two.R;
import com.java.two.annotation.ActivityFragmentInject;
import com.java.two.base.BaseActivity;
import com.java.two.module.settings.presenter.ISettingsPresenter;
import com.java.two.module.settings.presenter.ISettingsPresenterImpl;
import com.java.two.module.settings.view.ISettingsView;
import com.java.two.utils.ClickUtils;
import com.java.two.utils.RxBus;
import com.java.two.utils.SpUtil;
import com.java.two.utils.ThemeUtil;
import com.java.two.utils.ViewUtil;
import com.zhy.changeskin.SkinManager;

@ActivityFragmentInject(contentViewId = R.layout.activity_settings,
        menuId = R.menu.menu_settings,
        hasNavigationView = true,
        toolbarTitle = R.string.settings,
        toolbarIndicator = R.drawable.ic_list_white,
        menuDefaultCheckedItem = R.id.action_settings)
public class SettingsActivity extends BaseActivity<ISettingsPresenter> implements ISettingsView {

    private CheckedTextView mNightModeCheckedTextView;
    private CheckedTextView mSlideModeCheckedTextView;

    @Override
    protected void initView() {

        mNightModeCheckedTextView = (CheckedTextView) findViewById(R.id.ctv_night_mode);
        mSlideModeCheckedTextView = (CheckedTextView) findViewById(R.id.ctv_slide_mode);

        mNightModeCheckedTextView.setOnClickListener(this);
        mSlideModeCheckedTextView.setOnClickListener(this);

        findViewById(R.id.tv_about).setOnClickListener(this);

        mPresenter = new ISettingsPresenterImpl(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ctv_night_mode:

                if (ClickUtils.isFastDoubleClick()) {
                    return;
                }

                boolean nightModeCheck = !((CheckedTextView) v).isChecked();

                ((CheckedTextView) v).setChecked(nightModeCheck);
                SkinManager.getInstance().changeSkin(nightModeCheck ? "night" : "");
                SpUtil.writeBoolean("enableNightMode", nightModeCheck);

                // 这里设置主题不起作用，但是我们弹窗时候的主题和着色时的颜色状态列表属性是需要主题支持的
                setTheme(nightModeCheck ? R.style.BaseAppThemeNight_AppTheme : R.style.BaseAppTheme_AppTheme);

                applyTint(mNightModeCheckedTextView);
                applyTint(mSlideModeCheckedTextView);

                mNightModeCheckedTextView.setText(nightModeCheck ? "关闭夜间模式" : "开启夜间模式");

                // 主题更改了，发送消息通知其他导航Activity销毁掉
                RxBus.get().post("finish", true);

                break;
            case R.id.ctv_slide_mode:

                final boolean slideModeCheck = !((CheckedTextView) v).isChecked();

                mSlideModeCheckedTextView.setText(slideModeCheck ? "关闭侧滑返回" : "开启侧滑返回");

                ((CheckedTextView) v).setChecked(slideModeCheck);

                SpUtil.writeBoolean("disableSlide", !slideModeCheck);

                if (slideModeCheck) {
                    String currentSlideMode = SpUtil.readBoolean("enableSlideEdge") ? "边缘侧滑" : "整页侧滑";
                    new MaterialDialog.Builder(this).title("选择侧滑模式(当前为" + currentSlideMode + ")").items(R.array.slide_mode_items)
                            .itemsCallbackSingleChoice(SpUtil.readBoolean("enableSlideEdge") ? 0 : 1, new MaterialDialog.ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    SpUtil.writeBoolean("enableSlideEdge", which == 0);
                                    return true;
                                }
                            }).positiveText("选择").show();
                }
                break;
            case R.id.tv_about:
                final MaterialDialog dialog = new MaterialDialog.Builder(this).title("说明").titleGravity(GravityEnum.CENTER).content("").show();
                dialog.getContentView().setText(Html.fromHtml("&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;此练习项目API来源于:高仿网易新闻客户端"+"<a href='https://github.com/tigerguixh/QuickNews'>QuickNews</a><br>"
                        +"&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;使用了诸如:<br>"
                        + "<a href='https://github.com/square/retrofit'>Retrofit2.0</a>,"
                        +"<a href='https://github.com/ReactiveX/RxJava'>RxJava</a>,"
                        +"<a href='https://github.com/greenrobot/greenDAO'>GreenDAO</a>,"
                        +"<a href='https://github.com/bumptech/glide'>Glide</a>,"
                        +"<a href='https://github.com/hongyangAndroid/AndroidChangeSkin'>AndroidChangeSkin</a>,"
                        +"<a href='https://github.com/Bilibili/ijkplayer'>Ijkplayer</a><br>等优秀开源项目。<br>"
                        +"&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;自己也自定义了刷新的控件，加载的控件，封装了RecyclerView的适配器，对MVP模式进行了基类的提取，用<a href='https://github.com/square/okhttp'>OkHttp</a>实现了请求缓存。<br>"
                        +"&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;App分为新闻、视频、图片三个模块，特色功能有换肤、侧滑返回。<br>"
                        +"&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;希望关注的朋友能与我交流学习。<br>"
                ));
                break;
        }
    }

    @Override
    public void initItemState() {

        applyTint(mNightModeCheckedTextView);
        applyTint(mSlideModeCheckedTextView);

        mNightModeCheckedTextView.setChecked(SpUtil.readBoolean("enableNightMode"));
        mNightModeCheckedTextView.setText(SpUtil.readBoolean("enableNightMode") ? "关闭夜间模式" : "开启夜间模式");

        mSlideModeCheckedTextView.setChecked(!SpUtil.readBoolean("disableSlide"));
        mSlideModeCheckedTextView.setText(!SpUtil.readBoolean("disableSlide") ? "关闭侧滑返回" : "开启侧滑返回");

    }

    // 因为这里是通过鸿洋大神的换肤做的，而CheckedTextView着色不兼容5.0以下，
    // 所以切换皮肤的时候动态加载当前主题自定义的ColorStateList，对CheckMarkDrawable进行着色
    private void applyTint(CheckedTextView checkedTextView) {
        ColorStateList indicator = ThemeUtil.getColorStateList(this, R.attr.checkTextViewColorStateList);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkedTextView.setCheckMarkTintList(indicator);
        } else {
            ViewUtil.tintDrawable(checkedTextView.getCheckMarkDrawable(), indicator);
        }
    }

}
