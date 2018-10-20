package com.mdgiitr.nanakshahicalendar.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.mdgiitr.nanakshahicalendar.adapter.SearchResultsAdapter;
import com.mdgiitr.nanakshahicalendar.model.Event;
import apps.savvisingh.nanakshahicalendar.R;
import com.mdgiitr.nanakshahicalendar.util.MySuggestionProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class SearchResultsActivity extends AppCompatActivity implements SearchResultsAdapter.OnItemClickListener{

    private Realm realm;
    private  RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @BindView(R.id.no_results_layout)
    AppCompatTextView textViewNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textViewNoResults.setVisibility(View.GONE);

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

            Answers.getInstance().logCustom(new CustomEvent("Search Opened")
                    .putCustomAttribute("query", query));

            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            getSupportActionBar().setTitle(query);

            RealmResults<Event> results =
                    realm.where(Event.class)
                        .like("description", "*" + query + "*", Case.INSENSITIVE)
                        .or()
                        .like("title", "*" + query + "*", Case.INSENSITIVE)
                        .findAllAsync();

            RealmChangeListener<RealmResults<Event>> callback = new RealmChangeListener<RealmResults<Event>>() {
                @Override
                public void onChange(RealmResults<Event> element) {
                    if(element!=null && element.size() > 0){
                        recyclerView.setVisibility(View.VISIBLE);
                        textViewNoResults.setVisibility(View.GONE);
                        adapter = new SearchResultsAdapter(element, SearchResultsActivity.this);
                        recyclerView.setAdapter(adapter);
                    }else {
                        recyclerView.setVisibility(View.GONE);
                        textViewNoResults.setVisibility(View.VISIBLE);
                    }
                }
            };

            results.addChangeListener(callback);

        }

    }

    @Override
    public void onClick(int eventId) {
        Intent intent = new Intent(SearchResultsActivity.this, EventActivity.class);
        intent.putExtra("event_id", String.valueOf(eventId));
        startActivity(intent);
    }
}
