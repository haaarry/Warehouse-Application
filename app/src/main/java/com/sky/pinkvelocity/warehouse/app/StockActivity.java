package com.sky.pinkvelocity.warehouse.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.test.ActivityTestCase;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

/**
 * Created by hac10 on 28/09/15.
 */


public class StockActivity extends Activity implements OnRetrieveHttpData{
    String hostUrl = "localhost:8080/";
    TextView stockScan;
    MediaPlayer beepSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        beepSuccess = MediaPlayer.create(this, R.raw.scanbeep);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if(parcelables != null && parcelables.length > 0)
        {
            readTextFromMessage((NdefMessage) parcelables[0]);
        }else{
            Toast.makeText(this, "No NDEF messages found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        beepSuccess.seekTo(0);
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length>0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            stockScan.setText(tagContent);
            beepSuccess.start();

        }else
        {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
        }

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




    public String getTextFromNdefRecord(NdefRecord ndefRecord)
    {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }
        return tagContent;
    }

}
