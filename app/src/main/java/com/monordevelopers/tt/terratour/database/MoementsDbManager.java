package com.monordevelopers.tt.terratour.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.monordevelopers.tt.terratour.model.MomentsModel;

import java.util.ArrayList;

public class MoementsDbManager {
    DBHelper dbHelper;
    Context mContext;

    public MoementsDbManager(Context context) {
        dbHelper = new DBHelper( context );
        this.mContext = context;
    }

    public long addMoments(MomentsModel momentsModel){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues(  );
        contentValues.put( dbHelper.COLUMN_MOMENTS_EVN_ID,momentsModel.getEvnId() );
        contentValues.put( dbHelper.COLUMN_MOMENTS_DETAILS,momentsModel.getMomentDetails() );
        contentValues.put( dbHelper.COLUMN_MOMENTS_TIME,momentsModel.getMomnentTime() );
        contentValues.put( dbHelper.COLUMN_MOMENTS_IMG_URL,momentsModel.getImgUrl() );
        long row = sqLiteDatabase.insert( dbHelper.DB_TABLE_MOMENTS_LIST,null,contentValues );
        return row;
    }

    public ArrayList<MomentsModel>getAllMomentsByEvnId(int evnId){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM "+dbHelper.DB_TABLE_MOMENTS_LIST+" WHERE "
                +dbHelper.COLUMN_MOMENTS_EVN_ID+" IN ( "+evnId+" ) " +
                "ORDER BY "+dbHelper.COLUMN_MOMENTS_ID+" DESC";

        //String query = "SELECT * FROM "+dbHelper.DB_TABLE_MOMENTS_LIST;
        ArrayList<MomentsModel>moments = new ArrayList<>(  );
        Cursor cursor = sqLiteDatabase.rawQuery( query,null );
        if (cursor.moveToFirst()){
            do{
                int evn_Id = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_MOMENTS_EVN_ID ) );
                int moment_Id = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_MOMENTS_ID ) );
                String moment_details = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_MOMENTS_DETAILS ) );
                String moment_time = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_MOMENTS_TIME ) );
                String moment_img_url = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_MOMENTS_IMG_URL ) );
                MomentsModel momentsModel = new MomentsModel(
                        moment_details,moment_time,moment_img_url,evn_Id,moment_Id
                );
                moments.add( momentsModel );
            }while (cursor.moveToNext());
        }
        return moments;
    }

    public MomentsModel getMomentById(int momentId){
        MomentsModel momentsModel = null;
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM "+dbHelper.DB_TABLE_MOMENTS_LIST+" WHERE "+dbHelper.COLUMN_MOMENTS_ID+" = "+momentId;
        Cursor cursor = sqLiteDatabase.rawQuery( query,null );
        if (cursor.moveToFirst()){
            int evn_Id = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_MOMENTS_EVN_ID ) );
            int moment_Id = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_MOMENTS_ID ) );
            String moment_details = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_MOMENTS_DETAILS ) );
            String moment_time = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_MOMENTS_TIME ) );
            String moment_img_url = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_MOMENTS_IMG_URL ) );
            momentsModel = new MomentsModel(
                    moment_details,moment_time,moment_img_url,evn_Id,moment_Id
            );
        }
        return momentsModel;
    }

    public  boolean deleteMomentById(int id){
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        long row = sqLiteDatabase.delete(DBHelper.DB_TABLE_MOMENTS_LIST,dbHelper.COLUMN_MOMENTS_ID+" = "+id,null);
        sqLiteDatabase.close();
        if (row>0) return true ; else return false;
    }

    public boolean deleteMomentFromEvent(int EventId){
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        long row = sqLiteDatabase.delete(DBHelper.DB_TABLE_MOMENTS_LIST,dbHelper.COLUMN_MOMENTS_EVN_ID+" = "+EventId,null);
        sqLiteDatabase.close();
        if (row>0) return true ; else return false;
    }

}
