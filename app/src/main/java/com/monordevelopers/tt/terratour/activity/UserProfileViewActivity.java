package com.monordevelopers.tt.terratour.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.adapter.ViewPagerAdapter;
import com.monordevelopers.tt.terratour.database.EventListDBManager;
import com.monordevelopers.tt.terratour.database.UserProfileDBManager;
import com.monordevelopers.tt.terratour.fragment.Current_Event_Fragment;
import com.monordevelopers.tt.terratour.fragment.NearBy_Fragment;
import com.monordevelopers.tt.terratour.model.EventListModel;
import com.monordevelopers.tt.terratour.model.UserModel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Locale.US;

public class UserProfileViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EventListDBManager EDBManager;
    UserProfileDBManager mProfileBDManager;
    SharedPreferences sharedPreferences;
    ImageView profilepic;
    UserModel usermodel;
    TextView userName, phoneEmail;
    ViewPager viewPager;
    TabLayout tabLayout;
    private int[] tabIcons = {R.mipmap.current_event, R.mipmap.oldevent, R.mipmap.nearbyicon};
    ActionBar actionbar;
    Dialog dialog;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    EditText travel_desti, trave_ludget, trave_from_date, trave_to_date;
    boolean trave_from_date_checked = false;
    boolean trave_to_date_checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_user_profile_view );


        EDBManager = new EventListDBManager( this );
        mProfileBDManager = new UserProfileDBManager( this );

        sharedPreferences = getSharedPreferences( "terratour", MODE_PRIVATE );
        int totalAmount = getIntent().getIntExtra( "totalexpense", 0 );
        if (totalAmount > 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt( "totalamount", totalAmount ).commit();
        }

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        actionbar = getSupportActionBar();

        viewPager = (ViewPager) findViewById( R.id.viewpager );
        setupViewPager( viewPager );
        tabLayout = (TabLayout) findViewById( R.id.tabs );
        tabLayout.setupWithViewPager( viewPager );
        setupTabIcons();

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.setDrawerListener( toggle );
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        View layout = navigationView.getHeaderView( 0 );
        profilepic = (ImageView) layout.findViewById( R.id.profilePicIV );
        userName = (TextView) layout.findViewById( R.id.profileUserNameTV );
        phoneEmail = (TextView) layout.findViewById( R.id.profileUserEmailTV );
        setUserDetails();

        dialog = new Dialog( this );
        dialog.setContentView( R.layout.add_event_dialog_layout );

        travel_desti = (EditText) dialog.findViewById( R.id.travel_Destination_ET );
        trave_ludget = (EditText) dialog.findViewById( R.id.travel_Estimate_Budget_ET );
        trave_from_date = (EditText) dialog.findViewById( R.id.travel_From_Date_ET );
        trave_to_date = (EditText) dialog.findViewById( R.id.travel_To_Date_ET );

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
                DatePickerDialog datePickerDialog = new DatePickerDialog( UserProfileViewActivity.this, date, myCalendar
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
                /*new DatePickerDialog( UserProfileViewActivity.this, date, myCalendar
                        .get( Calendar.YEAR ), myCalendar.get( Calendar.MONTH ),
                        myCalendar.get( Calendar.DAY_OF_MONTH ) ).show();*/
                if(!trave_from_date.getText().toString().isEmpty()) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(UserProfileViewActivity.this, date, myCalendar
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
                    new AlertDialog.Builder(UserProfileViewActivity.this)
                            .setMessage("Please Set From Date First !! ")
                            .setCancelable(true)
                            .show();
                }
            }
        } );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.user_profile_view, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean( "islogin", false ).commit();
            startActivity( new Intent( this, UserEntryActivity.class ) );
            finish();
        }
        if (id==R.id.nav_weather){
            String lat =sharedPreferences.getString( "lat","" );
            String lon = sharedPreferences.getString( "lon","");
            if (!lat.equals( 0 ) && !lon.equals( 0 )) {
                startActivity( new Intent( this, WeatherActivity.class ) );
            }else{
                Toast.makeText( this, "Trying To Connecting...Please Wait ", Toast.LENGTH_SHORT ).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    private void setUserDetails() {
        String username = getIntent().getStringExtra( "userName" );
        if (username != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString( "username", username ).commit();
        } else {
            username = sharedPreferences.getString( "username", "" );
        }
        usermodel = mProfileBDManager.getUserByName( username );
        if (usermodel != null) {
            try {
                File imgFile = new File( usermodel.getImgUrl() );
                if (imgFile.exists()) {
                    profilepic.setImageURI( Uri.fromFile( imgFile ) );
                }

                userName.setText( usermodel.getUserName() );

                if (usermodel.getEmail().isEmpty()) {
                    phoneEmail.setText( usermodel.getPhoneNumber() );
                } else {
                    phoneEmail.setText( usermodel.getEmail() );
                }
                int id = usermodel.getId();
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putInt( "id", id ).commit();

            } catch (NullPointerException e) {
            }
        }
    }

    public void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewAdapter = new ViewPagerAdapter( getSupportFragmentManager() );
        viewAdapter.addFragment( new Current_Event_Fragment(), "Current Event" );
        //viewAdapter.addFragment( new Old_Enents_fragment(), "Old Events" );
        viewAdapter.addFragment( new NearBy_Fragment(), "NearBy" );
        viewPager.setAdapter( viewAdapter );

        viewPager.setOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        actionbar.setTitle( "Current Event" );
                        break;
                    /*case 1:
                        actionbar.setTitle( "Oldest Events" );
                        break;*/
                    case 1:
                        actionbar.setTitle( "NearBy" );
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        } );
    }

    private void setupTabIcons() {
        tabLayout.getTabAt( 0 ).setIcon( tabIcons[0] );
        //tabLayout.getTabAt( 1 ).setIcon( tabIcons[1] );
        tabLayout.getTabAt( 1 ).setIcon( tabIcons[2] );
    }

    public void addEvent(MenuItem item) {
        dialog.setTitle( "Travel Event" );
        dialog.show();
    }

    public void createEvent(final View view) {
        int id = 0;
        try { id = usermodel.getId(); } catch (NullPointerException e) {}

        String desti = travel_desti.getText().toString();
        String budget = trave_ludget.getText().toString();
        String from_date = trave_from_date.getText().toString();
        String to_date = trave_to_date.getText().toString();
        view.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundResource( R.drawable.save_evnet_button_click_shape );
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setBackgroundResource( R.drawable.save_event_button_current_shape );
                }
                return false;
            }
        } );

        if (evetFildCheck( desti, budget, from_date, to_date )) {
            EventListModel event = new EventListModel( desti, budget, from_date, to_date, id );
            long row = EDBManager.addCurrentEvent( event );
            if (row > 0) {
                Toast.makeText( this, "Your Event Created", Toast.LENGTH_SHORT ).show();
                setupViewPager( viewPager );
                dialog.dismiss();
            }
        }
    }

    private boolean evetFildCheck(String desti, String budget, String from_date, String to_date) {
        if (desti.isEmpty()) {
            travel_desti.setError( "Empty ! " );
            return false;
        }
        if (budget.isEmpty()) {
            trave_ludget.setError( "Empty ! " );
            return false;
        }
        if (from_date.isEmpty()) {
            trave_from_date.setError( "Empty ! " );
            return false;
        }
        if (to_date.isEmpty()) {
            trave_to_date.setError( "Empty ! " );
            return false;
        }
        return true;
    }

}
