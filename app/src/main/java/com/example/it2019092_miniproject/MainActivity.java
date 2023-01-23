package com.example.it2019092_miniproject;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.example.it2019092_miniproject.databinding.ActivityMainBinding;
import com.example.it2019092_miniproject.ui.booking.UserViewBookingsFragment;
import com.example.it2019092_miniproject.ui.home.HomeFragment;
import com.example.it2019092_miniproject.ui.login.LoginFragment;
import com.example.it2019092_miniproject.ui.register.RegisterFragment;
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
    boolean status= false;
    boolean register= false;
    String NIC;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
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

        //shared preference part
        SharedPreference preference= new SharedPreference();
        register =  preference.GetBoolean(getApplicationContext(),SharedPreference.REGISTER);
        status = preference.GetBoolean(getApplicationContext(),SharedPreference.LOGIN_STATUS);
        NIC=preference.GetString(getApplicationContext(),SharedPreference.USER_NIC);
        Temp.setNIC(NIC);

        //check register
        if (register){
            //check login
            if(status) {
//                hide();

                //moving to frag
                HomeFragment fragment = new HomeFragment();
                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                trans.addToBackStack(null);
                trans.commit();
            }
            else {
                //moving to frag
                LoginFragment fragment = new LoginFragment();
                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                trans.addToBackStack(null);
                trans.commit();
            }
        }
        else {
            //moving to frag
            RegisterFragment fragment = new RegisterFragment();
            trans.replace(R.id.nav_host_fragment_content_main,fragment);
            trans.addToBackStack(null);
            trans.commit();
        }


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
                else if(menuID ==R.id.nav_login){
                    LoginFragment fragment =new LoginFragment();
                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
                }
                else if(menuID ==R.id.nav_register){
                    RegisterFragment fragment =new RegisterFragment();
                    trans.replace(R.id.nav_host_fragment_content_main,fragment);
                }
                else if(menuID ==R.id.nav_userViewBookings){
                        UserViewBookingsFragment fragment = new UserViewBookingsFragment();
                        trans.replace(R.id.nav_host_fragment_content_main, fragment);
                }
                trans.addToBackStack(null);
                trans.commit();
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

//    public void hide(){
//        Menu menu = navigationView.getMenu();
//        MenuItem item;
//        item=menu.findItem(R.id.nav_login);
//        item.setVisible(false);
//    }
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
