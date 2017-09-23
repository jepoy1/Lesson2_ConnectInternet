package com.estaris.lesson2_connectinternet;

import android.content.Context;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

import utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {
    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView;
    private TextView mSearchResults;
    TextView errorMessageDisplay;
    ProgressBar mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainActivityLayouts();
    }

    private void setMainActivityLayouts() {
        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResults = (TextView) findViewById(R.id.tv_github_search_results_json);
        errorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }

/* MENU BAR */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        //You can use switch statements for cases like choosing an option such as this. If statement is used for simplicity
        if(menuItemThatWasSelected == R.id.action_search){
            Context context = MainActivity.this;
            /*String message = "Search clicked!";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();*/
            makeGithubSearchQuery();
            //In order for the program to proceed with true values:
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void makeGithubSearchQuery(){
        String githubQuery = mSearchBoxEditText.getText().toString();
        URL gitHubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(gitHubSearchUrl.toString());
        //Call getResponseFromHttpUrl and display the results in mSearchResultsTextView
        //using Custom Asynch Task:
        new GithubQueryTask().execute(gitHubSearchUrl);
    }

    //Asynch Task for the internet:
    public class GithubQueryTask extends AsyncTask<URL, Void, String>{
        @Override
        protected String doInBackground(URL... urls) {
            //Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
            URL searchUrl = urls[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return githubSearchResults;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(s != null && !s.equals("")){
                // COMPLETED (17) Call showJsonDataView if we have valid, non-null results
                showJsonDataView();
                mSearchResults.setText(s);
            }else{
                // COMPLETED (16) Call showErrorMessage if the result is null in onPostExecute
                showErrorMessage();
            }
        }
    }

    // Create a method called showJsonDataView to show the data and hide the error
    private void showJsonDataView(){
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        mSearchResults.setVisibility(View.VISIBLE);
    }

    //Create a method called showErrorMessage to show the error and hide the data
    private void showErrorMessage(){
        errorMessageDisplay.setVisibility(View.VISIBLE);
        mSearchResults.setVisibility(View.INVISIBLE);
    }



}
