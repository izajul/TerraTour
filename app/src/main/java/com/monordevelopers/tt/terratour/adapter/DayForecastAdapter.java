package com.monordevelopers.tt.terratour.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.weatherPojo.dayforcastpojo.Forecastday_;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DayForecastAdapter extends RecyclerView.Adapter<DayForecastAdapter.ViewHolder>{
    ArrayList<Forecastday_> hourlyForecasts;
    Context mContext;

    public DayForecastAdapter(ArrayList<Forecastday_> hourlyForecasts) {
        this.hourlyForecasts = hourlyForecasts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from( mContext ).inflate( R.layout.weather_day_forecast_row,parent,false );
        return new ViewHolder( v );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Forecastday_ forecastday = hourlyForecasts.get( position );
        try{
            holder.day.setText( forecastday.getDate().getWeekday() );
            holder.high.setText( forecastday.getHigh().getCelsius()+(char) 0x00B0+"C" );
            holder.low.setText( forecastday.getLow().getCelsius()+(char) 0x00B0+"C" );
            Picasso.with(mContext)
                    .load(forecastday.getIconUrl())
                    .into(holder.img);
        }catch (NullPointerException e){}
    }

    @Override
    public int getItemCount() {
        return hourlyForecasts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day , high,low;ImageView img;

        public ViewHolder(View v) {
            super(v);
            day = (TextView) v.findViewById( R.id.week_day_show_TV );
            high = (TextView) v.findViewById( R.id.week_day_High_Temp_TV );
            low = (TextView) v.findViewById( R.id.week_day_Low_Temp_TV );
            img = (ImageView) v.findViewById( R.id.show_week_day_condition_IM );
        }

    }
}
