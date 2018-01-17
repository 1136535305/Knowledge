package com.yjq.knowledge.comments;

import android.content.Intent;
import android.content.MutableContextWrapper;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.orhanobut.logger.Logger;
import com.yjq.knowledge.R;
import com.yjq.knowledge.adapter.CommentAdapter;
import com.yjq.knowledge.beans.zhihu.ZhihuLongComments;
import com.yjq.knowledge.beans.zhihu.ZhihuShortComments;
import com.yjq.knowledge.editor.EditorDetailActivity;
import com.yjq.knowledge.network.ApiManager;
import com.yjq.knowledge.util.animate.MyLinearLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CommentsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.comments_rcy)
    RecyclerView commentsRcy;

    private int newsId;
    private volatile boolean loadLongCommentsFinish = false;
    private volatile boolean loadShortCommentsFinish = false;
    private ZhihuLongComments mLongComments;
    private ZhihuShortComments mShortComments;
    private CommentAdapter mCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        initPara();
        initToolbar();
        initRecyclerView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mCommentAdapter.getOnClicks().observeOn(AndroidSchedulers.mainThread())    //点击“x条短评论” item项触发的事件
                .subscribeOn(Schedulers.io())
                .subscribe(position -> {
                            boolean isShowShortComment = mCommentAdapter.ismShowShortComments();

                            mCommentAdapter.setmShowShortComments(!isShowShortComment);
                            mCommentAdapter.notifyDataSetChanged();
                            if (!isShowShortComment) {
                                commentsRcy.smoothScrollToPosition(position);                           //展开短评论，“x条短评论”item项置顶
                            } else {
                                commentsRcy.smoothScrollToPosition(0);                                  //折叠短评论，“x条长评论”并且回到顶部
                            }
                        }
                );
    }


    private void initPara() {
        newsId = getIntent().getIntExtra("newsId", -1);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    //回退按钮
        toolbar.setTitle("0 条评论");
    }

    private void initRecyclerView() {
        mCommentAdapter = new CommentAdapter(this);
        commentsRcy.setLayoutManager(new MyLinearLayoutManager(this));
        commentsRcy.setAdapter(mCommentAdapter);
    }

    private void initData() {

        //网络加载长评论信息
        ApiManager.getInstance().createZhihuService().getLongComments(newsId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZhihuLongComments>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("加载知乎日报新闻ID为%d的【长评论数据】完成", newsId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("加载知乎日报新闻ID为%d的【长评论数据】出錯", newsId);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ZhihuLongComments zhihuLongComments) {
                        mLongComments = zhihuLongComments;
                        loadLongCommentsFinish = true;
                        if (loadLongCommentsFinish && loadShortCommentsFinish) {
                            mCommentAdapter.setDataSet(mLongComments, mShortComments);
                        }


                    }
                });

        //网络加载段评论信息
        ApiManager.getInstance().createZhihuService().getShortCommnets(newsId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ZhihuShortComments>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("加载知乎日报新闻ID为%d的【短评论数据】完成", newsId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("加载知乎日报新闻ID为%d的【短评论数据】出錯", newsId);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ZhihuShortComments zhihuShortComments) {
                        mShortComments = zhihuShortComments;
                        loadShortCommentsFinish = true;
                        if (loadLongCommentsFinish && loadShortCommentsFinish) {
                            mCommentAdapter.setDataSet(mLongComments, mShortComments);
                        }
                    }
                });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comments, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:                                        //点击回退按钮结束当前Activity
                finish();
                break;
            case R.id.menu_action_edit_comment:
                //TODO 跳转编辑评论
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
