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
import simit.org.jobbole.fragment.BlogItemsFragment;
import simit.org.jobbole.fragment.ResourceFragment;

/**
 * Created by liuchun on 2016/4/17.
 */
public class SubResActivity extends AppCompatActivity {
    private static final String SUB_RES_TAG = "SubRes";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //
        initView();
    }

    /** View initialization */
    private void initView(){
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String link = intent.getStringExtra("link");
        int parentChannel = intent.getIntExtra("channel", JobboleConstants.RESOURCE);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /** fragment replacement */
        Fragment fragment;
        if(parentChannel == JobboleConstants.RESOURCE){
            fragment = ResourceFragment.newInstance(JobboleConstants.SUB_RES_CHANNEL, link);
        }else {
            fragment = BlogItemsFragment.newInstance(JobboleConstants.SUB_SUB_RES_CHANNEL, link);
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_container, fragment, SUB_RES_TAG);
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
