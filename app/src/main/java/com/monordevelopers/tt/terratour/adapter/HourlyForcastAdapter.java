package com.monordevelopers.tt.terratour.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.weatherPojo.hourlyforecastpojo.HourlyForecast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HourlyForcastAdapter extends RecyclerView.Adapter<HourlyForcastAdapter.ViewHolder> {
    ArrayList<HourlyForecast> hourlyForecasts;
    Context mContext;

    public HourlyForcastAdapter(ArrayList<HourlyForecast> hourlyForecasts) {
        this.hourlyForecasts = hourlyForecasts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.weather_hourly_forecast_row,parent,false );
        return new ViewHolder( v );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourlyForecast hourlyForecast =hourlyForecasts.get( position );
        try{
            holder.time.setText( hourlyForecast.getFCTTIME().getCivil() );
            holder.temp.setText( hourlyForecast.getTemp().getMetric()+(char) 0x00B0+"C" );
            Picasso.with(mContext)
                    .load(hourlyForecast.getIconUrl())
                    .into(holder.condition);
        }catch (NullPointerException e){}
    }

    @Override
    public int getItemCount() {
        try {
            return hourlyForecasts.size();
        }catch (NullPointerException e){}
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView time;
        public TextView temp;
        ImageView condition ;

        public ViewHolder(View v) {
            super(v);
            time = (TextView) v.findViewById( R.id.hourly_time_TV );
            temp = (TextView) v.findViewById( R.id.hourly_Temp_TV );
            condition=(ImageView) v.findViewById( R.id.hourly_condition_imageView );
        }
    }

}
