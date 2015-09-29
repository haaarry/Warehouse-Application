package com.sky.pinkvelocity.warehouse.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

/**
 * Created by hac10 on 28/09/15.
 */
public class IncorrectStockActivity extends Activity {

    RadioGroup radioGroup;
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incorrectstock);

        radioGroup = (RadioGroup) findViewById(R.id.incorrectItemRadioGroup);
        numberPicker= (NumberPicker)findViewById(R.id.correctItemNumberPicker);


    }

    public void sendIssueButtonCicked(View v){
        if(v.getId()==R.id.sendIssueButton){
            //send issue logic here

            Intent intent = new Intent(this, StockActivity.class);
            startActivity(intent);
        }

    }


}
