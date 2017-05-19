package com.monordevelopers.tt.terratour.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.model.EventListModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventListViewAdapter extends ArrayAdapter{

    ArrayList<EventListModel> events;
    Context context;

    public EventListViewAdapter(Context context,ArrayList<EventListModel> events) {
        super( context, R.layout.event_list_row, events );
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventListModel eventListModel = events.get( position );
        convertView = LayoutInflater.from( context ).inflate( R.layout.event_list_row,parent,false );

        TextView destination = (TextView) convertView.findViewById( R.id.destination_TV );
        TextView time = (TextView) convertView.findViewById( R.id.showDate_TV );
        TextView budget = (TextView) convertView.findViewById( R.id.totalBudget_TV );
        TextView day = (TextView) convertView.findViewById( R.id.showTotalDay_TV );
        TextView expend = (TextView) convertView.findViewById( R.id.totalExpned_TV );
        TextView event_condition_TextView = (TextView) convertView.findViewById( R.id.event_condition_textView );
        ImageView event_condition_ImageView = (ImageView) convertView.findViewById(R.id.event_condition_imageView);
        ProgressBar mProgress = (ProgressBar) convertView.findViewById( R.id.progressBar );


        String date = findDiffrentBetweenDate(eventListModel.getFromDate(),eventListModel.getToDate());
        int expenseParsen = calculateParsent(Integer.valueOf(eventListModel.getTotalExpense()),
                Integer.valueOf(eventListModel.getBudget()));
        if(expenseParsen>100 && expenseParsen<=200){
            mProgress.setProgress(expenseParsen-100);
            mProgress.setProgressDrawable(context.getResources().getDrawable(R.drawable.red_progress_drawable));
        }else if (expenseParsen>200 && expenseParsen<=300){
            mProgress.setProgress(expenseParsen-200);
            mProgress.setProgressDrawable(context.getResources().getDrawable(R.drawable.blue_progress_drawable));
        }else{
            mProgress.setProgress(expenseParsen);
            mProgress.setProgressDrawable(context.getResources().getDrawable(R.drawable.green_progress_drawable));
        }

        String cDatestring = new SimpleDateFormat("MM/dd/yy").format(new Date());
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy");

        //Date cDate,fDate,toDate = new Date();
        //Date fDate = new Date();
       try {
           Date  cDate = myFormat.parse(cDatestring);
           Date  fDate = myFormat.parse(eventListModel.getFromDate());
           Date  toDate = myFormat.parse(eventListModel.getToDate());
           if((cDate.getTime()<fDate.getTime())){
               event_condition_TextView.setText("Not Active");
               event_condition_ImageView.setImageResource(R.mipmap.not_active);
           }else if((cDate.getTime()>=fDate.getTime()) && (cDate.getTime()<=toDate.getTime())){
               event_condition_TextView.setText("");
               event_condition_ImageView.setImageResource(R.mipmap.active);
           }else{
               event_condition_TextView.setText("");
               event_condition_ImageView.setImageResource(R.mipmap.closed);
           }
       } catch (ParseException e) {e.printStackTrace();}

        destination.setText( eventListModel.getDestination() );
        budget.setText((char)0x09F3+ eventListModel.getBudget() );
        time.setText( eventListModel.getFromDate()+" to "+eventListModel.getToDate() );
        expend.setText( (char)0x09F3+""+Integer.valueOf(eventListModel.getTotalExpense()));
        day.setText( date );

        return convertView;
    }

    private int calculateParsent(int expenseAmount, int budget) {
        int paresent =expenseAmount*100;
        paresent=paresent/budget;
        return paresent;
    }

    private String findDiffrentBetweenDate(String fromDate, String toDate) {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        String inputString1 = fromDate;
        String inputString2 = toDate;

        Date date1 ;
        Date date2 ;
        long diff =0;
        try {
            date1 = myFormat.parse(inputString1);
            date2 = myFormat.parse(inputString2);
            diff = date2.getTime() - date1.getTime();
        } catch (ParseException e) {e.printStackTrace();}
        long oneDay = 1000 * 60 * 60 * 24;long dff = diff/oneDay;

        long year = dff / 365;
        long rest = dff % 365;
        long month = rest / 30;
        rest = rest % 30;
        long weeks = rest / 7;
        long days = rest % 7;
        return checkIsZero(year,month,weeks,days);
    }

    private String checkIsZero(long year, long month, long weeks, long days) {

        String returns = "";
        if (year == 0 && month==0 && weeks == 0){
            returns = days+"d";
        }else if (year == 0 && month==0 && weeks != 0){
            returns = weeks+"w "+days+"d";
        }
        else if (year == 0 && month!=0 && weeks == 0){
            returns = month+"m "+days+"d";
        }
        else if (year == 0 && month!=0 && weeks != 0){
            returns = month+"m "+weeks+"w "+days+"d";
        }
        else if (year != 0 && month==0 && weeks == 0){
            returns = year+"y "+days+"d";
        }
        else if (year != 0 && month==0 && weeks != 0){
            returns = year+"y "+weeks+"w "+days+"d";
        }
        else if (year != 0 && month!=0 && weeks == 0){
            returns = year+"y "+month+"m "+days+"d";
        }else if (year != 0 && month!=0 && weeks != 0){
            returns = year+"y "+month+"m "+weeks+"w "+days+"d";
        }
        return returns;
    }
}
