package com.akella.exua;


import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExMoviesDataAdapter extends ArrayAdapter<ExMoviePage> {
	
	private List<ExMoviePage> items;
	
	public ExMoviesDataAdapter(Context context, int textViewResourceId,
			List<ExMoviePage> objects) {
		super(context, textViewResourceId, objects);
		items = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent )
	{
		View v = convertView;
		
		if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item, null);
        }
		
		Log.i("Akella_ids", Integer.toString(v.hashCode()));
		
		ExMoviePage pData = items.get(position);
		
		if(pData != null)
		{
			
			
			TextView twMovieTitle = (TextView) v.findViewById(R.id.movie_title);
			twMovieTitle.setText(pData.Title);
			
			ImageView imageView = (ImageView) v.findViewById(R.id.movie_image);
			
			
			if(pData.Image == null){
				
				pData.Image = SyncBitmapCache.fetch(pData.GetImageLink(BitmapSize.Small.size()));
				
				Log.i("Akella_fetch", pData.GetImageLink(BitmapSize.Small.size()));
				
				if(pData.Image == null){
					Log.i("Akella_download", pData.GetImageLink(BitmapSize.Small.size()));
					new DownloadImage(imageView, BitmapSize.Small).execute(pData);
				}
				
				
			}
			
			if(pData.Image != null)
				imageView.setImageBitmap(pData.Image);
			else				
				((ImageView) imageView).setImageDrawable(getContext().getResources().getDrawable(R.drawable.reel_film));
			
		}
		
		return v;
	}
	
	
	

}
