package simit.org.jobbole.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import simit.org.jobbole.activity.R;
import simit.org.jobbole.config.JobboleConstants;
import simit.org.jobbole.widget.RadioGroup;

/**
 * Created by liuchun on 2016/4/6.
 */
public class ChannelFragment extends Fragment {
    private static final int SUB_CHANNEL_COUNT = 6;
    private static final int[] RADIO_BUTTON_IDS = {R.id.design_channel_btn, R.id.web_channel_btn,
            R.id.python_channel_btn, R.id.java_channel_btn, R.id.android_channel_btn, R.id.ios_channel_btn};
    private static final String[] SUB_FRAG_TAGS = {"design", "web", "python", "java", "android", "ios"};
    // UI components
    //private RadioGroup radioGroup;
    // Fragments for Container
    private List<Fragment> fragments;
    // current show views
    private int curpos = 0;

    public static Fragment newInstance(int channelId){
        Bundle args = new Bundle();
        args.putInt(JobboleConstants.CHANNEL_NAME, channelId);
        Fragment fragment = new ChannelFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // layout
        return inflater.inflate(R.layout.fragment_channel, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //
        // initial fragments
        initFragments();
        // View initialization
        initViews(getView());
    }

    /** initial views in fragment */
    private void initViews(View rootView) {
        RadioGroup radioGroup = (RadioGroup) rootView.findViewById(R.id.grid_container);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                //
                for (int i = 0; i < RADIO_BUTTON_IDS.length; i++) {
                    if (checkedId != RADIO_BUTTON_IDS[i]) {
                        continue;
                    }
                    //
                    if (curpos != i) {
                        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                        ft.hide(fragments.get(curpos));  // hide current page
                        ft.show(fragments.get(i));  // show next page
                        ft.commit();
                        curpos = i;  // update current page index
                    }
                }
            }
        });
    }

    /** initial fragments */
    private void initFragments(){
        // no cached fragments
        fragments = new ArrayList<>();
        for(int i = 0; i < SUB_CHANNEL_COUNT; i++){
            Fragment fragment = FragmentFactory.createFragment(i + JobboleConstants.SUB_OFFSET);
            fragments.add(fragment);
        }
        // begin fragment transition
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        for(int i = 0; i < SUB_CHANNEL_COUNT; i++){
            ft.add(R.id.sub_channel_container, fragments.get(i), SUB_FRAG_TAGS[i]);
            ft.hide(fragments.get(i));  // hide all fragments
        }
        ft.show(fragments.get(curpos));   // show first fragments
        ft.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /** Fix the no activity exception */
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
