package com.anglll.aflow.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.anglll.aflow.R;
import com.anglll.aflow.base.BaseFragment;
import com.anglll.aflow.base.BaseMusicActivity;
import com.anglll.aflow.ui.discovery.DiscoveryFragment;
import com.anglll.aflow.ui.home.HomeFragment;
import com.anglll.aflow.ui.user.UserFragment;
import com.anglll.aflow.utils.statusbar.StatusBarUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseMusicActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String SHOW_FRAGMENT_INDEX = "SHOW_FRAGMENT_INDEX";
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    private int showFragmentIndex;
    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
//        StatusBarUtil.setColor(this,ContextCompat.getColor(this,R.color.colorPrimary),255);
        StatusBarUtils.setImmersiveStatusBar(this,true);
        initView(savedInstanceState);
    }

    //https://github.com/gpfduoduo/android-article/blob/master/Activity%20%2B%20%E5%A4%9AFrament%20%E4%BD%BF%E7%94%A8%E6%97%B6%E7%9A%84%E4%B8%80%E4%BA%9B%E5%9D%91.md
    private void initView(Bundle savedInstanceState) {
//        navigation.setSelectedItemId(navigation.getMenu().getItem(position).getItemId());
        mNavigation.setOnNavigationItemSelectedListener(this);
        HomeFragment homeFragment;
        DiscoveryFragment discoveryFragment;
        UserFragment userFragment;
        if (savedInstanceState != null) {
            homeFragment = (HomeFragment) getSupportFragmentManager()
                    .findFragmentByTag(HomeFragment.class.getName());
            discoveryFragment = (DiscoveryFragment) getSupportFragmentManager()
                    .findFragmentByTag(DiscoveryFragment.class.getName());
            userFragment = (UserFragment) getSupportFragmentManager()
                    .findFragmentByTag(UserFragment.class.getName());

            fragments = new ArrayList<>();
            fragments.add(homeFragment);
            fragments.add(discoveryFragment);
            fragments.add(userFragment);
            showFragment(savedInstanceState.getInt(SHOW_FRAGMENT_INDEX));
        } else {
            showFragmentIndex = 0;
            fragments.add(HomeFragment.newInstance());
            fragments.add(DiscoveryFragment.newInstance());
            fragments.add(UserFragment.newInstance());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragments.get(0), HomeFragment.class.getName())
                    .add(R.id.container, fragments.get(1), DiscoveryFragment.class.getName())
                    .add(R.id.container, fragments.get(2), UserFragment.class.getName())
                    .hide(fragments.get(1))
                    .hide(fragments.get(2))
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SHOW_FRAGMENT_INDEX, showFragmentIndex);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        resetDefaultIcon();
        Log.d(TAG, String.valueOf(item.getTitle()));
        switch (item.getItemId()) {
            case R.id.navigation_home:
                item.setIcon(R.drawable.ic_home_selected);
                showFragment(0);
                return true;
            case R.id.navigation_discovery:
                item.setIcon(R.drawable.ic_discovery_selected);
                showFragment(1);
                return true;
            case R.id.navigation_mine:
                item.setIcon(R.drawable.ic_mine_selected);
                showFragment(2);
                return true;
        }
        return false;
    }

    private void showFragment(int index) {
        if (showFragmentIndex == index)
            return;
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == index) {
                transaction.show(fragments.get(i));
            } else {
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commit();
        showFragmentIndex = index;
    }

    private void resetDefaultIcon() {
        mNavigation.getMenu()
                .findItem(R.id.navigation_home)
                .setIcon(R.drawable.ic_home);
        mNavigation.getMenu()
                .findItem(R.id.navigation_discovery)
                .setIcon(R.drawable.ic_discovery);
        mNavigation.getMenu()
                .findItem(R.id.navigation_mine)
                .setIcon(R.drawable.ic_mine);

    }
}
