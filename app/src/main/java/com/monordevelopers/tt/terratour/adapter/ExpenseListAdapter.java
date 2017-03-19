package com.monordevelopers.tt.terratour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.model.ExpenseModel;

import java.util.ArrayList;

/**
 * Created by izajul on 2/7/2017.
 */

public class ExpenseListAdapter extends ArrayAdapter {
    ArrayList<ExpenseModel> mExpenseModels;
    Context context;
    public ExpenseListAdapter(Context context,ArrayList<ExpenseModel> expenseModels) {
        super( context, R.layout.expense_list_row_layout, expenseModels );
        this.context =context;
        mExpenseModels = expenseModels;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExpenseModel expenseModel = mExpenseModels.get( position );
        convertView= LayoutInflater.from( context ).inflate( R.layout.expense_list_row_layout,parent,false );

        TextView about  = (TextView) convertView.findViewById( R.id.expense_details_amount_TV );
        TextView date  = (TextView) convertView.findViewById( R.id.expense_date_Show_TV );
        try {
            about.setText( expenseModel.getAboutExpense() + "     "+(char)0x09F3+expenseModel.getAmount() );
            date.setText( expenseModel.getDate() );
        }catch (NullPointerException e){}

        return convertView;
    }
}
