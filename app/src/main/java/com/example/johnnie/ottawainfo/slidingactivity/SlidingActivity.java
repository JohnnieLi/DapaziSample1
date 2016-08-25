/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.johnnie.ottawainfo.slidingactivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.johnnie.ottawainfo.R;
import com.example.johnnie.ottawainfo.model.DealerModel;
import com.example.johnnie.ottawainfo.slidingactivity.slidingFragment.SlidingTabsBasicFragment;


public class SlidingActivity extends ActionBarActivity
        implements SlidingTabsBasicFragment.basicFragmentClickedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DealerModel dealerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);

        Intent intentExtras = getIntent();
        Bundle extraBundle = intentExtras.getExtras();
        if(extraBundle.containsKey("message")){
            String message = extraBundle.getString("message");
            String[] messages = message.split("_");
            dealerModel = new DealerModel();
            //Messages[]:address,Id,category,name,info,faq
            dealerModel.setAddress(messages[0]);
            dealerModel.setDealId(new Long(messages[1]));
            dealerModel.setCategory(messages[2]);
            dealerModel.setName(messages[3]);
            dealerModel.setInformation(messages[4]);
            dealerModel.setFAQ(messages[5]);
            Log.d("BUNDLE",dealerModel.getFAQ());
        }


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            if(dealerModel != null){
                fragment.getDealerModel(dealerModel);
            }
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }





        //configureToolbar();
        configureDrawer();
    }

//    private void configureToolbar() {
//        Toolbar mainToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mainToolbar);
//        getSupportActionBar().setTitle("Sliding");
//
//        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
//                    mDrawerLayout.closeDrawer(Gravity.START);
//
//                } else {
//                    mDrawerLayout.openDrawer(Gravity.START);
//                }
//            }
//        });
//    }

    private void configureDrawer() {
        // Configure drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_closed) {

            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBasicFragmentClicked() {
        Toast.makeText(this, "fragment clicked", Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, TransitionFirstActivity.class);
        //startActivity(intent);

    }
}
