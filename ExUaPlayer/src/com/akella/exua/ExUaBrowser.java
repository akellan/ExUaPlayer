package com.akella.exua;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.Html;



public class ExUaBrowser {
	
	final static Pattern _movieFlvLink = Pattern.compile("(.*)(http:\\/\\/www.ex.ua[^\"]+?.flv)");
	
	final static Pattern _movieLink = Pattern.compile(".*?<td align=center valign=center><a href='([^']+)'.*?src='([^']+)'.*?alt='([^']+)'", Pattern.DOTALL);
	
	final static Pattern _movieDescription = Pattern.compile("(.*)(��������|� ������)[^:]*?:[A-Z<>/a-z]*([^<]+)");
	
	final static Pattern _movieLinkSearch = Pattern.compile(".*?<td><a href='([^']+)'.*?src='([^']+)'.*?alt='([^']+)'", Pattern.DOTALL);
	
	final static String _searchHint = "http://www.ex.ua/r_search_hint?s=%1$s";
	
	public static ExPage GetForeignMovies(ExPage page)
	{
		ExPage _result = page == null ? new ExPage() : page;
		
		HttpRetriever _http = new HttpRetriever();
				
		String pageRawData = _http.retrieve(_result.GetLink());
		
		
		
		Matcher _matches = _movieLink.matcher(pageRawData);
		
		ArrayList<ExMoviePage> movies = new ArrayList<ExMoviePage>();
		
		
		while(_matches.find())
		{
			ExMoviePage mPage = new ExMoviePage();
			
			mPage.SetImageLink(_matches.group(2));
			
			mPage.Link = _matches.group(1);
			mPage.Title = Html.fromHtml(_matches.group(3)).toString();
		
						
			
			movies.add(mPage);
		}
				
		
		_result.Movies = movies;
		
		_result.SetContentLoaded();
		
		return _result;
	}
	
	public static ExPage GetForeignMoviesParser(ExPage page) throws IOException
	{
		ExPage _result = page == null ? new ExPage() : page;
		
		Document doc = null;
		
			
		doc = Jsoup.connect(_result.GetLink()).get();
			
			
		Elements els = doc.select("table.include_0 td > a");
		
		_result.Movies = new ArrayList<ExMoviePage>();
		
		for (Element element : els) {
			
			ExMoviePage mPage = new ExMoviePage();
			
			mPage.Link = element.attr("href");
			Element child = element.child(0);
			mPage.SetImageLink(child.attr("src"));
			mPage.Title = child.attr("alt");
			
			_result.Movies.add(mPage);
		}
		
		return _result;
	}
	
	public static void GetMovieData(ExMoviePage page) throws Exception
	{
		if(page == null)
			throw new Exception("Wrong argument 'page'");
		
		HttpRetriever _http = new HttpRetriever();
		String pageRawData = _http.retrieve(page.GetLink());
		
		Matcher _matches = _movieFlvLink.matcher(pageRawData);
		
		if(_matches.find())
		{	
			page.FlvFullLink = _matches.group(2);
		}
		
		Matcher _descMatch = _movieDescription.matcher(pageRawData);
		
		
		if(_descMatch.find()){
			page.Description = Html.fromHtml(_descMatch.group(3)).toString();
		}
		
		page.SetContentLoaded();
	}
	
	public static void GetMovieDataParser(ExMoviePage page) throws IOException, JSONException
	{
		Document doc = null;
				
		
		doc = Jsoup.connect(page.GetLink()).get();
		
		
		Elements els = doc.select("span.r_button_small + script");
		
		if(els.size() > 0){
		
			String script_text = els.get(0).data();
			int last_pos = 0;
			int second_pos = 0;
			String[] arrData = new String[3];
			int iteration = 0;
			while((last_pos = script_text.indexOf('\'', last_pos)) >= 0 && iteration < 3){
				
				second_pos = script_text.indexOf('\'', last_pos + 1);
				
				arrData[iteration++] = script_text.substring(last_pos + 1, second_pos);
				
				last_pos = second_pos + 1;
			}
			
			page.StoreFileName = arrData[0];
			
			
			
			JSONObject object = (JSONObject) new JSONTokener(arrData[2]).nextValue();
			page.FlvFullLink = object.getString("url");
			
			page.setIsHaveLink(true);
			
		}
		else{
			page.setIsHaveLink(false);
		}
	}
	
	
	
	public static void GetImage(ExMoviePage page, BitmapSize size)
	{
		HttpRetriever _http = new HttpRetriever();
		
		
		try {
			page.Image = _http.retrieveBitmap(page.GetImageLink(size.size()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void Search(ExMovieSearchPage page)
	{
		Document doc = null;
		
		try {
			doc = Jsoup.connect(page.GetLink()).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<ExMoviePage> movies = new ArrayList<ExMoviePage>();
		
		Elements els = doc.select("table.panel tr");
		
		for(Element element : els){
			
			if(element.attr("id").equals("ad_block")) continue;
			
			ExMoviePage movie = new ExMoviePage();
			
			Element anchor = element.child(0).child(0); 
			movie.Link = anchor.attr("href").trim();
			
			Element img = anchor.child(0);
			
			movie.SetImageLink(img.attr("src").trim());
			movie.Title = Html.fromHtml(img.attr("alt").trim()).toString();
			
			movies.add(movie);
		}
				
		page.Movies = movies;
		page.SetContentLoaded();
	}
	
	public static String[] SearchHint(String sSearch){
		
		ArrayList<String> result = new ArrayList<String>();
		
		if(sSearch == null || sSearch.isEmpty())
			return result.toArray(new String[result.size()]);
		
		HttpRetriever _http = new HttpRetriever();
		String pageRawData = _http.retrieve(String.format( _searchHint, sSearch) );
		
		
		String[] splitted = pageRawData.split("\n");
		
		for (String string : splitted) {
			
			String trimmed_string = string.trim();
			
			if(!trimmed_string.isEmpty())
				result.add(trimmed_string);
		}
		
		return result.toArray(new String[result.size()]);
				
	}
}
