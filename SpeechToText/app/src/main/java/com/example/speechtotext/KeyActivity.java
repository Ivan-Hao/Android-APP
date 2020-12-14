package com.example.speechtotext;


import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class KeyActivity extends AppCompatActivity {
    private final static String PREFERENCES_NAME = "preferences";
    private EditText keyErrorEditor, keyCorrectEditor;
    private TextView keyShowMessageText;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key);
        findViews();
        loadPreference();

    }
    private void findViews(){
        keyErrorEditor = findViewById(R.id.keyErrorEditor);
        keyCorrectEditor = findViewById(R.id.keyCorrectEditor);
        keyShowMessageText = findViewById(R.id.keyShowMessageText);
    }

    private void loadPreference(){
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME,MODE_PRIVATE);
    }

    public void keySubmitClick(View view){
        String error = keyErrorEditor.getText().toString();
        String correct = keyCorrectEditor.getText().toString();
        String error_old = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
                .getString(""+error, "");
        if(error_old == ""){
            sharedPreferences.edit()
                    .putString(""+error,""+correct)
                    .apply();
            keyShowMessageText.setText("資料庫已更新成功，請返回上頁 或 繼續輸入");
        }else{
            sharedPreferences.edit()
                    .remove(""+error)
                    .putString(""+error,""+correct)
                    .apply();
        }
    }

    public void keyBackClick(View view){
        this.finish();
    }
}
