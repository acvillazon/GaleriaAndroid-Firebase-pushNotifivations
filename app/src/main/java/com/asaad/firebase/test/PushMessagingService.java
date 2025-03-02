package com.asaad.firebase.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class PushMessagingService extends FirebaseMessagingService {
    public PushMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendBroadcastMessage("Push",remoteMessage.getNotification().getBody());
        }else{
            try {
                JSONObject object = new JSONObject(remoteMessage.getData());
                String new_image = object.get("new_image_url").toString();
                Log.d(TAG, "Message data payload:URGENTEEE" + new_image);
                sendBroadcastMessage("image",new_image);
            }catch (Exception e){

            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }

    public void sendBroadcastMessage(String key,String message){
        try{
            Intent intentToBeSent=new Intent();
            intentToBeSent.putExtra(key,message);
            intentToBeSent.setAction("com.asaad.firebase.test.broadcast");
            intentToBeSent.addCategory(Intent.CATEGORY_DEFAULT);
            this.sendBroadcast(intentToBeSent);
        }catch(Exception error){

        }
    }


}
