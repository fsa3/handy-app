package is.hi.handy_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private SearchView mSearchView;
    private MenuItem mSearchMenu;
    private Boolean mSearchVisible = true;
    private String mSearchHint;
    private SearchView.OnQueryTextListener mSearchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HandymenFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_handymen);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_advertisements:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdvertisementsFragment()).commit();
                break;
            case R.id.nav_handymen:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HandymenFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
}