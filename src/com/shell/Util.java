package com.shell;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import android.annotation.SuppressLint;
import android.content.Context;

public class Util {
	@SuppressLint("NewApi") 
	public static void copyJarFile(Context paramContext)
	  {
	    String str = "/data/data/" + paramContext.getPackageName() + "/.cache/classdex.jar";
	    try
	    {
	      JarFile localJarFile = new JarFile(paramContext.getApplicationInfo().publicSourceDir);
	      ZipEntry localZipEntry = localJarFile.getEntry("assets/classdex.jar");
	      File localFile = new File(str);
	      byte[] arrayOfByte = new byte[65536];
	      BufferedInputStream localBufferedInputStream = new BufferedInputStream(localJarFile.getInputStream(localZipEntry));
	      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localFile));
	      while (true)
	      {
	        int i = localBufferedInputStream.read(arrayOfByte);
	        if (i <= 0)
	        {
	          localBufferedOutputStream.flush();
	          localBufferedOutputStream.close();
	          localBufferedInputStream.close();
	          return;
	        }
	        localBufferedOutputStream.write(arrayOfByte, 0, i);
	      }
	    }
	    catch (Exception localException)
	    {
	      localException.printStackTrace();
	    }
	  }
	
	public static boolean assetToFile(Context context) {
		
		String name = "classdex.jar";
		File dir = context.getDir("libs", 0);
		File soFile = new File(dir,name);
		boolean copyFinish = false;
		if (!soFile.exists()) {
			try {	
				InputStream in = null;
				in = context.getAssets().open(name);
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
	
	  private static byte[] readFileBytes(File file) throws IOException {  
	        byte[] arrayOfByte = new byte[1024];  
	        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();  
	        FileInputStream fis = new FileInputStream(file);  
	        while (true) {  
	            int i = fis.read(arrayOfByte);  
	            if (i != -1) {  
	                localByteArrayOutputStream.write(arrayOfByte, 0, i);  
	            } else {  
	                return localByteArrayOutputStream.toByteArray();  
	            }  
	        }  
	    }  
}
