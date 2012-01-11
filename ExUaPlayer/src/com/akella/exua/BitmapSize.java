package com.akella.exua;

public enum BitmapSize {
	Small (100),
	Middle (200);
	
	private final int size;
	
	private BitmapSize(int _size) {
		size = _size;
	} 
	
	public int size()
	{
		return size;
	}
}
