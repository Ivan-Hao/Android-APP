package com.example.speechtotext;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MethodActivity extends AppCompatActivity {
    static Activity Method_Activity;

    private  HashMap<String,String> product = new HashMap<>();
    private HashMap<String,String> tool =new HashMap<>();
    private ArrayList<String> location = new ArrayList();
    private ArrayList<String> task = new ArrayList();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method);

        Method_Activity=this;

        Response.Listener<String> LoadListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray Product = jsonObject.getJSONArray("product");
                    JSONArray Task = jsonObject.getJSONArray("task");
                    JSONArray Location = jsonObject.getJSONArray("location");
                    JSONArray Tooltype = jsonObject.getJSONArray("tooltype");

                    for (int i = 0; i < Product.length(); i++) {
                        JSONObject productItem = Product.getJSONObject(i);
                        product.put(productItem.getString("product_name"), productItem.getString("product_id"));
                    }

                    for (int i = 0; i < Task.length(); i++) {
                        JSONObject taskItem = Task.getJSONObject(i);
                        task.add(taskItem.getString("task"));
                    }

                    for (int i = 0; i < Location.length(); i++) {
                        JSONObject locationItem = Location.getJSONObject(i);
                        location.add(locationItem.getString("location"));
                    }

                    for (int i = 0; i < Tooltype.length(); i++) {
                        JSONObject tooltypeItem  = Tooltype.getJSONObject(i);
                        tool.put(tooltypeItem.getString("tool"), tooltypeItem.getString("tool_type"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MethodActivity.this, "伺服器發生錯誤，請稍後再試", Toast.LENGTH_LONG).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MethodActivity.this, volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        LoadRequest loadRequest = new LoadRequest(LoadListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(MethodActivity.this);
        queue.add(loadRequest);

    }

    public void methodHandSpeechClick(View view){
        Intent intent = new Intent(this, HandSpeechActivity.class);
        ProductTool productTool = new ProductTool(product,task,location,tool);
        Bundle bundle = new Bundle();
        bundle.putSerializable("productTool",productTool);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void methodHandClick(View view){
        Intent intent = new Intent(this, HandActivity.class);
        ProductTool productTool = new ProductTool(product,task,location,tool);
        Bundle bundle = new Bundle();
        bundle.putSerializable("productTool",productTool);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void methodSpeechClick(View view){
        Intent intent = new Intent(this, SpeechActivity.class);
        ProductTool productTool = new ProductTool(product,task,location,tool);
        Bundle bundle = new Bundle();
        bundle.putSerializable("productTool",productTool);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void  methodKeyWordClick(View view){
        Intent intent = new Intent(this,KeyActivity.class);
        startActivity(intent);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(MethodActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setIcon(R.drawable.ic_launcher_foreground)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                }
                            }).show();
        }
        return true;
    }
}
