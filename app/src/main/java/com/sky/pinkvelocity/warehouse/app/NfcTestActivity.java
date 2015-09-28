package com.sky.pinkvelocity.warehouse.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hac10 on 28/09/15.
 */
public class NfcTestActivity extends Activity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";


    NfcAdapter mNfcAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfctest);
        // Check for available NFC Adapter
        if (!chkNFCAv()) {
            return;
        }
        if
                (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
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

        Toast.makeText(this, "onNewIntent", Toast.LENGTH_SHORT).show();
        handleIntent(intent);


//        setIntent(intent);
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
//            processIntent(getIntent());
//        }
//        else{
//
//            Toast.makeText(this, "What you doin in ere m8?? OnNewIntent else",Toast.LENGTH_SHORT).show();
//
//        }
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


       private void handleIntent(Intent intent) {
//        String action = intent.getAction();
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
//
//            String type = intent.getType();
//            if (MIME_TEXT_PLAIN.equals(type)) {
//
//                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//                new NdefReaderTask().execute(tag);
//
//            } else {
//                Log.d(TAG, "Wrong mime type: " + type);
//            }
//        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
//
//            // In case we would still use the Tech Discovered Intent
//            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            String[] techList = tag.getTechList();
//            String searchedTech = Ndef.class.getName();
//
//            for (String tech : techList) {
//                if (searchedTech.equals(tech)) {
//                    new NdefReaderTask().execute(tag);
//                    break;
//                }
//            }
//        }
    }







    void processIntent(Intent intent) {
        Toast.makeText(this, "processIntent called, WOOP", Toast.LENGTH_LONG).show();



//        Parcelable[] msgs = intent
//                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//        NdefMessage[] nmsgs = new NdefMessage[msgs.length];
//        for (int i = 0; i < msgs.length; i++) {
//            nmsgs[i] = (NdefMessage) msgs[i];
//        }
//        byte[] payload = nmsgs[0].getRecords()[0].getPayload();
//        if (payload != null) {
//            TextView pageTitle = (TextView) findViewById(R.id.textView2);
//            ImageView iv = new ImageView(this);
//            TextView SizeIVTV = new TextView(this);
//            TextView HeightIVTV = new TextView(this);
//            TextView WidthIVTV = new TextView(this);
//            LinearLayout ll1 = (LinearLayout) findViewById(R.id.ll1);
//            if (nmsgs[0].getRecords()[0].getTnf() == 2) {
//                Toast.makeText(this, "New picture received!",
//                        Toast.LENGTH_SHORT).show();
//                ByteArrayInputStream imageStream = new ByteArrayInputStream(
//                        payload);
//                Bitmap b = BitmapFactory.decodeStream(imageStream);
//                iv.setImageBitmap(b);
//                // saveImage(b);
//                String sizeIV = "Size = " + b.getByteCount() + " Bytes";
//                SizeIVTV.setText(sizeIV);
//                ll1.addView(SizeIVTV);
//                String heightIV = "Height = " + b.getHeight() + " Pixels";
//                HeightIVTV.setText(heightIV);
//                ll1.addView(HeightIVTV);
//                pageTitle.setText("New picture received!\n\n");
//                String widthIV = "Width = " + b.getWidth() + " Pixels";
//                WidthIVTV.setText(widthIV);
//                ll1.addView(WidthIVTV);
//                ll1.addView(iv);
//            }
//        }
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
