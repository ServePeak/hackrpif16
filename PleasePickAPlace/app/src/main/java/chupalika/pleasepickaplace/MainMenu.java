package chupalika.pleasepickaplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by aleung013 on 11/12/2016.
 */

public class MainMenu extends ActionBarActivity{
    Toast groupCreateSuccess;
    Toast unknownError;
    Toast logoutToast;

    Callback getGroupCallback;
    ArrayList<Group> groupsList;
    /*
    ArrayAdapter<Group> adapter;
    ListView lw;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferences sp = this.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        String user = sp.getString(getString(R.string.login_username),"");
        String pw = sp.getString(getString(R.string.login_password),"");

        groupCreateSuccess = Toast.makeText(this.getApplicationContext(),"Group created successfully",Toast.LENGTH_SHORT);
        unknownError = Toast.makeText(this.getApplicationContext(), "Unknown error occured", Toast.LENGTH_SHORT);
        logoutToast = Toast.makeText(this.getApplicationContext(), "Logged out", Toast.LENGTH_SHORT);

        getGroupCallback = new GroupCallback();

        // TODO: show the groups on the main page
                    /* Probably like
                        Group name 1            <button> View Group </button>
                        Group name 2            <button> View Group </button>
                        etc.

                        Probably change the layout of the page so that it's
                        <Create Group button> <Join Group Button>
                        <list of groups> (if user is not in any groups, show a message that tells him to create/join one)
                    */

        groupsList = new ArrayList<Group>();
        /*
        lw = (ListView)findViewById(R.id.group_list);
        adapter = new ArrayAdapter<Group>(this,R.layout.group_item,R.id.a_group_item,groupsList);
        lw.setAdapter(adapter);
        */
        String url = Requester.SERVERURL + "/login?user=" + user + "&pass=" + pw;
        //add the request to queue
        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url,getGroupCallback);
    }

    private class GroupCallback implements Callback{
        @Override
        public void callback(Requester requester) {
            String response = requester.getLastMessage();

            //squiggly braces indicate groups were returned
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
            readGroups(jr);
        }catch(Exception e){
            unknownError.show();
        }finally{
            jr.close();
        }
    }

    private void readGroups(JsonReader jr) throws IOException{
        jr.beginObject();
        while(jr.hasNext()){
            String name = jr.nextName();
            String key = jr.nextString();
            groupsList.add(new Group(name, key));
            //adapter.notifyDataSetChanged();
        }
        jr.endObject();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Called when the user click create group
    private void createGroup(View view){
        //TODO: createGroup
        //Create a textbox (popup?) for user to enter group name
        //Make a /makegroup API call
    }

    // Called when the user clicks join group
    private void joinGroup(View view){
        //TODO: joinGroup
        //Create a textbox (popup?) for user to enter group key
        //Make a /joingroup API call
    }

    // Called when group create is successfully returned
    private void groupCreateSuccess(){
        Intent grouppage = new Intent(this, GroupScreen.class);
        startActivity(grouppage);
    }

    private void logout() {
        SharedPreferences SP = this.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        editor.putString(getString(R.string.login_username), "");
        editor.putString(getString(R.string.login_password), "");
        editor.commit();

        logoutToast.show();
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}
