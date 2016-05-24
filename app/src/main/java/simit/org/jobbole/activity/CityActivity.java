package simit.org.jobbole.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import simit.org.jobbole.adapter.CityItemAdapter;
import simit.org.jobbole.network.JobboleHttpClient;

/**
 * Created by liuchun on 2016/5/22.
 */
public class CityActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int DEFAULT_COLUMN = 3;
    // UI components
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CityItemAdapter mAdapter;
    //
    private TextView mCity;
    private TextView mAllCity;
    //
    private String[] cityNames;
    private String[] cityTags;

    private LocationManager locationManager;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        // initView
        cityNames = getResources().getStringArray(R.array.city_names);
        cityTags = getResources().getStringArray(R.array.city_tags);
        initView();
        // get current location city
        mCity = (TextView) findViewById(R.id.location_city);
        mCity.setOnClickListener(this);
        mAllCity = (TextView) findViewById(R.id.all_city);
        mAllCity.setOnClickListener(this);
        // get current location city name
        mCity.setText(restoreCity());
        getCurrentCity();
    }

    private void initView(){
        // setup ToolBar
        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = "选择城市";
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, DEFAULT_COLUMN);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //
        mAdapter = new CityItemAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        // set onClickListener
        mAdapter.setOnItemClickListener(new CityItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String cityName = cityNames[position];
                String cityTag = cityTags[position];

                Intent intent = new Intent();
                intent.putExtra("cityName", cityName);
                intent.putExtra("cityTag", cityTag);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String cityName = "全国", cityTag = "";
        switch (v.getId()){
            case R.id.all_city:
                intent.putExtra("cityName", cityName);
                intent.putExtra("cityTag", cityTag);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.location_city:
                cityName = mCity.getText().toString();
                int index = getCityIndex(cityName);
                if(index > 0){
                    cityTag = cityTags[index];
                    intent.putExtra("cityName", cityName);
                    intent.putExtra("cityTag", cityTag);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(this, "当前城市无内容", Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }

    }

    /** 获取当前定位城市 **/
    private void getCurrentCity(){
        //
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        List<String> providers = locationManager.getAllProviders();
        for(String provider : providers){
            if(provider.equals(LocationManager.GPS_PROVIDER)){

                Location location = locationManager.getLastKnownLocation(provider);
                if(location != null){
                    getCurrentCityName(location);
                }


                locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
            }else if(provider.equals(LocationManager.NETWORK_PROVIDER)){

                Location location = locationManager.getLastKnownLocation(provider);
                if(location != null){
                    getCurrentCityName(location);
                }

                locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
            }
        }
    }

    // location all
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            // subThread
            getCurrentCityName(latitude, longitude);
            //
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private String restoreCity(){
        SharedPreferences preferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        return preferences.getString("city", "北京");
    }

    private void saveCity(String cityName){
        SharedPreferences preferences = getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("city", cityName);
        editor.apply();
    }

    private void getCurrentCityName(Location location){

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        getCurrentCityName(latitude, longitude);
    }

    private void getCurrentCityName(double latitude, double longitude){
        String google_api_url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&language=zh-CN";
        final String url = String.format(Locale.getDefault(), google_api_url, latitude, longitude);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String cityName = JobboleHttpClient.getCityName(url);
                //
                Log.d("CityActivity", "current city: " + cityName);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCity.setText(cityName);
                        saveCity(cityName);
                    }
                });
            }
        });
        thread.start();
    }

    private int getCityIndex(String cityName){

        for(int i = 0; i < cityNames.length; i++){
            if(cityName.equals(cityNames[i])){
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
