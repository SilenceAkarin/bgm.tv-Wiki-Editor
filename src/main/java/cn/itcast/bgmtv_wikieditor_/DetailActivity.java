package cn.itcast.bgmtv_wikieditor_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private TextView albumTitleTextView;
    private TextView keyValueTextView;
    private TextView valueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        albumTitleTextView = findViewById(R.id.albumTitleTextView);
        keyValueTextView = findViewById(R.id.keyValueTextView);
        valueTextView = findViewById(R.id.valueTextView);

        // 获取从HistoryActivity传递过来的数据
        Intent intent = getIntent();
        String albumtitle = intent.getStringExtra("albumtitle");
        String key_value = intent.getStringExtra("key_value");
        String value = intent.getStringExtra("value");

        // 将数据显示在TextViews中
        albumTitleTextView.setText(albumtitle);
        keyValueTextView.setText(key_value);
        valueTextView.setText(value);
    }
}
