package com.sky.pinkvelocity.warehouse.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by hac10 on 27/09/15.
 */
public class ErrorPopUpActivity extends Activity {
    private static String errorString;
    private static String itemId;
    TextView errorMessageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        errorMessageTV = (TextView) findViewById(R.id.errorMessageTextView);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        double displayPercent = 0.8;
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * displayPercent), (int) (height * displayPercent));



        errorMessageTV.setText("Error: " + errorString +"Item: " + itemId);
    }

    public void setErrorString(String error){
        errorString = error;

    }

    public void setItemIdInt(String item){
        itemId = item;

    }

    public String getErrorString(){

        return errorString;
    }

    public String getItemId(){

        return itemId;
    }


    public void errorCloseButtonClicked(View v){

        if(v.getId()==R.id.errorCloseButton){

            //Close Window Here
        }

    }
}
