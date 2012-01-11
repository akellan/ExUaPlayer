package com.akella.exua;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class DownloadImage extends AsyncTask<ExMoviePage, Void, ExMoviePage>{
	
	private static HashMap<ImageView, WeakReference<ImageView>> _vlMap = new HashMap<ImageView, WeakReference<ImageView>>();
	
	private WeakReference<ImageView> ivRef = null;
	private BitmapSize size;
	
	public DownloadImage(ImageView view, BitmapSize size)  {
		ivRef = new WeakReference<ImageView>(view);
		this.size = size;
		
		synchronized (_vlMap) {
			if(_vlMap.containsKey(view)){
				_vlMap.remove(view);
				_vlMap.put(view, ivRef);
			}else{
				_vlMap.put(view, ivRef);
			}
		} 
	}
	

	@Override
	protected ExMoviePage doInBackground(ExMoviePage... params) {
			
		if(params[0].Image == null){
			
			ExUaBrowser.GetImage(params[0], size);
			SyncBitmapCache.add(params[0].GetImageLink(size.size()), params[0].Image);
		}
		
		return params[0];
	}
	
	
	@Override
	protected void onPostExecute(ExMoviePage result) {
		
		if(result.Image != null){
			
			synchronized (_vlMap) {
				
				ImageView imageView = ivRef.get();
				
				if(_vlMap.containsKey(imageView)){
					WeakReference<ImageView> ref = _vlMap.get(imageView);
					
					if(ref.equals(ivRef)){
						imageView.setImageBitmap(result.Image);
					}
				}
			}
		}
		
		super.onPostExecute(result);
	}
}
