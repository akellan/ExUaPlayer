package com.akella.exua;

import java.io.Serializable;
import java.util.List;

public class ExPage extends ExPageBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	public List<ExMoviePage> Movies;
	public int Number = 0;
	public int ResultsCount = 20;
	
	private String Link = "/view/2?r=23775&p=%1$s&per=%2$s";
	
	@Override
	public String GetLink()
	{
		return String.format(AppendBase(Link), Number, ResultsCount);
	}
}
