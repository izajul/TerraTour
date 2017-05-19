package com.monordevelopers.tt.terratour.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.activity.Event_Expense;
import com.monordevelopers.tt.terratour.activity.UserProfileViewActivity;
import com.monordevelopers.tt.terratour.adapter.EventListViewAdapter;
import com.monordevelopers.tt.terratour.database.EventListDBManager;
import com.monordevelopers.tt.terratour.model.EventListModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.util.Locale.US;

public class Current_Event_Fragment extends Fragment {
    Context mContext;
    SharedPreferences sharedPreferences;
    EventListDBManager eventListDBManager;
    ArrayList<EventListModel>events;
    Dialog eventEditDialog ;
    EditText desti,budget,fDate,tDate;
    int eventId = 0;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    boolean trave_from_date_checked = false;
    boolean trave_to_date_checked = false;


    public Current_Event_Fragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = inflater.getContext();
        View view = inflater.inflate( R.layout.fragment_current__event, container, false );
        ListView listView = (ListView) view.findViewById( R.id.currentEventList );
        eventListDBManager = new EventListDBManager( inflater.getContext() );
        sharedPreferences =  this.getActivity().getSharedPreferences( "terratour", Context.MODE_PRIVATE );
        events = eventListDBManager.getAllEventByUid( sharedPreferences.getInt( "id",0 ) );

        eventEditDialog = new Dialog(inflater.getContext());
        eventEditDialog.setContentView(R.layout.add_event_dialog_layout);

        desti= (EditText) eventEditDialog.findViewById( R.id.travel_Destination_ET );
        budget=(EditText) eventEditDialog.findViewById( R.id.travel_Estimate_Budget_ET );
        fDate=(EditText) eventEditDialog.findViewById( R.id.travel_From_Date_ET );
        tDate=(EditText) eventEditDialog.findViewById( R.id.travel_To_Date_ET );

        setdate(fDate,tDate); // Method for Set From and To date While Click..

        Button updateEvent = (Button) eventEditDialog.findViewById( R.id.save_Event_BTN );
        updateEvent.setText("Update");
        updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEvnet(desti,budget,fDate,tDate,eventId);
            }
        });

        EventListViewAdapter eventListViewAdapter = new EventListViewAdapter( inflater.getContext(),events);
        listView.setAdapter( eventListViewAdapter );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventListModel eventListModel =events.get( position );
                Date cDate = new Date();
                String cDatestring = new SimpleDateFormat("MM/dd/yy").format(cDate);
                SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy");
                try {
                    desti.setText(eventListModel.getDestination());
                    budget.setText(eventListModel.getBudget());
                    fDate.setText(eventListModel.getFromDate());
                    tDate.setText(eventListModel.getToDate());

                    eventId = eventListModel.getId();
                    String date1string = eventListModel.getFromDate();
                    Date date1 = myFormat.parse(date1string);
                    long date1long = date1.getTime();
                    Date date2 = myFormat.parse(cDatestring);
                    long date2long = date2.getTime();
                    if (date1long>date2long){
                        new AlertDialog.Builder(inflater.getContext())
                                .setMessage("Event Not Active Yet !!.. You Can Only Edit The Event")
                                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        eventEditDialog.setTitle("Update Event");
                                        eventEditDialog.show();
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }else {
                        int listId = eventListModel.getId();
                        startActivity( new Intent( getActivity(), Event_Expense.class ).putExtra( "id",listId ));
                        getActivity().finish();
                    }
                } catch (ParseException e) {e.printStackTrace();}

            }
        } );
        return view;
    }

    private void setdate(final EditText trave_from_date, final EditText trave_to_date){
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set( Calendar.YEAR, year );
                myCalendar.set( Calendar.MONTH, monthOfYear );
                myCalendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );

                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat( myFormat, US );
                if (trave_from_date_checked)
                    trave_from_date.setText( sdf.format( myCalendar.getTime() ) );
                if (trave_to_date_checked)
                    trave_to_date.setText( sdf.format( myCalendar.getTime() ) );
            }
        };
        trave_from_date.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar
                        .get( Calendar.YEAR ), myCalendar.get( Calendar.MONTH ),
                        myCalendar.get( Calendar.DAY_OF_MONTH ) );
                long l = System.currentTimeMillis()-1000;
                datePickerDialog.getDatePicker().setMinDate(l);
                datePickerDialog.show();

                trave_from_date_checked = true;
                trave_to_date_checked = false;
            }
        } );
        trave_to_date.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!trave_from_date.getText().toString().isEmpty()) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    long startDate =0;
                    try {
                        String dateString = trave_from_date.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
                        Date date = sdf.parse(dateString);
                        startDate = date.getTime();
                    } catch (ParseException e) {e.printStackTrace();}

                    datePickerDialog.getDatePicker().setMinDate(startDate);
                    datePickerDialog.show();
                    trave_from_date_checked = false;
                    trave_to_date_checked = true;
                }else{
                    new android.app.AlertDialog.Builder(mContext)
                            .setMessage("Please Set From Date First !! ")
                            .setCancelable(true)
                            .show();
                }
            }
        } );
    }

    private void updateEvnet(EditText desti, EditText budget, EditText from, EditText to,int id){
        if (evetFildCheck(
                desti.getText().toString(),
                budget.getText().toString(),
                from.getText().toString(),
                to.getText().toString()
        )){
            EventListModel eventListModel = new EventListModel(
                    desti.getText().toString(),
                    budget.getText().toString(),
                    from.getText().toString(),
                    to.getText().toString(),0,id,0
            );
            long row = eventListDBManager.UpdateEventByid(eventListModel);
            if (row>0){
                Toast.makeText( mContext, "Update Successed", Toast.LENGTH_SHORT ).show();
                eventEditDialog.dismiss();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
            }else{
                new android.app.AlertDialog.Builder(mContext)
                        .setTitle("Update Failed ! ")
                        .setMessage("You Must Fill All Fields")
                        .setCancelable(false)
                        .setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        } ).show();
            }
        }
    }

    private boolean evetFildCheck(String desti, String budget, String from_date, String to_date) {
        if (desti.isEmpty()) {
            //travel_desti.setError( "Empty ! " );
            return false;
        }
        if (budget.isEmpty()) {
            // trave_ludget.setError( "Empty ! " );
            return false;
        }
        if (from_date.isEmpty()) {
            // trave_from_date.setError( "Empty ! " );
            return false;
        }
        if (to_date.isEmpty()) {
            //trave_to_date.setError( "Empty ! " );
            return false;
        }
        return true;
    }

}
