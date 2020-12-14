package com.example.speechtotext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



public class HandActivity extends AppCompatActivity {
    static Activity Hand_Activity;
    private Spinner handProductSpinner,handTaskSpinner,handLocationSpinner,handToolSpinner;
    private TextView handToolTypeText;
    private String[] record;
    private ProductTool productTool;

    Spinner.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
            int position = 0;
            if (parent.getId() == R.id.handProductSpinner ) {
                position = 0;
            }else if(parent.getId() == R.id.handTaskSpinner ) {
                position = 2;
            }else if(parent.getId() == R.id.handLocationSpinner) {
                position = 3;
            }else if(parent.getId() == R.id.handToolSpinner){
                position = 4;
                record[5] = productTool.getToolType(parent.getItemAtPosition(pos).toString().trim());
                handToolTypeText.setText(" "+record[5]);
            }
            record[position] = parent.getItemAtPosition(pos).toString().trim();
            record[6] = "手動";
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        Hand_Activity=this;
        setContentView(R.layout.activity_hand);
        Bundle bundle = getIntent().getExtras();
        productTool = (ProductTool) bundle.getSerializable("productTool");
        record = new String[7];
        findViews();
    }


    private void findViews(){
        handProductSpinner = findViewById(R.id.handProductSpinner);
        handTaskSpinner =  findViewById(R.id.handTaskSpinner);
        handLocationSpinner = findViewById(R.id.handLocationSpinner);
        handToolSpinner =  findViewById(R.id.handToolSpinner);
        handToolTypeText =  findViewById(R.id.handToolTypeText);

        ArrayAdapter<String> adapterProduct = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,productTool.getProduct());
        ArrayAdapter<String> adapterTask = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,productTool.getTask());
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,productTool.getLocation());
        ArrayAdapter<String> adapterTool = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,productTool.getTool());

        handProductSpinner.setAdapter(adapterProduct);
        handProductSpinner.setOnItemSelectedListener(listener);
        handTaskSpinner.setAdapter(adapterTask);
        handTaskSpinner.setOnItemSelectedListener(listener);
        handLocationSpinner.setAdapter(adapterLocation);
        handLocationSpinner.setOnItemSelectedListener(listener);
        handToolSpinner.setAdapter(adapterTool);
        handToolSpinner.setOnItemSelectedListener(listener);
    }


    public void handSubmitClick(View view){
        long epoch = System.currentTimeMillis()/1000;
        record[1] = Long.toString(epoch);
        Intent intent = new Intent(this, ResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("record",record);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    public void handBackClick(View view){
        this.finish();
    }
}
