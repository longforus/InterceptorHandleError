package com.longforus.resulthandle.subscribers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.longforus.resulthandle.exception.ResultException;
import com.longforus.resulthandle.net.NetworkUtil;

/**
 * Created by Void Young on 11/11/2016  9:04 PM.
 * Description : 更一般的订阅者,在开始前检查网络状态,没有网络则直接调用onError
 */

public abstract class CommonSubscriber<T> extends BaseSubscriber<T> {
    private static final String TAG = "CommonSubscriber";
    protected Context mContext;

    public CommonSubscriber(Context context) {
        mContext = context;
    }

    @Override
    public void onStart() {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "当前无网络", Toast.LENGTH_SHORT)
                 .show();
            onCompleted();
        } else {
            startSubscriber();
        }
    }

    /**
     * 有网络正式开始了
     */
    protected abstract void startSubscriber();

    @Override
    protected void onError(ResultException e) {
        Log.w(TAG, "onError: "+e.getMessage(),e );
        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT)
             .show();
    }
}
