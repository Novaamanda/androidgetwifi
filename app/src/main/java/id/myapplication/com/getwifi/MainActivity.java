package id.myapplication.com.getwifi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.myapplication.com.getwifi.Adapter.SignalAdatperRV;
import id.myapplication.com.getwifi.Feature.WebViewActivity;
import id.myapplication.com.getwifi.Model.DataRadius;
import id.myapplication.com.getwifi.Model.DataWifi;
import id.myapplication.com.getwifi.Network.ApiConfig;
import id.myapplication.com.getwifi.Network.IAPI;
import id.myapplication.com.getwifi.utils.DistanceUtil;
import id.myapplication.com.getwifi.utils.ScanResultComparator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //region VARIABLE.
    private int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    private RecyclerView listSignals;
    private Button btnWebView;

    private List<ScanResult> results = new ArrayList<>();
    private SignalAdatperRV adapter;

    private Retrofit retrofit;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRV();
        initListener();
        initRetrofit();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            scanWifi();
        }else{
            scanWifi();
        }
    }


    //region INIT.
    private void initRV(){
        listSignals = (RecyclerView) findViewById(R.id.listview);
        adapter = new SignalAdatperRV(results, MainActivity.this);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listSignals.setLayoutManager(llm);
        listSignals.setAdapter(adapter);
    }

    private void initListener(){
        btnWebView = (Button) findViewById(R.id.btn_webview);
        btnWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(i);
            }
        });
    }
    //endregion

    //region WIFI.
    private List<DataWifi> buatModelWifi(List<ScanResult> results){
        List<DataWifi> listData = new ArrayList<>();

        for(ScanResult dataScan : results){
            DataWifi tmpData = new DataWifi(dataScan.SSID, dataScan.frequency, dataScan.level);

            // if untuk filter jarak disini
            double distance = DistanceUtil.calculateDistance((double) tmpData.getLevel(),
                    (double) tmpData.getFrekuensi());
            tmpData.setJarak(distance);

            if(distance < 10) {
                listData.add(tmpData);
            }
        }

        return listData;
    }

    private void scanWifi(){
        @SuppressLint("WifiManagerLeak") final
        WifiManager wifi = (WifiManager) getSystemService(this.getApplicationContext().WIFI_SERVICE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wifi.startScan();
                results = wifi.getScanResults();
                Collections.sort(results, new ScanResultComparator());
                adapter.setData(buatModelWifi(results));
                adapter.notifyDataSetChanged();

                List<DataWifi> listData = new ArrayList<>();
                listData = buatModelWifi(results);

                kirimDataRadius(String.valueOf(listData.get(0).getJarak()), String.valueOf(listData.get(1).getJarak())
                        , String.valueOf(listData.get(2).getJarak()));


                Log.d("LOG_RESULT", wifi.getScanResults().toString());
                Log.d("LOG_RESULT_2", buatModelWifi(results).toString());
                scanWifi();
            }
        }, 3000);
    }
    //endregion

    //region NETWORK. CONTOH AJA.
    // Inisialisasi retrofit.
    private void initRetrofit(){
        retrofit = new Retrofit.Builder().baseUrl(ApiConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        ambilData(retrofit);
    }

    // POST data ke server
//    private void kirimData(){
//        IAPI iApi = retrofit.create(IAPI.class);
//        Call<DataWifi> data = iApi.postData("", "");
//        data.enqueue(new Callback<DataWifi>() {
//            @Override
//            public void onResponse(Call<DataWifi> call, Response<DataWifi> response) {
//                Log.d("LOG_RESPONSE", response.toString());
//            }
//
//            @Override
//            public void onFailure(Call<DataWifi> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }


    // POST data ke server
    private void kirimDataRadius(String dataRadius1, String dataRadius2, String dataRadius3){
        IAPI iApi = retrofit.create(IAPI.class);
        Call<DataRadius> data = iApi.postDataRadius(dataRadius1, dataRadius2, dataRadius3);
        data.enqueue(new Callback<DataRadius>() {
            @Override
            public void onResponse(Call<DataRadius> call, Response<DataRadius> response) {
                Log.d("LOG_RESPONSE", response.toString());
                Log.d("LOG_RESPONSE_2", response.body().getPesan());
            }

            @Override
            public void onFailure(Call<DataRadius> call, Throwable t) {
                t.printStackTrace();
                Log.d("LOG_FAIL", "fail");

            }
        });
    }


    // GET data dari server.
//    private void ambilData(Retrofit retrofit){
//        IAPI iApi = retrofit.create(IAPI.class);
//        Call<DataRadius> data = iApi.getData("popular", getString(R.string.api_key));
//        data.enqueue(new Callback<DataRadius>() {
//            @Override
//            public void onResponse(Call<DataRadius> call, Response<DataRadius> response) {
//                Log.d("LOG_RESPONSE", response.toString());
//                Log.d("LOG_RESPONSE_BODY", response.body().toString());
//                Log.d("LOG_CALL", call.toString());
//            }
//
//            @Override
//            public void onFailure(Call<DataWifi> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }
    //endregion


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do Nothing.
        }
    }
}
