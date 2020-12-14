package com.example.speechtotext;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class SpeechActivity extends AppCompatActivity implements RecognitionListener {
    public static Activity Speech_Activity;

    private static final String utteranceId = "utteranceId" ;
    private final static String PREFERENCES_NAME = "preferences";
    private static final int REQUEST_RECORD_PERMISSION = 100;

    private SeekBar speechPitchSeekBar;
    private SeekBar speechSpeedSeekBar;
    private ToggleButton speechStartToggleButton;
    private ProgressBar speechStartProgressBar;
    private TextView speechText;

    private String[] speaking = {"請輸入產品名稱","請輸入工作名稱","請輸入地點","請輸入使用工具","請輸入送出或取消"};
    private String[] resultSpeak = {"本次輸入產品為","輸入工作項目為","輸入地點為","使用工具為"};
    private String[] output = new String[4];

    private ProductTool productTool;
    private Map<String,?> preferences;
    private boolean ready = false;
    private int index = 0;

    private TextToSpeech tts = null;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "SpeechActivity";


    private void findViews(){
        speechPitchSeekBar = findViewById(R.id.speechPitchSeekBar);
        speechSpeedSeekBar = findViewById(R.id.speechSpeedSeekBar);
        speechText =  findViewById(R.id.speechText);
        speechStartProgressBar =  findViewById(R.id.speechStartProgressBar);
        speechStartToggleButton =  findViewById(R.id.speechStartToggleButton);
        speechStartProgressBar.setVisibility(View.INVISIBLE);
    }

    private void setTTS(){
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = tts.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","not support");
                    }
                }else{
                    Toast.makeText(SpeechActivity.this,"TTS initialization failed",Toast.LENGTH_LONG).show();
                }
            }
        });

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(this));
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        Speech_Activity = this;
        preferences = getSharedPreferences(PREFERENCES_NAME,MODE_PRIVATE).getAll();
        Bundle bundle = getIntent().getExtras();
        productTool = (ProductTool) bundle.getSerializable("productTool");
        findViews();
        setTTS();

        speechStartToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    float pitch = (float) speechPitchSeekBar.getProgress() / 50;
                    if( pitch < 0.1){
                        pitch = 0.1f;
                    }
                    float speed = (float) speechSpeedSeekBar.getProgress() / 50;
                    if( speed < 0.1){
                        speed = 0.1f;
                    }
                    tts.setPitch(pitch);
                    tts.setSpeechRate(speed);
                    tts.speak(speaking[index],TextToSpeech.QUEUE_ADD,null,utteranceId);
                    while(tts.isSpeaking()){}
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return;
                    }
                    speechStartProgressBar.setVisibility(View.VISIBLE);
                    speechStartProgressBar.setIndeterminate(true);
                    ActivityCompat.requestPermissions(SpeechActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
                } else {
                    speechStartProgressBar.setIndeterminate(false);
                    speechStartProgressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                }
            }
        });
    }

    private String[] setText(String[] record){
        String ret[] = new String[7];
        long epoch = System.currentTimeMillis()/1000;
        ret[1] = Long.toString(epoch);
        ret[0]=record[0];
        ret[2]=record[1];
        ret[3]=record[2];
        ret[4]=record[3];
        ret[5]=productTool.getToolType(record[3]);
        ret[6]="語音導覽輸入";
        return ret;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(SpeechActivity .this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
    }
    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        speechStartProgressBar.setIndeterminate(false);
        speechStartProgressBar.setMax(10);
    }
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }
    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        speechStartProgressBar.setIndeterminate(true);
        speechStartToggleButton.setChecked(false);
    }
    @Override
    public void onError(int errorCode) {
        String errorMessage = getKeyErrorEditorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        speechText.setText(errorMessage);
        speechStartToggleButton.setChecked(false);
        if(errorMessage == "No match"){
            speechStartToggleButton.setChecked(true);
        }
    }
    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }
    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }
    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }
    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = matches.get(0);
        for (String result : matches){
            if(preferences.containsKey(result)){
                text = "" + preferences.get(result);
                break;
            }
        }

        if (text.equals("")) {
            speechStartToggleButton.setChecked(true);
        }
        else if (text.equals("取消")) {
            this.finish();
        }
        else if (index < 4) {
            output[index] = text;
            index++;
            speechText.setText(text);
            speechStartToggleButton.setChecked(true);
        }
        else if(index == 4 && !ready && text.equals("送出")){
            String tmpText="";
            for(int i =0; i <output.length; i++){
                tts.speak(resultSpeak[i],TextToSpeech.QUEUE_ADD,null,utteranceId);
                tts.speak(output[i],TextToSpeech.QUEUE_ADD,null,utteranceId);
                tmpText += resultSpeak[i] + ": " + output[i] + "\n";
            }
            ready = true;
            speechText.setText(tmpText);
            speechStartToggleButton.setChecked(true);
        }
        else if(index == 4 && ready && text.equals("送出")){
            String[] temp = setText(output);
            Intent intent = new Intent(this, ResultsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("record", temp);
            bundle.putBoolean("AllSpeech", true);
            intent.putExtras(bundle);
            startActivity(intent);
            this.finish();
        }else{
            speechStartToggleButton.setChecked(true);
        }
    }
    
    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        speechStartProgressBar.setProgress((int) rmsdB);
    }

    public static String getKeyErrorEditorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    public void speechBackClick(View view){
        this.finish();
    }
}
