package com.sky.pinkvelocity.warehouse.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hac10 on 26/09/15.
 */
public class MainMenuActivity extends Activity implements OnRetrieveHttpData{

    private ItemRecyclerAdapter myAdapter;

    String urlAddress = "http://10.0.2.2:8080/";


    // "http://localhost:8080/";


    PopupWindow flagIssuePopup;
    ListView itemList;
    ArrayList<String> items;
    Button dispatchButton;
    MediaPlayer beepSuccess;
    MediaPlayer beepFail;
    RelativeLayout mLayout;
    ErrorPopUpActivity errorMess;

    RecyclerView itemRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        beepSuccess = MediaPlayer.create(this, R.raw.scanbeep);
        beepFail = MediaPlayer.create(this, R.raw.scanerror);
        itemList = (ListView) findViewById(R.id.itemListView);
        items = new ArrayList<String>();
        dispatchButton = (Button) findViewById(R.id.dispatchButton);
        mLayout = (RelativeLayout) findViewById(R.id.activity_mainmenu);
        errorMess = new ErrorPopUpActivity();


        itemRV = (RecyclerView) findViewById(R.id.itemsRecyclerView);
        myAdapter = new ItemRecyclerAdapter(this, tempFiller());
        itemRV.setAdapter(myAdapter);
        itemRV.setLayoutManager(new LinearLayoutManager(this));



        listFiller();

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        // Set The Adapter
        itemList.setAdapter(arrayAdapter);


        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                String selectedItem = items.get(position);

                removeItem(selectedItem);
            }
        });

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
                return true;
            case R.id.action_requestorders:
                String itemGet = "http://httpbin.org/ip";//urlAddress+"order/get/six";
                RetrieveHTTPDataAsync retrieveBlogData = new RetrieveHTTPDataAsync(this);
                retrieveBlogData.execute(itemGet);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dispatchButtonClicked(View v){
        if(v.getId() == R.id.dispatchButton){
            Toast.makeText(this, "Dispatch request sent", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeItem(String selectedItem){
        beepSuccess.seekTo(0);

        for(int i=0; i < items.size(); i++){
            String n = items.get(i);
            if(n.equals(selectedItem)){
                items.remove(i);
                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
                itemList.setAdapter(arrayAdapter);


                beepSuccess.start();


                if(items.size() == 0){

                    dispatchButton.setClickable(true);
                    dispatchButton.getBackground().setColorFilter(null);
                }
                break;
            }else if(i == items.size()-1 && !n.equals(selectedItem)){

                errorMessage(selectedItem);
            }

        }
    }

    private void errorMessage(String selectedItem) {
        beepFail.seekTo(0);
        beepFail.start();

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

    @Override
    protected void onResume() {
        super.onResume();

        mLayout.setBackgroundColor(Color.WHITE);
    }




    public void wrongItemButtonClicked(View v){
        if(v.getId()==R.id.wrongItemButton) {

            removeItem("WRONG ITEM");
        }
    }



    public JSONObject getJson(String url){

        InputStream is = null;
        String result = "";
        JSONObject jsonObject = null;

        // HTTP
        try {
            HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e) {
            return null;
        }

        // Read response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch(Exception e) {
            return null;
        }

        // Convert string to object
        try {
            jsonObject = new JSONObject(result);
        } catch(JSONException e) {
            return null;
        }

        return jsonObject;

    }

    //////////
    public List<ItemInfo> tempFiller(){

        List<ItemInfo> data = new ArrayList<ItemInfo>();
        int[] icons ={R.drawable.logo,R.drawable.logo, R.drawable.logo, R.drawable.logo, R.drawable.logo};
        String[] itemTitles={"item 1","item 1","item 1","item 1","item 1"};

        for(int i=0; i<icons.length; i++){
            ItemInfo current = new ItemInfo();

            current.iconId= icons[i];
            current.itemName = itemTitles[i];
            data.add(current);
        }

        return data;
    }

    @Override
    public void onTaskCompleted(String httpData) {
        Toast.makeText(this, httpData, Toast.LENGTH_LONG).show();
    }

}
