package com.sky.pinkvelocity.warehouse.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.test.ActivityTestCase;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;

/**
 * Created by hac10 on 28/09/15.
 */


public class StockActivity extends Activity implements OnRetrieveHttpData{
    String hostUrl = "localhost:8080/";
    TextView stockScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        stockScan = (TextView)findViewById(R.id.stockScanItemTextView);
    }

    @Override
    public void onResume() {
        super.onResume();
        PendingIntent intent = PendingIntent.getActivity(this, 0, new
                        Intent(
                        this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this,
                intent, null, null);
    }

    @Override
    public void onNewIntent(Intent intent) {
        stockScan.setText("Mock Item");


        sendStockId();
    }


    public void incorrectStockButtonClicked(View v) {
        if (v.getId() == R.id.incorrectStockButton) {
            Intent intent = new Intent(this, IncorrectStockActivity.class);
            startActivity(intent);
        }

    }

    public void confirmStockButtonClicked(View v){
        if(v.getId()==R.id.confirmStockButton){
            String itemGet = "http://httpbin.org/ip";
            RetrieveHTTPDataAsync retrieveBlogData = new RetrieveHTTPDataAsync(this);
            retrieveBlogData.execute(itemGet);
        }

    }

    public void sendStockId(){
        String itemGet = hostUrl + "http://httpbin.org/ip";
        RetrieveHTTPDataAsync retrieveBlogData = new RetrieveHTTPDataAsync(this);
        retrieveBlogData.execute(itemGet);

    }

    @Override
    public void onTaskCompleted(String httpData) {
        Toast.makeText(this, httpData, Toast.LENGTH_SHORT).show();
    }






}
