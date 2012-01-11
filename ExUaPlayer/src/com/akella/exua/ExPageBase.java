package com.akella.exua;



public abstract class ExPageBase {
	protected static final String _basePath = "http://www.ex.ua";
	
	protected static String AppendBase(String path) {
		return _basePath + path;
	}
	
	public abstract String GetLink();
	
	private Boolean isContentLoaded = false;
	
	public Boolean IsContentLoaded(){
		return isContentLoaded;
	}
	
	public void SetContentLoaded()
	{
		isContentLoaded = true;
	}
}
