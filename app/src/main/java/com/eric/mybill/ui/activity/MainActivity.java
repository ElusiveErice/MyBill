package com.eric.mybill.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.eric.mybill.R;
import com.eric.mybill.ui.fragment.AssessmentFragment;
import com.eric.mybill.ui.fragment.BillListFragment;
import com.eric.mybill.ui.fragment.HomepageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment_homepage);
        if(mFragment == null){
            mFragment = HomepageFragment.newInstance();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_homepage, mFragment)
                    .commit();
        }


        mBottomNavigationView = findViewById(R.id.bottom_menu);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.item_homepage:
                        mFragment = HomepageFragment.newInstance();
                        break;
                    case R.id.item_bill:
                        mFragment = BillListFragment.newInstance();
                        break;
                    case R.id.item_assessment:
                        mFragment = AssessmentFragment.newInstance();
                        break;
                }

                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_homepage, mFragment)
                        .commit();

                return true;
            }
        });
    }
}
