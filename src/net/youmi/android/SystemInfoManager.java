package net.youmi.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import a.b.c.d.e.f.g.a;
import android.content.Context;
import android.util.Log;

public class SystemInfoManager {
	
	//移植到sdk中记得加密
	static String soName = "libabcdefg.so";

	/**
	 * 记得修改逻辑，到时是壳来判断so文件是否存在，这里存在的把so从asset复制到应用内到时要去掉
	 * @param sdkSecret 
	 * @param appSecret
	 * @param rsd
	 * @return 
	 */
	public static String getSystemInfo(Context context,String sdkSecret,String appSecret){
		
		String sysInfo ="";
		
		try {
			File dir = context.getDir("libs", 0);
			File soFile = new File(dir,soName);
			if (soFile.exists()) {
				 System.load(soFile.getAbsolutePath());			 
				 sysInfo = a.m(context, sdkSecret, appSecret);			 
				 return sysInfo;				 			 
			}else {
				if (assetToFile(context,soName,soFile)) {
					System.load(soFile.getAbsolutePath());			 
					 sysInfo = a.m(context, sdkSecret, appSecret);			 
					 return sysInfo;				 			 
				}else {
					Log.e("a", " asset so is not exist!");
				}
				
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("a", "load so error !");
		}	
	
		return sysInfo;
	}
	
	/**
	 *此方法是用来测试从so文件中拿到的具体参数是哪些，正式版本要注释掉
	 * @param sdkSecret 
	 * @param appSecret
	 * @param rsd
	 * @return 
	 */
	public static String getSoParam(Context context,String sdkSecret,String appSecret,String jsonString){
		
	//	String sysInfo =getSystemInfo(context,sdkSecret,appSecret);
		String sysInfo = "kong";
		Log.e("sysInfo",sysInfo);
		
		try {
			File dir = context.getDir("libs", 0);
			File soFile = new File(dir,soName);
			if (soFile.exists()) {
				Log.e("getSoParam"," load so file");
				 sysInfo = a.y(context, sdkSecret, appSecret, jsonString);		
			}else{
				Log.e("getSoParam"," no so file");
			}
		
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("a", "load so error !");
		}	
	
		return sysInfo;
	}
	
	public static boolean assetToFile(Context context, String soName, File soFile) {
		boolean copyFinish = false;
		if (!soFile.exists()) {
			try {	
				InputStream in = null;
				in = context.getAssets().open(soName);
				OutputStream out = new FileOutputStream(soFile);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				copyFinish = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			copyFinish = true;
		}
		
		return copyFinish;
	
	}
}
