package com.akella.exua.test;

import java.io.IOException;

import org.json.JSONException;

import android.util.Log;

import com.akella.exua.BitmapSize;
import com.akella.exua.ExMoviePage;
import com.akella.exua.ExMovieSearchPage;
import com.akella.exua.ExPage;
import com.akella.exua.ExUaBrowser;
import com.akella.utils.StringUtils;

import junit.framework.TestCase;

public class ExUaBrowserTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}
	
	public void testMoviePage() throws Exception{
		ExMoviePage pData = new ExMoviePage();
		pData.Link = "/view/9412310?r=2,23775";
		
		ExUaBrowser.GetMovieData(pData);
		
		assertEquals("http://www.ex.ua/show/15052634/0bada51df8eb83d875692e67f5e521b1.flv", pData.FlvFullLink);
		assertEquals("fake", pData.Description);
	}
	
	public void testMovieDataParser()
	{
		ExMoviePage pData = new ExMoviePage();
		pData.Link = "/view/9412310?r=2,23775";
		try {
			ExUaBrowser.GetMovieDataParser(pData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals("http://www.ex.ua/show/15052634/0bada51df8eb83d875692e67f5e521b1.flv", pData.FlvFullLink);
	}
	
	public void testGetForeignMovies(){
		
		Log.i("Akella", "Start Regex");
		
		ExPage page = ExUaBrowser.GetForeignMovies(null);
		
		assertEquals(20, page.Movies.size());
		
		Log.i("Akella", "End Regex");
	}
	
	public void testGetForeignMoviesParser(){
		Log.i("Akella", "Start Parser");
		
		try{
		ExUaBrowser.GetForeignMoviesParser(null);
		}catch(Exception ex){
			assertEquals(null, ex);
		}
		
		
		
		Log.i("Akella", "End Parser");
	}
	
	public void testSearch(){
		ExMovieSearchPage searchPage = new ExMovieSearchPage();
		searchPage.Query = "hangover";
		
		Log.i("Akella", searchPage.GetLink());
		
		Log.i("Akella", "Start Search");
		ExUaBrowser.Search(searchPage);
		Log.i("Akella", "End Search");
		
		Log.i("Akella", Integer.toString(searchPage.Movies.size()));
		Log.i("Akella", searchPage.Movies.get(0).Title);
		Log.i("Akella", searchPage.Movies.get(0).Link);
		Log.i("Akella", searchPage.Movies.get(0).GetImageLink(BitmapSize.Small.size()));
		Log.i("Akella", searchPage.Movies.get(0).GetImageLink(BitmapSize.Middle.size()));
		
		assertEquals(20, searchPage.Movies.size());
	}
	
	public void testSearchHint(){
		String[] hints = ExUaBrowser.SearchHint("девушка");
		
		//Boolean haveResults = ;
		
		assertEquals(true, (hints.length > 0));
		
		
		
		Log.i("Akella", StringUtils.Implode(hints, ","));
	}
}
