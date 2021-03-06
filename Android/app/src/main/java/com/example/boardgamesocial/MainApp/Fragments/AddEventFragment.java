package com.example.boardgamesocial.MainApp.Fragments;

import static com.example.boardgamesocial.API.RetrofitClient.getObject;
import static com.example.boardgamesocial.API.RetrofitClient.getObjectList;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardgamesocial.API.RetrofitClient;
import com.example.boardgamesocial.Commons.Utils;
import com.example.boardgamesocial.DataClasses.Event;
import com.example.boardgamesocial.DataClasses.Game;
import com.example.boardgamesocial.DataClasses.HostedGame;
import com.example.boardgamesocial.DataClasses.Relationships.GameToUser;
import com.example.boardgamesocial.DataViews.Adapters.DataClsAdapter;
import com.example.boardgamesocial.DataViews.Adapters.DataClsAdapter.OnItemListener;
import com.example.boardgamesocial.DataViews.Adapters.ViewHolders.GameVH;
import com.example.boardgamesocial.DataViews.DataClsVM;
import com.example.boardgamesocial.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import io.reactivex.Observable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment implements OnItemListener {

    public static final String TAG = "AddEventFragment";

    private final Gson gson = new GsonBuilder().create();

    private List<GameToUser> gameToUserList;

    private EditText etEventName;
    private EditText etEventDateTime;
    private EditText etEventDescription;
    private int mYear, mMonth, mDay, mHour, mMinute, mSeconds;

    private RecyclerView recyclerView;

    private Set<Integer> gameIdList = new HashSet<>();

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
        ImageButton ibtnSetDate = view.findViewById(R.id.btn_set_date);

        Button btnSaveEvent = view.findViewById(R.id.btn_save_event);

        recyclerView = view.findViewById(R.id.hostedGameCK_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        DataClsAdapter<Game, GameVH> dataClsAdapter = new DataClsAdapter<>(
                this,
                Game.class,
                getActivity(),
                R.layout.hosted_game_item);
        recyclerView.setAdapter(dataClsAdapter);

        DataClsVM dataClsVM = DataClsVM.getInstance();
        dataClsVM.getMediatorLiveData(fetchNonPrivateGames(), Game.class, true)
                .observe(getViewLifecycleOwner(), dataClsAdapter::addNewObjects);

        AtomicReference<StringBuilder> sbEventDateTime = new AtomicReference<>();

        ibtnSetDate.setOnClickListener(s -> {

            sbEventDateTime.set(new StringBuilder());

            //Log.i(TAG, "Acceptable time format: " + java.time.Clock.systemUTC().instant());

            //StringBuilder dateTime = new StringBuilder();

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            mSeconds = c.get(Calendar.SECOND);
            //mMilliseconds = c.get(Calendar.MILLISECOND);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                    (view12, hourOfDay, minute) -> {

                        sbEventDateTime.get()
                                .append(" | ")
                                .append(hourOfDay).append(":")
                                .append(minute).append(":")
                                .append(mSeconds);

                        etEventDateTime.setText(sbEventDateTime.toString());
                    }, mHour, mMinute, true);

            timePickerDialog.show();

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view1, year, monthOfYear, dayOfMonth) -> {

                        sbEventDateTime.get()
                                .append(year).append("-")
                                .append(monthOfYear + 1).append("-")
                                .append(dayOfMonth);

                        etEventDateTime.setText(sbEventDateTime.toString());
                    },
                    mYear, mMonth, mDay);

            datePickerDialog.show();

            //Log.i(TAG, "-------------------------> dateTime: " + tvEventDateTime.getText().toString());
        });

        btnSaveEvent.setOnClickListener(v -> {

            etEventDateTime.setText(sbEventDateTime.toString());
            Log.i(TAG, "-----------------------> Updated Event Date Time: " + etEventDateTime.getText().toString());

            String eventName = Objects.nonNull(etEventName) ? etEventName.getText().toString() : null;

            String eventDateTime = Objects.nonNull(etEventDateTime)
                    ? etEventDateTime.getText().toString()
                    : String.valueOf(java.time.Clock.systemUTC().instant());

            String eventDescription = Objects.nonNull(etEventDescription) ? etEventDescription.getText().toString() : null;

            //Need to add date picker at FE
            eventDateTime = String.valueOf(java.time.Clock.systemUTC().instant());

            addEvent(new Event(eventName, Utils.getUserId()), eventDateTime, eventDescription);
            //requireActivity().getSupportFragmentManager().popBackStack();
            NavHostFragment
                    .findNavController(AddEventFragment.this)
                    .navigate(R.id.action_addEventFragment_to_eventsFragment);
        });
    }

    @Override
    public void onItemClick(Bundle contextBundle) {
        Game game = (Game) contextBundle.getSerializable(GameVH.GAME_KEY);
        DataClsAdapter<Game, GameVH> dataClsAdapter = (DataClsAdapter<Game, GameVH>) recyclerView.getAdapter();
        assert dataClsAdapter != null;

        RecyclerView.ViewHolder viewHolder = recyclerView
                .findViewHolderForAdapterPosition(dataClsAdapter
                        .getObjectList()
                        .indexOf(game));
        assert viewHolder != null;

        CheckBox checkBox = viewHolder.itemView.findViewById(R.id.ck_hosted_game);
        checkBox.setVisibility(View.INVISIBLE);
        RelativeLayout relativeLayout = viewHolder.itemView.findViewById(R.id.hosted_game_card);

        if(Objects.nonNull(game)) {
            Log.i(TAG, game.getId().toString());
            if(checkBox.isChecked()) {
                checkBox.setChecked(false);
                gameIdList.remove(game.getId());
                Log.i(TAG, "Removed - GameId: " + game.getId().toString());
                relativeLayout.setBackgroundColor(Color.parseColor("#5EFF9B44"));
            } else {
                checkBox.setChecked(true);
                gameIdList.add(game.getId());
                Log.i(TAG, "Added - GameId: " + game.getId().toString());
                relativeLayout.setBackgroundColor(Color.parseColor("#60000000"));
            }
        }
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

                if(!gameIdList.isEmpty()) {

                    for (Integer gameId : gameIdList) {
                        HostedGame hostedGame = new HostedGame(addedEvent.getId(), gameId, 10);

                        //Call to add hosted game in db for the event.
                        retrofitClient.postCall(HostedGame.class, hostedGame).subscribe(hostedGameJson -> {
                            HostedGame addedHostedGame = getObject(hostedGameJson, HostedGame.class);
                            if (Objects.nonNull(addedHostedGame)) {
                                Log.i(TAG, addedHostedGame.getId() + " : " + addedHostedGame.getEventId());
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
                    }
                } else
                    Log.i(TAG, "GameIdList is empty!");

            } else
                Log.e(TAG, "Unable to add event at the moment!");
        });
    }

    //Method to remove private games from the games collection
    private Observable<JsonArray> fetchNonPrivateGames() {

        Log.i(TAG, "Inside fetchNonPrivateGames");

        gameToUserList = new ArrayList<>();

        RetrofitClient.getClient().getCall(GameToUser.class, new HashMap<String, String>() {{
            put("private", "True");
        }}).blockingSubscribe(gameToUserJson -> gameToUserList = getObjectList(gameToUserJson, GameToUser.class));

        List<Integer> gameIdList = gameToUserList.stream().map(GameToUser::getGameId).collect(Collectors.toList());

        JsonArray jsonArray = new JsonArray();

        RetrofitClient.getClient().getCall(Game.class, new HashMap<>()).blockingSubscribe(j -> {
            if(!j.isEmpty()) {

                List<Game> gameList = getObjectList(j, Game.class);

                if(Objects.nonNull(gameList) && !gameList.isEmpty()) {

                    gameList.forEach(game -> {
                        if(!gameIdList.contains(game.getId())) {
                            jsonArray.add(gson.toJsonTree(game));
                        }
                    });
                }
            }
        });

        return Observable.fromArray(jsonArray);
    }

    //-------------------------------------------update date---//
    /*private void updateDate() {
        mDateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("/")
                        .append(mMonth + 1).append("/")
                        .append(mYear).append(" "));
        showDialog(TIME_DIALOG_ID);
    }

    //-------------------------------------------update time---//
    public void updatetime() {
        mTimeDisplay.setText(
                new StringBuilder()
                        .append(pad(mhour)).append(":")
                        .append(pad(mminute)));
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);


        //Datepicker dialog generation

        private DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updateDate();
                    }
                };


        // Timepicker dialog generation
        private TimePickerDialog.OnTimeSetListener mTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mhour = hourOfDay;
                        mminute = minute;
                        updatetime();
                    }
                };

        @Override
        protected Dialog onCreateDialog(int id) {
            switch (id) {
                case DATE_DIALOG_ID:
                    return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth, mDay);

                case TIME_DIALOG_ID:
                    return new TimePickerDialog(this,
                            mTimeSetListener, mhour, mminute, false);

            }
            return null;
        }*/
}