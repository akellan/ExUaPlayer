package com.akella.exua;



import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

public class SearchContentProvider extends SearchRecentSuggestionsProvider{
	
	public final static String AUTHORITY = "com.akella.exua.SearchContentProvider";
	public final static int MODE = DATABASE_MODE_QUERIES;
	private static final UriMatcher sURIMatcher = buildUriMatcher();
	
	private static final int SEARCH_SUGGEST = 0;
        
    private static final String[] COLUMNS = {
        "_id",  // must include this column
        SearchManager.SUGGEST_COLUMN_TEXT_1,
        SearchManager.SUGGEST_COLUMN_INTENT_DATA
        };
	
	
	public SearchContentProvider() {
		setupSuggestions(AUTHORITY, MODE);
	}
	
	
	private static UriMatcher buildUriMatcher() {
        UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
        return matcher;
    }
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		
		//String segQuery = uri.getLastPathSegment().toLowerCase().trim();
		
		Log.i("Akella", uri.toString());
		
		switch (sURIMatcher.match(uri)) {
			case SEARCH_SUGGEST:
				String query = null;
				if (uri.getPathSegments().size() > 1) {
					query = uri.getLastPathSegment().toLowerCase();
				}
				return getSuggestions(query, projection);
			default:
				throw new IllegalArgumentException("Unknown URL " + uri);
		}
	}
	
	private Cursor getSuggestions(String query, String[] projection) {
        String processedQuery = query == null ? "" : query.toLowerCase();
        
        String[] hints = ExUaBrowser.SearchHint(processedQuery);
        
        int i = 0;

        MatrixCursor cursor = new MatrixCursor(COLUMNS);
        for (String hint : hints) {
            cursor.addRow(new String[]{Integer.toString(++i), hint , hint});
        }

        return cursor;
    }

}
