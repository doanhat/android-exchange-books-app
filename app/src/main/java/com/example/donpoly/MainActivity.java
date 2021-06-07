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

import com.example.donpoly.ui.home.HomeFragment;
import com.example.donpoly.ui.login.LoginActivity;
import com.example.donpoly.ui.messages.MessagesFragment;
import com.example.donpoly.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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
    private final static String CHANNEL_ID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_messages, R.id.navigation_profil)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);



        initData();
        initView();
    }

    private void initData() {
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
//        isLogin = LoginRepository.getInstance(new LoginDataSource()).isLoggedIn();
//        Toast.makeText(MainActivity.this, FirebaseAuth.getInstance().getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            isLogin = false;
        }else {
            isLogin = true;
        }
//        createNotificationChannel();
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
                        if (isLogin){
                            mViewPager.setCurrentItem(1);
                            return true;
                        }else {
//                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                            startActivity(intent);
                            return false;
                        }
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

        // Create an explicit intent for an Activity in your app
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(getString(R.string.bought))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(1,builder.build());
    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }


}