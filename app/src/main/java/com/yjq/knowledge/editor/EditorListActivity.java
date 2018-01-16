package com.yjq.knowledge.editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yjq.knowledge.R;
import com.yjq.knowledge.adapter.EditorAdapter;
import com.yjq.knowledge.beans.zhihu.ZhihuThemeListDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditorListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rcy_editor)
    RecyclerView rcyEditor;


    public static final String EDITOR_LIST_DATA = "editorListData";
    private ArrayList<ZhihuThemeListDetail.EditorsBean> mEditorList;
    private EditorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_list);
        ButterKnife.bind(this);
        initToolbar();
        initData();
        initView();
        initEvent();

    }


    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);          //显示toolbar的回退按钮
    }

    private void initData() {
        mEditorList = (ArrayList<ZhihuThemeListDetail.EditorsBean>) getIntent().getSerializableExtra(EDITOR_LIST_DATA);

    }

    private void initView() {
        mAdapter = new EditorAdapter(this);
        rcyEditor.setAdapter(mAdapter);
        rcyEditor.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setmDataSet(mEditorList);

    }


    private void initEvent() {
        mAdapter.getOnClicks().observeOn(AndroidSchedulers.mainThread())    //左侧边栏菜单Item项的点击事件，具体是指点击某个主题日报时
                .subscribeOn(Schedulers.io())
                .subscribe(editorId -> {
                    Intent intent = new Intent(this, EditorDetailActivity.class);
                    intent.putExtra(EditorDetailActivity.EDITOR_ID, editorId);
                    startActivity(intent);
                });
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
