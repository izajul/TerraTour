package com.monordevelopers.tt.terratour.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.monordevelopers.tt.terratour.model.UserModel;

public class UserProfileDBManager {
    DBHelper dbHelper;
    Context mContext;
   // UserModel mUsermodel;

    public UserProfileDBManager(Context context) {
        this.mContext = context;
        dbHelper = new DBHelper( context );
    }

    public long addUserToDB(UserModel usermodel){
        SQLiteDatabase sqlitedatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(  );
        contentValues.put( dbHelper.COLUMN_FULlNAME,usermodel.getFullName() );
        contentValues.put( dbHelper.COLUMN_USERNAME,usermodel.getUserName() );
        contentValues.put( dbHelper.COLUMN_PASSWORD,usermodel.getPassword() );
        contentValues.put( dbHelper.COLUMN_PHONE,usermodel.getPhoneNumber() );
        contentValues.put( dbHelper.COLUMN_EMAIL,usermodel.getEmail() );
        contentValues.put( dbHelper.COLUMN_ADDRESS,usermodel.getAddress() );
        contentValues.put( dbHelper.COLUMN_IMG_URL,usermodel.getImgUrl() );

        long row = sqlitedatabase.insert( dbHelper.DB_TABLE_PROFILE,null,contentValues );
        sqlitedatabase.close();
        return row;
    }

    public UserModel getUserByName (String name){
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
       // String query = "select * from "+dbHelper.DB_TABLE_PROFILE+" where "+dbHelper.COLUMN_USERNAME+" = "+name;
        String query = "SELECT * FROM profile_details WHERE username LIKE '"+name+"'";
        Cursor cusor = sqlite.rawQuery( query,null );
        UserModel usermodel = null;
        if (cusor.moveToFirst()){
            String fullname = cusor.getString( cusor.getColumnIndex( dbHelper.COLUMN_FULlNAME ) );
            String username = cusor.getString( cusor.getColumnIndex( dbHelper.COLUMN_USERNAME ) );
            String password = cusor.getString( cusor.getColumnIndex( dbHelper.COLUMN_PASSWORD ) );
            String phone = cusor.getString( cusor.getColumnIndex( dbHelper.COLUMN_PHONE ) );
            String email = cusor.getString( cusor.getColumnIndex( dbHelper.COLUMN_EMAIL ) );
            String address = cusor.getString( cusor.getColumnIndex( dbHelper.COLUMN_ADDRESS ) );
            String imgurl = cusor.getString( cusor.getColumnIndex( dbHelper.COLUMN_IMG_URL ) );
            int id = cusor.getInt( cusor.getColumnIndex( dbHelper.COLUMN_ID ) );
            usermodel = new UserModel( fullname,username,password,phone,email,address,imgurl,id );

        }
        return usermodel;
    }

    public boolean cheeckUser(String username,String password){
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        String query = "SELECT id FROM profile_details WHERE username LIKE '"+username+"' AND password LIKE '"+password+"'";
        Cursor cusor = sqlite.rawQuery( query,null );
        if (cusor.moveToFirst()){
            int id = cusor.getInt( cusor.getColumnIndex( dbHelper.COLUMN_ID ) );
            if (id>0){
                return true;
            }
        }
        return false;
    }

    public boolean checkUnikUsername(String userName){
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM profile_details WHERE username LIKE '"+userName+"'";
        Cursor cusor = sqlite.rawQuery( query,null );
        if (cusor.moveToFirst()){
            return false;
        }
        return true;
    }
}
