package com.yjq.knowledge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoViewActivity extends AppCompatActivity {
    @BindView(R.id.photo_view)
    PhotoView photoView;
    private String mPhoneUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ButterKnife.bind(this);
        initPara();
        initPhotoView();

    }


    private void initPara() {
        mPhoneUrl = getIntent().getStringExtra("photoUrl");
    }

    private void initPhotoView() {
        GlideApp.with(this)
                .load(mPhoneUrl)
                .centerInside()   //尽量在代码中使用该方法
                .into(photoView);
    }
}
