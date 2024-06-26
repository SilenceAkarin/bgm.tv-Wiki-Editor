package cn.itcast.bgmtv_wikieditor_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class HistoryAdded extends AppCompatActivity {
    private ListView historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_added);

        historyList = findViewById(R.id.historyList);

        // 获取所有的历史记录
        List<SubjectInfo> history = SubjectDBHelper.getInstance(this).getAllHistory();

        // 创建一个适配器来显示历史记录
        ArrayAdapter<SubjectInfo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, history);
        historyList.setAdapter(adapter);

        // 设置列表项的点击事件
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取选中的历史记录
                SubjectInfo selectedHistory = history.get(position);

                // 创建一个Intent来启动DetailActivity
                Intent intent = new Intent(HistoryAdded.this, DetailActivity.class);

                // 将选中的历史记录的详细信息放入Intent
                intent.putExtra("albumtitle", selectedHistory.getAlbumtitle());
                intent.putExtra("key_value", selectedHistory.getKey_value());
                intent.putExtra("value", selectedHistory.getValue());

                // 启动DetailActivity
                startActivity(intent);
            }
        });
    }
}
