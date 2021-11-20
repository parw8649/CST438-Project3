package com.example.boardgamesocial.MainApp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardgamesocial.API.RetrofitClient;
import com.example.boardgamesocial.DataClasses.Event;
import com.example.boardgamesocial.DataViews.Adapters.DataClsAdapter;
import com.example.boardgamesocial.DataViews.Adapters.ViewHolders.EventVH;
import com.example.boardgamesocial.DataViews.DataClsVM;
import com.example.boardgamesocial.R;
import com.example.boardgamesocial.databinding.FragmentEventsBinding;

import java.util.HashMap;

public class EventsFragment extends Fragment implements DataClsAdapter.OnItemListener {

    private FragmentEventsBinding binding;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.eventFeed_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        DataClsAdapter<Event, EventVH> dataClsAdapter = new DataClsAdapter<>(
                this,
                Event.class,
                getActivity(),
                R.layout.event_item);
        recyclerView.setAdapter(dataClsAdapter);

        /*RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(view);
        TextView tvEventDescription = childViewHolder.itemView.findViewById(R.id.cd_view_event_description);
        tvEventDescription.setVisibility(View.GONE);*/

        DataClsVM dataClsVM = DataClsVM.getInstance();
        dataClsVM.getMediatorLiveData(RetrofitClient.getClient().getCall(Event.class, new HashMap<>()), Event.class)
                .observe(getViewLifecycleOwner(), newEvents -> {
                    dataClsAdapter.getObjectList().addAll(newEvents);
                    dataClsAdapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {

        DataClsAdapter<Event, EventVH> dataClsAdapter = (DataClsAdapter<Event, EventVH>) recyclerView.getAdapter();
        assert dataClsAdapter != null;
        Event event = dataClsAdapter.getObjectList().get(position);

        Bundle args = new Bundle();
        args.putString(getString(R.string.key_event_name), event.getName());
        args.putString(getString(R.string.key_event_date_time), event.getDateTime().toString());
        args.putString(getString(R.string.key_event_description), event.getDescription());

        NavHostFragment.findNavController(EventsFragment.this)
                .navigate(R.id.action_EventsFragment_to_singleEventFragment, args);
        Toast.makeText(getContext(),"Event clicked", Toast.LENGTH_LONG).show();
    }

}