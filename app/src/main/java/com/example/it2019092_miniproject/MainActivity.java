package com.example.it2019092_miniproject;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.example.it2019092_miniproject.databinding.ActivityMainBinding;
import com.example.it2019092_miniproject.ui.booking.AllPackagesFragment;
import com.example.it2019092_miniproject.ui.home.HomeFragment;
import com.example.it2019092_miniproject.ui.login.LoginFragment;
import com.example.it2019092_miniproject.ui.register.RegisterFragment;
import com.example.it2019092_miniproject.ui.tour_package.NewPackageFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        //adding the navigation manually
        getSupportFragmentManager().popBackStack();
        FragmentTransaction trans =getSupportFragmentManager().beginTransaction();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuID =item.getItemId();
                getSupportFragmentManager().popBackStack();
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

                if(menuID==R.id.nav_home){
                    HomeFragment fragment =new HomeFragment();
                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
                }
                else if(menuID ==R.id.nav_newPackage){
                    NewPackageFragment fragment =new NewPackageFragment();
                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
                }
                else if(menuID ==R.id.nav_login){
                    LoginFragment fragment =new LoginFragment();
                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
                }
                else if(menuID ==R.id.nav_register){
                    RegisterFragment fragment =new RegisterFragment();
                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
                }
                else if(menuID ==R.id.nav_allPacks){
                    AllPackagesFragment fragment =new AllPackagesFragment();
                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
                }
//                else if(menuID ==R.id.nav_profile){
//                    ProfileFragment fragment =new ProfileFragment();
//                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
//                }
//                else if(menuID ==R.id.nav_allPosts){
//                    AllPostsFragment fragment =new AllPostsFragment();
//                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
//                }
//                else if(menuID ==R.id.nav_add){
//                    AddpostFragment fragment =new AddpostFragment();
//                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
//                }
//                else if(menuID ==R.id.nav_myPosts){
//                    MyPostsFragment fragment =new MyPostsFragment();
//                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
//                }
//                else if(menuID ==R.id.nav_myJobs){
//                    MyJobsFragment fragment =new MyJobsFragment();
//                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
//                }
//                else if(menuID ==R.id.nav_floorPrice){
//                    FloorPriceFragment fragment =new FloorPriceFragment();
//                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
//                }
//                else if(menuID ==R.id.nav_adminViewUsers){
//                    AdminViewUsersFragment fragment =new AdminViewUsersFragment();
//                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
//                }
//
//                else if (menuID==R.id.nav_exit){
//                    finish();
//
//                }
//                else if (menuID==R.id.nav_logout){
//                    LoginFragment fragment = new LoginFragment();
//                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
//                    trans.addToBackStack(null);
//
//                    preference.SaveBool(getApplicationContext(),false,SharedPreference.LOGIN_STATUS);
//                    preference.SaveString(getApplicationContext(),null,SharedPreference.USER_TYPE);
//                    preference.SaveString(getApplicationContext(),null,SharedPreference.USER_NIC);
//
//                    Menu menu1 = navigationView.getMenu();
//                    MenuItem item1=menu1.findItem(R.id.nav_allPosts);
//                    item1.setVisible(false);
//                    item1=menu1.findItem(R.id.nav_home);
//                    item1.setVisible(false);
//                    item1=menu1.findItem(R.id.nav_add);
//                    item1.setVisible(false);
//                    item1=menu1.findItem(R.id.nav_myPosts);
//                    item1.setVisible(false);
//                    item1=menu1.findItem(R.id.nav_myJobs);
//                    item1.setVisible(false);
//                    item1=menu1.findItem(R.id.nav_logout);
//                    item1.setVisible(false);
//                    item1=menu1.findItem(R.id.nav_register);
//                    item1.setVisible(false);
//                    item1=menu1.findItem(R.id.nav_floorPrice);
//                    item1.setVisible(false);
//                    item1=menu1.findItem(R.id.nav_profile);
//                    item1.setVisible(false);
//                    item1=menu1.findItem(R.id.nav_adminViewUsers);
//                    item1.setVisible(false);
//
//                }
                trans.addToBackStack(null);
                trans.commit();
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}