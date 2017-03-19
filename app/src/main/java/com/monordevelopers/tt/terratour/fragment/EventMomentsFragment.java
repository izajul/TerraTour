package com.monordevelopers.tt.terratour.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.monordevelopers.tt.terratour.R;
import com.monordevelopers.tt.terratour.adapter.MomentListAdapter;
import com.monordevelopers.tt.terratour.database.MoementsDbManager;
import com.monordevelopers.tt.terratour.model.MomentsModel;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class EventMomentsFragment extends Fragment {
    MoementsDbManager moementsDbManager;
    MomentListAdapter momentListAdapter;
    ArrayList<MomentsModel> momentsModels ;
    ListView listView;
    SharedPreferences sharedPreferences;
    int evnId;
    Context mContext;
    Dialog mDialog;

    public EventMomentsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDialog = new Dialog( inflater.getContext() );
        mDialog.setContentView( R.layout.show_moment_dialog_box );
        mDialog.getWindow().setLayout( WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        mContext = inflater.getContext();
        sharedPreferences = this.getActivity().getSharedPreferences( "terratour",MODE_PRIVATE );
        //evnId = sharedPreferences.getInt( "id",0 );
        evnId = sharedPreferences.getInt( "eventid",0 );
        View view = inflater.inflate( R.layout.fragment_event_moments, container, false );
        moementsDbManager = new MoementsDbManager( inflater.getContext() );
        listView = (ListView) view.findViewById( R.id.moment_list_View );
        momentsModels = moementsDbManager.getAllMomentsByEvnId( evnId );
        if (moementsDbManager!=null){
            momentListAdapter = new MomentListAdapter( inflater.getContext(),momentsModels );
            listView.setAdapter( momentListAdapter );
        }
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final MomentsModel momentsModel = momentsModels.get( position );
                TextView details = (TextView) mDialog.findViewById( R.id.show_description_of_moment );
                TextView time = (TextView) mDialog.findViewById( R.id.show_time_of_moment );
                ImageView imageView = (ImageView) mDialog.findViewById( R.id.show_moment_img );
                try{
                    time.setText( momentsModel.getMomnentTime() );
                    details.setText( momentsModel.getMomentDetails() );
                    setImage(imageView,momentsModel.getImgUrl());
                    mDialog.setTitle( "Moment Gallery" );
                    mDialog.show();
                }catch (NullPointerException e){}
                Button delete= (Button) mDialog.findViewById( R.id.delete_moment_BT );
                delete.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteItem(momentsModel.getMonentId());
                    }
                } );
            }
        } );
        return view;
    }

    private void setImage(ImageView imageView, String imgUrl) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUrl, bmOptions);
        bmOptions.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imgUrl, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    public void deleteItem(int momentId){
        if(moementsDbManager.deleteMomentById(momentId)){
            Toast.makeText( mContext, "Delete Success", Toast.LENGTH_SHORT ).show();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
            mDialog.dismiss();
        }
    }



}
