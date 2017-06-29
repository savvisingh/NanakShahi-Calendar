package com.mdgiitr.nanakshahicalendar.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mdgiitr.nanakshahicalendar.adapter.SearchResultsAdapter;
import com.mdgiitr.nanakshahicalendar.model.Event;

import apps.savvisingh.nanakshahicalendar.R;

import com.mdgiitr.nanakshahicalendar.util.MySuggestionProvider;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class SearchResultsActivity extends AppCompatActivity {

    private Realm realm;
    private  RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        realm = Realm.getDefaultInstance();

        handleIntent(getIntent());




    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            getSupportActionBar().setTitle(query);

            RealmResults<Event> results = realm.where(Event.class).like("description", "*" + query + "*", Case.INSENSITIVE).findAllAsync();

            RealmChangeListener<RealmResults<Event>> callback = new RealmChangeListener<RealmResults<Event>>() {
                @Override
                public void onChange(RealmResults<Event> element) {
                    if(element!=null){
                        adapter = new SearchResultsAdapter(element);
                        recyclerView.setAdapter(adapter);
                    }
                }
            };

            results.addChangeListener(callback);

        }

    }

}
