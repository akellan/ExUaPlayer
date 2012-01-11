package com.akella.exua;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.util.Log;





public class ExMoviePage extends ExPageBase implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String Title;
	public String Link;
	public String FlvFullLink;
	private String ImageLink;
	public transient Bitmap Image;
	public String Description;
	public String StoreFileName;
	
	private Boolean IsHaveLink;
	
	@Override
	public String GetLink() {
		
		return AppendBase(Link);
		
	}
	
	public String GetImageLink(int size)
	{
		return String.format("%2$s?%1$s", size , ImageLink);		
	}
	
	public void SetImageLink(String link)
	{
		Log.i("Akella", link);
		if(link != null && link.trim() != "" )
			ImageLink = link.substring(0, link.lastIndexOf("?"));
	}

	public Boolean getIsHaveLink() {
		return IsHaveLink;
	}

	public void setIsHaveLink(Boolean isHaveLink) {
		IsHaveLink = isHaveLink;
	}

	
}
