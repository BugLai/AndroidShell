package com.shell;

import java.io.InputStream;
import android.annotation.SuppressLint;
import dalvik.system.DexClassLoader;

@SuppressLint("NewApi") 
public class CustomClassLoader extends DexClassLoader {

	private ClassLoader mClassLoader = null;

	  public CustomClassLoader(String paramString1, String paramString2, String paramString3, ClassLoader paramClassLoader)
	  {
	    super(paramString1, paramString2, paramString3, paramClassLoader.getParent());
	    this.mClassLoader = paramClassLoader;
	  }

	  public InputStream getResourceAsStream(String paramString)
	  {
	    return this.mClassLoader.getResourceAsStream(paramString);
	  }
	@Override
	protected Class<?> loadClass(String className, boolean resolve)
			throws ClassNotFoundException {
		return super.loadClass(className, resolve);
	}

	@Override
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		return super.loadClass(className);
	}
   
}
