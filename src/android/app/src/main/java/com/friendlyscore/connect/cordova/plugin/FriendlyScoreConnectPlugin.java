package com.friendlyscore.connect.cordova.plugin;

import android.content.Intent;

import com.friendlyscore.base.Environments;
import com.friendlyscore.ui.obp.FriendlyScoreView;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendlyScoreConnectPlugin extends CordovaPlugin {

    public CordovaInterface cordovaInterface;
    CallbackContext callbackContext;
    String client_id = "client_id";
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        this.cordovaInterface = cordova;
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("startFriendlyScoreConnect")) {
            userReference = args.getString(0);
            this.callbackContext = callbackContext;
            this.startFriendlyScoreConnect(userReference);
            cordova.setActivityResultCallback (this);
            return true;
        }
        return true;
    }


    /**
     In order to initialize FriendlyScore for your user you must have the `userReference` for that user.
     The `userReference` uniquely identifies the user in your systems.
     This `userReference` can then be used to access information from the FriendlyScore [api](https://friendlyscore.com/developers/api).
     */
    public String userReference = "your_user_reference";
    /**
     * Declare the environment to use the FriendlyScore Connect.
     * The client_id declared in gradle.properties must be for the same environment
     */
    public Environments environment = Environments.PRODUCTION;

    /**
     In order to listen when the user returns from the FriendlyScoreView in your `onActivityResult`, you must provide the `requestcode` that you will be using.
     */
    public static final int REQUEST_CODE_FRIENDLY_SCORE = 11;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_FRIENDLY_SCORE){
            JSONObject object = new JSONObject();
            PluginResult  pluginResult = new PluginResult(PluginResult.Status.ERROR,object);

            if(data!=null){
                //Present if there was error in creating an access token for the supplied userReference.
                if(data.hasExtra("userReferenceAuthError")){
                     object = new JSONObject();
                    try {
                        object.put("userReferenceAuthError",true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pluginResult = new PluginResult(PluginResult.Status.ERROR,object);
                    //Do Something
                }

                //Present if there was service denied.
                if( data.hasExtra("serviceDenied")){
                     object = new JSONObject();
                    if(data.hasExtra("serviceDeniedMessage")){
                        String serviceDeniedMessage = data.getStringExtra("serviceDeniedMessage");
                        if(serviceDeniedMessage!=null) {

                            try {
                                object.put("serviceDenied",true);

                                object.put("serviceDeniedMessage",serviceDeniedMessage);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    pluginResult = new PluginResult(PluginResult.Status.ERROR,object);
                }
                //Present if the configuration on the server is incomplete.
                if(data!=null && data.hasExtra("incompleteConfiguration")){
                     object = new JSONObject();
                    if(data.hasExtra("incompleteConfigurationMessage")){
                        String errorDescription = data.getStringExtra("incompleteConfigurationMessage");
                        if(errorDescription!=null){
                            try {
                                object.put("incompleteConfiguration",true);

                                object.put("incompleteConfigurationMessage",errorDescription);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    pluginResult = new PluginResult(PluginResult.Status.ERROR,object);

                }
                //Present if there was error in obtaining configuration from server
                if(data.hasExtra("serverError")){
                     object = new JSONObject();
                    try {
                        object.put("serverError",true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pluginResult = new PluginResult(PluginResult.Status.ERROR,object);


                    //Try again later
                }
                //Present if the user closed the flow
                if(data.hasExtra("userClosedView")){

                     object = new JSONObject();
                    try {
                        object.put("userClosedView",true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pluginResult = new PluginResult(PluginResult.Status.OK,object);
                }
                pluginResult.setKeepCallback(false);
                this.callbackContext.sendPluginResult(pluginResult);
            }

        }

    }

    public void startFriendlyScoreConnect(final String userReference) {
        cordovaInterface.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                FriendlyScoreView.Companion.startFriendlyScoreView( cordovaInterface.getActivity(),  client_id, userReference, REQUEST_CODE_FRIENDLY_SCORE, environment);
            }
        });
    }

}
