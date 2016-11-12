package com.longforus.resulthandle.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Void Young on 11/12/2016  9:00 PM.
 * Description :  显示进度框
 */

public class DialogUtils {

    static ProgressDialog progressDlg = null;

    public static void showProgressDlg(Context ctx, String strMessage) {

        if (null == progressDlg) {
            if (ctx == null) return;
            progressDlg = new ProgressDialog(ctx);
            progressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDlg.setMessage(strMessage);
            progressDlg.setIndeterminate(false);
            progressDlg.setCancelable(true);
            progressDlg.show();
        }
    }

    public static void stopProgressDlg() {
        if (null != progressDlg && progressDlg.isShowing()) {
            progressDlg.dismiss();
            progressDlg = null;
        }
    }
}
