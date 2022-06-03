package com.example.flixster;

//import static com.example.flixster.MainActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/";

    //the movie to display
    Movie movie;

    //the view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        imagePreview = (ImageView) findViewById(R.id.imagePreview);

        //unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        //set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        String imageUrl = movie.getPosterPath();
        int placeholder = R.drawable.flicks_movie_placeholder;
        Glide.with(this).load(imageUrl).placeholder(placeholder).centerCrop().transform(new RoundedCorners(15)).into(imagePreview);

        //convert vote average by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage /2.0f);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL + String.valueOf(movie.getId()) + "/videos?api_key=" + R.string.movie_api_key, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                //Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    //Log.i(TAG, "Results: " + results.toString());
                    if(results.length() > 0) {
                        Integer ytID = (Integer) results.get(0);
                    }
                    //movieAdapter.notifyDataSetChanged();
                    //Log.i(TAG, "Movies: " + movies.size());
                }
                catch (JSONException e) {
                    //Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                //Log.d(TAG, "onFailure" + statusCode);
            }
        });
    }
}