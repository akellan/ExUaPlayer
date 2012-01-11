package com.akella.exua;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class SearchActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		handleIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		setContentView(R.layout.search);
		
		
		
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      
	      /*SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, 
	    		  SearchContentProvider.AUTHORITY, SearchContentProvider.MODE);
	      
	      suggestions.saveRecentQuery(query.trim(), null);*/
	      
	      
	      doSearch(query);
	    } else if(Intent.ACTION_VIEW.equals(intent.getAction())){
	    	String sData = intent.getDataString();
	    	    	
	    	doSearch(sData);	    	
	    }
	}

	private void doSearch(String query) {
		Log.i("Akella", query);
		
		ExMovieSearchPage sPage = new ExMovieSearchPage();
		sPage.Query = query.trim();
		
		new ContentLoader().execute(sPage);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	ExMoviesDataAdapter adapter = (ExMoviesDataAdapter)l.getAdapter();
		ExMoviePage pData = adapter.getItem(position);
		
		Intent intent = new Intent(SearchActivity.this, MovieActivity.class);
		intent.putExtra(MovieActivity.extraName, pData);
		startActivity(intent);		
		
		super.onListItemClick(l, v, position, id);
	}
	
	
	private class ContentLoader extends AsyncTask<ExMovieSearchPage, Void, ExMovieSearchPage>
    {
    	
    	ProgressDialog pDialog = null;
    	
    	@Override
    	protected void onPreExecute() {
    		
    		pDialog = ProgressDialog.show(SearchActivity.this, "", 
                    "Loading. Please wait...", true);
    		
    		super.onPreExecute();
    	}

		@Override
		protected ExMovieSearchPage doInBackground(ExMovieSearchPage... params) {
			ExUaBrowser.Search(params[0]);
			return params[0];
		}
    	
		@Override
		protected void onPostExecute(ExMovieSearchPage result) {
			
			pDialog.dismiss();
			
			ExMoviesDataAdapter adapter = new ExMoviesDataAdapter(SearchActivity.this, R.layout.list_item, result.Movies);
	        setListAdapter(adapter);
	        
			super.onPostExecute(result);
		}
		
		
    }
}
