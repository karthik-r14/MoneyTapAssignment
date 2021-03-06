package com.assignment.moneytap.moneytap;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    private static final String JSON_URL = "https://en.wikipedia.org//w/api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpssearch=";
    public static final String PAGEID = "pageid";

    private ListView searchResultsListView;
    private EditText searchEditText;
    private ProgressBar progressBar;
    private ArrayList<Person> personsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.search_edit_text);
        searchResultsListView = findViewById(R.id.results_listview);
        progressBar = findViewById(R.id.progress_bar);

        searchResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (connectivityAvailable()) {
                    Intent intent = new Intent(MainActivity.this, PageDetailActivity.class);
                    intent.putExtra(PAGEID, personsList.get(i).getPageId());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.no_internet_msg, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void onSearchButtonClick(View view) {
        String searchString = searchEditText.getText().toString();
        if (connectivityAvailable()) {
            if (searchString.trim().isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.empty_string_msg, Toast.LENGTH_LONG).show();
            } else {
                progressBar.setVisibility(VISIBLE);
                loadData(searchString);
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_internet_msg, Toast.LENGTH_LONG).show();
        }
    }

    private void loadData(String searchString) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL + searchString + "&gpslimit=10",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        progressBar.setVisibility(GONE);
                        personsList = new ArrayList<>();

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray pagesArray = obj.getJSONObject("query").getJSONArray("pages");
                            String title, pageId, description, imageUrl;

                            for (int i = 0; i < pagesArray.length(); i++) {
                                JSONObject personObject = pagesArray.getJSONObject(i);

                                if (!personObject.toString().contains("thumbnail")) {
                                    pageId = personObject.getString("pageid").toString();
                                    title = personObject.getString("title").toString();
                                    imageUrl = null;
                                    description = personObject.getJSONObject("terms").getJSONArray("description").toString();

                                    Person person = new Person(pageId, title, imageUrl, description);
                                    personsList.add(person);
                                } else {
                                    pageId = personObject.getString("pageid").toString();
                                    title = personObject.getString("title").toString();
                                    imageUrl = personObject.getJSONObject("thumbnail").getString("source").toString();
                                    description = personObject.getJSONObject("terms").getJSONArray("description").toString();

                                    Person person = new Person(pageId, title, imageUrl, description);
                                    personsList.add(person);
                                }
                            }

                            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), personsList);
                            searchResultsListView.setAdapter(adapter);
                            searchResultsListView.setVisibility(VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), personsList);
                            searchResultsListView.setAdapter(adapter);
                            searchResultsListView.setVisibility(VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(GONE);
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public boolean connectivityAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}