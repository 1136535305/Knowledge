package com.yjq.knowledge.photo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yjq.knowledge.GlideApp
import com.yjq.knowledge.R
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_photo_view.*

class PhotoViewActivity : AppCompatActivity() {
    private var mPhoneUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)
        ButterKnife.bind(this)
        initPara()
        initPhotoView()

    }


    private fun initPara() {
        mPhoneUrl = intent.getStringExtra("photoUrl")
    }

    private fun initPhotoView() {

        GlideApp.with(this)
                .load(mPhoneUrl)
                .centerInside()   //尽量在代码中使用该方法，xml里使用scaleType属性貌似不起作用
                .into(photoView)
    }
}
