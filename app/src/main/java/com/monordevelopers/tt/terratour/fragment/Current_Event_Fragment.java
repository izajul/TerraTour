package com.monordevelopers.tt.terratour.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.activity.Event_Expense;
import com.monordevelopers.tt.terratour.adapter.EventListViewAdapter;
import com.monordevelopers.tt.terratour.database.EventListDBManager;
import com.monordevelopers.tt.terratour.model.EventListModel;

import java.util.ArrayList;

public class Current_Event_Fragment extends Fragment {
    SharedPreferences sharedPreferences;
    EventListDBManager eventListDBManager;
    ArrayList<EventListModel>events;

    public Current_Event_Fragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_current__event, container, false );
        ListView listView = (ListView) view.findViewById( R.id.currentEventList );
        eventListDBManager = new EventListDBManager( inflater.getContext() );
        sharedPreferences =  this.getActivity().getSharedPreferences( "terratour", Context.MODE_PRIVATE );
        events = eventListDBManager.getAllEventByUid( sharedPreferences.getInt( "id",0 ) );

        EventListViewAdapter eventListViewAdapter = new EventListViewAdapter( inflater.getContext(),events);
        listView.setAdapter( eventListViewAdapter );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventListModel eventListModel =events.get( position );
                int listId = eventListModel.getId();
                startActivity( new Intent( getActivity(), Event_Expense.class ).putExtra( "id",listId ));
                getActivity().finish();
            }
        } );
        return view;
    }

}
