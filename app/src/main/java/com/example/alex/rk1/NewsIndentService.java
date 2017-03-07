package com.example.alex.rk1;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.NewsLoader;
import ru.mail.weather.lib.Storage;

public class NewsIndentService extends IntentService {
    public final static String ACTION_NEWS = "action.NEWS";

    public static final String EXTRA_NEWS_RESULT_RECEIVER = "extra.NEWS_RESULT_RECEIVER";

    public final static int RESULT_SUCCESS = 0;
    public final static int RESULT_ERROR = 1;

    public NewsIndentService() {
        super("NewsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action.equals(ACTION_NEWS)) {
                final Bundle data = new Bundle();
                final ResultReceiver receiver =
                        intent.getParcelableExtra(EXTRA_NEWS_RESULT_RECEIVER);
                try {
                    Storage storage = Storage.getInstance(getApplicationContext());
                    String topic = storage.loadCurrentTopic();
                    // Thread.sleep(6000); // Debug
                    News news = (new NewsLoader()).loadNews(topic);
                    storage.saveNews(news);
                    receiver.send(RESULT_SUCCESS, data);
                }
                catch (Exception e) {
                    receiver.send(RESULT_ERROR, data);
                }
            }
        }
    }
}
