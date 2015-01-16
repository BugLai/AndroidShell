package com.shell;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.ArrayMap;

public class ProxyApplication extends Application {
	

	@SuppressLint("NewApi") 
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		File file = new File("/data/data/" + base.getPackageName() + "/.cache/");
		if (!file.exists()) {
			file.mkdirs();
		}
//	    try {
//			Runtime.getRuntime().exec("chmod 755 " + file.getAbsolutePath()).waitFor();
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//		
//			e1.printStackTrace();
//		}
		Util.copyJarFile(this);
		
		try {
			
//			Object currentActivityThread = RefInvoke.invokeStaticMethod("android.app.ActivityThread", "currentActivityThread",new Class[] {}, new Object[] {});
//			String packageName = getPackageName();
//			HashMap mPackages = null;
//			try {
//				mPackages = (HashMap)RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mPackages");
//			} catch (ClassCastException  e) {
//				mPackages =(ArrayMap) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mPackages");
//			}		
//			WeakReference wr = (WeakReference)mPackages.get(packageName);
			
			Object packageInfo = RefInvoke.getField(base, "mPackageInfo");
			CustomClassLoader dLoader = new CustomClassLoader("/data/data/"+ base.getPackageName() + "/.cache/classdex.jar", 
					"/data/data/"+ base.getPackageName() + "/.cache",
					"/data/data/"+ base.getPackageName() + "/.cache/", 
					base.getClassLoader());
			RefInvoke.setFieldOjbect("android.app.LoadedApk", "mClassLoader",packageInfo, dLoader);
		} catch (NoSuchFieldException e) {
			
			e.printStackTrace();
		}
		
	
	}

	@Override
	public void onCreate() {
		// 如果源应用配置有Appliction对象，则替换为源应用Applicaiton，以便不影响源程序逻辑。  
        String appClassName = null;  
        try {  
            ApplicationInfo ai = this.getPackageManager().getApplicationInfo(this.getPackageName(),  PackageManager.GET_META_DATA);  
            Bundle bundle = ai.metaData;  
            if (bundle != null  && bundle.containsKey("APPLICATION_CLASS_NAME")) {  
                appClassName = bundle.getString("APPLICATION_CLASS_NAME");  
            } else {  
                return;  
            }  
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
        }  
        
        Object currentActivityThread = RefInvoke.invokeStaticMethod(  "android.app.ActivityThread", "currentActivityThread",new Class[] {}, new Object[] {});  
        Object mBoundApplication = RefInvoke.getFieldOjbect(  "android.app.ActivityThread", currentActivityThread,  "mBoundApplication");  
        Object loadedApkInfo = RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication, "info");  
        RefInvoke.setFieldOjbect("android.app.LoadedApk", "mApplication", loadedApkInfo, null);  
        Object oldApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mInitialApplication");  
        ArrayList<Application> mAllApplications = (ArrayList<Application>) RefInvoke.getFieldOjbect("android.app.ActivityThread",currentActivityThread, "mAllApplications");  
        mAllApplications.remove(oldApplication);  
        ApplicationInfo appinfo_In_LoadedApk = (ApplicationInfo) RefInvoke.getFieldOjbect("android.app.LoadedApk", loadedApkInfo,"mApplicationInfo");  
        ApplicationInfo appinfo_In_AppBindData = (ApplicationInfo) RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData",mBoundApplication, "appInfo");  
        appinfo_In_LoadedApk.className = appClassName;  
        appinfo_In_AppBindData.className = appClassName;  
        Application app = (Application) RefInvoke.invokeMethod( "android.app.LoadedApk", "makeApplication", loadedApkInfo, new Class[] { boolean.class, Instrumentation.class },  new Object[] { false, null });  
        RefInvoke.setFieldOjbect("android.app.ActivityThread",  "mInitialApplication", currentActivityThread, app);  
        HashMap mProviderMap = (HashMap) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mProviderMap");  
        Iterator it = mProviderMap.values().iterator();  
        while (it.hasNext()) {  
            Object providerClientRecord = it.next();  
            Object localProvider = RefInvoke.getFieldOjbect("android.app.ActivityThread$ProviderClientRecord",providerClientRecord, "mLocalProvider");  
            RefInvoke.setFieldOjbect("android.content.ContentProvider","mContext", localProvider, app);  
        }  
        app.onCreate();  
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

}
