package com.weishu.upf.ams_pms_hook.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Process;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * @author weishu
 * @date 16/3/7
 */
public class MainActivity extends Activity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
    }

    // 这个方法比onCreate调用早; 在这里Hook比较好.
    @Override
    protected void attachBaseContext(Context newBase) {
        HookHelper.hookActivityManager();
        HookHelper.hookPackageManager(newBase);
        super.attachBaseContext(newBase);
    }

    private List get(int uid){
        Class c = null;
        List list = null;
        try {
            c = Class.forName("android.app.ApplicationPackageManager");
            Method m = c.getMethod("getInstalledPackages", new Class[] {int.class,int.class});

            if (!m.isAccessible()) {
                m.setAccessible(true);  //当私有方法时，设置可访问
            }
            Class uh = Class.forName("android.os.UserHandle");

            Method method = uh.getMethod("getUserId", new Class[]{int.class});
            if (!method.isAccessible()) {
                method.setAccessible(true);  //当私有方法时，设置可访问
            }
            int id = Binder.getCallingUid() /100000;

            Object object = m.invoke(getPackageManager(),new Object[]{0,0});

            if(object != null){
                list = (List) object;
                Log.d(TAG,"get done "+list.size());
            }else {
                Log.d(TAG,"get done null");
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return  list;
    }

    private void bind(){
        Intent intent = new Intent(MainActivity.this,MyService.class);

        startService(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:

               bind();
                break;
            case R.id.btn2:

//                 测试PMS HOOK (调用其相关方法)
                List list = getPackageManager().getInstalledPackages(0);

                int uid = Process.myUid();
//                List list = null;
//
                ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

                List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(Integer.MAX_VALUE);
                for (ActivityManager.RunningServiceInfo service : runningServices) {
                    String appName;
                    try {
                        appName = getPackageManager().getApplicationInfo(service.process, 0).loadLabel(getPackageManager()).toString();
                    } catch (PackageManager.NameNotFoundException e) {
                        appName = null;
                    }

                    Log.d(TAG,appName+" "+service.process+" "+service.uid + " "+service.pid);


                    list = get(service.uid);
                    if(list != null && list.size() > 0){
                        break;
                    }

                    long ulBytes = TrafficStats.getUidTxBytes(uid);
                    long dlBytes = TrafficStats.getUidRxBytes(uid);
                }


                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);


//                List<ActivityManager.RunningAppProcessInfo> list1 =manager.getRunningExternalApplications();
//                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo :list1){
//                    list = get(runningAppProcessInfo.uid);
//                    if(list != null && list.size() > 0){
//                        break;
//                    }
//                }break

                if(list != null){

                    Toast.makeText(MainActivity.this,"size "+list.size(),Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(MainActivity.this,"0 ",Toast.LENGTH_SHORT).show();

                }




//                bind();



                int i=0;

//                Toast.makeText(MainActivity.this,list.size() + "",Toast.LENGTH_SHORT).show();

                break;
        }





    }
}
