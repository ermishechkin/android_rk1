package com.example.alex.rk1;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.Scheduler;
import ru.mail.weather.lib.Storage;

public class MainActivity extends AppCompatActivity implements NewsListener {
    static {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .penaltyLog()
                .penaltyDeath()
                .build()
        );
    }

    private final View.OnClickListener onSettingsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
    };

    private final View.OnClickListener onRefreshClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            requestUpdate();
        }
    };

    private final View.OnClickListener onAutoRefreshClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent =
                    ServiceHelper.getInstance().getIntent(getApplicationContext());
            if (Storage.getInstance(getApplicationContext()).loadIsUpdateInBg()) {
                Scheduler.getInstance().unschedule(getApplicationContext(), intent);
                Storage.getInstance(getApplicationContext()).saveIsUpdateInBg(false);
            }
            else {
                Scheduler.getInstance().schedule(getApplicationContext(), intent, 60000);
                Storage.getInstance(getApplicationContext()).saveIsUpdateInBg(true);
            }
        }
    };

    private void requestUpdate() {
        ServiceHelper.getInstance().requestNews(getApplicationContext(), MainActivity.this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.showSettings).setOnClickListener(onSettingsClick);
        findViewById(R.id.refresh).setOnClickListener(onRefreshClick);
        findViewById(R.id.toggleAutoRefresh).setOnClickListener(onAutoRefreshClick);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshFields();
        requestUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceHelper.getInstance().removeListener();
    }

    private void refreshFields() {
        TextView title = (TextView) findViewById(R.id.news_title);
        TextView content = (TextView) findViewById(R.id.news_content);
        TextView date = (TextView) findViewById(R.id.news_date);
        News news = Storage.getInstance(getApplicationContext()).getLastSavedNews();
        if (news != null) {
            String converted_date =
                    new SimpleDateFormat("yyyy MM dd HH:mm").format(new Date(news.getDate()));
            title.setText(news.getTitle());
            content.setText(news.getBody());
            date.setText(converted_date);
        }
        else {
            title.setText("");
            content.setText("");
            date.setText("");
        }

    }

    @Override
    public void onNewsResult(boolean success) {
        refreshFields();
        ServiceHelper.getInstance().removeListener();
    }

}
