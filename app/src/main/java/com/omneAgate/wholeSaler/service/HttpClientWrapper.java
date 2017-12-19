package com.omneAgate.wholeSaler.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.omneAgate.wholeSaler.DTO.EnumDTO.RequestType;
import com.omneAgate.wholeSaler.DTO.EnumDTO.ServiceListenerType;
import com.omneAgate.wholeSaler.Util.Constants.WholeSaleConstants;
import com.omneAgate.wholeSaler.Util.SessionId;
import com.omneAgate.wholeSaler.Util.WholesaleDBHelper;
import com.omneAgate.wholeSaler.activity.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;


public class HttpClientWrapper {


    /*Used to send http request to FPS server and return the data back*/
    public void sendRequest(final String requestData, final Bundle extra,
                            final ServiceListenerType what, final Handler messageHandler,
                            final RequestType method, final StringEntity entity, final Context context) {

         Log.e("HttpClientWrapper","<=== sendRequest called ====>");
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                BufferedReader in = null;
                Message msg = Message.obtain();
                msg.obj = what;
                try {

                   //String url = GlobalAppState.serverUrl + requestData;

                    String serverUrl = WholesaleDBHelper.getInstance(context).getMasterData("serverUrl");
                    String url = serverUrl + requestData;
                    URI website = new URI(url);

                    HttpResponse response = requestType(website, method, entity);
                    in = new BufferedReader(new InputStreamReader(response
                            .getEntity().getContent()));
                    StringBuffer sb = new StringBuffer("");
                    String l;
                    String nl = System.getProperty("line.separator");
                    while ((l = in.readLine()) != null) {
                        sb.append(l + nl);
                    }
                    in.close();
                    String responseData = sb.toString();
                    Log.e("HttpClientWrapper", responseData);
                    Bundle b = new Bundle();
                    if (extra != null)
                        b.putAll(extra);
                    if (responseData.trim().length() != 0) {
                        b.putString(WholeSaleConstants.RESPONSE_DATA, responseData);
                    } else {
                        msg.obj = ServiceListenerType.ERROR_MSG;
                        b.putString(WholeSaleConstants.RESPONSE_DATA, "Empty Data");
                    }
                    msg.setData(b);
                } catch (SocketTimeoutException e) {
                    Log.e("HttpClientWrapper", "SocketTimeoutException", e);
                    msg.obj = ServiceListenerType.ERROR_MSG;
                    Bundle b = new Bundle();
                    if (extra != null)
                        b.putAll(extra);
                    b.putString(WholeSaleConstants.RESPONSE_DATA,
                            "Cannot establish connection to server. Please try again later.");
                    msg.setData(b);
                } catch (SocketException e) {
                    Log.e("HttpClientWrapper", "SocketException", e);
                    msg.obj = ServiceListenerType.ERROR_MSG;
                    Bundle b = new Bundle();
                    if (extra != null)
                        b.putAll(extra);
                    b.putString(WholeSaleConstants.RESPONSE_DATA,
                    context.getString(R.string.connectionError));
                    msg.setData(b);
                } catch (IOException e) {
                    Log.e("HttpClientWrapper", "IOException", e);
                    msg.obj = ServiceListenerType.ERROR_MSG;
                    Bundle b = new Bundle();
                    if (extra != null)
                        b.putAll(extra);
                    b.putString(WholeSaleConstants.RESPONSE_DATA, ""
                            + "Internal Error.Please Try Again");
                    msg.setData(b);
                }


                catch (Exception e) {
                    Log.e("HttpClientWrapper", "Exception", e);
                    msg.obj = ServiceListenerType.ERROR_MSG;
                    Bundle b = new Bundle();
                    if (extra != null)
                        b.putAll(extra);
                    b.putString(WholeSaleConstants.RESPONSE_DATA, context.getString(R.string.connectionRefused));
                    msg.setData(b);
                } finally {
                    Log.e("HttpClientWrapper","<=== sendRequest End ====>");
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (Exception e) {
                        Log.e("HTTP", "Error", e);
                    }
                    messageHandler.sendMessage(msg);
                }

            }
        }.start();

    }

    /*return http GET,POST and PUT method using parameters*/
    private HttpResponse requestType(URI website, RequestType method,
                                     StringEntity entity) {
        try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 50000;
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    timeoutConnection);
            int timeoutSocket = 50000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient client = new DefaultHttpClient(httpParameters);

            switch (method) {
                case POST:
                    HttpPost postRequest = new HttpPost();
                    postRequest.setURI(website);
                    postRequest.setHeader("Content-Type", "application/json");
                    postRequest.addHeader("Cookie", "JSESSIONID=" + SessionId.getInstance().getSessionId());
                    postRequest.setHeader("Cookie", "SESSION=" + SessionId.getInstance().getSessionId());
                    Log.e("SessionId", "" + SessionId.getInstance().getSessionId());
                    postRequest.setEntity(entity);
                    return client.execute(postRequest);
                case PUT:
                    HttpPut putRequest = new HttpPut();
                    putRequest.setURI(website);
                    putRequest.setHeader("Content-Type", "application/json");
                    putRequest.setHeader("Cookie", "JSESSIONID:" + null);
                    putRequest.setHeader("Cookie", "SESSION=" + SessionId.getInstance().getSessionId());
                    putRequest.setEntity(entity);
                    return client.execute(putRequest);
                case GET:
                    HttpGet getRequest = new HttpGet();
                    HttpContext localContext = new BasicHttpContext();
                    getRequest.addHeader("Content-Type", "application/json");
                    getRequest.addHeader("Cookie", "JSESSIONID=" + SessionId.getInstance().getSessionId());
                    getRequest.setHeader("Cookie", "SESSION=" + SessionId.getInstance().getSessionId());
                    Log.e("GET", "GET Method");
                    Log.e("SessionId", "" + SessionId.getInstance().getSessionId());
                    getRequest.setURI(website);
                    return client.execute(getRequest);

            }

        } catch (Exception e) {
            Log.e("Error", e.toString(), e);
        }
        return null;
    }

}