package com.akella.exua;

import java.io.File;
import java.io.Serializable;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieActivity extends BaseActivity {
	

	public static final String extraName = "movie_page";
	
	
	public ExMoviePage moviePage = null;
	
	private TextView tvTitle = null;
	private ImageView ivCover = null;
	private TextView tvDescription = null;
	
	private ContentLoader loader = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.movie);
		GetViewItems();
		
		if(savedInstanceState != null)
		{
			Serializable data = savedInstanceState.getSerializable(extraName);
			
			if(data != null){
				moviePage = (ExMoviePage) data;
				UpdateView();
			}
		}
		
		if(moviePage == null)
			moviePage = (ExMoviePage) getIntent().getExtras().get(MovieActivity.extraName);
		
		if(moviePage.Image == null){
			moviePage.Image = SyncBitmapCache.fetch(moviePage.GetImageLink(BitmapSize.Middle.size()));
			
			if(moviePage.Image == null){
				new DownloadImage(ivCover, BitmapSize.Middle).execute(moviePage);
			}
		}
		
		if(!moviePage.IsContentLoaded()){
			loader = new ContentLoader();
			loader.execute(moviePage);
		}
		
	}
	
	@Override
	protected void onBackgroundLoad(ExPageBase... params) throws Exception {
		// TODO Auto-generated method stub
		ExUaBrowser.GetMovieDataParser((ExMoviePage)params[0]);
	}
	
	private void GetViewItems()
	{
		tvTitle = (TextView)findViewById(R.id.movie_title);
		ivCover = (ImageView) findViewById(R.id.movie_image);
		tvDescription = (TextView) findViewById(R.id.movie_description);
		//tvDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
	
	protected void UpdateView()
	{
		tvTitle.setText(moviePage.Title);	
		ivCover.setImageBitmap(moviePage.Image);
		tvDescription.setText(moviePage.Description);
	}
	
	public void onBtWatch(View v)
	{
		if(!moviePage.getIsHaveLink()){
			Utils.StdErrorBox(MovieActivity.this, "Movie without link");
			return;
		}
		
		Uri uri = Uri.parse(moviePage.FlvFullLink);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setDataAndType(uri,"video/x-flv");
		
		try{
			startActivity(intent);
		}catch(Exception ex){
			Utils.StdErrorBox(MovieActivity.this, "Player (.flv) not installed.");
		}
	}
	
	public void onBtDownload(View v)
	{
		if(!moviePage.getIsHaveLink()){
			Utils.StdErrorBox(MovieActivity.this, "Movie without link");
			return;
		}
		
		File downloadsDir = new File(Environment.DIRECTORY_DOWNLOADS);
		downloadsDir.mkdirs();
		
		DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		Uri link = Uri.parse(moviePage.FlvFullLink);
		Request request = new Request(link);
		request.setDescription(moviePage.Title);
		request.setMimeType("video/x-flv");
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, link.getLastPathSegment());
		
		dm.enqueue(request);				
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
		outState.putSerializable(extraName, moviePage);
	}

	
	
	
}
