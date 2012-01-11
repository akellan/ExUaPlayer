package com.akella.exua;

import java.io.Serializable;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity {
	
	private ExPage exPage = null;
	private ContentLoader loader = null;
	private static final String SavedStateTag = "SavedState";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if(savedInstanceState != null)
        {
        	Serializable state = savedInstanceState.getSerializable(SavedStateTag);
        
	        if(state != null){
	        	exPage = (ExPage) state;
	        	UpdateView();
	        }
        }
        
        if(exPage == null){
        	loader = new ContentLoader();
        	loader.execute((ExPage)null);
        }
    }
    
    
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	ExMoviesDataAdapter adapter = (ExMoviesDataAdapter)l.getAdapter();
		ExMoviePage pData = adapter.getItem(position);
		
		
		
		Intent intent = new Intent(MainActivity.this, MovieActivity.class);
		intent.putExtra(MovieActivity.extraName, pData);
		startActivity(intent);		
		
		super.onListItemClick(l, v, position, id);
	}
    
    @Override
    protected void onPause() {
    	
    	if(loader != null){
    		loader.closeProgressBar();
    		loader.cancel(false);
    	}
    	
    	super.onPause();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putSerializable(SavedStateTag, exPage);
    }
    
    private void UpdateView()
    {
    	ExMoviesDataAdapter adapter = new ExMoviesDataAdapter(MainActivity.this, R.layout.list_item, exPage.Movies);
        setListAdapter(adapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	
    	switch (item.getItemId()) {
		case R.id.main_menu_refresh:
			loader = new ContentLoader();
        	loader.execute(exPage);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    }
    
    private class ContentLoader extends AsyncTask<ExPage, Void, ExPage>
    {
    	
    	ProgressDialog pDialog = null;
    	
    	Exception ex = null;
    	
    	@Override
    	protected void onPreExecute() {
    		
    		pDialog = ProgressDialog.show(MainActivity.this, "", 
                    "Loading. Please wait...", true);
    		
    		super.onPreExecute();
    	}

		@Override
		protected ExPage doInBackground(ExPage... params) {
			
			try{
				return ExUaBrowser.GetForeignMoviesParser(params[0]);
			}catch(Exception e)
			{
				e.printStackTrace();
				
				ex = e;
			}
			
			return params[0];
		}
    	
		@Override
		protected void onPostExecute(ExPage result) {
			
			pDialog.dismiss();
			
			if(ex != null){
				
				Class<? extends Exception> c = ex.getClass();
				Utils.StdErrorBox(MainActivity.this, c.getName() + " " + ex.getMessage());
			}
			
			if(result == null) return;
			
			exPage = result;
			UpdateView();
	        
			super.onPostExecute(result);
		}
		
		public void closeProgressBar() {
			pDialog.dismiss();
		}
		
		
		
    }
    
}