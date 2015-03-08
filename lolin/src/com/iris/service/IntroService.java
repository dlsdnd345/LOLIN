package com.iris.service;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iris.config.Config;
import com.iris.entities.Reple;
import com.iris.entities.UpdateBoard;
import com.iris.libs.TrippleDes;
import com.iris.util.SignatureUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by woong on 2015. 3. 8..
 */
public class IntroService {

    Gson gson ;
    PackageInfo packageInfo = null;

    public IntroService(Context context){

        gson = new Gson();

        try {
            packageInfo =  context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * 앱 업데이트 데이터 받기 위한 서브 url 반환
     * @return
     */
    public String updateAppCheck(){

        String versionName =  packageInfo.versionName;

        String hash = null;
        String encodeHash = null;
        String encodeVersionName = null;

        try {

            String signatureData = versionName + Config.KEY.SECRET;
            hash = SignatureUtil.getHash(signatureData);


            encodeHash = URLEncoder.encode(hash,"UTF-8");
            encodeVersionName = URLEncoder.encode(versionName,"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "?versionName="+encodeVersionName+"&hash="+encodeHash;
    }

    /**
     * 앱 업데이트 데이의 댓글 파싱
     * @param jsonData
     * @return
     */
    public UpdateBoard parseUpdateAppCheck(String jsonData){

        Log.i("jsonData  : " , jsonData);

        JSONObject JsonObject;
        String ok 	= null;
        String data = null;

        UpdateBoard updateBoardGson = null;

        try {
            JsonObject = new JSONObject(jsonData);
            ok = JsonObject.getString(Config.FLAG.OK);
            if(ok.equals(Config.FLAG.TRUE)){
                data = JsonObject.getString(Config.FLAG.DATA);
                Type type = new TypeToken<UpdateBoard>(){}.getType();
                updateBoardGson = gson.fromJson(data, type);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return updateBoardGson;
    }

}
