package com.example.boardgamesocial;

import static com.example.boardgamesocial.API.RetrofitClient.getObjectList;

import android.os.Bundle;

import com.example.boardgamesocial.API.HeaderInterceptor;
import com.example.boardgamesocial.API.RetrofitClient;
import com.example.boardgamesocial.DataClasses.Game;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.boardgamesocial.databinding.ActivityLoginAndSignUpBinding;
import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAndSignUpActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoginAndSignUpBinding binding;

    private RetrofitClient retrofitClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginAndSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofitClient = RetrofitClient.getClient();

        // This token needs to be acquired with login request
        HeaderInterceptor.token.setToken("e4a36ccc86bc71b7e78c5d42bbd3109ab4764af1");
        retrofitClient.getCall(Game.class, new HashMap<String, String>(){{
            put("gameTitle", "7 Wonders Duel");
        }}).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    List<Game> list = getObjectList(response.body(), Game.class);
                    Log.i("Game", list.get(0).getGameTitle());
                } else {
                    new Exception("Request failed, code: " + response.code()).printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_app);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_app);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}