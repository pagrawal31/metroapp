package com.pstech.hydmetro;

import android.support.v4.app.FragmentManager;
// import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pstech.hydmetro.fragments.AboutFragment;
import com.pstech.hydmetro.fragments.AppFragment;
import com.pstech.hydmetro.fragments.HomeFragment;
import com.pstech.hydmetro.fragments.MetroMapFragment;
import com.pstech.hydmetro.fragments.SearchRouteFragment;
import com.pstech.hydmetro.fragments.StationFragment;
import com.pstech.hydmetro.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MetroMapFragment.OnFragmentInteractionListener,
        SearchRouteFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        StationFragment.OnFragmentInteractionListener  {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.navigation) BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onBottomNavigationItemSelected(item);
            }
        });

        fragmentManager.beginTransaction().
                replace(R.id.main_content, SearchRouteFragment.newInstance("param1", "param2")).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean onBottomNavigationItemSelected(@NonNull MenuItem item) {
        AppFragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = HomeFragment.newInstance("param1", "param2");
                break;
            case R.id.nav_search_route:
                fragment = SearchRouteFragment.newInstance("param1", "param2");
                break;
            case R.id.nav_map:
                fragment = MetroMapFragment.newInstance("param1", "param2");
                break;
            case R.id.nav_stations:
                fragment = StationFragment.newInstance("param1", "param2");
                break;
        }
        fragmentManager.beginTransaction().
                replace(R.id.main_content, fragment).commit();
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottomNavigation view item clicks here.
        int id = item.getItemId();
        AppFragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = HomeFragment.newInstance("param1", "param2");
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        } else if (id == R.id.nav_search_route) {
            fragment = SearchRouteFragment.newInstance("param1", "param2");
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        } else if (id == R.id.nav_stations) {
            fragment = StationFragment.newInstance("param1", "param2");
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        } else if (id == R.id.nav_map) {
            fragment = MetroMapFragment.newInstance("param1", "param2");
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        } else if (id == R.id.nav_about) {
            fragment = AboutFragment.newInstance("param1", "param2");
            fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();
        } else if (id == R.id.nav_news) {

        } else if (id == R.id.nav_stories) {

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, AppConstants.SHARE_TXT);
            sendIntent.setType("text/html");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        } else if (id == R.id.nav_rate) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        } else if (id == R.id.nav_feedback) {

            String emailId = getResources().getString(R.string.emailId);
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", emailId, null));
            String[] addresses = {emailId};
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, addresses); // String[] addresses
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
