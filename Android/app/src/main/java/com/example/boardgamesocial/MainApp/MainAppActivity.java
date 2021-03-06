package com.example.boardgamesocial.MainApp;

import static com.example.boardgamesocial.API.RetrofitClient.getObject;
import static com.example.boardgamesocial.Commons.Constants.fabOffsetInvisible;
import static com.example.boardgamesocial.Commons.Constants.fabOffsetVisible;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.boardgamesocial.API.RetrofitClient;
import com.example.boardgamesocial.Commons.Utils;
import com.example.boardgamesocial.DataClasses.User;
import com.example.boardgamesocial.MainApp.Fragments.EditIconFragment;
import com.example.boardgamesocial.databinding.ActivityMainAppBinding;
import com.example.boardgamesocial.LoginAndSignUp.LoginAndSignUpActivity;
import com.example.boardgamesocial.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class MainAppActivity extends AppCompatActivity {

    public static final String TAG = "MainAppActivity";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainAppBinding binding;
    private NavController navController;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_app);

        bottomAppBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bottomAppBar);

        fab = findViewById(R.id.bottom_app_bar_fab);
        // default fab onClick after login
        setFabOnClick(R.id.home_option);

        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_option:
                    navController.navigate(R.id.HomePostFragment);
                    fab.setVisibility(View.VISIBLE);
                    break;
                case R.id.events_option:
                    navController.navigate(R.id.eventsFragment);
                    fab.setVisibility(View.VISIBLE);
                    break;
                case R.id.games_option:
                    // this should direct to a fragment showing all games available in database
                    navController.navigate(R.id.gameCollectionFragment);
                    fab.setVisibility(View.VISIBLE);
                    break;
                case R.id.search_option:
                    navController.navigate(R.id.searchFragment);
                    fab.setVisibility(View.INVISIBLE);
                    break;
                case R.id.profile_option:
                    navController.navigate(R.id.profileFragment);
                    fab.setVisibility(View.INVISIBLE);
                    break;
                case R.id.logout_option:
                    userLogout();
                    break;
                case R.id.chat_option:
                    navController.navigate(R.id.chatFragment);
                    fab.setVisibility(View.INVISIBLE);
                    break;
            }
            setFabOnClick(item.getItemId());
            return true;
        });

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                int visibility = bundle.getInt("visibility");
                Log.i(TAG, "onFragmentResult: visibility: " + visibility);
                View view = findViewById(R.id.content).getRootView();
                setAppBarFab(view, visibility);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_app);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.navbar_options, menu);
        return true;
    }

    public static Intent getIntent(Context context) {
        System.out.println("Inside MainAppActivity getIntent!");
        return new Intent(context, MainAppActivity.class);
    }

    private void setFabOnClick(int fragmentId) {
        switch (fragmentId) {
            case R.id.home_option:
                fab.setOnClickListener(v -> {
                    setAppBarFab(v, View.INVISIBLE);
                    Log.i(TAG, "setFabOnClick: fab cradle margin: " + bottomAppBar.getFabCradleMargin());
                    Log.i(TAG, "setFabOnClick: fab vertical offset: " + bottomAppBar.getCradleVerticalOffset());
                    Log.i(TAG, "setFabOnClick: fab cradle corner radius: " + bottomAppBar.getFabCradleRoundedCornerRadius());
                    navController.navigate(R.id.addPostFragment);
                });
                return;
            case R.id.events_option:
                fab.setOnClickListener(v -> {
                    setAppBarFab(v, View.INVISIBLE);
                    navController.navigate(R.id.addEventFragment);
                });
                return;
            case R.id.games_option:
                fab.setOnClickListener(v -> {
                    setAppBarFab(v, View.INVISIBLE);
                    navController.navigate(R.id.addGameFragment);
                });
                return;
            case R.id.search_option:
                return;
            case R.id.profile_option:
                return;
        }
    }

    private void setAppBarFab(View view, int visibility) {
        if (visibility == View.VISIBLE) {
//            bottomAppBar.setCradleVerticalOffset(fabOffsetVisible);
            fab.setVisibility(View.VISIBLE);
        } else {
//            bottomAppBar.setCradleVerticalOffset(fabOffsetInvisible);
            fab.setVisibility(View.INVISIBLE);
        }
    }

    private void userLogout() {
        try {
            RetrofitClient.getClient().logoutCall(new HashMap<String, String>() {{
                put("user_id", Utils.getUserId().toString());
            }}).subscribe(jsonObject -> {
                runOnUiThread(() -> {
                    Toast.makeText(this,"Successful logout", Toast.LENGTH_SHORT).show();
                    Intent goToHomePostActivity = LoginAndSignUpActivity.getIntent(MainAppActivity.this);
                    startActivity(goToHomePostActivity);
                });
            }); // look at doOnError instead of try catch
        } catch (Exception e) {
            Toast.makeText(this,"Unable to logout", Toast.LENGTH_SHORT).show();
        }
    }

    public void setIcon(View view) {
        ImageView imageView = (ImageView) view;
        Toast.makeText(this, "setIcon: icon img: " + EditIconFragment.ICON_MAP.get(imageView.getTag()), Toast.LENGTH_LONG).show();
    }
}