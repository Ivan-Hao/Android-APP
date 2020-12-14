package com.example.speechtotext;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class HandSpeechActivity extends AppCompatActivity {
    static Activity HandSpeech_Activity;

    private final static String PREFERENCES_NAME = "preferences";
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private TextView handSpeechProductText, handSpeechTaskText, handSpeechLocationText, handSpeechToolText, handSpeechToolTypeText;
    private Button handSpeechProductButton, handSpeechTaskButton, handSpeechLocationButton, handSpeechToolButton;

    private int textNumber=0;
    private String[] record;
    private ProductTool productTool;
    private Map<String,?> preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HandSpeech_Activity = this;
        setContentView(R.layout.activity_handspeech);
        Bundle bundle = getIntent().getExtras();
        productTool = (ProductTool) bundle.getSerializable("productTool");
        preferences = getSharedPreferences(PREFERENCES_NAME,MODE_PRIVATE).getAll();
        record = new String[7];
        findViews();
    }

    private void findViews(){

        handSpeechProductText = findViewById(R.id.handSpeechProductText);
        handSpeechTaskText = findViewById(R.id.handSpeechTaskText);
        handSpeechLocationText = findViewById(R.id.handSpeechLocationText);
        handSpeechToolText = findViewById(R.id.handSpeechToolText);
        handSpeechToolTypeText = findViewById(R.id.handSpeechToolTypeText);
        handSpeechProductButton = findViewById(R.id.handSpeechProductButton);
        handSpeechTaskButton = findViewById(R.id.handSpeechTaskButton);
        handSpeechLocationButton = findViewById(R.id.handSpeechLocationButton);
        handSpeechToolButton = findViewById(R.id.handSpeechToolButton);

        record[6] = "半語音";

        Button.OnClickListener listener = new View.OnClickListener(){
           /* @Override
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        setText(v,"listening...");
                        getSpeechInput();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(result != null){
                            setText(v,result.get(0));
                        }else{
                            setText(v,"重新輸入");
                        }
                        break;
                }

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        speechRecognizer.startListening(speechReconizerIntent);
                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();

                        break;
                }
                return false;
            }*/

            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.handSpeechProductButton){
                    textNumber=0;
                }else if(view.getId() == R.id.handSpeechTaskButton){
                    textNumber=2;
                }else if(view.getId() ==R.id.handSpeechLocationButton){
                    textNumber=3;
                }else if(view.getId() == R.id.handSpeechToolButton){
                    textNumber=4;
                }
                getSpeechInput();
            }
        };

        handSpeechProductButton.setOnClickListener(listener);
        handSpeechTaskButton.setOnClickListener(listener);
        handSpeechLocationButton.setOnClickListener(listener);
        handSpeechToolButton.setOnClickListener(listener);
    }
    private void setText(int textNumber, String temp) {
        switch (textNumber) {
            case 0:
                handSpeechProductText.setText(temp);
                record[0]=temp;
                break;
            case 2:
                handSpeechTaskText.setText(temp);
                record[2]=temp;
                break;
            case 3:
                handSpeechLocationText.setText(temp);
                record[3]=temp;
                break;
            case 4:
                handSpeechToolText.setText(temp);
                record[4]=temp;
                handSpeechToolTypeText.setText(productTool.getToolType(temp));
                record[5]=productTool.getToolType(temp);
                break;
        }
    }
    private void getSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try{
            startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT);
        }catch (Exception e){
            Toast.makeText(HandSpeechActivity.this,""+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode , Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:
                if(resultCode == RESULT_OK && data !=null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if(preferences.containsKey(result.get(0))) {
                        setText(textNumber,""+preferences.get(result.get(0)));
                    }else {
                        setText(textNumber, result.get(0));
                    }
                }
                break;
        }
    }

    public void handSpeechSubmitButton(View v){
        long epoch = System.currentTimeMillis()/1000;
        record[1] = Long.toString(epoch);
        Intent intent = new Intent(this,ResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("record",record);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public  void handSpeechBackButton(View v){
        this.finish();
    }
}