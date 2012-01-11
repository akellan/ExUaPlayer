package com.akella.exua;

import java.util.LinkedHashMap;

import android.graphics.Bitmap;

public class SyncBitmapCache {
	private static LinkedHashMap<String, Bitmap> bitmapCache = new LinkedHashMap<String, Bitmap>();
	
	public static void add(String url, Bitmap bitmap)
	{
		if(bitmap != null)
		{
			if(!bitmapCache.containsKey(url)){
				synchronized (bitmapCache) {
					if(!bitmapCache.containsKey(url))
						bitmapCache.put(url, bitmap);
				}
			}
		}
	}
	
	public static Bitmap fetch(String url)
	{
		synchronized (bitmapCache) {
			final Bitmap bitmap = bitmapCache.get(url);
			
			//if(bitmap != null){
				//bitmapCache.remove(url);
				//bitmapCache.put(url, bitmap);
			//}
				
			return bitmap;
		}
	}
	
}
