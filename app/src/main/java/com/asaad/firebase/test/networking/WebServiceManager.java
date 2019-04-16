package com.asaad.firebase.test.networking;

import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeoutException;

public class WebServiceManager {

    public static void CallWebServiceOperation(final WebServiceManagerInterface  caller,
                                               final String  webServiceURL,
                                               final String resourceName,
                                               final String operation,
                                               final String methodType,
                                               final String payload,
                                               final String userState){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    //System.out.println("HOLAWBSERVICE");
                    URL url=new URL(webServiceURL);
                    HttpURLConnection httpURLConnection= (HttpURLConnection)url.openConnection();
                    //System.out.println("NOEXPLOTO");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setConnectTimeout(4000);
                    httpURLConnection.setReadTimeout(4000);
                    if(methodType.equals("POST")){
                        httpURLConnection.setRequestProperty("Content-Type", "application/json");
                        httpURLConnection.setRequestProperty("Accept", "application/json");
                    }
                    httpURLConnection.setRequestMethod(methodType);
                    httpURLConnection.getOutputStream().write(payload.getBytes());
                    int responseCode=httpURLConnection.getResponseCode();
                    if(responseCode==HttpURLConnection.HTTP_OK){
                        InputStream in=httpURLConnection.getInputStream();
                        StringBuffer stringBuffer=new StringBuffer();
                        int charIn=0;
                        while((charIn=in.read())!=-1){
                            stringBuffer.append((char)charIn);
                        }

                        System.out.println("Mensaje "+stringBuffer.toString());
                        caller.WebServiceMessageReceived(userState,new String(stringBuffer));
                        httpURLConnection.disconnect();

                    }
                    //httpURLConnection.getOutputStream().flush();
                } catch (SocketTimeoutException e){
                    System.out.println("NO CONNEXION");
                    caller.WebServiceMessageReceived("DOWN","Down");
                } catch (Exception error){
                    caller.WebServiceMessageReceived(userState,"Down");
                    System.out.println("NO CONexion");
                }
            }
        });
    }


}
