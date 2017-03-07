package com.example.alex.rk1;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

class ServiceHelper {
    private static ServiceHelper instance;

    private NewsResultReceiver resultReceiver;

    private ServiceHelper() {

    }

    static ServiceHelper getInstance() {
        if (instance == null)
            instance = new ServiceHelper();
        return instance;
    }

    void requestNews(Context context, NewsListener listener) {
        if (resultReceiver != null) {
            if (resultReceiver.getListener() == listener)
                return;
            else
                resultReceiver.setListener(listener);
        }
        else {
            Intent intent = createIntent(context, listener);
            context.startService(intent);
        }
    }

    Intent getIntent(Context context) {
        return createIntent(context, null);
    }

    private Intent createIntent(Context context, NewsListener listener) {
        final NewsResultReceiver receiver = new NewsResultReceiver(new Handler());
        receiver.setListener(listener);
        if (listener != null) {
            if (resultReceiver == null || resultReceiver.getListener() == null)
                resultReceiver = receiver;
            else
                return null;
        }
        Intent intent = new Intent(context, NewsIndentService.class);
        intent.setAction(NewsIndentService.ACTION_NEWS);
        intent.putExtra(NewsIndentService.EXTRA_NEWS_RESULT_RECEIVER, receiver);
        return intent;
    }

    void removeListener() {
        if (resultReceiver != null) {
            resultReceiver.setListener(null);
            resultReceiver = null;
        }
    }

}
