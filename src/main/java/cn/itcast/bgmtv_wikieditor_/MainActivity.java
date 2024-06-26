package cn.itcast.bgmtv_wikieditor_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private LinearLayout beginningmode_edittext;
    private LinearLayout beginningmode_layout;
    private LinearLayout wikimode_layout;
    private LinearLayout columnadd_layout;
    public static EditText wikiText;
    private Button wikimode;
    private Button beginningmode;
    private Button subjectadd;
    private ImageButton login_button;
    private Button history_botton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wikimode_layout = findViewById(R.id.wikimode_layout);
        columnadd_layout = findViewById(R.id.columnadd_layout);
        beginningmode_edittext = findViewById(R.id.mode_select);
        wikiText = findViewById(R.id.wikiText);
        EditText albumtitleEditText = findViewById(R.id.albumtitle);
        //ScrollView scrollView = findViewById(R.id.scroll_view);
        beginningmode_layout = findViewById(R.id.beginningmode_layout);
        Button columnAdd = findViewById(R.id.columnadd);
        history_botton = findViewById(R.id.histroy);
        subjectadd = findViewById(R.id.subjectadd);
        wikimode = findViewById(R.id.wikimode);
        beginningmode = findViewById(R.id.beginnermode);
        login_button = findViewById(R.id.login);


        columnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewLinearLayout(beginningmode_layout); //添加新的键和值
            }
        });

        wikimode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createAndManageEditTexts(beginningmode_layout,wikimode,DataConventToJson());
                wikimodeVisibility();
                convertJSONToWiki(convertBeginningToJSON(beginningmode_layout));
            }
        });

        beginningmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginningmode_layout.removeAllViews();
                createAndManageEditTexts(beginningmode_layout, null, DataConventToJson());
                beginningmodeVisibility();
            }
        });

        subjectadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String albumtitle = albumtitleEditText.getText().toString();
                convertBeginningToDatabase(beginningmode_layout,albumtitle);
            }
        });

        history_botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryAdded.class);
                startActivity(intent);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void beginningmodeVisibility() {
        if (beginningmode_layout.getVisibility() == View.GONE && columnadd_layout.getVisibility() == View.GONE) {
            wikimode_layout.setVisibility(View.GONE);
            beginningmode_layout.setVisibility(View.VISIBLE);
            columnadd_layout.setVisibility(View.VISIBLE);
            columnadd_layout.bringToFront();
        }
    }

    private void wikimodeVisibility() {
        if (beginningmode_layout.getVisibility() == View.VISIBLE && columnadd_layout.getVisibility() == View.VISIBLE ||
                beginningmode_layout.getVisibility() == View.GONE && columnadd_layout.getVisibility() == View.GONE) {
            wikimode_layout.setVisibility(View.VISIBLE);
            beginningmode_layout.setVisibility(View.GONE);
            columnadd_layout.setVisibility(View.GONE);
            wikimode_layout.bringToFront();
        }
    }

    //将数据转换为json形式
    public static String DataConventToJson() {
        String getwikiText = wikiText.getText().toString();
        String json = convertWikiToJSON(getwikiText);
        return json;
    }

    public static String convertWikiToJSON(String getwikiText) {
        JSONObject jsonObject = new JSONObject();
        Scanner scanner = new Scanner(getwikiText);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("|")) {
                String[] parts = line.split("=", 2);
                String key = parts[0].substring(1).trim();
                String value = parts.length > 1 ? parts[1].trim() : "";
                try {
                    jsonObject.put(key, value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return jsonObject.toString();
    }

    //这里会根据入门模式输入的内容将内容转为JSON格式的文本
    public static String convertBeginningToJSON(LinearLayout layout) {
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout rowLayout = (LinearLayout) view;
                if (rowLayout.getChildCount() >= 2) {
                    View view1 = rowLayout.getChildAt(0);
                    View view2 = rowLayout.getChildAt(1);
                    if (view1 instanceof EditText && view2 instanceof EditText) {
                        EditText keyEditText = (EditText) view1;
                        EditText valueEditText = (EditText) view2;
                        String key = keyEditText.getText().toString();
                        String value = valueEditText.getText().toString();
                        try {
                            jsonObject.put(key, value);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return jsonObject.toString();
    }

    public void convertJSONToWiki(String JSONText) {
        try {
            JSONObject jsonObject = new JSONObject(JSONText);
            Iterator<String> keys = jsonObject.keys();

            StringBuilder wikiTextBuilder = new StringBuilder("{{Infobox Album\n");
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);
                wikiTextBuilder.append("|").append(key).append("= ").append(value).append("\n");
            }
            wikiTextBuilder.append("}}");

            wikiText.setText(wikiTextBuilder.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /* 这里将创建新的EditText对，并且当对应按钮按下时候将更新的键和值返回到Wiki模式 */
    public void createAndManageEditTexts(LinearLayout linearLayout, Button button, String jsonText) {
        Map<String, EditText> keyEditTexts = new HashMap<>();
        Map<String, EditText> valueEditTexts = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonText);
            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);

                EditText keyEditText = new EditText(this);
                keyEditText.setText(key);
                keyEditText.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1));
                keyEditText.setBackground(getResources().getDrawable(R.drawable.datainput_column));
                keyEditTexts.put(key, keyEditText);

                EditText valueEditText = new EditText(this);
                valueEditText.setText(value);
                valueEditText.setLayoutParams(new LinearLayout.LayoutParams(350,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1));
                valueEditText.setBackground(getResources().getDrawable(R.drawable.datainput_column));
                valueEditTexts.put(key, valueEditText);

                LinearLayout rowLayout = new LinearLayout(this);
                /* 设置LinearLayout的布局参数，将45dp转换为像素，并设置marginTop */
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // 宽度
                        LinearLayout.LayoutParams.WRAP_CONTENT); // 高度
                int marginTop = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        10,
                        getResources().getDisplayMetrics());
                layoutParams.setMargins(0, marginTop, 0, 0);
                rowLayout.setLayoutParams(layoutParams);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                rowLayout.addView(keyEditText);
                rowLayout.addView(valueEditText);

                linearLayout.addView(rowLayout);
            }

            button.setOnClickListener(v -> {
                JSONObject newJsonObject = new JSONObject();

                for (String key : keyEditTexts.keySet()) {
                    String newKey = keyEditTexts.get(key).getText().toString();
                    String newValue = valueEditTexts.get(key).getText().toString();
                    try {
                        newJsonObject.put(newKey, newValue);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.println(newJsonObject.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //添加新的LinearLayout组件，给columnAdd按钮用
    public void addNewLinearLayout(LinearLayout linearLayout) {
        EditText keyEditText = new EditText(this);
        keyEditText.setText("");
        keyEditText.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1));
        keyEditText.setBackground(getResources().getDrawable(R.drawable.datainput_column));

        EditText valueEditText = new EditText(this);
        valueEditText.setText("");
        valueEditText.setLayoutParams(new LinearLayout.LayoutParams(350,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1));
        valueEditText.setBackground(getResources().getDrawable(R.drawable.datainput_column));

        LinearLayout rowLayout = new LinearLayout(this);
        /* 设置LinearLayout的布局参数，将45dp转换为像素，并设置marginTop */
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // 宽度
                LinearLayout.LayoutParams.WRAP_CONTENT); // 高度
        int marginTop = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10,
                getResources().getDisplayMetrics());
        layoutParams.setMargins(0, marginTop, 0, 0);
        rowLayout.setLayoutParams(layoutParams);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        rowLayout.addView(keyEditText);
        rowLayout.addView(valueEditText);

        linearLayout.addView(rowLayout);
    }

    public void convertBeginningToDatabase(LinearLayout layout, String albumtitle) {
        int row = 0;
        int failedCount = 0;
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout rowLayout = (LinearLayout) view;
                if (rowLayout.getChildCount() >= 2) {
                    View view1 = rowLayout.getChildAt(0);
                    View view2 = rowLayout.getChildAt(1);
                    if (view1 instanceof EditText && view2 instanceof EditText) {
                        EditText keyEditText = (EditText) view1;
                        EditText valueEditText = (EditText) view2;
                        String key = keyEditText.getText().toString();
                        String value = valueEditText.getText().toString();
                        try {
                            row = SubjectDBHelper.getInstance(MainActivity.this).subjectadd(albumtitle,key,value);
                            if (row <= 0) {
                                failedCount++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            failedCount++;
                        }
                    }
                }
            }
        }
        if (failedCount == 0) {
            Toast.makeText(MainActivity.this, "维基添加成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "有 " + failedCount + "次操作失败", Toast.LENGTH_SHORT).show();
        }
    }

}




 /*   //添加部件
    private void createEditTextPair(String label, LinearLayout parent) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        EditText labelEditText = new EditText(this);
        labelEditText.setText(label);
        labelEditText.setLayoutParams(new LinearLayout.LayoutParams(0, 45, 1));
        labelEditText.setBackground(getResources().getDrawable(R.drawable.datainput_column));

        EditText valueEditText = new EditText(this);
        valueEditText.setLayoutParams(new LinearLayout.LayoutParams(200, 45,1));
        valueEditText.setBackground(getResources().getDrawable(R.drawable.datainput_column));

        row.addView(labelEditText);
        row.addView(valueEditText);

        // 在LinearLayout的顶部添加新的行
        parent.addView(row, parent.getChildCount() - 1);
    }

    private void getJsonFromEditTexts() {
        StringBuilder wikiTextBuilder = new StringBuilder("{{Infobox Album\n");
        for (int i = 0; i < beginningmode_layout.getChildCount(); i++) {
            View child = beginningmode_layout.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) child;
                if (linearLayout.getChildCount() > 1) {
                    if (linearLayout.getChildAt(0) instanceof EditText && linearLayout.getChildAt(1) instanceof EditText) {
                        EditText leftEditText = (EditText) linearLayout.getChildAt(0);
                        EditText rightEditText = (EditText) linearLayout.getChildAt(1);
                        wikiTextBuilder.append("|").append(leftEditText.getText()).append("= ").append(rightEditText.getText()).append("\n");
                    }
                }
            } else if (child instanceof EditText) {
                EditText editText = (EditText) child;
                wikiTextBuilder.append(editText.getText()).append("\n");
            }
        }
        wikiTextBuilder.append("}}");
        wikiText.setText(wikiTextBuilder.toString());
    }
}*/

/*    private void convertBeginningModeToWiki() {
        StringBuilder wikiTextBuilder = new StringBuilder("{{Infobox Album\n");
        for (int i = 0; i < beginningmode_layout.getChildCount(); i++) {
            View child = beginningmode_layout.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) child;
                EditText leftEditText = (EditText) linearLayout.getChildAt(0);
                EditText rightEditText = (EditText) linearLayout.getChildAt(1);
                wikiTextBuilder.append("|").append(leftEditText.getText()).append("= ").append(rightEditText.getText()).append("\n");
            }
        }
        wikiTextBuilder.append("}}");
        wikiText.setText(wikiTextBuilder.toString());
    }

    private void convertWikiToBeginningMode() {
        String wikiTextString = wikiText.getText().toString();
        String[] lines = wikiTextString.split("\n");
        for (String line : lines) {
            if (line.startsWith("|")) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String leftText = parts[0].substring(1).trim();  // 去掉开头的"|"
                    String rightText = parts[1].trim();
                    addNewLinearLayout_addtext(leftText, rightText);
                }
            }
        }
    }*/
/*
    public void addNewLinearLayout_addtext(String leftText, String rightText) {
        // 创建一个新的LinearLayout
        LinearLayout newLinearLayout = new LinearLayout(this);
        newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        newLinearLayout.setLayoutParams(layoutParams);

        // 创建左边的EditText组件
        EditText leftEditText = new EditText(this);
        leftEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        leftEditText.setText(leftText);
        newLinearLayout.addView(leftEditText);

        // 创建右边的EditText组件
        EditText rightEditText = new EditText(this);
        rightEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        rightEditText.setText(rightText);
        newLinearLayout.addView(rightEditText);

        // 将新的LinearLayout添加到你的布局中
        beginningmode_layout.addView(newLinearLayout);
    }*/
