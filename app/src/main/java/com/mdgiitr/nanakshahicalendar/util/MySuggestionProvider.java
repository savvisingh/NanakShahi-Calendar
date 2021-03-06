package com.mdgiitr.nanakshahicalendar.util;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by SavviSingh on 28/01/17.
 */

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.mdgiitr.nanakshahicalendar.search";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
