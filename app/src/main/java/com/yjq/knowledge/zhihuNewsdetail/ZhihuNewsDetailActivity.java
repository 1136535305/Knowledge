package com.yjq.knowledge.zhihuNewsdetail;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yjq.knowledge.R;
import com.yjq.knowledge.beans.ZhihuDaily;
import com.yjq.knowledge.beans.ZhihuNewsDetail;
import com.yjq.knowledge.beans.ZhihuStoryExtra;
import com.yjq.knowledge.contract.ZhihuNewsDetailContract;
import com.yjq.knowledge.source.HtmlHttpImageGetter;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhihuNewsDetailActivity extends AppCompatActivity implements ZhihuNewsDetailContract.Iview {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.html_text)
    HtmlTextView htmlText;
    @BindView(R.id.root_view)
    CoordinatorLayout rootView;
    @BindView(R.id.image_zhihu)
    ImageView imageZhihu;
    private TextView tvPopularity;
    private TextView tvComments;
    private ImageButton btnShare;


    private ZhihuNewsDetailContract.Ipresenter mPresenter;
    private ZhihuDaily.StoriesBean mStoriesBean;
    private String mNewsId;//知乎日报新闻唯一标志ID


    private Bundle mStartValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_news_detail);
        ButterKnife.bind(this);

        initToolbar();

        mStoriesBean = (ZhihuDaily.StoriesBean) getIntent().getSerializableExtra("storiesBean");

        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new ZhihuNewsDetailPresenter(this);
        mPresenter.loadNewsDetailById(mStoriesBean.getId());
        mPresenter.showNewsExtra(mStoriesBean.getId());
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);                                   //用自带的Toolbar替换掉原来的状态栏
        View v =getLayoutInflater().inflate(R.layout.menu_toolbar, toolbar);
        btnShare=v.findViewById(R.id.btn_share);
        tvPopularity=v.findViewById(R.id.tv_popularity);
        tvComments=v.findViewById(R.id.tv_comments);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);          //显示toolbar的回退按钮
    }

    @Override
    public void showNewsDetail(ZhihuNewsDetail zhihuNewsDetail) {


        toolbarLayout.setTitle(zhihuNewsDetail.getTitle());                //加载标题
        Glide.with(this)                                           //加载图片
                .load(zhihuNewsDetail.getImage())
                .into(imageZhihu);
        HtmlHttpImageGetter htmlHttpImageGetter = new HtmlHttpImageGetter(htmlText);
        htmlHttpImageGetter.enableCompressImage(false);
        htmlText.setHtml(zhihuNewsDetail.getBody(), htmlHttpImageGetter);
    }

    @Override
    public void showNewsExtra(ZhihuStoryExtra zhihuStoryExtra) {
        tvComments.setText(zhihuStoryExtra.getComments());
        tvPopularity.setText(zhihuStoryExtra.getPopularity());
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
