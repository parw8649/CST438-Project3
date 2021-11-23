package com.example.boardgamesocial.MainApp.Fragments;

import static com.example.boardgamesocial.API.RetrofitClient.getObject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.boardgamesocial.API.RetrofitClient;
import com.example.boardgamesocial.Commons.Utils;
import com.example.boardgamesocial.DataClasses.Event;
import com.example.boardgamesocial.DataClasses.HostedGame;
import com.example.boardgamesocial.R;

import java.util.HashMap;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment {

    public static final String TAG = "AddEventFragment";

    private EditText etEventName;
    private EditText etEventDateTime;
    private EditText etEventDescription;
    private EditText etMaxPlayer;
    private EditText etDescription;
    private EditText etImageUrl;
    private EditText etOverallPlayCount;
    private CheckBox cxIsGamePrivate;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
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
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEventName = view.findViewById(R.id.et_event_name);
        etEventDateTime = view.findViewById(R.id.et_event_date_time);
        etEventDescription = view.findViewById(R.id.et_event_description);

        Button btnSaveEvent = view.findViewById(R.id.btn_save_event);

        btnSaveEvent.setOnClickListener(v -> {

            String eventName = Objects.nonNull(etEventName) ? etEventName.getText().toString() : null;
            String eventDateTime = Objects.nonNull(etEventDateTime) ? etEventDateTime.getText().toString() : null;
            String eventDescription = Objects.nonNull(etEventDescription) ? etEventDescription.getText().toString() : null;

            //TODO: Need to add date picker at FE
            eventDateTime = String.valueOf(java.time.Clock.systemUTC().instant());

            addEvent(new Event(eventName, Utils.getUserId()), eventDateTime, eventDescription);
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void addEvent(Event event, String eventDateTime, String eventDescription) {

        Log.i(TAG, event.toString());

        RetrofitClient retrofitClient = RetrofitClient.getClient();

        //Call to save event initially with event name & host user id
        retrofitClient.postCall(Event.class, event).subscribe(eventJson -> {
            Event addedEvent = getObject(eventJson, Event.class);
            if (Objects.nonNull(addedEvent)) {
                Log.i(TAG, addedEvent.toString());
                Log.i(TAG, "Event added successfully");

                HostedGame hostedGame = new HostedGame(addedEvent.getId(), 103, 10);

                //Call to add hosted game in db for the event.
                retrofitClient.postCall(HostedGame.class, hostedGame).subscribe(hostedGameJson -> {
                    HostedGame addedHostedGame = getObject(hostedGameJson, HostedGame.class);
                    if (Objects.nonNull(addedHostedGame)) {
                        Log.i(TAG, addedHostedGame.getId() +" : "+ addedHostedGame.getEventId());
                        Log.i(TAG, "HostedGame added successfully");

                        //Call to update event object with pending values and map hosted games to it.
                        retrofitClient.putCall(Event.class, new HashMap<String, String>() {{
                            put("id", String.valueOf(addedEvent.getId()));
                            put("name", addedEvent.getName());
                            put("hostUserId", String.valueOf(addedEvent.getHostUserId()));
                            put("dateTime", eventDateTime);
                            put("description", eventDescription);
                            put("hostedGames", String.valueOf(addedHostedGame.getId()));
                        }}).subscribe(updatedEventJson -> {
                            Event updatedEvent = getObject(updatedEventJson, Event.class);
                            if (Objects.nonNull(updatedEvent)) {
                                Log.i(TAG, updatedEvent.toString());
                                Log.i(TAG, "Event updated successfully");
                            } else
                                Log.e(TAG, "Unable to update event at the moment");
                        });
                    } else
                        Log.e(TAG, "Unable to add hosted game at the moment!");
                });

            } else
                Log.e(TAG, "Unable to add event at the moment!");
        });
    }
}