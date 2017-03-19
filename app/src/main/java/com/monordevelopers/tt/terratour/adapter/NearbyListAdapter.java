package com.monordevelopers.tt.terratour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.nearbypojo.Result;

import java.util.ArrayList;

public class NearbyListAdapter extends ArrayAdapter {
    ArrayList<Result>results;
    Context context;
    public NearbyListAdapter(Context context,ArrayList<Result> results) {
        super( context, R.layout.nearby_list_view_row, results );
        this.results = results;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Result result = results.get( position );
        convertView = LayoutInflater.from( context ).inflate( R.layout.nearby_list_view_row,parent,false );

        TextView name = (TextView) convertView.findViewById( R.id.nearby_location_name_TV );
        TextView openNrating = (TextView) convertView.findViewById( R.id.nearby_location_openclose_rating_TV );
        TextView address = (TextView) convertView.findViewById( R.id.nearby_location_address_TV );

        try {
            name.setText( result.getName() );
            String openORclose = result.getOpeningHours().getOpenNow() ? "Open Now" : "Close Now";
            openNrating.setText(
                    "Rating:" + result.getRating() + "," + openORclose
            );
            address.setText( result.getVicinity() );
        }catch (NullPointerException e){}

        return convertView;
    }
}
