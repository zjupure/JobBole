package simit.org.jobbole.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import simit.org.jobbole.adapter.CityItemAdapter;

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
