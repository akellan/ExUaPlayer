package com.akella.exua;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class BaseActivity extends Activity {
	
	
	protected abstract void onBackgroundLoad(ExPageBase... params) throws Exception;
	protected abstract void UpdateView();
	
	protected class ContentLoader extends AsyncTask<ExPageBase, Void, Void>
    {
    	
    	ProgressDialog pDialog = null;
    	Exception ex = null;
    	
    	@Override
    	protected void onPreExecute() {
    		
    		pDialog = ProgressDialog.show(BaseActivity.this, "", 
                    "Loading. Please wait...", true);
    		
    		super.onPreExecute();
    	}

		@Override
		protected Void doInBackground(ExPageBase... params) {
			try {
				//
				onBackgroundLoad(params);
			} catch (Exception e) {
				e.printStackTrace();
				ex = e; 
			}
			
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			
			pDialog.dismiss();
			
			if(ex != null){
				Utils.StdErrorBox(BaseActivity.this, ex.getMessage());
			}
			
			UpdateView();
	        
			super.onPostExecute(result);
		}
		
		
		public void closeProgressBar() {
			pDialog.dismiss();
		}
    }
	
}
