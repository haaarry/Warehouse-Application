package com.sky.pinkvelocity.warehouse.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.nfc.NfcAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class LogInActivity extends Activity {

    NfcAdapter mNFcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNFcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(mNFcAdapter !=null && mNFcAdapter.isEnabled()){
            Toast.makeText(this, "NFC ENDABLED", Toast.LENGTH_LONG).show();
        }
        else {

            Toast.makeText(this, "NFC NOT AVAILABLE", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this,intent,
                null, null);
*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NfcAdapter.getDefaultAdapter(this) != null)
            NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent){
        //on Resume gets called after this ti handke tge intent
        setIntent(intent);

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
            processIntent(getIntent());
        }
    }

    public boolean chkNFCAv(){
        mNFcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(mNFcAdapter == null){
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
        return true;
    }

    void processIntent(Intent intent){
        Toast.makeText(this, "NFC Read", Toast.LENGTH_SHORT).show();
        finish();

    }

    public void logInButtonClicked(View v){
        if(v.getId() == R.id.logInButton){
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }

    }

    public void nfcButtonClicked(View v){
        if(v.getId()== R.id.nfcTestButton){
            Intent intent = new Intent(this, NfcTestActivity.class);
            startActivity(intent);
        }

    }

    public void stockActivityButtonClicked(View v){
        if(v.getId()== R.id.stockActivityButton){
            Intent intent = new Intent(this, StockActivity.class);
            startActivity(intent);
        }

    }

}
