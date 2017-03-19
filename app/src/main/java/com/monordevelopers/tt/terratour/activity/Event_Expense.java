package com.monordevelopers.tt.terratour.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.adapter.ViewPagerAdapter;
import com.monordevelopers.tt.terratour.database.EventListDBManager;
import com.monordevelopers.tt.terratour.database.ExpenseBDManager;
import com.monordevelopers.tt.terratour.database.MoementsDbManager;
import com.monordevelopers.tt.terratour.fragment.EventMomentsFragment;
import com.monordevelopers.tt.terratour.fragment.ExpenseListFragment;
import com.monordevelopers.tt.terratour.model.EventListModel;
import com.monordevelopers.tt.terratour.model.ExpenseModel;
import com.monordevelopers.tt.terratour.model.MomentsModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Locale.US;


public class Event_Expense extends AppCompatActivity {
    private static final int PICK_PHOTO = 222;
    ViewPager viewPager;
    TabLayout tabLayout;
    ActionBar actionbar;
    Menu mMenu;
    int id;
    int totalExpens = 0;
    Dialog mExpensDialog,mMomentDialog;
    ExpenseBDManager mExpenseBDManager;
    SharedPreferences sharedPreferences ;
    String mCurrentPhotoPath = "";
    ImageView momentImageShow;
    Bitmap mBitmap;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    EditText getMomentDescription;
    Dialog dialog;
    MoementsDbManager moementsDbManager;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    boolean trave_from_date_checked = false;
    boolean trave_to_date_checked = false;
    Dialog mEventEditDialog;

    EventListDBManager eventListDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_event__expense );
        mExpenseBDManager = new ExpenseBDManager(this);
        sharedPreferences = getSharedPreferences( "terratour",MODE_PRIVATE );
        id = getIntent().getIntExtra( "id",0 );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt( "eventid",id ).commit();
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionbar = getSupportActionBar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onBackPressed();}
        });

        eventListDBManager = new EventListDBManager( this );

        viewPager = (ViewPager) findViewById( R.id.event_viewpager );
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById( R.id.event_tabs );
        tabLayout.setupWithViewPager( viewPager );

        dialog = new Dialog(this);
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        mExpensDialog = new Dialog( this );
        mExpensDialog.setContentView( R.layout.add_expense_dialog_box );
        mMomentDialog = new Dialog( this );
        mMomentDialog.setContentView( R.layout.add_moment_dialog_box );
        mMomentDialog.getWindow().setLayout( WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT );
        momentImageShow = (ImageView) mMomentDialog.findViewById( R.id.moment_show_tooked_image );
        getMomentDescription = (EditText) findViewById( R.id.moment_description_ET );

        moementsDbManager = new MoementsDbManager( this );

        mEventEditDialog  = new Dialog( this );
        mEventEditDialog.setContentView( R.layout.add_event_dialog_layout );


    }

    public void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter viewAdapter = new ViewPagerAdapter( getSupportFragmentManager() );
        viewAdapter.addFragment( new ExpenseListFragment(),"Expense" );
        viewAdapter.addFragment( new EventMomentsFragment(),"Moments" );
        viewPager.setAdapter( viewAdapter );
        setMenuItemOptionally(viewPager);
        viewPager.setCurrentItem( 0 );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.event_activity_menu, menu );
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=  item.getItemId();
        switch (itemId){
            case R.id.item_Add_Expense:
                mExpensDialog.setTitle( "Expense Entry" );
                mExpensDialog.show();
                break;
            case R.id.item_Add_Moment:
                addMomentPic();
                break;
            case R.id.item_Delete_Event:
                deleteEvent();
                break;
            case R.id.item_Edit_Event:
                editEven();
                break;
        }
        return super.onOptionsItemSelected( item );
    }

    public void setMenuItemOptionally(ViewPager viewPager){
        ArrayList<ExpenseModel> expenseModels = mExpenseBDManager.GetExpenseByEventId(id);
        int totalExpense = 0;
        if (expenseModels.size()>0){
            for (ExpenseModel expenseModel : expenseModels){
                totalExpense+=expenseModel.getAmount();
            }
            if (totalExpense>0){
                totalExpens =  totalExpense;
                actionbar.setTitle("Expenses "+(char)0x09F3+totalExpens);
            }
        }else{actionbar.setTitle( "Add Expenses" );}

        viewPager.setOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        if (totalExpens>0){actionbar.setTitle("Expenses "+(char)0x09F3+totalExpens);}
                        else{actionbar.setTitle( "Add Expenses" );}
                        mMenu.findItem( R.id.item_Add_Expense ).setVisible( true );
                        mMenu.findItem( R.id.item_Add_Moment ).setVisible( false );
                        break;
                    case 1:
                        actionbar.setTitle( "Add Moments" );
                        mMenu.findItem( R.id.item_Add_Moment ).setVisible( true );
                        mMenu.findItem( R.id.item_Add_Expense ).setVisible( false );
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        } );
    }

    public void RecordExpense(View view) {
        String mydate = java.text.DateFormat.getDateTimeInstance().format( Calendar.getInstance().getTime());
        EditText aboutExpense = (EditText) mExpensDialog.findViewById( R.id.expense_details_ET );
        EditText amount = (EditText) mExpensDialog.findViewById( R.id.expense_amount_ET );
        ExpenseModel expenseModel = new ExpenseModel(aboutExpense.getText().toString(),Integer.valueOf( amount.getText().toString()),mydate,id);
        long row = mExpenseBDManager.addExpense( expenseModel );
        if (row>0){
            setupViewPager(viewPager);
            mExpensDialog.dismiss();
            Toast.makeText( this, "Expense Recorded", Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity( new Intent( this,UserProfileViewActivity.class ));
        finish();
    }

    public void addMomentPic(){
        dialog.setCancelable(true);
        dialog.setContentView( R.layout.selectimagedialog );
        dialog.getWindow().setBackgroundDrawableResource( R.drawable.dialogbox );
        dialog.show();

        dialog.findViewById( R.id.cameraIB ).getBackground().setAlpha( 0 );
        dialog.findViewById( R.id.galleryIB ).getBackground().setAlpha( 0 );

        dialog.findViewById( R.id.cameraIB ).setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        dialog.findViewById( R.id.cameraIB ).getBackground().setAlpha( 255 );
                        break;
                    case MotionEvent.ACTION_UP:
                        dialog.findViewById( R.id.cameraIB ).getBackground().setAlpha( 0 );
                        permission(110);
                }return false;
            }
        } );
        dialog.findViewById( R.id.galleryIB ).setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        dialog.findViewById( R.id.galleryIB ).getBackground().setAlpha( 255 );
                        break;
                    case MotionEvent.ACTION_UP:
                        dialog.findViewById( R.id.galleryIB ).getBackground().setAlpha( 0 );
                        permission(111);
                }return false;
            }
        } );
    }

    private void permission(int PermissionCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.CAMERA )
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this,
                    Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission( this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions( new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionCode );
                return;
            }else {
                if (PermissionCode==110) takePictureByCamera(); else pickImage();
            }
        } else {
            if (PermissionCode==110) takePictureByCamera(); else pickImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 110:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePictureByCamera();
                }
                return;
            case 111:
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                }
        }
    }

    private void takePictureByCamera() {
        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (Exception ex){}
            if (photoFile != null) {
                takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile) );
                dialog.dismiss();
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String path = Environment.getExternalStorageDirectory().toString();
        File appDirectory = new File(path + "/" +"terratour"+"/" + "momentsImages");
        appDirectory.mkdirs();

        File currentImage = File.createTempFile( imageFileName,".jpg",appDirectory );
        mCurrentPhotoPath = currentImage.getAbsolutePath();
        return  currentImage;
    }

    private  void pickImage(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        dialog.dismiss();
        startActivityForResult(intent, PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            //dialog.dismiss();
            mMomentDialog.setTitle( "Add Moment" );
            mMomentDialog.show();
            setPic();
        }
        if (requestCode == PICK_PHOTO && resultCode == RESULT_OK && null != data){
            mMomentDialog.setTitle( "Add Moment" );
            mMomentDialog.show();
            //dialog.dismiss();
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mCurrentPhotoPath = cursor.getString(columnIndex);
            cursor.close();
            setPic();
        }
    }

    private void setPic() {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;

        mBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);// bmOptions
        momentImageShow.setImageBitmap(mBitmap);//mMomentDialog.show();
    }

    public void saveMoments(View view) {
        EditText details = (EditText) mMomentDialog.findViewById( R.id.moment_description_ET );
        String time = java.text.DateFormat.getDateTimeInstance().format( Calendar.getInstance().getTime());
        if (details.getText().toString().isEmpty()){
            details.setError( "Description Empty !" );return;
        }
        MomentsModel momentsModel = new MomentsModel( details.getText().toString(),time,mCurrentPhotoPath,id );
        long row =moementsDbManager.addMoments( momentsModel );
        if (row>0){
            Toast.makeText( this, "Moment Recorded", Toast.LENGTH_SHORT ).show();
            mMomentDialog.dismiss();
            setupViewPager(viewPager);
            viewPager.setCurrentItem( 1 );
        }
    }

    private void deleteEvent(){
        new AlertDialog.Builder(this)
                .setTitle("Delete The Event")
                .setMessage("If you Delete The Event, All of Yours Moments,Expense Record Will Destroyed !!! ")
                .setCancelable(false)
                .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } )
                .setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(eventListDBManager.deleteEventByid( id )){
                            Toast.makeText( Event_Expense.this, "Event Deleted", Toast.LENGTH_SHORT ).show();
                            startActivity( new Intent( Event_Expense.this,UserProfileViewActivity.class ) );
                            finish();
                            dialog.dismiss();
                        }
                    }
                } ).show();
    }

    private void editEven(){
        mEventEditDialog.setTitle( " Update Event " );
        mEventEditDialog.show();
        Button update = (Button) mEventEditDialog.findViewById( R.id.save_Event_BTN );

        final EditText desti = (EditText) mEventEditDialog.findViewById( R.id.travel_Destination_ET );
        final EditText budget = (EditText) mEventEditDialog.findViewById( R.id.travel_Estimate_Budget_ET );
        final EditText fromdate = (EditText) mEventEditDialog.findViewById( R.id.travel_From_Date_ET );
        final EditText todate = (EditText) mEventEditDialog.findViewById( R.id.travel_To_Date_ET );
        setdate(fromdate,todate);
        update.setText( "Update" );

        EventListModel eventListModel = eventListDBManager.getEventById( id );
        try {
            desti.setText(eventListModel.getDestination());
            budget.setText(eventListModel.getBudget());
            fromdate.setText(eventListModel.getFromDate());
            todate.setText(eventListModel.getToDate());
        }catch (NullPointerException e){}

        update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEvnet(desti,budget,fromdate,todate);
            }
        } );
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
                new DatePickerDialog( Event_Expense.this, date, myCalendar
                        .get( Calendar.YEAR ), myCalendar.get( Calendar.MONTH ),
                        myCalendar.get( Calendar.DAY_OF_MONTH ) ).show();
                trave_from_date_checked = true;
                trave_to_date_checked = false;
            }
        } );
        trave_to_date.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog( Event_Expense.this, date, myCalendar
                        .get( Calendar.YEAR ), myCalendar.get( Calendar.MONTH ),
                        myCalendar.get( Calendar.DAY_OF_MONTH ) ).show();
                trave_from_date_checked = false;
                trave_to_date_checked = true;
            }
        } );
    }

    private void updateEvnet(EditText desti,EditText budget,EditText from,EditText to){
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
                Toast.makeText( this, "Update Successed", Toast.LENGTH_SHORT ).show();
                mEventEditDialog.dismiss();
            }else{
                new AlertDialog.Builder(this)
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
