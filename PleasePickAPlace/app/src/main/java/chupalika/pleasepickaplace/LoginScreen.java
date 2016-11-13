package chupalika.pleasepickaplace;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginScreen extends ActionBarActivity {
    Toast loginSucceededToast;
    Toast loginBadCredentials;
    Toast loginEmptyFields;
    Toast loginFailedToast;
    Toast unknownError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        loginSucceededToast = Toast.makeText(this.getApplicationContext(), "Login Succeeded!", Toast.LENGTH_SHORT);
        loginBadCredentials = Toast.makeText(this.getApplicationContext(), "Username or Password is wrong", Toast.LENGTH_SHORT);
        loginEmptyFields = Toast.makeText(this.getApplicationContext(), "Username or Password field is empty", Toast.LENGTH_SHORT);
        loginFailedToast = Toast.makeText(this.getApplicationContext(), "Error on Login Request!", Toast.LENGTH_SHORT);
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

    //called when the user clicks the login button
    //sends a http request to server, checks the response, logs in if the response is correct
    public void login(View view) {
        EditText temp1 = (EditText)findViewById(R.id.username_input);
        EditText temp2 = (EditText)findViewById(R.id.password_input);
        String loginUsername = temp1.getText().toString();
        String loginPassword = temp2.getText().toString();

        //don't do anything if the username or password is empty
        if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
            loginEmptyFields.show();
            return;
        }

        //Get a RequestQueue, create the url from the inputted username and password
        RequestQueue queue = Requester.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "http://762ffcaf.ngrok.io/api?user=" + loginUsername + "&pass=" + loginPassword;

        //Request a String response the url
        StringRequest loginRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            //called when a response is received
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                //bad credentials
                if (response.contains("wrong")) {
                    loginBadCredentials.show();
                }
                //correct credentials: start the main menu activity
                else if (response.contains("success")) {
                    loginSucceededToast.show();
                    loginSucceeded();
                }
                //unknown error
                else {
                    unknownError.show();
                }
            }
        }, new Response.ErrorListener() {
            //called when there is an error on the request or no response
            @Override
            public void onErrorResponse(VolleyError error) {
                loginFailedToast.show();
                System.out.println("Error on Login Request!");
            }
        });

        //Add the request to RequestQueue
        queue.add(loginRequest);
    }

    //starts the Main Menu activity
    private void loginSucceeded() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
