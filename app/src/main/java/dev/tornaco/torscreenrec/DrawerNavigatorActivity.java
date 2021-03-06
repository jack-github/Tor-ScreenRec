package dev.tornaco.torscreenrec;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;

import java.util.List;

import dev.tornaco.torscreenrec.common.SharedExecutor;
import dev.tornaco.torscreenrec.pref.SettingsProvider;
import dev.tornaco.torscreenrec.ui.AboutFragment;
import dev.tornaco.torscreenrec.ui.ContainerHostActivity;
import dev.tornaco.torscreenrec.ui.FragmentController;
import dev.tornaco.torscreenrec.ui.ScreenCastFragment;
import dev.tornaco.torscreenrec.ui.ShopFragment;
import dev.tornaco.torscreenrec.ui.widget.RecordingButton;
import lombok.Getter;

public class DrawerNavigatorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int INDEX_SCREEN_CAST = 0;

    @Getter
    private FragmentController cardController;

    @Getter
    private RecordingButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        floatingActionButton = (RecordingButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupFragment();
    }

    protected void setupFragment() {
        final List<? extends Fragment> cards =
                ImmutableList.of(
                        ScreenCastFragment.create());
        cardController = new FragmentController(getSupportFragmentManager(), cards, R.id.container);
        cardController.setDefaultIndex(INDEX_SCREEN_CAST);
        cardController.setCurrent(INDEX_SCREEN_CAST);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String uName = SettingsProvider.get().getString(SettingsProvider.Key.USR_NAME);
        TextView textView = (TextView) findViewById(R.id.user_name);
        if (textView != null) {
            textView.setText(uName);
        }
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_cast:
                getCardController().setCurrent(INDEX_SCREEN_CAST);
                break;
            case R.id.nav_shop:
                SharedExecutor.runOnUIThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                       startActivity(ContainerHostActivity.getIntent(getApplicationContext(), ShopFragment.class));
                    }
                }, 300);
                break;
            case R.id.nav_about:
                SharedExecutor.runOnUIThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(ContainerHostActivity.getIntent(getApplicationContext(), AboutFragment.class));
                    }
                }, 300);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
