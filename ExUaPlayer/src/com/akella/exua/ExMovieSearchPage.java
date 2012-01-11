package com.akella.exua;

import java.io.Serializable;
import java.util.List;

import android.net.Uri;

public class ExMovieSearchPage extends ExPageBase implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String Query;
	public List<ExMoviePage> Movies;
	public int ResultsCount = 20;
	public int Number = 0;
	
	private String Link = "/search?s=%1$s&original_id=2&per=%3$s&p=%2$s";

	@Override
	public String GetLink() {
		return String.format(AppendBase(Link), Uri.encode(Query), Number, ResultsCount);
	}
}
