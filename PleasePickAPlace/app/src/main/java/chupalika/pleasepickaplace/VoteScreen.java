package chupalika.pleasepickaplace;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by admin on 11/13/2016.
 */

public class VoteScreen extends ActionBarActivity{
    ArrayList<Restaurant> restaurants;
    ArrayAdapter<Restaurant> adapter;
    ListView listView;

    Toast unknownError;

    Callback restaurantsCallback;
    Callback voteCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_screen);

        unknownError = Toast.makeText(this.getApplicationContext(), "Unknown error occured", Toast.LENGTH_SHORT);
        voteCallback = new VoteCallback();
        restaurantsCallback = new RestaurantsCallback();
        //TODO: Need some method for user to reorder their ranking on the screen (drag/drop? Prompts for first second third and use back button to undo?)

        restaurants = new ArrayList<Restaurant>();
        adapter = new ArrayAdapter<Restaurant>(this, R.layout.vote_option_item, R.id.a_vote_option, restaurants);
        listView = (ListView)findViewById(R.id.vote_options);
        listView.setAdapter(adapter);

        String url = Requester.SERVERURL + "/location?lat=42.729781&lng=-73.679248";
        //add the request to queue
        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url, restaurantsCallback);
    }

    private class VoteCallback implements Callback {
        @Override
        public void callback(Requester requester) {
            System.out.println("voted!");
        }
    }

    private class RestaurantsCallback implements Callback{
        @Override
        public void callback(Requester requester) {
            String response = requester.getLastMessage();
            //squiggly braces indicate restaurants were returned
            if (response.contains("{")) {
                try {
                    parseInput(response);

                }catch(IOException e){
                    unknownError.show();
                }
            }
            //unknown error
            else {
                unknownError.show();
            }
        }
    }

    private void parseInput(String response) throws IOException {
        JsonReader jr = new JsonReader(new StringReader(response));
        try{
            readRestaurants(jr);
        }catch(Exception e){
            unknownError.show();
        }finally{
            jr.close();
        }
    }

    private void readRestaurants(JsonReader jr) throws IOException{
        String id = "";
        jr.beginObject();
        while(jr.hasNext()){
            id = jr.nextName();
            readARestaurant(jr,id);
        }
        jr.endObject();
    }
    private void readARestaurant(JsonReader jr, String id) throws IOException{
        String name ="", rating = "", location="", distance="", price="", desc="", temp ="";
        jr.beginObject();
        while(jr.hasNext()){
            temp = jr.nextName();
            if(temp.equals("rating"))
                rating = jr.nextString();
            else if (temp.equals("description"))
                rating = jr.nextString();
            else if (temp.equals("distance"))
                distance = jr.nextString();
            else if (temp.equals("cost"))
                price = jr.nextString();
            else if (temp.equals("location"))
                location = jr.nextString();
            else if (temp.equals("name"))
                name = jr.nextString();
            else if (temp.equals("distance"))
                distance = jr.nextString();
        }
        jr.endObject();
        restaurants.add(new Restaurant(id,name,rating,location,price,desc,distance));
        adapter.notifyDataSetChanged();
    }

    // Called when user presses Vote
    private void vote(View view){
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String user = sp.getString(getString(R.string.group_key),"");
        String url = Requester.SERVERURL + "/vote?" + user;

        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url,voteCallback);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_screen, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}
