package a.b.c.d.e.f.g;

import android.content.Context;

public class a {

	
	
//	static {
//		System.loadLibrary("abcdefg");
//	}
	
	
	/**
	 * @param a->sdkSecret
	 * @param b->appSecret
	 * 此方法用于传入密钥参数,获取so层参数加密后返回
	 * return 密串
	 */
	public native static String m(Context  context,String a,String b);
	
	
	/**
	 * @param a->sdkSecret
	 * @param b->appSecret
	 * @param c->rsd;
	 * @param c->密串
	 * 此方法用于测试解密后的so层参数,正式版本要注释掉
	 * return 原始参数
	 * 
	 */
	public native static String y(Context context,String a,String b,String c);
	
	
	
	
	/*
	 * 此方法用于测试升级接口
	 */
	public native static String testupdate();

}
