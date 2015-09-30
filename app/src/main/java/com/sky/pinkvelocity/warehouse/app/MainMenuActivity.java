package com.sky.pinkvelocity.warehouse.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hac10 on 26/09/15.
 */
public class MainMenuActivity extends Activity implements OnRetrieveHttpData{

    private ItemRecyclerAdapter myAdapter;

    //String urlAddress = "http://172.16.5.84:8080/customer/order/pick";//(Jo's)//10.128.3.73
    String urlStatic ="http://10.128.3.73:8080"; //"http://172.16.5.84:8080";//"http://10.128.3.73:8080";


    // "http://localhost:8080/";

    boolean orderErrors= false;
    PopupWindow flagIssuePopup;
    //ListView itemList;
    ArrayList<String> items;
    Button dispatchButton;
    MediaPlayer beepSuccess;
    MediaPlayer beepFail;
    RelativeLayout mLayout;
    ErrorPopUpActivity errorMess;
    String mockJSON;
    TextView titltTextView;
    List<ItemInfo> data;
    String orderNumber = "";
    RecyclerView itemRV;
    ArrayAdapter<String> arrayAdapter;

    List<String>orderErrorsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        orderErrorsList = new ArrayList<String>();

        mockJSON = "{\"orderNumber\":1,\"dateOrderPlaced\":{\"centuryOfEra\":20,\"yearOfEra\":2015,\"yearOfCentury\":15,\"weekyear\":2015,\"monthOfYear\":9,\"weekOfWeekyear\":40,\"hourOfDay\":13,\"minuteOfHour\":27,\"secondOfMinute\":38,\"millisOfSecond\":983,\"millisOfDay\":48458983,\"secondOfDay\":48458,\"minuteOfDay\":807,\"year\":2015,\"dayOfMonth\":30,\"dayOfWeek\":3,\"era\":1,\"dayOfYear\":273,\"zone\":{\"uncachedZone\":{\"cachable\":true,\"fixed\":false,\"id\":\"Europe/London\"},\"fixed\":false,\"id\":\"Europe/London\"},\"millis\":1443616058983,\"chronology\":{\"zone\":{\"uncachedZone\":{\"cachable\":true,\"fixed\":false,\"id\":\"Europe/London\"},\"fixed\":false,\"id\":\"Europe/London\"}},\"afterNow\":false,\"beforeNow\":true,\"equalNow\":false},\"productsOrdered\":{\"ProductId: 1, name String name, imageUrl String imageUrl, shelf null, row null\":100,\"ProductId: 1, name Gnome, imageUrl String imageUrl, shelf null, row null\":200,\"ProductId: 1, name Remote, imageUrl String imageUrl, shelf null, row null\":300},\"productsDelivered\":{},\"totalPrice\":10,\"customer\":{\"firstName\":null,\"address\":\"null null null null null\",\"titleAndFullName\":\"null null null\",\"orders\":null},\"dispatchedProducts\":{\"ProductId: 1, name String name, imageUrl String imageUrl, shelf null, row null\":{\"2015-09-30T13:27:39.104+01:00\":5}},\"deliveredProducts\":{}}";

        titltTextView = (TextView)findViewById(R.id.orderNumberTextView);

        beepSuccess = MediaPlayer.create(this, R.raw.scanbeep);
        beepFail = MediaPlayer.create(this, R.raw.scanerror);
        //itemList = (ListView) findViewById(R.id.itemListView);
        items = new ArrayList<String>();
        dispatchButton = (Button) findViewById(R.id.dispatchButton);
        mLayout = (RelativeLayout) findViewById(R.id.activity_mainmenu);
        errorMess = new ErrorPopUpActivity();


        //itemRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        itemRV = (RecyclerView) findViewById(R.id.itemsRecyclerView);
        itemRV.setAdapter(myAdapter);
        itemRV.setLayoutManager(new LinearLayoutManager(this));
        itemRV.addItemDecoration(new SimpleDividerItemDecoration(this));

        listFiller();

        dispatchButton.setClickable(false);
        dispatchButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_flagissue:
                mLayout.setBackgroundColor(Color.GRAY);
                startActivity(new Intent(MainMenuActivity.this, FlagIssuePopUpActivity.class));
                return true;
            case R.id.action_settings:
                sortData(mockJSON);
                return true;
            case R.id.action_requestorders:
                String itemGet = urlStatic +"/customer/order/pick";
                RetrieveHTTPDataAsync retrieveOrderData = new RetrieveHTTPDataAsync(this);
                retrieveOrderData.execute(itemGet);
                return true;
            case R.id.action_post:
                //postOrders();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dispatchButtonClicked(View v){
        if(v.getId() == R.id.dispatchButton){
            Toast.makeText(this, "Dispatch request sent", Toast.LENGTH_SHORT).show();


            postOrders(orderErrorsList, orderNumber);
            titltTextView.setText("");
        }
    }

    public void removeItem(String selectedItem){

        for(int i=0; i < data.size(); i++){



            ItemInfo tempItem = data.get(i);
            String n = tempItem.productTypeId.trim();
            if(n.equals(selectedItem)){

                data.get(i).quantity--;
                //tempItem.quantity--;
                myAdapter.notifyDataSetChanged();
                if(tempItem.quantity == 0){
                    data.remove(i);
                    myAdapter.notifyItemRemoved(i);
                }
                if(data.size() == 0){
                    dispatchButton.setClickable(true);
                    dispatchButton.getBackground().setColorFilter(null);
                }
                break;
            }else if(i == data.size()-1 && !n.equals(selectedItem)) {
                errorMessage(selectedItem);
            }else if(data.size()==0){
                Toast.makeText(this, "No more items in your order list!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void errorMessage(String selectedItem) {

        errorMess.setErrorString("Item not in list.");
        errorMess.setItemIdInt(selectedItem);

        mLayout.setBackgroundColor(Color.GRAY);
        startActivity(new Intent(MainMenuActivity.this, ErrorPopUpActivity.class));
}


    public void listFiller(){
        String itemInList = "item";

        for(int i=0; i < 10; i++){
            items.add(itemInList +" " + i);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        PendingIntent intent = PendingIntent.getActivity(this, 0, new
//                        Intent(
//                        this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
//                0);
//        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this,
//                intent, null, null);
//
//        mLayout.setBackgroundColor(Color.WHITE);
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//
//        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//        if(parcelables != null && parcelables.length > 0)
//        {
//            readTextFromMessage((NdefMessage) parcelables[0]);
//        }else{
//            Toast.makeText(this, "No NDEF messages found!", Toast.LENGTH_SHORT).show();
//        }
    }

//    private void readTextFromMessage(NdefMessage ndefMessage) {
//        beepSuccess.seekTo(0);
//        NdefRecord[] ndefRecords = ndefMessage.getRecords();
//
//        if(ndefRecords != null && ndefRecords.length>0){
//
//            NdefRecord ndefRecord = ndefRecords[0];
//
//            String tagContent = getTextFromNdefRecord(ndefRecord);
//
//            removeItem(tagContent);
//
//        }else
//        {
//            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
//        }
//
//    }

//    public String getTextFromNdefRecord(NdefRecord ndefRecord)
//    {
//        String tagContent = null;
//        try {
//            byte[] payload = ndefRecord.getPayload();
//            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
//            int languageSize = payload[0] & 0063;
//            tagContent = new String(payload, languageSize + 1,
//                    payload.length - languageSize - 1, textEncoding);
//        } catch (UnsupportedEncodingException e) {
//            Log.e("getTextFromNdefRecord", e.getMessage(), e);
//        }
//        return tagContent;
//    }







    public void postOrders(final List<String> errors, final String orderNo){
        Thread post = new Thread(new Runnable(){
            @Override
            public void run(){
                try
                {
                    String postResponse = "";
                    String [] mockPost = {"1", "33", "10"};


                    HttpClient httpclient = new DefaultHttpClient();

                    if(orderErrors){
                        HttpPost httppostreq = new HttpPost(urlStatic+"/customer/order/dispatched");

                        StringEntity se = new StringEntity(mockPost.toString());
                        se.setContentType("application/json;charset=UTF-8");
                        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                        httppostreq.setEntity(se);

                        HttpResponse httpresponse = httpclient.execute(httppostreq);

                        postResponse = EntityUtils.toString(httpresponse.getEntity());
                        showToast(postResponse);

                    }else{
                        HttpPost httppostreq = new HttpPost(urlStatic+"/customer/order/dispatched/errors");

                        StringEntity se = new StringEntity(orderNo);
                        se.setContentType("application/json;charset=UTF-8");
                        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                        httppostreq.setEntity(se);

                        HttpResponse httpresponse = httpclient.execute(httppostreq);

                        postResponse = EntityUtils.toString(httpresponse.getEntity());
                        showToast(postResponse);
                    }
                }catch(Exception e){

                    String ew = e.toString();
                }
            }


        });
        post.start();

        if(post.getState()!=Thread.State.TERMINATED){

        }

    }

    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainMenuActivity.this, toast, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onTaskCompleted(String httpData) {
        //Toast.makeText(this, httpData, Toast.LENGTH_LONG).show();
        //mockJSON = "{\"orderNumber\":1,\"dateOrderPlaced\":{\"centuryOfEra\":20,\"yearOfEra\":2015,\"yearOfCentury\":15,\"weekyear\":2015,\"monthOfYear\":9,\"weekOfWeekyear\":40,\"hourOfDay\":13,\"minuteOfHour\":27,\"secondOfMinute\":38,\"millisOfSecond\":983,\"millisOfDay\":48458983,\"secondOfDay\":48458,\"minuteOfDay\":807,\"year\":2015,\"dayOfMonth\":30,\"dayOfWeek\":3,\"era\":1,\"dayOfYear\":273,\"zone\":{\"uncachedZone\":{\"cachable\":true,\"fixed\":false,\"id\":\"Europe/London\"},\"fixed\":false,\"id\":\"Europe/London\"},\"millis\":1443616058983,\"chronology\":{\"zone\":{\"uncachedZone\":{\"cachable\":true,\"fixed\":false,\"id\":\"Europe/London\"},\"fixed\":false,\"id\":\"Europe/London\"}},\"afterNow\":false,\"beforeNow\":true,\"equalNow\":false},\"productsOrdered\":{\"ProductId: 1, name String name, imageUrl String imageUrl, shelf null, row null\":100,\"ProductId: 1, name Gnome, imageUrl String imageUrl, shelf null, row null\":200,\"ProductId: 1, name Remote, imageUrl String imageUrl, shelf null, row null\":300},\"productsDelivered\":{},\"totalPrice\":10,\"customer\":{\"firstName\":null,\"address\":\"null null null null null\",\"titleAndFullName\":\"null null null\",\"orders\":null},\"dispatchedProducts\":{\"ProductId: 1, name String name, imageUrl String imageUrl, shelf null, row null\":{\"2015-09-30T13:27:39.104+01:00\":5}},\"deliveredProducts\":{}}";
        //sortData(mockJSON);
        if(httpData!=null){
           sortData(httpData);

        }



    }

    public void sortData(String data){
        List<String> productName = new ArrayList<String>();
        List<String> productQuantity = new ArrayList<String>();
        try {
            JSONObject orderObject = new JSONObject(mockJSON);
            orderNumber = orderObject.getString("orderNumber");
            JSONObject productsOrdered = orderObject.getJSONObject("productsOrdered");

            Iterator<String> iter = productsOrdered.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = productsOrdered.get(key);
                    productName.add(key);
                    productQuantity.add(value.toString());
                } catch (JSONException e) {
                    // Something went wrong!
                }
            }


            Toast.makeText(this, orderNumber + productsOrdered, Toast.LENGTH_LONG).show();



        }catch (Exception e){
            String E = e.toString();

        }

        myAdapter = new ItemRecyclerAdapter(this, tempFiller(productName, productQuantity, orderNumber));
        myAdapter.notifyDataSetChanged();
        itemRV.setAdapter(myAdapter);
        itemRV.setLayoutManager(new LinearLayoutManager(this));

    }


    //////////
    public List<ItemInfo> tempFiller(List<String> productName, List<String> productQuantity, String orderNumber){

        data = new ArrayList<ItemInfo>();


        //int[] icons ={R.drawable.logo,R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo};
        //String[] itemTitles={"item 1","item 1","item 1","item 1","item 1"};
        titltTextView.setText("Order Number: " + orderNumber);

        for(int i=0; i<productName.size(); i++){
            ItemInfo current = new ItemInfo();
            String productInfo = productName.get(i);
            String[] productInfoSplit = productInfo.split(",");
            String[] productId = productInfoSplit[0].split(":");
            //String quant = productQuantity.get(i);


            current.quantity = Integer.parseInt(productQuantity.get(i));
            current.location = productInfoSplit[0] + "\nLocation: " + productInfoSplit[3]+productInfoSplit[4];
            current.iconId= R.drawable.logo;
            current.itemName = productInfoSplit[1];
            current.productTypeId = productId[1].trim();
            data.add(current);
        }

        return data;
    }

    public void TestClicked(View v){

        removeItem("1");
    }

}
