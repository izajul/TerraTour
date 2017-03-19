package com.monordevelopers.tt.terratour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.model.MomentsModel;

import java.util.ArrayList;

public class MomentListAdapter extends ArrayAdapter{
    ArrayList<MomentsModel>momentsModels;
    Context context;

    public MomentListAdapter(Context context, ArrayList<MomentsModel> momentsModels) {
        super( context, R.layout.moment_list_row, momentsModels );
        this.context = context;
        this.momentsModels = momentsModels;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MomentsModel momentsModel =momentsModels.get( position );
        convertView = LayoutInflater.from( context ).inflate( R.layout.moment_list_row,parent,false);

        TextView details = (TextView) convertView.findViewById( R.id.moment_description_TV );
        TextView time = (TextView) convertView.findViewById( R.id.moment_time_TV );
        details.setText( momentsModel.getMomentDetails().toString() );
        time.setText( momentsModel.getMomnentTime().toString() );
        return convertView;
    }
}
