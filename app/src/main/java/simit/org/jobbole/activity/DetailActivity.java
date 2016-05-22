package simit.org.jobbole.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import simit.org.jobbole.config.JobboleConstants;
import simit.org.jobbole.fragment.DetailFragment;

/**
 * Created by liuchun on 2016/4/16.
 */
public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "Detail";
    // 当前页面标识
    private int curChannel = 0;
    // 当前文章的链接
    private String curLink = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //
        Intent intent = getIntent();
        curChannel = intent.getIntExtra("channel", 0);
        curLink = intent.getStringExtra("link");
        // init view
        initView();
    }

    /** init UI components */
    private void initView(){
        // Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // get Title
        String title;
        if(curChannel < 6){
            // main channel
            String[] titles = getResources().getStringArray(R.array.page_titles);
            title = titles[curChannel];
        }else if(curChannel < 12){
            // sub channel
            String[] titles = getResources().getStringArray(R.array.channel_titles);
            title = titles[curChannel - JobboleConstants.SUB_OFFSET];
        }else {
            title = getIntent().getStringExtra("title");
        }

        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // begin transaction
        Fragment fragment = DetailFragment.newInstance(curChannel, curLink);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_container, fragment, TAG);
        ft.commit();
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
