package com.sky.pinkvelocity.warehouse.app;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by hac10 on 26/09/15.
 */
public class FlagIssuePopUpActivity extends Activity implements OnRetrieveHttpData{

    EditText issueEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_flagissue);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        double displayPercent = 0.8;
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*displayPercent), (int)(height*displayPercent));



        issueEditText = (EditText)findViewById(R.id.issueEditText);

    }

    public void issueButtonClicked(View v){
        if(v.getId()==R.id.sendIssueButton){
            String message = issueEditText.getText().toString();
            //send issue logic here
        }

    }

    @Override
    public void onTaskCompleted(String httpData) {

    }
}
