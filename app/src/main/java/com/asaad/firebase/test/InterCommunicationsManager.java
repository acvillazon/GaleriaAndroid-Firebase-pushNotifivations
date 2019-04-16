package com.asaad.firebase.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class InterCommunicationsManager extends BroadcastReceiver {

    Context appContext;
    BroadCastReceiverInterfaceCaller caller;

    public InterCommunicationsManager(Context appContext,BroadCastReceiverInterfaceCaller caller){
        this.appContext=appContext;
        this.caller=caller;
        this.registerToBroadcastReceiver();
    }

    public void registerToBroadcastReceiver(){

        IntentFilter filter = new IntentFilter("com.asaad.firebase.test.broadcast");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        appContext.registerReceiver(this, filter);
    }

    public void sendBroadcastMessage(String key,String message){
        try{
            Intent intentToBeSent=new Intent();
            intentToBeSent.putExtra(key,message);
            intentToBeSent.setAction("com.asaad.firebase.test.broadcast");
            intentToBeSent.addCategory(Intent.CATEGORY_DEFAULT);
            this.appContext.sendBroadcast(intentToBeSent);
        }catch(Exception error){

        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        caller.intentHasBeenReceivedThroughBroadcast(intent);

    }
}
