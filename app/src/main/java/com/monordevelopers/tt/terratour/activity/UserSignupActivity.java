package com.monordevelopers.tt.terratour.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.database.UserProfileDBManager;
import com.monordevelopers.tt.terratour.databinding.ActivityUserSignupBinding;
import com.monordevelopers.tt.terratour.model.UserModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserSignupActivity extends AppCompatActivity {
    //private static final int PERMISSION_REQUEST_CODE =123;
    private static final int PICK_PHOTO =123;
    ActivityUserSignupBinding binding;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath ="";
    Bitmap mBitmap;
    UserProfileDBManager userProfileDBManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_signup);
        sharedPreferences = getSharedPreferences( "terratour",MODE_PRIVATE );
        imageView = binding.selectProfilePic;
        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageOption();

            }
        } );
        userProfileDBManager = new UserProfileDBManager( this );
    }

    public void exitSignup(View view) {
        finish();
    }

    public void signup(View view) {
        String fullName=binding.singUpfullNameET.getText().toString();
        String userName=binding.singUpUserNameET.getText().toString();
        String password=binding.singPasswordET.getText().toString();
        String address=binding.singUpAddressET.getText().toString();
        String phoneNumber = binding.singUpPhoneET.getText().toString();

        if (checkfieldisempty(fullName,userName,password,phoneNumber,address)){
            if (!userProfileDBManager.checkUnikUsername( userName )){
                new AlertDialog.Builder(this)
                        .setTitle("Registration  Failed ! ")
                        .setMessage("Sorry UserName Already Available.\n" +
                                "Try Different ")
                        .setCancelable(false)
                        .setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        } ).show();
                return;
            }
            long row = userProfileDBManager.addUserToDB( new UserModel( fullName,userName,password,
                    phoneNumber,"",address,mCurrentPhotoPath ) );
            if (row>0){
                Toast.makeText( this, "Your Registration Success ", Toast.LENGTH_SHORT ).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean( "islogin",true ).commit();
                startActivity( new Intent( UserSignupActivity.this,UserEntryActivity.class ).putExtra( "userName",userName ) );
                finish();
            }else{
                Toast.makeText( this, "Registration Unsuccessful! Try Again Plz.", Toast.LENGTH_SHORT ).show();
            }
        }
    }
    public boolean checkfieldisempty(String fullName,
                                     String userName,
                                     String password,
                                     String phoneNumber,
                                     String address){
        if(fullName.isEmpty()){
            binding.singUpfullNameET.setError( "Full Name Required" );
            return false;
        }
        if(userName.isEmpty()){
            binding.singUpUserNameET.setError( "User Name Required" );
            return false;
        }
        if(password.isEmpty()){
            binding.singPasswordET.setError( "Password Required" );
            return false;
        }
        if(phoneNumber.isEmpty()){
            binding.singUpPhoneET.setError( "phoneNumber Required" );
            return false;
        }
        if(address.isEmpty()){
            binding.singUpAddressET.setError( "Address Required" );
            return false;
        }
        return true;
    }

// ============ Start Choose Image From Camera Or Gallery=============//
    private void chooseImageOption(){
        final Dialog dialog = new Dialog(this);
        //dialog.requestWindowFeature( Window.FEATURE_NO_TITLE);
        dialog.setTitle( "Choose option" );
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
                        permission(100);
                }
                return false;
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
                        permission(101);
                        break;
                }return false;
            }
        } );
    }

//Request permission for API-23 ANd above
private void permission(int permission){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.CAMERA )
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this,
                Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission( this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, permission );
            return;
        }else {
            if (permission==100) takePictureByCamera(); else pickImage();
        }
    } else {
        if (permission==100) takePictureByCamera(); else pickImage();
    }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)&&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePictureByCamera();
                }else{
                    new AlertDialog.Builder(this)
                            .setMessage("Sorry You Denied A Permission.\n" +
                                    "Do you like Try Again? ")
                            .setCancelable(false)
                            .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            } )
                            .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    permission(100);
                                    dialog.dismiss();
                                }
                            } ).show();
                }break;
            case 101:
                if ((grantResults.length > 0 && grantResults[3] == PackageManager.PERMISSION_GRANTED)&&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                }else{
                    new AlertDialog.Builder(this)
                            .setMessage("Sorry You Denied A Permission.\n" +
                                    "Do you like Try Again? ")
                            .setCancelable(false)
                            .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            } )
                            .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    permission(101);
                                    dialog.dismiss();
                                }
                            } ).show();
                }break;


        }
    }

    public void takePictureByCamera(){
        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (Exception ex){}
            if (photoFile != null) {
                takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile) );
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File currentImage = File.createTempFile( imageFileName,".jpg",storageDir );
        mCurrentPhotoPath = currentImage.getAbsolutePath();
        return  currentImage;
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            setPic();
        }
        if (requestCode == PICK_PHOTO && resultCode == RESULT_OK && null != data){
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
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        mBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);// bmOptions
        imageView.getBackground().setAlpha( 0 );
        imageView.setImageBitmap(mBitmap);
    }
// ============= End Choosing Image =============== //

}
