package chupalika.pleasepickaplace;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    Toast parseError;
    Toast threeError;
    Toast voteConfirmed;

    Callback restaurantsCallback;
    Callback voteCallback;

    ArrayList<Restaurant> votes;
    ArrayAdapter<Restaurant> vadapter;
    ListView voteView;
    int i;
    private AdapterView.OnItemClickListener voteClickedHandler;
    private AdapterView.OnItemClickListener choiceClickedHandler;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_screen);

        i = 0;
        threeError = Toast.makeText(this.getApplicationContext(),"Please select three restaurants", Toast.LENGTH_SHORT);
        unknownError = Toast.makeText(this.getApplicationContext(), "Unknown error occured", Toast.LENGTH_SHORT);
        parseError = Toast.makeText(this.getApplicationContext(), "Parse error occured", Toast.LENGTH_SHORT);
        voteConfirmed = Toast.makeText(this.getApplicationContext(), "Vote confirmed", Toast.LENGTH_SHORT);
        voteCallback = new VoteCallback();
        restaurantsCallback = new RestaurantsCallback();
        //TODO: Need some method for user to reorder their ranking on the screen (drag/drop? Prompts for first second third and use back button to undo?)

        restaurants = new ArrayList<Restaurant>();
        adapter = new ArrayAdapter<Restaurant>(this, R.layout.vote_option_item, R.id.a_vote_option, restaurants);
        listView = (ListView) findViewById(R.id.vote_options);
        listView.setAdapter(adapter);

        votes = new ArrayList<Restaurant>();
        vadapter = new ArrayAdapter<Restaurant>(this, R.layout.vote_confirm_item, R.id.choice, votes);
        voteView = (ListView) findViewById(R.id.vote_confirm_item);
        voteView.setAdapter(vadapter);
        voteClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if(votes.size() < 3) {
                    votes.add(restaurants.remove(position));
                    adapter.notifyDataSetChanged();
                    vadapter.notifyDataSetChanged();
                }
            }
        };
        listView.setOnItemClickListener(voteClickedHandler);
        choiceClickedHandler = new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView parent, View v, int position, long id){
                restaurants.add(votes.remove(position));
                adapter.notifyDataSetChanged();
                vadapter.notifyDataSetChanged();
            }
        };
        voteView.setOnItemClickListener(choiceClickedHandler);
        
        String url = Requester.SERVERURL + "/location?lat=42.729781&lng=-73.679248";
        //add the request to queue
        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url, restaurantsCallback);
    }

    private class VoteCallback implements Callback {
        @Override
        public void callback(Requester requester) {
            String response = requester.getLastMessage();
            if(response.equals("Success")){
                    submitVote();

            }else {
                unknownError.show();
            }
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
            //parseError.show();
        }finally{
            jr.close();
        }
    }

    private void readRestaurants(JsonReader jr) throws IOException{
        String id = "";
        System.out.println("Reading Restaurants");
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
            else if (temp.equals("cost")) {
                try{
                    jr.nextNull();
                }catch(Exception e){
                    price = jr.nextString();
                }
            }
            else if (temp.equals("location"))
                location = jr.nextString();
            else if (temp.equals("name"))
                name = jr.nextString();
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

    public void confirmVote(View view){
        if (votes.size() != 3)
            threeError.show();
        else{
            submitVote();
        }
    }
    private void submitVote(){
        if(votes.size() == 0){
            voteConfirmed.show();
            finish();
            return;
            //Intent intent = new Intent(this, MainMenu.class);
            //startActivity(intent);
        }
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String user = sp.getString(getString(R.string.login_username), "");
        SharedPreferences gp = this.getSharedPreferences(getString(R.string.preference_group_key), Context.MODE_PRIVATE);
        String group = gp.getString(getString(R.string.group_key), "");

        Restaurant r = votes.remove(0);
        i++;

        String url = Requester.SERVERURL + "/getvotes?user=" + user + "&group=" + group + "&option=" + r.getId() + "&rank=" + i;
        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url,voteCallback);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}
