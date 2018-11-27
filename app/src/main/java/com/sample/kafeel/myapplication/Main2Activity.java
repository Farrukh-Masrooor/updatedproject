package com.sample.kafeel.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity implements Tab1.OnFragmentInteractionListener,Tab2.OnFragmentInteractionListener {

    SharedPreferences preferences;
    Toolbar toolbar;
    LinearLayout layout;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setTag("Tab1").setText("Explore"));
        tabLayout.addTab(tabLayout.newTab().setTag("Tab2").setText("Accounts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        layout=findViewById(R.id.linear_layout);
        frameLayout=findViewById(R.id.frame);
        final ViewPager viewPager=(ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter=new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tab.setText("Tab "+tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());

            }



            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.settings,menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        preferences= getSharedPreferences("MyPrefsFile",0);
        Boolean b=preferences.getBoolean("my_login",true);
        Log.d("My_log",""+b.toString());
        preferences.edit().putBoolean("my_login",false).commit();

        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

//    @Override
//    public void onBackPressed() {
//
//        Intent a = new Intent(Intent.ACTION_MAIN);
//        a.addCategory(Intent.CATEGORY_HOME);
//       // a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(a);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


//    @Override
//    public void addBook() {
//        layout.setVisibility(View.INVISIBLE);
//        //frameLayout.setVisibility(View.VISIBLE);
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction().add(R.id.book_root_view, new AddBook());
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}
