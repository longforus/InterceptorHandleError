package com.longforus.resulthandle.subscribers;

import android.content.Context;
import com.longforus.resulthandle.exception.ResultException;
import com.longforus.resulthandle.utils.DialogUtils;

/**
 * Created by Void Young on 11/11/2016  9:15 PM.
 * Description :
 */

public abstract class MySubscriber<T> extends CommonSubscriber<T> {

    public MySubscriber(Context context) {
        super(context);
    }

    @Override
    protected void startSubscriber() {
        DialogUtils.showProgressDlg(mContext,"开始加载");
    }

    @Override
    protected void onError(ResultException e) {
        super.onError(e);
        DialogUtils.stopProgressDlg();
    }

    @Override
    public void onCompleted() {
        DialogUtils.stopProgressDlg();
    }
}
