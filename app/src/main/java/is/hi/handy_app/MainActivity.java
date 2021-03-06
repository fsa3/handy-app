package is.hi.handy_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pusher.pushnotifications.PushNotifications;

import is.hi.handy_app.Fragments.AdvertisementsFragment;
import is.hi.handy_app.Fragments.HandymenFragment;
import is.hi.handy_app.Fragments.LoginFragment;
import is.hi.handy_app.Fragments.MyHandyProfileFragment;
import is.hi.handy_app.Fragments.MyMessagesFragment;
import is.hi.handy_app.Fragments.MyProfileFragment;
import is.hi.handy_app.Services.UserService;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String SHARED_PREFS = "handy-shared-prefs";

    private UserService mUserService;

    private TextView mNavTitle;
    private TextView mNavSubtitle;
    private MenuItem mSignIn;
    private MenuItem mMyProfile;
    private MenuItem mMyMessages;
    private MenuItem mSignOut;

    private FrameLayout mFragmentContainer;

    private DrawerLayout mDrawer;

    public NavigationView mNavigationView;
    private SearchView mSearchView;
    private MenuItem mSearchMenu;
    private Boolean mSearchVisible = true;
    private String mSearchHint;
    private SearchView.OnQueryTextListener mSearchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PushNotifications.start(getApplicationContext(), "0a915ad4-4103-4743-96ed-c2d40f7bbe1c");

        mUserService = new UserService(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentContainer = findViewById(R.id.fragment_container);

        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View navHeader = mNavigationView.getHeaderView(0);
        mNavTitle = navHeader.findViewById(R.id.nav_header_title);
        mNavSubtitle = navHeader.findViewById(R.id.nav_header_subtitle);

        Menu navMenu = mNavigationView.getMenu();
        mSignIn = navMenu.findItem(R.id.nav_login);
        mMyProfile = navMenu.findItem(R.id.nav_my_profile);
        mMyMessages = navMenu.findItem(R.id.nav_my_messages);
        mSignOut = navMenu.findItem(R.id.nav_logout);
        resetMenu();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HandymenFragment()).commit();
        mNavigationView.setCheckedItem(R.id.nav_handymen);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_advertisements:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdvertisementsFragment()).commit();
                break;
            case R.id.nav_handymen:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HandymenFragment()).commit();
                break;
            case R.id.nav_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                break;
            case R.id.nav_logout:
                mUserService.logout();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HandymenFragment()).commit();
                this.resetMenu();
                Snackbar snackbar = Snackbar.make(mFragmentContainer, "Successfully signed out", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
            case R.id.nav_my_profile:
                if (mUserService.getIsHandyUserLoggedIn()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyHandyProfileFragment()).commit();
                }
                else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyProfileFragment()).commit();
                }
                break;
            case R.id.nav_my_messages:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyMessagesFragment()).commit();
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        mSearchMenu = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchMenu.getActionView();

        if (!mSearchVisible) {
            mSearchMenu.collapseActionView();
            mSearchMenu.setVisible(false);
            mSearchView.setVisibility(View.GONE);
        }

        if (mSearchHint != null && mSearchListener != null) {
            mSearchView.setQueryHint(mSearchHint);
            mSearchView.setOnQueryTextListener(mSearchListener);
        }

        return super.onCreateOptionsMenu(menu);
    }

    public void setSearch(String hint, SearchView.OnQueryTextListener listener) {
        mSearchHint = hint;
        mSearchListener = listener;
        if (mSearchView != null && mSearchMenu != null) {
            mSearchView.setQueryHint(mSearchHint);
            mSearchView.setOnQueryTextListener(mSearchListener);
            mSearchMenu.setVisible(true);
            mSearchView.setVisibility(View.VISIBLE);
        }
    }

    public void hideSearch() {
        mSearchVisible = false;
        if (mSearchView != null && mSearchMenu != null) {
            mSearchMenu.collapseActionView();
            mSearchMenu.setVisible(false);
            mSearchView.setVisibility(View.GONE);
        }
    }

    public void resetMenu() {
        PushNotifications.clearDeviceInterests();
        if (mUserService.isUserLoggedIn()) {
            mNavTitle.setText(mUserService.getLoggedInUserName());
            mNavSubtitle.setText(mUserService.getLoggedInUserEmail());

            mSignIn.setVisible(false);
            mMyProfile.setVisible(true);
            mMyMessages.setVisible(true);
            mSignOut.setVisible(true);

            if (mUserService.getIsHandyUserLoggedIn()) {
                String trade = mUserService.getLoggedInUserTrade();
                PushNotifications.addDeviceInterest(trade);
                PushNotifications.addDeviceInterest(mUserService.getLoggedInUserEmail());
            } else {
                PushNotifications.clearDeviceInterests();
            }
        } else {
            mNavTitle.setText(getResources().getString(R.string.header_title));
            mNavSubtitle.setText(getResources().getString(R.string.header_subtitle));

            mSignIn.setVisible(true);
            mMyProfile.setVisible(false);
            mMyMessages.setVisible(false);
            mSignOut.setVisible(false);
        }
    }
}