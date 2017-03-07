package com.example.alex.rk1;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;

import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        createButtons();
    }

    private void createButtons() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_settings);

        for (String category: Topics.ALL_TOPICS) {
            final Button button = new Button(this);

            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            button.setLayoutParams(layoutParams);
            button.setText(category);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String categoryName = button.getText().toString();
                    Storage storage = Storage.getInstance(getApplicationContext());
                    storage.saveCurrentTopic(categoryName);
                    storage.saveNews(null);

                    finish();
                }
            });

            layout.addView(button);
        }

    }
}
