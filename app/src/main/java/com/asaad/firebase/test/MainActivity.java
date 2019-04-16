package com.asaad.firebase.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.asaad.firebase.test.networking.WebServiceManager;
import com.asaad.firebase.test.networking.WebServiceManagerInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements BroadCastReceiverInterfaceCaller, WebServiceManagerInterface {

    InterCommunicationsManager interCommunicationsManager=null;
    RecyclerView recyclerView;
    public JSONArray jsonArrayImage;
    RecyclerView.LayoutManager layoutManager;
    public AdapterImage adapterImage;

    public String GET_IMAGE = "https://us-central1-fir-movil-224dc.cloudfunctions.net/images";
    public String JOIN_TO_GROUP_NOTIFICATION = "https://us-central1-fir-movil-224dc.cloudfunctions.net/createGroup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GetToken();
        WebServiceManager.CallWebServiceOperation(this, GET_IMAGE,
                "user",
                "getImages",
                "GET",
                "Images",
                "getImages");

        jsonArrayImage = new JSONArray();
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView = findViewById(R.id.rv_images);
        recyclerView.setLayoutManager(layoutManager);
        adapterImage = new AdapterImage(getApplicationContext(),jsonArrayImage);

        recyclerView.setAdapter(adapterImage);
        interCommunicationsManager=new InterCommunicationsManager(this,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            GetToken();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void GetToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("GetToken", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "Token:"+ token;
                        Log.d("GetTokenAndresVillazon", msg);
                        JoinGroupPushNotification(token);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void JoinGroupPushNotification(String token){

        try{
            JSONObject TokenNotification = new JSONObject();
            TokenNotification.put("user_key",token);


            WebServiceManager.CallWebServiceOperation(this, JOIN_TO_GROUP_NOTIFICATION,
                    "user",
                    "pushToken",
                    "POST",
                    TokenNotification.toString(),
                    "pushToken");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"EXPLOTO",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void intentHasBeenReceivedThroughBroadcast(Intent intent) {
        String new_image = intent.getStringExtra("image");
        Toast.makeText(this,"Se ha añadido una nueva imagen",Toast.LENGTH_LONG).show();
        jsonArrayImage.put(new_image);
        Log.d(TAG, "MAIN" + jsonArrayImage);
        adapterImage.notifyItemChanged(jsonArrayImage.length()-1);

    }

    @Override
    public void WebServiceMessageReceived(final String userState, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(userState.equals("getImages")){
                    try {
                        JSONObject obj = new JSONObject(message);
                        JSONArray ja= (JSONArray) obj.get("imagesUrls");
                        jsonArrayImage = ja;

                        if(ja.length()>0){
                            Toast.makeText(getApplicationContext(),"Se han cargado todas las imagenes",Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getApplicationContext(),"No se han encontrado imagenes para cargar",Toast.LENGTH_LONG).show();

                        }
                        adapterImage = new AdapterImage(getApplicationContext(),jsonArrayImage);
                        recyclerView.setAdapter(adapterImage);
                        adapterImage.notifyDataSetChanged();
                        Log.d(TAG, "ZAZAZAZA"+ja.length());

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Fallo durante la conversion StringToJson",Toast.LENGTH_LONG).show();

                    }
                }else if(userState.equals("pushToken")){
                    //Toast.makeText(getApplicationContext(),"Se ha unido al Pull de notificaciones!",Toast.LENGTH_LONG).show();
                }else if(userState.equals("DOWN")){
                    Toast.makeText(getApplicationContext(),"Servidor sin respuesta",Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
