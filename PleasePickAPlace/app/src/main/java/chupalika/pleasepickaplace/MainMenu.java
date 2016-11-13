package chupalika.pleasepickaplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by aleung013 on 11/12/2016.
 */

public class MainMenu extends ActionBarActivity{
    Toast groupCreateSuccess;
    Toast unknownError;
    Toast logoutToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        groupCreateSuccess = Toast.makeText(this.getApplicationContext(),"Group created successfully",Toast.LENGTH_SHORT);
        unknownError = Toast.makeText(this.getApplicationContext(), "Unknown error occured", Toast.LENGTH_SHORT);
        logoutToast = Toast.makeText(this.getApplicationContext(), "Logged out", Toast.LENGTH_SHORT);
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
    public void createGroup(View view){
        //Get a RequestQueue, create the url from the inputted username and password
        RequestQueue queue = Requester.getInstance(this.getApplicationContext()).getRequestQueue();
        System.out.println("Creating group for user");
        String url = "http://762ffcaf.ngrok.io/api?user="; // TODO: edit this to create group api call

        StringRequest groupRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Got a response");
                if (!response.isEmpty()) {
                    groupCreateSuccess.show();
                    groupCreateSuccess();
                }else{
                    unknownError.show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                System.out.println("Am i in here?");
                unknownError.show();
            }
        });

        queue.add(groupRequest);
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
