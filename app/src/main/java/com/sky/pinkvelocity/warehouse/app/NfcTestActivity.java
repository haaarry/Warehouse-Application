package com.sky.pinkvelocity.warehouse.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hac10 on 28/09/15.
 */
public class NfcTestActivity extends Activity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    NfcAdapter nfc;
    NfcAdapter mNfcAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfctest);
        // Check for available NFC Adapter



        nfc = NfcAdapter.getDefaultAdapter(this);
        if (!chkNFCAv()) {
            return;
        }
        if
                (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {

        }
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
    protected void onPause() {
        super.onPause();
        if (NfcAdapter.getDefaultAdapter(this) != null)

            NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }
    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if(parcelables != null && parcelables.length > 0)
        {
            readTextFromMessage((NdefMessage) parcelables[0]);
        }else{
            Toast.makeText(this, "No NDEF messages found!", Toast.LENGTH_SHORT).show();
        }


    }

    private void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length>0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);


            Toast.makeText(this, tagContent, Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
        }
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


    private void readTextFromTag(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords !=null && ndefRecords.length>0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromTag(ndefRecord);

        }else{

            Toast.makeText(this, "readTextFromTag: No Messages Found", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean chkNFCAv() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
                    .show();
            finish();
            return false;
        }
        return true;
    }




    public String getTextFromTag(NdefRecord ndefR){

        String tagContent = null;
        try{
            byte[] payload = ndefR.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            int languagesize = payload[0] & 0063;
            tagContent = new String(payload, languagesize + 1,
                    payload.length - languagesize - 1, textEncoding);

        }catch (UnsupportedEncodingException e){


        }

        return tagContent;
    }








    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public Bitmap saveImage(Bitmap b) {
        String fileName = md5(b.getDensity() + "" + b.getHeight()
                + b.getWidth());
        String fname = fileName + ".jpeg";
        Toast.makeText(getApplicationContext(), fname, Toast.LENGTH_LONG)
                .show();
        File dir = getDir(Environment.DIRECTORY_PICTURES,
                Context.MODE_PRIVATE);
        File internalFile = null;
        internalFile = new File(dir, fname);
        internalFile.setReadable(true);
        internalFile.setWritable(true);
        internalFile.setExecutable(true);
        try {
            internalFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(internalFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            Log.e("eks", e.getMessage());
        }
        return (b);
    }
}
