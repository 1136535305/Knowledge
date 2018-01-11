package com.yjq.knowledge.zhihuNewsdetail;

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
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.yjq.knowledge.GlideApp;
import com.yjq.knowledge.R;
import com.yjq.knowledge.beans.zhihu.ZhihuNewsDetail;
import com.yjq.knowledge.beans.zhihu.ZhihuStoryExtra;
import com.yjq.knowledge.contract.ZhihuNewsDetailContract;
import com.yjq.knowledge.util.HtmlUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhihuNewsDetailActivity extends AppCompatActivity implements ZhihuNewsDetailContract.Iview, View.OnClickListener {

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
    private ZhihuNewsDetailContract.Ipresenter mPresenter;
    private int mNewsId;//知乎日报新闻唯一标志ID


    private Bundle mStartValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_news_detail);
        ButterKnife.bind(this);

        initToolbar();

        mNewsId = getIntent().getIntExtra("newsId", 0);

        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new ZhihuNewsDetailPresenter(this);
        mPresenter.loadNewsDetailById(mNewsId);
        mPresenter.showNewsExtra(mNewsId);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);                                   //用自带的Toolbar替换掉原来的状态栏
        View v = getLayoutInflater().inflate(R.layout.menu_toolbar, toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);          //显示toolbar的回退按钮
        btnShare = v.findViewById(R.id.btn_share);
        btnThumbsUp = v.findViewById(R.id.btn_thumbs_up);
        tvComments = v.findViewById(R.id.tv_comments);
        tvPopularity = v.findViewById(R.id.tv_popularity);
        btnThumbsUp.setOnClickListener(this);
        tvPopularity.setOnClickListener(this);
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

        String htmlData = HtmlUtil.createHtmlData(zhihuNewsDetail, false);
        webView.loadData(htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING);

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
            default:
                break;
        }
    }

    /**
     * 处理点赞的方法操作
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
}
