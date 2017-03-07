package com.example.alex.rk1;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

class NewsResultReceiver extends ResultReceiver {
    private NewsListener listener;

    public NewsResultReceiver(Handler handler) {
        super(handler);
    }

    public void setListener(NewsListener listener) {
        this.listener = listener;
    }

    public NewsListener getListener() {
        return listener;
    }

    @Override
    protected void onReceiveResult(final int code, final Bundle data) {
        if (listener != null) {
            boolean success = (code == NewsIndentService.RESULT_SUCCESS);
            listener.onNewsResult(success);
        }
        //ServiceHelper.getInstance().removeListener();
    }
}
