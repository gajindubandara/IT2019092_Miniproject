package com.example.it2019092_miniproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class PreLoader {

    private Activity activity;
    private AlertDialog dialog;

    public PreLoader(Activity myActivity){

        activity =myActivity;
    }

    public void  startLoadingDialog(){

        AlertDialog.Builder builder =new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading,null));
        builder.setCancelable(false);

        dialog =builder.create();
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }
}