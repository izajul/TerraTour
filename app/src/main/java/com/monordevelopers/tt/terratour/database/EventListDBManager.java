package com.monordevelopers.tt.terratour.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.monordevelopers.tt.terratour.model.EventListModel;

import java.util.ArrayList;

public class EventListDBManager {
    DBHelper dbHelper;
    Context context;
    ExpenseBDManager expenseBDManager;
    MoementsDbManager moementsDbManager;
    public EventListDBManager(Context context){
        this.context = context;
        dbHelper = new DBHelper( context );
        expenseBDManager = new ExpenseBDManager( context );
        moementsDbManager = new MoementsDbManager( context );
    }

    public long addCurrentEvent(EventListModel eventListModel){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(  );
        contentValues.put( dbHelper.COLUMN_EVENT_UID,eventListModel.getUid() );
        contentValues.put( dbHelper.COLUMN_DESTINATION,eventListModel.getDestination() );
        contentValues.put( dbHelper.COLUMN_BUDGET,eventListModel.getBudget() );
        contentValues.put( dbHelper.COLUMN_FROM_DATE,eventListModel.getFromDate() );
        contentValues.put( dbHelper.COLUMN_TO_DATE,eventListModel.getToDate() );

        long row  = sqLiteDatabase.insert( dbHelper.DB_TABLE_EVENT_LIST,null,contentValues );
        sqLiteDatabase.close();
        return row;
    }
    public long addTotalExpenseById(int evnetId,int totalExpense){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(  );
        contentValues.put( dbHelper.COLUMN_TOTALEXPENSE,totalExpense );
        int row = sqLiteDatabase.update( dbHelper.DB_TABLE_EVENT_LIST,contentValues," id="+evnetId, null);
        sqLiteDatabase.close();
        return row;
    }

    public ArrayList<EventListModel> getAllEventByUid(int uid){

        ArrayList<EventListModel> eventlist = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM "+dbHelper.DB_TABLE_EVENT_LIST+" WHERE "+dbHelper.COLUMN_EVENT_UID+" IN ( "+uid+" ) "+"ORDER BY "+dbHelper.COLUMN_EVENT_ID+" DESC";
        try {
            Cursor cursor = sqLiteDatabase.rawQuery( query, null );
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_EVENT_ID ) );
                    int uid2 = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_EVENT_ID ) );
                    int totalExpense = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_TOTALEXPENSE ) );
                    String destination = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_DESTINATION ) );
                    String budget = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_BUDGET ) );
                    String fromdate = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_FROM_DATE ) );
                    String todate = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_TO_DATE ) );
                    EventListModel eventListModel = new EventListModel( destination, budget, fromdate, todate, uid2, id, totalExpense );
                    eventlist.add( eventListModel );
                } while (cursor.moveToNext());
            }
        }catch (Exception e){}
        return eventlist;
    }

    public EventListModel getEventById(int id){
        EventListModel eventListModel = null;
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM "+dbHelper.DB_TABLE_EVENT_LIST+" WHERE "+dbHelper.COLUMN_EVENT_ID+" = "+id;
        Cursor cursor = sqLiteDatabase.rawQuery( query,null );
        if (cursor.moveToFirst()){
            int uid2 = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_EVENT_ID ) );
            int totalExpense = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_TOTALEXPENSE ) );
            String destination = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_DESTINATION ) );
            String budget = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_BUDGET ) );
            String fromdate = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_FROM_DATE ) );
            String todate = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_TO_DATE ) );
            eventListModel = new EventListModel(destination,budget,fromdate,todate,uid2,id,totalExpense);
        }
        return eventListModel;
    }

    public long UpdateEventByid(EventListModel eventListModel){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.COLUMN_DESTINATION,eventListModel.getDestination());
        contentValues.put(dbHelper.COLUMN_BUDGET,eventListModel.getBudget());
        contentValues.put(dbHelper.COLUMN_FROM_DATE,eventListModel.getFromDate());
        contentValues.put(dbHelper.COLUMN_TO_DATE,eventListModel.getToDate());
        long row = sqLiteDatabase.update(dbHelper.DB_TABLE_EVENT_LIST,contentValues," id="+eventListModel.getId(),null);
        sqLiteDatabase.close();
        return row;
    }

    public boolean deleteEventByid(int id){
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        long row = sqLiteDatabase.delete(DBHelper.DB_TABLE_EVENT_LIST,dbHelper.COLUMN_EVENT_ID+" = "+id,null);
        sqLiteDatabase.close();
        if (row>0) {
            if (expenseBDManager.deleteExpenseFromEventList( id ) &&
                    moementsDbManager.deleteMomentFromEvent( id )){
                return true;
            }
            return true;
        } else return false;
    }
}
