package com.monordevelopers.tt.terratour.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.adapter.ExpenseListAdapter;
import com.monordevelopers.tt.terratour.database.ExpenseBDManager;
import com.monordevelopers.tt.terratour.model.ExpenseModel;

import java.util.ArrayList;

public class ExpenseListFragment extends Fragment{
    ListView listView;
    ExpenseListAdapter expenseListAdapter;
    SharedPreferences sharedPreferences;
    ArrayList<ExpenseModel> expenseModels;
    ExpenseBDManager expenseBDManager;
    Dialog dialog;
    Button update,delete;
    EditText expdtls,expamont;
    Context mContext;

    public ExpenseListFragment() {}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = inflater.getContext();
        expenseBDManager = new ExpenseBDManager( inflater.getContext() );
        sharedPreferences = getActivity().getSharedPreferences( "terratour", Context.MODE_PRIVATE );
        View view = inflater.inflate( R.layout.fragment_expense_list, container, false );
        listView  = (ListView) view.findViewById( R.id.expenseListView );
        int eventId = sharedPreferences.getInt("eventid",0 );
        try {
            expenseModels = expenseBDManager.GetExpenseByEventId( eventId );
        }catch (Exception e){}
        expenseListAdapter = new ExpenseListAdapter( inflater.getContext(),expenseModels );
        listView.setAdapter( expenseListAdapter );

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ExpenseModel expenseModel = expenseModels.get( position );
                dialog = new Dialog( inflater.getContext() );
                dialog.setContentView( R.layout.expense_show_dialog_box );
                dialog.setTitle( "Expense deatils" );
                dialog.show();
                expdtls = (EditText) dialog.findViewById( R.id.expense_details_ET );
                expamont = (EditText) dialog.findViewById( R.id.expenses_amount_ET );
                try{
                    expdtls.setText( expenseModel.getAboutExpense() );
                    String ss = expenseModel.getAmount()+"";
                    expamont.setText(ss );
                }catch (Exception e){}
                update = (Button) dialog.findViewById( R.id.update_expense_BTN );
                delete = (Button) dialog.findViewById( R.id.delete_expense_BTN );
                update.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update(expenseModel.getExpenseID());
                    }
                } );

                delete.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete(expenseModel.getExpenseID());
                    }
                } );
            }
        } );
        return view;
    }

    private void update(int expenseID){
        try{
            ExpenseModel expenseModel = new ExpenseModel( expdtls.getText().toString(),
                    Integer.valueOf(expamont.getText().toString()),"",0);
            long row = expenseBDManager.updateById( expenseModel,expenseID );
            if (row>0){
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
                Toast.makeText( mContext, "Update Success", Toast.LENGTH_SHORT ).show();
                dialog.dismiss();
            }
        }catch (Exception e){}
    }
    private void delete(int id){
        if (expenseBDManager.deleteexpensebyid(id)){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
            Toast.makeText( mContext, "Delete Success", Toast.LENGTH_SHORT ).show();
            dialog.dismiss();
        }
    }


}
