package com.example.boardgamesocial.MainApp.Fragments;

import static com.example.boardgamesocial.API.RetrofitClient.getObjectList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardgamesocial.API.RetrofitClient;
import com.example.boardgamesocial.Commons.Utils;
import com.example.boardgamesocial.DataClasses.Game;
import com.example.boardgamesocial.DataClasses.Relationships.GameToUser;
import com.example.boardgamesocial.DataViews.Adapters.DataClsAdapter;
import com.example.boardgamesocial.DataViews.Adapters.ViewHolders.GameVH;
import com.example.boardgamesocial.DataViews.DataClsVM;
import com.example.boardgamesocial.R;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserGamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserGamesFragment extends Fragment implements DataClsAdapter.OnItemListener {

    private RecyclerView recyclerView;
    private List<Game> games;

    private List<GameToUser> gameToUserList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserGamesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserGamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserGamesFragment newInstance(String param1, String param2) {
        UserGamesFragment fragment = new UserGamesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Utils.checkForUser(requireContext());
        Log.i("GamesCollection - User Token: ", String.valueOf(Utils.getUserId()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_games, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.userGames_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager((this.getContext())));

        DataClsAdapter<Game, GameVH> dataClsAdapter = new DataClsAdapter<>(
                this,
                Game.class,
                getActivity(),
                R.layout.game_item);
        recyclerView.setAdapter(dataClsAdapter);

        DataClsVM dataClsVM = DataClsVM.getInstance();
        dataClsVM.getMediatorLiveData(fetchUserSpecificGames(), Game.class)
                .observe(getViewLifecycleOwner(), dataClsAdapter::addNewObjects);
    }

    private Observable<JsonArray> fetchUserSpecificGames() {

        gameToUserList = new ArrayList<>();

        RetrofitClient.getClient().getCall(GameToUser.class, new HashMap<String, String>() {{
            put("userId", String.valueOf(Utils.getUserId()));
        }}).blockingSubscribe(gameToUserJson -> gameToUserList = getObjectList(gameToUserJson, GameToUser.class));

        JsonArray jsonArray = new JsonArray();

        gameToUserList.forEach(gameToUser -> RetrofitClient.getClient().getCall(Game.class, new HashMap<String, String>() {{
            put("id", String.valueOf(gameToUser.getGameId()));
        }}).blockingSubscribe(j -> {
            if(!j.isEmpty())
                jsonArray.add(j.get(0));
        }));

        return Observable.fromArray(jsonArray);
    }

    @Override
    public void onItemClick(Bundle contextBundle) {
        Toast.makeText(getContext(),"User Game clicked", Toast.LENGTH_LONG).show();
    }
}