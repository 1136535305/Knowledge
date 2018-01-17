package com.yjq.knowledge.editor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yjq.knowledge.GlideApp;
import com.yjq.knowledge.R;
import com.yjq.knowledge.network.ApiManager;
import com.yjq.knowledge.util.GlideCircleTransform;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditorDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    public static final String EDITOR_ID = "editor_id";
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_editor_name_1)
    TextView tvEditorName1;
    @BindView(R.id.tv_bio)
    TextView tvBio;
    @BindView(R.id.tv_zhihu_name)
    TextView tvZhihuName;
    @BindView(R.id.tv_xinlang_name)
    TextView tvXinlangName;
    @BindView(R.id.tv_website)
    TextView tvWebsite;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    private int mEditorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_detail);
        ButterKnife.bind(this);
        initToolbar();
        initPara();
        initData();
    }


    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);          //显示toolbar的回退按钮
    }

    private void initPara() {
        mEditorId = Integer.valueOf(getIntent().getStringExtra(EDITOR_ID));
    }

    private void initData() {
        ApiManager.getInstance().createZhihuService().getEditorMainPageHtml(mEditorId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Logger.i("请求某一主编Id为%s的Html主页数据完成！", mEditorId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("请求某一主编Id为%s的Html主页数据失败！", mEditorId);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            analyzeHtmlString(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                });
    }


    private void analyzeHtmlString(String htmlString) {
        Document doc = Jsoup.parse(htmlString);

        String imgUrl = doc.select("img").first().attr("src");      //获取主编的头像Url地址
        String editorName = doc.select("h1").first().text();                    //获取主编的昵称
        String bio = doc.select("p").first().text();                            //获取主编的简要职位介绍

        String zhihuName = doc.select("span.content").eq(0).text();             //知乎账号名称
        String xinlangName = doc.select("span.content").eq(1).text();           //新浪账号名称
        String personalWebSite = doc.select("span.content").eq(2).text();       //个人网站地址
        String email = doc.select("span.content").eq(3).text();                 //邮箱地址


        tvBio.setText(bio);
        tvEditorName1.setText(editorName);
        tvZhihuName.setText(zhihuName);
        tvXinlangName.setText(xinlangName);
        tvEmail.setText(email);
        tvWebsite.setText(personalWebSite);
        GlideApp.with(this)
                .load(imgUrl)
                .transform(new GlideCircleTransform(this))
                .into(ivAvatar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
