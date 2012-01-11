package com.akella.exua;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class HttpRetriever {
   
   private DefaultHttpClient client = new DefaultHttpClient();   
   
   public String retrieve(String url) {
      
        HttpGet getRequest = new HttpGet(url);
        
      try {
         
         HttpResponse getResponse = client.execute(getRequest);
         final int statusCode = getResponse.getStatusLine().getStatusCode();
         
         if (statusCode != HttpStatus.SC_OK) { 
            Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url); 
            return null;
         }
         
         HttpEntity getResponseEntity = getResponse.getEntity();
         
         if (getResponseEntity != null) {
            return EntityUtils.toString(getResponseEntity);
         }
         
      } 
      catch (IOException e) {
         getRequest.abort();
         Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
      }
      
      return null;
      
   }
   
   public InputStream retrieveStream(String url) {
      
      HttpGet getRequest = new HttpGet(url);
        
      try {
         
         HttpResponse getResponse = client.execute(getRequest);
         final int statusCode = getResponse.getStatusLine().getStatusCode();
         
         if (statusCode != HttpStatus.SC_OK) { 
            Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url); 
            return null;
         }

         HttpEntity getResponseEntity = getResponse.getEntity();
         return getResponseEntity.getContent();
         
      } 
      catch (IOException e) {
         getRequest.abort();
         Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
      }
      
      return null;
      
   }
   
   public Bitmap retrieveBitmap(String url) throws Exception { // to return Bitmap objects.

       InputStream inputStream = null;
       try {
           inputStream = this.retrieveStream(url);
           // using the decodeStream method of the BitmapFactory class to create a new Bitmap object                       
           final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream)); //first wrap the InputStream  with a FlushedInputStream class.otherwise an exception will through over slower connection.
           return bitmap;
       } finally {
           Utils.closeStreamQuietly(inputStream); // closing the InputStream quietly.closeStreamQuietly class handles the exceptions thrown when closing InputStream.                                                  // 
       }

   }
   
}
