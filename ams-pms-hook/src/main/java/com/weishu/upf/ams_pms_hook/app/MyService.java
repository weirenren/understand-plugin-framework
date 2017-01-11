package com.weishu.upf.ams_pms_hook.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by weichao13 on 2016/12/20.
 */

public class MyService extends Service {

    public static final String TAG = "MyService";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        测试PMS HOOK (调用其相关方法)
                List list = getPackageManager().getInstalledPackages(0);



        if(list != null){

            Toast.makeText(this,"size "+list.size(),Toast.LENGTH_SHORT).show();


        }else {
            Toast.makeText(this,"0 ",Toast.LENGTH_SHORT).show();

        }

        Log.d(TAG,"onCreate");
    }
}
