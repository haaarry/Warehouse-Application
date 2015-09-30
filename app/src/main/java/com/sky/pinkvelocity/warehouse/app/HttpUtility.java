package com.sky.pinkvelocity.warehouse.app;


        import android.app.Activity;
        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.util.Log;

        import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
        import org.springframework.web.client.RestClientException;
        import org.springframework.web.client.RestTemplate;
        import java.util.List;

/**
 * A utility class to provide utility methods for http connections.
 */
public class HttpUtility {
    private static HttpUtility httpUtility = new HttpUtility();
    private static final String URL_FOR_GET_ORDER = "http://172.16.5.84:8080/customer/order/pick";
    private RestTemplate restTemplate;

    private HttpUtility(){};

    public static HttpUtility getHttpUtility(){
        return httpUtility;
    }

    public boolean isConnectedToInternet(ConnectivityManager cm){
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public RestTemplate getRestTemplate(){
        if(restTemplate == null){
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }
        return restTemplate;
    }

    public Order getOrder(){
        try {
            return HttpUtility.getHttpUtility().getRestTemplate().getForObject(URL_FOR_GET_ORDER, Order.class);
        } catch (RestClientException e) {
            if(e.getMessage() == "patient.notfound"){
                return null;
            }else{
                Log.e("MainActivity", e.getMessage(), e);
            }
        }
        return null;
    }



}