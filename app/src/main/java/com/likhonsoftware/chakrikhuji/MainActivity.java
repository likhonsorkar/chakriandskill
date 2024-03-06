package com.likhonsoftware.chakrikhuji;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ImageView imageMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Navagation Drawar------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        imageMenu = findViewById(R.id.imageMenu);
        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Drawar click event
        // Drawer item Click event ------
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.dvlinkedin){
                    String url = "https://bd.linkedin.com/in/likhonsorkar";
                    CustomTabsIntent intent = new CustomTabsIntent.Builder()
                            .build();
                    intent.launchUrl(MainActivity.this, Uri.parse(url));
                  drawerLayout.closeDrawers();
                }else if(item.getItemId() == R.id.dvfacebook){
                    String url = "https://facebook.com/wdlikhonsorkar";
                    CustomTabsIntent intent = new CustomTabsIntent.Builder()
                            .build();
                    intent.launchUrl(MainActivity.this, Uri.parse(url));
                  drawerLayout.closeDrawers();
                }
                return false;
            }
        });
        //------------------------------

        // ------------------------
        // App Bar Click Event
        imageMenu = findViewById(R.id.imageMenu);

        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code Here
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // -----------------------------
        // Fragement Code
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentcontainer, new home_fragment());
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        // Check if the current fragment is the home fragment
        if (getSupportFragmentManager().findFragmentById(R.id.fragmentcontainer) instanceof home_fragment) {
            super.onBackPressed(); // Execute default back button behavior
        } else {
            // Navigate back to the home fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentcontainer, new home_fragment())
                    .commit();
        }
    }

}