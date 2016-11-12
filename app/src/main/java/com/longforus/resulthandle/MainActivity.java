package com.longforus.resulthandle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.longforus.resulthandle.bean.GalleryListBean;
import com.longforus.resulthandle.net.GalleryListInt;
import com.longforus.resulthandle.net.RetrofitProxy;
import com.longforus.resulthandle.net.SchedulerTransformer;
import com.longforus.resulthandle.subscribers.MySubscriber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_send = (Button) findViewById(R.id.btn_send);

        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                initData();
                break;
        }
    }

    private void initData() {
        RetrofitProxy.createService(GalleryListInt.class)
                     .getGalleryList(1, 1, 20)
                     .compose(SchedulerTransformer.<GalleryListBean>getInstance())
                     .subscribe(
                         new MySubscriber<GalleryListBean>(this) {
                             @Override
                             public void onNext(GalleryListBean galleryListBean) {
                                 Toast.makeText(MainActivity.this, "一共获取到了"+galleryListBean.total+"个妹纸", Toast.LENGTH_SHORT)
                                      .show();
                             }
                         });
    }
}
