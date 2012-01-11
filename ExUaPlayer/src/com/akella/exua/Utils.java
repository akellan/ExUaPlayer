package com.akella.exua;

import java.io.InputStream;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class Utils {

	public static void closeStreamQuietly(InputStream inputStream) {
		
		try {
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void StdErrorBox(Context ctx, String msg)
	{
		AlertDialog.Builder builder = new Builder(ctx);
		
		builder.setMessage(msg)
			.setCancelable(true);
		AlertDialog dialog = builder.create();
		dialog.show();
	
	}
}
