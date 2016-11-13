package chupalika.pleasepickaplace;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        groupCreateSuccess = Toast.makeText(this.getApplicationContext(),"Group created successfully",Toast.LENGTH_SHORT);
        unknownError = Toast.makeText(this.getApplicationContext(), "Unknown error occured", Toast.LENGTH_SHORT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Called when the user click create group
    public void createGroup(View view){
        //Get a RequestQueue, create the url from the inputted username and password
        RequestQueue queue = Requester.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://762ffcaf.ngrok.io/api?user="; // TODO: edit this to create group api call

        StringRequest groupRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    groupCreateSuccess.show();
                    groupCreateSuccess(response);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                unknownError.show();
            }
        });
    }

    // Called when group create is successfully returned
    private void groupCreateSuccess(String response){
        Intent grouppage = new Intent(this, GroupScreen.class);
        startActivity(grouppage);
    }
}
