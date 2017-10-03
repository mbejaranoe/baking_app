package com.example.android.mbejaranoe.bakingapp.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Manolo on 02/10/2017.
 * Class storing the steps data
 */

public class Step {

    private final String LOG_TAG = Step.class.getSimpleName();

    /* Member variable for the step id */
    int mId;

    /* Member variable for the short description */
    String mShortDescription;

    /* Member variable for the long description */
    String mDescription;

    /* Member variable for video URL */
    String mVideoURL;

    /* Member variable for the thumbnail URL */
    String mThumbnailURL;

    /* Constructors */
    public Step(){
        mId = 0;
        mShortDescription = null;
        mDescription = null;
        mVideoURL = null;
        mThumbnailURL = null;
    }

    public Step(JSONObject jsonObject){
        try {
            mId = jsonObject.getInt("id");
            mShortDescription = jsonObject.getString("shortDescription");
            mDescription = jsonObject.getString("description");
            mVideoURL = jsonObject.getString("videoURL");
            mThumbnailURL = jsonObject.getString("thumbnailURL");
        } catch (JSONException e){
            Log.e(LOG_TAG, "Invalid Json object");
        }
    }

    /* Helper method for handling Step objects */
    public JSONObject getJSonFromStep(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", mId);
            jsonObject.put("shortDescription", mShortDescription);
            jsonObject.put("description", mDescription);
            jsonObject.put("videoURL", mVideoURL);
            jsonObject.put("thumbnailURL", mThumbnailURL);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Invalid Json object");
        }
        return jsonObject;
    }

    /*public String getStringFromJson(){
        String stepString;

    }
    */

    /* Setter methods */
    public void setId(int id){
        mId = id;
    }

    public void setShortDescription(String shortDescription){
        mShortDescription = shortDescription;
    }

    public void setDescription(String description){
        mDescription = description;
    }

    public void setVideoURL(String videoURL){
        mVideoURL = videoURL;
    }

    public void setThumbnailURL(String thumbnailURL){
        mThumbnailURL = thumbnailURL;
    }

    /* Getter methods */
    public int getId(){
        return  mId;
    }

    public String getShortDescription(){
        return mShortDescription;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getVideoURL(){
        return mVideoURL;
    }

    public String getThumbnailURL(){
        return mThumbnailURL;
    }
}
