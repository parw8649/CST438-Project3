package com.example.boardgamesocial;

import static com.example.boardgamesocial.API.RetrofitClient.getObjectList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardgamesocial.DataClasses.Post;
import com.example.boardgamesocial.DataViews.Adapters.DataClsAdapter;
import com.example.boardgamesocial.DataViews.Adapters.ViewHolders.PostVH;
import com.example.boardgamesocial.DataViews.DataClsVM;
import com.example.boardgamesocial.databinding.FragmentHomePostBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class HomePostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentHomePostBinding binding;
    private FloatingActionButton fab;

    public HomePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * returns a new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePostFragment newInstance(String param1, String param2) {
        HomePostFragment fragment = new HomePostFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomePostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.postFeed_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        List<Post> posts = new ArrayList<>();
        DataClsAdapter<Post, PostVH> dataClsAdapter = new DataClsAdapter<>(posts, PostVH.class);
        recyclerView.setAdapter(dataClsAdapter);
        DataClsVM<Post> dataClsVM = new DataClsVM<>(Post.class, new HashMap<>());
        dataClsVM.getLiveData().observe(getViewLifecycleOwner(), res -> {
            posts.addAll(getObjectList((JsonArray) res, Post.class));
            dataClsAdapter.notifyDataSetChanged();
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}