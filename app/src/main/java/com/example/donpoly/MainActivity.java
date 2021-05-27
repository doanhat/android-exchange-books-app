package com.example.donpoly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.donpoly.data.LoginDataSource;
import com.example.donpoly.data.LoginRepository;
import com.example.donpoly.ui.home.HomeFragment;
import com.example.donpoly.ui.login.LoginActivity;
import com.example.donpoly.ui.messages.MessagesFragment;
import com.example.donpoly.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mNavView;
    private MenuItem mMenuItem;
    private SharedPreferences sharedPreferences;
    private Boolean isLogin;
//    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData() {
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        isLogin = LoginRepository.getInstance(new LoginDataSource()).isLoggedIn();
    }

    private void initView() {
        mViewPager = findViewById(R.id.viewPager);
        mNavView = findViewById(R.id.nav_view);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MessagesFragment());
        fragments.add(new ProfileFragment());

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(myFragmentPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mMenuItem != null){
                    mMenuItem.setChecked(false);
                }else {
                    mNavView.getMenu().getItem(0).setChecked(false);
                }

                if (position == 2 && !isLogin){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    mMenuItem = mNavView.getMenu().getItem(position);
                    mMenuItem.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mMenuItem = item;
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        mViewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_messages:
                        mViewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_profil:
                        if (isLogin){
                            mViewPager.setCurrentItem(2);
                            return true;
                        }else {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            return false;
                        }

                }
                return false;
            }
        });
    }


}