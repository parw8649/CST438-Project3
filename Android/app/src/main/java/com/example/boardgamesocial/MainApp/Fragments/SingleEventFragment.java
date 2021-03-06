package com.example.boardgamesocial.MainApp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.boardgamesocial.DataClasses.Event;
import com.example.boardgamesocial.DataViews.Adapters.ViewHolders.EventVH;
import com.example.boardgamesocial.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleEventFragment extends Fragment {

    public static final String TAG = "SingleEventFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton fab;

    public SingleEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SingleEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleEventFragment newInstance(String param1, String param2) {
        SingleEventFragment fragment = new SingleEventFragment();
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
        return inflater.inflate(R.layout.fragment_single_event, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvEventName = view.findViewById(R.id.tv_single_event_title);
        TextView tvEventDateTime = view.findViewById(R.id.tv_single_event_date_time);
        TextView tvEventDescription = view.findViewById(R.id.tv_single_event_description);
        Button btnHostedGames = view.findViewById(R.id.btn_single_event_hosted_games);
        Button btnPeopleInEvent = view.findViewById(R.id.btn_single_event_people_in_event);

        //Retrieve the value
        assert getArguments() != null;
        Event event = (Event) getArguments().getSerializable(EventVH.EVENT_KEY);

        tvEventName.setText(event.getName());
        tvEventDateTime.setText(event.getDateTime().toString());
        tvEventDescription.setText(event.getDescription());

        btnHostedGames.setOnClickListener(v -> NavHostFragment.findNavController(SingleEventFragment.this)
                .navigate(R.id.action_singleEventFragment_to_hostedGamesFragment, getArguments()));

        btnPeopleInEvent.setOnClickListener(v -> NavHostFragment.findNavController(SingleEventFragment.this)
                .navigate(R.id.action_singleEventFragment_to_eventAttendeesFragment, getArguments()));

        fab = requireActivity().findViewById(R.id.bottom_app_bar_fab);
        fab.setVisibility(View.INVISIBLE);
    }
}