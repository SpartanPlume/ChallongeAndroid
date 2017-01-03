package plume.spartan.challongeandroid.activities;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import plume.spartan.challongeandroid.R;
import plume.spartan.challongeandroid.adapters.NavDrawerAdapter;
import plume.spartan.challongeandroid.fragments.BracketPage;
import plume.spartan.challongeandroid.fragments.HomePage;
import plume.spartan.challongeandroid.fragments.LogInPage;
import plume.spartan.challongeandroid.fragments.ParticipantsPage;
import plume.spartan.challongeandroid.global.MyApplication;
import plume.spartan.challongeandroid.helpers.Keyboard;
import plume.spartan.challongeandroid.helpers.ReloadCurrentFragment;
import plume.spartan.challongeandroid.helpers.ShowFragment;
import plume.spartan.challongeandroid.store.Participant;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private static ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setTitle(getString(R.string.app_name));
        try {
            getSupportActionBar().setTitle("");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        toolbar.setOverflowIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_more_vert_white_24dp, null));

        initializeNavDrawer();
        invalidateOptionsMenu();

        if (savedInstanceState != null) {
            String fragment = savedInstanceState.getString("fragment");
            if (fragment != null) {
                switch (fragment) {
                    case HomePage.TAG:
                        ShowFragment.changeFragment(MainActivity.this, HomePage.newInstance(savedInstanceState.getInt("tab")), HomePage.TAG);
                        break;
                    case BracketPage.TAG:
                        ShowFragment.changeFragment(MainActivity.this, BracketPage.newInstance(), BracketPage.TAG);
                        break;
                    case ParticipantsPage.TAG:
                        ShowFragment.changeFragment(MainActivity.this, ParticipantsPage.newInstance(), ParticipantsPage.TAG);
                        break;
                    default:
                        ShowFragment.changeFragment(MainActivity.this, LogInPage.newInstance(), LogInPage.TAG);
                }
            } else {
                ShowFragment.changeFragment(MainActivity.this, LogInPage.newInstance(), LogInPage.TAG);
            }
        } else {
            ShowFragment.changeFragment(MainActivity.this, LogInPage.newInstance(), LogInPage.TAG);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbarTitle.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                ReloadCurrentFragment.execute(getSupportFragmentManager(), ((MyApplication) getApplicationContext()).getCurrentFragmentTag());
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Keyboard.hide(MainActivity.this);
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    private void initializeNavDrawer() {
        final String[] navMenuItems = getResources().getStringArray(R.array.nav_drawer_items);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer);

        ArrayList<String> navDrawerItems = new ArrayList<>();

        int i = 0;
        while (i < navMenuItems.length) {
            navDrawerItems.add(navMenuItems[i]);
            i++;
        }

        final NavDrawerAdapter adapter = new NavDrawerAdapter(this, navDrawerItems);
        mDrawerList.setOnItemClickListener(navigationWithNavDrawer);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                Keyboard.hide(MainActivity.this);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    private ListView.OnItemClickListener navigationWithNavDrawer = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mDrawerLayout.closeDrawers();
            String currentFragmentTag = ((MyApplication) getApplicationContext()).getCurrentFragmentTag();
            switch (i) {
                case 0:
                    ShowFragment.changeFragment(MainActivity.this, HomePage.newInstance(), HomePage.TAG);
                    break;
                case 1:
                    if (currentFragmentTag.equals(BracketPage.TAG)) {
                        ReloadCurrentFragment.execute(getSupportFragmentManager(), currentFragmentTag);
                    } else {
                        ShowFragment.changeFragment(MainActivity.this, BracketPage.newInstance(), BracketPage.TAG);
                    }
                    break;
                case 2:
                    if (currentFragmentTag.equals(ParticipantsPage.TAG)) {
                        ReloadCurrentFragment.execute(getSupportFragmentManager(), currentFragmentTag);
                    } else {
                        ShowFragment.changeFragment(MainActivity.this, ParticipantsPage.newInstance(), ParticipantsPage.TAG);
                    }
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
            }
        }
    };

    public void setOpenableDrawer(boolean isOpenableDrawer) {
        if (!isOpenableDrawer) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toolbar.setNavigationIcon(null);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String currentFragment = ((MyApplication) getApplicationContext()).getCurrentFragmentTag();
        outState.putString("fragment", currentFragment);
        System.out.println(currentFragment);
        if (currentFragment.equals(HomePage.TAG)) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(HomePage.TAG);
            if (fragment != null) {
                Bundle fragmentBundle = fragment.getArguments();
                if (fragmentBundle != null) {
                    outState.putInt("tab", fragmentBundle.getInt("tab"));
                }
            }
        }
        super.onSaveInstanceState(outState);
    }
}