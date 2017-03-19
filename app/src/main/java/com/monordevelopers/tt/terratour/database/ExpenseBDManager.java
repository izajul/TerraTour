package com.monordevelopers.tt.terratour.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.monordevelopers.tt.terratour.model.ExpenseModel;

import java.util.ArrayList;

public class ExpenseBDManager {
    DBHelper dbHelper;
    Context mContext;
    int totalAmount =0;
    public ExpenseBDManager(Context context) {
        dbHelper = new DBHelper( context );
        mContext=context;
    }

    public long addExpense(ExpenseModel expenseModel){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(  );
        contentValues.put( dbHelper.COLUMN_EVN_ID,expenseModel.getEventId() );
        contentValues.put( dbHelper.COLUMN_EXPENSE_DETAILS,expenseModel.getAboutExpense() );
        contentValues.put( dbHelper.COLUMN_EXPENSE_AMOUNT,expenseModel.getAmount() );
        contentValues.put( dbHelper.COLUMN_EXPENSE_DATE,expenseModel.getDate() );
        long row  = sqLiteDatabase.insert( dbHelper.DB_TABLE_EXPENSE_LIST,null,contentValues );
        sqLiteDatabase.close();
        return row;
    }

    public ArrayList<ExpenseModel> GetExpenseByEventId (int event_Id){
        totalAmount = 0;
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        ArrayList<ExpenseModel> expenseModels = new ArrayList<>(  );
        String query = "SELECT * FROM "+dbHelper.DB_TABLE_EXPENSE_LIST+" WHERE "
                +dbHelper.COLUMN_EVN_ID+" IN ( "+event_Id+" ) "+"ORDER BY "+dbHelper.COLUMN_EXPENSE_ID+" DESC";
        try {
            Cursor cursor = sqLiteDatabase.rawQuery( query, null );
            if (cursor.moveToFirst()){
                do {
                    int expId = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_EXPENSE_ID ) );
                    int evnId = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_EVN_ID ) );
                    String details = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_EXPENSE_DETAILS ) );
                    int amount = cursor.getInt( cursor.getColumnIndex( dbHelper.COLUMN_EXPENSE_AMOUNT ) );
                    String date = cursor.getString( cursor.getColumnIndex( dbHelper.COLUMN_EXPENSE_DATE ) );
                    totalAmount+=amount;
                    ExpenseModel expenseModel = new ExpenseModel( details,amount,date,evnId,expId );
                    expenseModels.add( expenseModel );
                }while (cursor.moveToNext());
            }
        }catch (Exception e){}
        EventListDBManager eventListDBManager = new EventListDBManager( mContext );
        long row =  eventListDBManager.addTotalExpenseById( event_Id,totalAmount );
        return expenseModels;
    }
    public int getToatalamount(){
        return totalAmount;
    }

    public boolean deleteExpenseFromEventList(int EventId){
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        //long row = sqLiteDatabase.delete(DBHelper.DB_TABLE_EXPENSE_LIST,dbHelper.COLUMN_EVN_ID+" IN "+"("+EventId +")",null);
        long row = sqLiteDatabase.delete(DBHelper.DB_TABLE_EXPENSE_LIST,dbHelper.COLUMN_EVN_ID+" = "+EventId,null);
        sqLiteDatabase.close();
        if (row>0) return true ; else return false;
    }

    public long updateById(ExpenseModel expenseModel,int id){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.COLUMN_EXPENSE_DETAILS,expenseModel.getAboutExpense());
        contentValues.put(dbHelper.COLUMN_EXPENSE_AMOUNT,expenseModel.getAmount());
        long row = sqLiteDatabase.update(dbHelper.DB_TABLE_EXPENSE_LIST,contentValues,dbHelper.COLUMN_EXPENSE_ID+"="+id,null);
        sqLiteDatabase.close();
        return row;
    }

    public boolean deleteexpensebyid(int id){
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        //long row = sqLiteDatabase.delete(DBHelper.DB_TABLE_EXPENSE_LIST,dbHelper.COLUMN_EVN_ID+" IN "+"("+EventId +")",null);
        long row = sqLiteDatabase.delete(DBHelper.DB_TABLE_EXPENSE_LIST,dbHelper.COLUMN_EXPENSE_ID+" = "+id,null);
        sqLiteDatabase.close();
        if (row>0) return true ; else return false;
    }
}
