package com.example.speechtotext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class ResultsActivity extends AppCompatActivity {
    private TextView showProduct, showUNIX, showTask, showLocation, showTool, showType, showMethod;
    private ArrayList<TextView> show = new ArrayList();
    private String[] result ;
    private String product_id="", timestamp="", task="", location="", tool="", tool_type="", input_method="";
    private boolean AllSpeech;
    private Button resultsSubmitAgainButton;


    Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                if(success){

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ResultsActivity.this);
                    builder.setMessage("資料傳輸發生錯誤")
                            .setNegativeButton("請重新嘗試",null)
                            .create()
                            .show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ResultsActivity.this);
            builder.setMessage("資料傳輸發生錯誤")
                    .setNegativeButton("請重新嘗試",null)
                    .create()
                    .show();
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        findViews();
        Bundle b = getIntent().getExtras();
        result = b.getStringArray("record");
        AllSpeech = b.getBoolean("AllSpeech",false);

        showResults();
        matchString();

        if(AllSpeech){
            resultsSubmitAgainButton.performClick();
        }

    }
    private void findViews(){

        showProduct = findViewById(R.id.showProduct);
        showUNIX = findViewById(R.id.showUNIX);
        showTask = findViewById(R.id.showTask);
        showLocation = findViewById(R.id.showLocation);
        showTool = findViewById(R.id.showTool);
        showType = findViewById(R.id.showType);
        showMethod = findViewById(R.id.showMethod);
        resultsSubmitAgainButton = findViewById(R.id.resultsSubmitAgainButton);

        show.add(showProduct);
        show.add(showUNIX);
        show.add(showTask);
        show.add(showLocation);
        show.add(showTool);
        show.add(showType);
        show.add(showMethod);
    }

    //顯示表格
    private void showResults(){
        ArrayList<TextView> temp_show = new ArrayList<>(show);
        for(String t :result) {
            if (!temp_show.isEmpty()) {
                temp_show.remove(0).setText(t);
            }
        }
    }
    private void matchString(){
        product_id = result[0];
        timestamp = result[1];
        task = result[2];
        location = result[3];
        tool = result[4];
        tool_type = result[5];
        input_method = result[6];
    }



    public void resultsBackToMethodClick(View view){
        InsertRequest insertRequest = new InsertRequest(product_id,timestamp,task,location,tool,tool_type,input_method, responseListener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(ResultsActivity.this);
        queue.add(insertRequest);

        if(!MethodActivity.Method_Activity.isFinishing()){
            MethodActivity.Method_Activity.finish();
        }
        if( HandActivity.Hand_Activity != null && (!HandActivity.Hand_Activity.isFinishing())){
            HandActivity.Hand_Activity.finish();
        }
        if( HandSpeechActivity.HandSpeech_Activity != null && (!HandSpeechActivity.HandSpeech_Activity.isFinishing()) ){
            HandSpeechActivity.HandSpeech_Activity.finish();
        }

        if( SpeechActivity.Speech_Activity != null && (!SpeechActivity.Speech_Activity.isFinishing()) ){
            SpeechActivity.Speech_Activity.finish();
        }
        Intent intent = new Intent(this, MethodActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void exitApp(View view){
        InsertRequest insertRequest = new InsertRequest(product_id, timestamp, task, location, tool, tool_type, input_method, responseListener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(ResultsActivity.this);
        queue.add(insertRequest);
        if(!MethodActivity.Method_Activity.isFinishing()){
            MethodActivity.Method_Activity.finish();
        }
        if( HandActivity.Hand_Activity != null && (!HandActivity.Hand_Activity.isFinishing())){
            HandActivity.Hand_Activity.finish();
        }
        if( HandSpeechActivity.HandSpeech_Activity !=null && (!HandSpeechActivity.HandSpeech_Activity.isFinishing()) ){
            HandSpeechActivity.HandSpeech_Activity.finish();
        }

        if( SpeechActivity.Speech_Activity != null && (!SpeechActivity.Speech_Activity.isFinishing()) ){
            SpeechActivity.Speech_Activity.finish();
        }
        this.finish();
    }

    public void resultsBackClick(View view){
        this.finish();
    }


}
