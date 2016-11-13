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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by aleung013 on 11/12/2016.
 */

public class MainMenu extends ActionBarActivity /*implements LoaderManager.LoaderCallbacks<ArrayList<Group>>*/ {
    public final static String EXTRA_GROUP_NAME = "chupalika.GROUP_NAME";

    Toast groupCreateSuccess;
    Toast unknownError;
    Toast logoutToast;

    Callback getGroupCallback;

    ArrayAdapter<Group> adapter;
    ListView listView;
    ArrayList<Group> groupsList;

    private AdapterView.OnItemClickListener groupClickedHandler;

    /*
    ArrayAdapter<Group> adapter;
    ListView lw;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferences sp = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String user = sp.getString(getString(R.string.login_username), "");
        String pw = sp.getString(getString(R.string.login_password), "");

        groupCreateSuccess = Toast.makeText(this.getApplicationContext(), "Group created successfully", Toast.LENGTH_SHORT);
        unknownError = Toast.makeText(this.getApplicationContext(), "Unknown error occured", Toast.LENGTH_SHORT);
        logoutToast = Toast.makeText(this.getApplicationContext(), "Logged out", Toast.LENGTH_SHORT);

        getGroupCallback = new GroupCallback();

        groupsList = new ArrayList<Group>();
        adapter = new ArrayAdapter<Group>(this, R.layout.group_item, R.id.a_group_item, groupsList);
        listView = (ListView) findViewById(R.id.group_list);
        listView.setAdapter(adapter);

        groupClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                enterGroup(groupsList.get(position));
            }
        };
        listView.setOnItemClickListener(groupClickedHandler);
        //getLoaderManager().initLoader(0, null, this);
        /*
        lw = (ListView)findViewById(R.id.group_list);
        adapter = new ArrayAdapter<Group>(this,R.layout.group_item,R.id.a_group_item,groupsList);
        lw.setAdapter(adapter);
        */

        String url = Requester.SERVERURL + "/login?user=" + user + "&pass=" + pw;
        //add the request to queue
        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url, getGroupCallback);
    }

    /*
    @Override
    public Loader<ArrayList<Group>> onCreateLoader(int id, Bundle args) {
        return new GroupsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Group>> loader, ArrayList<Group> groups) {
        listView.setAdapter(new GroupAdapter(this, groups));
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Group>> loader) {
        listView.setAdapter(null);
    }
    */

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
            adapter.notifyDataSetChanged();
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
    public void createGroupListener(View view){
        Intent intent = new Intent(this, CreateGroup.class);
        startActivity(intent);

    }

    // Called when the user clicks join group
    public void joinGroupListener(View view){
        Intent intent = new Intent(this, JoinGroup.class);
        startActivity(intent);

    }

    // Called when group create is successfully returned
    private void groupCreateSuccess(){
        Intent grouppage = new Intent(this, GroupScreen.class);
        startActivity(grouppage);
    }

    private void enterGroup(Group g) {
        Intent intent = new Intent(this, GroupScreen.class);
        SharedPreferences SP = this.getSharedPreferences(getString(R.string.preference_group_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        editor.putString(getString(R.string.group_key),g.getKey());
        editor.commit();
        intent.putExtra(EXTRA_GROUP_NAME, g.toString());
        startActivity(intent);
    }

    //resets login info and kills activity
    private void logout() {
        SharedPreferences SP = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        editor.putString(getString(R.string.login_username), "");
        editor.putString(getString(R.string.login_password), "");
        editor.commit();
        SharedPreferences GP = this.getSharedPreferences(getString(R.string.preference_group_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor e2 = GP.edit();
        e2.putString(getString(R.string.group_key),"");
        e2.commit();

        logoutToast.show();
        finish();
    }

    //do not kill activity
    @Override
    public void onBackPressed() {

    }
}
