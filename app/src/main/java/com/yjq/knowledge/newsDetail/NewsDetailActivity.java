package com.yjq.knowledge.newsDetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.yjq.knowledge.GlideApp;
import com.yjq.knowledge.R;
import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail;
import com.yjq.knowledge.beans.zhihu.ZhihuStoryExtra;
import com.yjq.knowledge.comments.CommentsActivityKotlin;
import com.yjq.knowledge.contract.NewsDetailContract;
import com.yjq.knowledge.photo.PhotoViewActivity;
import com.yjq.knowledge.util.HtmlUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends AppCompatActivity implements NewsDetailContract.Iview, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.root_view)
    CoordinatorLayout rootView;
    @BindView(R.id.image_zhihu)
    ImageView imageZhihu;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_source)
    TextView tvSource;
    @BindView(R.id.web_view)
    WebView webView;
    private TextSwitcher tvPopularity;
    private TextView tvComments;
    private ImageButton btnThumbsUp;
    private ImageButton btnShare;


    private boolean mThumbsUp = false;
    private NewsDetailContract.Ipresenter mPresenter;
    private int mNewsId;//知乎日报新闻唯一标志ID

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_news_detail);
        ButterKnife.bind(this);
        initPara();
        initToolbar();
        initWebView();
        initPresenter();
        initEvent();
    }


    private void initPara() {
        mNewsId = getIntent().getIntExtra("newsId", 0);
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);                                   //用自带的Toolbar替换掉原来的状态栏
        View v = getLayoutInflater().inflate(R.layout.menu_toolbar, toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);          //显示toolbar的回退按钮
        //toolbarLayout.setTitle("collapse标题");                         //由于我们已经替换掉了toolbar上的布局，故设置标题不再起作用
        btnShare = v.findViewById(R.id.btn_share);
        btnThumbsUp = v.findViewById(R.id.btn_thumbs_up);
        tvComments = v.findViewById(R.id.tv_comments);
        tvPopularity = v.findViewById(R.id.tv_popularity);

    }


    private void initPresenter() {
        mPresenter = new NewsDetailPresenter(this);
        mPresenter.loadNewsDetailById(mNewsId);
        mPresenter.showNewsExtra(mNewsId);
    }


    private void initWebView() {

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //设置 缓存模式
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setLoadsImagesAutomatically(true); //自动加载图片
        settings.setPluginState(WebSettings.PluginState.OFF);


        webView.addJavascriptInterface(new MyJavascript(this), "imageListener");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，
                //函数的功能是在图片点击的时候调用本地java接口并传递url过去
                webView.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName(\"img\"); " +
                        "for(var i=0;i<objs.length;i++)  " +
                        "{"
                        + "    objs[i].onclick=function()  " +
                        "    {  "
                        + "        window.imageListener.openImage(this.src);  " +      //单击图片查看图片详情
                        "    }  " +
                        "}" +
                        "})()");

            }

        });


        //长按图片查看图片详情
        webView.setOnLongClickListener((View v) -> {
            WebView.HitTestResult result = webView.getHitTestResult();
            if (result.getType() == WebView.HitTestResult.IMAGE_TYPE) {
                String url = result.getExtra();

                Intent photoIntent = new Intent(this, PhotoViewActivity.class);
                photoIntent.putExtra("photoUrl", url);
                startActivity(photoIntent);
                return true;
            }
            return false;
        });


    }


    private void initEvent() {
        btnThumbsUp.setOnClickListener(this);
        tvPopularity.setOnClickListener(this);
        tvComments.setOnClickListener(this);
        btnShare.setOnClickListener(this);
    }

    @Override
    public void showNewsDetail(ZhihuNewsDetail zhihuNewsDetail) {


        tvTitle.setText(zhihuNewsDetail.getTitle());
        tvSource.setText(zhihuNewsDetail.getImage_source());
        if (zhihuNewsDetail.getImages() != null) {
            GlideApp.with(this)                                           //加载图片
                    .load(zhihuNewsDetail.getImages().get(0))
                    .into(imageZhihu);
        }

        String htmlData = HtmlUtil.createHtmlData(zhihuNewsDetail, false);//根据Api接口返回的数据重新构造一个完整的Html页面并且用webView加载这个页面
        webView.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);
        webView.getSettings().setBlockNetworkImage(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.getSettings().setBlockNetworkImage(false);
            }
        });

    }

    @Override
    public void showNewsExtra(ZhihuStoryExtra zhihuStoryExtra) {
        tvComments.setText(zhihuStoryExtra.getComments());
        tvPopularity.setCurrentText(zhihuStoryExtra.getPopularity());       //不带动画效果的
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:                                        //点击回退按钮结束当前Activity
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_popularity:     //点赞的图标部分
                dealThumbsUp();
                break;
            case R.id.btn_thumbs_up:    //点赞的文字部分
                dealThumbsUp();
                break;
            case R.id.tv_comments:      //跳转评论页面
                Intent commentIntent = new Intent(this, CommentsActivityKotlin.class);
                commentIntent.putExtra("newsId", mNewsId);
                startActivity(commentIntent);
                break;
            case R.id.btn_share:       //分享頁面
                //TODO 分享功能
                break;
            default:
                break;
        }
    }

    /**
     * 处理点赞的方法操作，仅仅涉及到UI上的效果
     */
    private void dealThumbsUp() {

        TextView temp = (TextView) tvPopularity.getCurrentView();
        Drawable nextDrawable;
        int currentValue = Integer.valueOf(temp.getText().toString());          //获取当前点赞数
        if (mThumbsUp) {                                                       //已经点赞过，再次点击会取消点赞，【赞】数 - 1
            nextDrawable = getResources().getDrawable(R.drawable.thumb_up_outline);
            tvPopularity.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_down_thumbs_up));      //新的TextView向下滑进
            tvPopularity.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_down_thumbs_up));   //旧的TextView项下滑出
            currentValue--;
        } else {                                                               //仍未点赞 ,  点击会点赞，【赞】数 + 1
            nextDrawable = getResources().getDrawable(R.drawable.thumb_up);
            tvPopularity.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_forward_thumbs_up));  //新的TextView向上滑进
            tvPopularity.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_forward_thumbs_up)); //旧的TextView向上滑出
            currentValue++;
        }

        mThumbsUp = !mThumbsUp;                                            //点赞的状态改变
        btnThumbsUp.setBackground(nextDrawable);
        tvPopularity.setText(currentValue + "");

    }


    /**
     * 自定义的用于实现注入JavaScript代码到WebView加载的Html页面中，从而调用自定义的Java方法代码
     */
    class MyJavascript {
        private Context context;

        public MyJavascript(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void openImage(String imgUrl) {
            Intent photoIntent = new Intent(context, PhotoViewActivity.class);
            photoIntent.putExtra("photoUrl", imgUrl);
            startActivity(photoIntent);
        }
    }


}
