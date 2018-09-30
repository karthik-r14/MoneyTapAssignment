package com.assignment.moneytap.moneytap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    }

    public void onSearchButtonClick(View view) {
        String searchString = searchEditText.getText().toString();
        progressBar.setVisibility(VISIBLE);
        loadData(searchString);
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
//                            Toast.makeText(getApplicationContext(), "Response : " + response, Toast.LENGTH_SHORT).show();
                            JSONArray pagesArray = obj.getJSONObject("query").getJSONArray("pages");
//                            Toast.makeText(getApplicationContext(), pagesArray.toString(), Toast.LENGTH_LONG).show();

                            for (int i = 0; i < pagesArray.length(); i++) {
                                JSONObject personObject = pagesArray.getJSONObject(i);

                                Toast.makeText(getApplicationContext(), personObject.getJSONObject("thumbnail").toString(), Toast.LENGTH_LONG).show();

                                if (personObject.getJSONObject("thumbnail") == null) {
                                    Person person = new Person(personObject.getString("pageid").toString(),
                                            personObject.getString("title").toString(),
                                            null,
                                            personObject.getJSONObject("terms").getJSONArray("description").toString());
                                    personsList.add(person);
                                } else {
                                    Person person = new Person(personObject.getString("pageid").toString(),
                                            personObject.getString("title").toString(),
                                            personObject.getJSONObject("thumbnail").getString("source").toString(),
                                    personObject.getJSONObject("terms").getJSONArray("description").toString());

                                    personsList.add(person);
                                }

//                                Toast.makeText(getApplicationContext(), "Person Title : " + person.getTitle() +
//                                        "\n page id : " + person.getPagesid() + "\n Image url : " + person.getImageUrl() + "\n Description : " + person.getDescription(), Toast.LENGTH_LONG).show();
                            }

                            for (Person person : personsList) {
                                Log.i("Tag", "person details");
                                Log.i("TAG", person.getTitle() + "\n" + person.getPagesid() + "\n" + person.getDescription());
                            }

                            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), personsList);
                            searchResultsListView.setAdapter(adapter);
                            searchResultsListView.setVisibility(VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
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
}