package com.monordevelopers.tt.terratour.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.database.UserProfileDBManager;
import com.monordevelopers.tt.terratour.databinding.ActivityUserEntryBinding;

public class UserEntryActivity extends AppCompatActivity {
    ActivityUserEntryBinding binding;
    SharedPreferences sharedPreferences;
    UserProfileDBManager userProfileDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_entry);
        sharedPreferences = getSharedPreferences( "terratour",MODE_PRIVATE );
        if (sharedPreferences.getBoolean( "islogin",false )){
            startActivity( new Intent( this,UserProfileViewActivity.class ).putExtra( "userName",getIntent().getStringExtra( "userName" ) ) );
            finish();
        }
        userProfileDBManager = new UserProfileDBManager( this );
    }

    public void userSignup(final View view) {
        view.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground( getResources().getDrawable( R.drawable.signuponclick ) );
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground( getResources().getDrawable( R.drawable.singupbuttonborder ) );
                        }
                        startActivity( new Intent(UserEntryActivity.this, UserSignupActivity.class) );
                }return false;
            }
        } );
    }

    public void login(View view) {

        String username = binding.userNameET.getText().toString();
        String password = binding.userPasswordET.getText().toString();
        if (username.isEmpty()){
            binding.userNameET.setError( "UserName Empty!" );
        }else if (password.isEmpty()){
            binding.userNameET.setError( "Password Empty!" );
        }else{
            if (userProfileDBManager.cheeckUser( username,password )){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean( "islogin",true ).commit();
                startActivity( new Intent( UserEntryActivity.this,UserProfileViewActivity.class ).putExtra( "userName",username ) );
                finish();
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("Login Failed ! ")
                        .setMessage("Username And Password Not Matched")
                        .setCancelable(true)
                        .show();

            }
        }
    }
}
