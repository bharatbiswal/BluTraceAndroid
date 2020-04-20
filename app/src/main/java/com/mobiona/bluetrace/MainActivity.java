package com.mobiona.bluetrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavController navController= Navigation.findNavController(this,R.id.mainNavigationFragment);
        BottomNavigationView bottomNav = findViewById(R.id.navigationView);
        NavigationUI.setupWithNavController(bottomNav,navController);

    }
}
