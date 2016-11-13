package chupalika.pleasepickaplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends ActionBarActivity {
    Toast loginSucceededToast;
    Toast loginBadCredentialsToast;
    Toast loginEmptyFieldsToast;
    Toast requestFailedToast;
    Toast registerSucceededToast;
    Toast registerFailedToast;
    Toast requestErrorToast;
    Toast unknownErrorToast;

    Callback loginCallback;
    Callback registerCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        loginSucceededToast = Toast.makeText(this.getApplicationContext(), "Login Succeeded!", Toast.LENGTH_SHORT);
        loginBadCredentialsToast = Toast.makeText(this.getApplicationContext(), "Username or Password is wrong", Toast.LENGTH_SHORT);
        loginEmptyFieldsToast = Toast.makeText(this.getApplicationContext(), "Username or Password field is empty", Toast.LENGTH_SHORT);
        requestFailedToast = Toast.makeText(this.getApplicationContext(), "Error on Server Request!", Toast.LENGTH_SHORT);
        registerSucceededToast = Toast.makeText(this.getApplicationContext(), "Successfully registered!", Toast.LENGTH_SHORT);
        registerFailedToast = Toast.makeText(this.getApplicationContext(), "Register failed!", Toast.LENGTH_SHORT);
        requestErrorToast = Toast.makeText(this.getApplicationContext(), "Error on Request!", Toast.LENGTH_SHORT);
        unknownErrorToast = Toast.makeText(this.getApplicationContext(), "Unknown error occured", Toast.LENGTH_SHORT);

        loginCallback = new LoginCallback();
        registerCallback = new RegisterCallback();
    }

    private class LoginCallback implements Callback{
        @Override
        public void callback(Requester requester) {
            String response = requester.getLastMessage();
            System.out.println("Response is " + response);

            //wrong credentials
            if (response.contains("does not exist")) {
                loginBadCredentialsToast.show();
            }
            //request error
            else if (response.contains("Error")) {
                requestErrorToast.show();
            }
            //every other string should indicate correct credentials: start the main menu activity
            else if (!(response.isEmpty())) {
                loginSucceededToast.show();
                loginSucceeded();
            }
            //unknown error
            else {
                unknownErrorToast.show();
            }
        }
    }

    private class RegisterCallback implements Callback{
        @Override
        public void callback(Requester requester) {
            String response = requester.getLastMessage();
            //System.out.println("Response is : " + response);

            //username already exists
            if (response.contains("exists")) {
                registerFailedToast.show();
            }
            //registration succeeded
            else if (response.contains("Success")) {
                registerSucceededToast.show();
            }
            //request error
            else if (response.contains("Error")) {
                requestErrorToast.show();
            }
            //unknown error
            else {
                unknownErrorToast.show();
            }
        }
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
            loginEmptyFieldsToast.show();
            return;
        }

        //Get a RequestQueue, create the url from the inputted username and password
        String url = "http://762ffcaf.ngrok.io/login?user=" + loginUsername + "&pass=" + loginPassword;

        //add the request to queue
        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url,loginCallback);

    }

    public void register(View view) {
        EditText temp1 = (EditText)findViewById(R.id.username_input);
        EditText temp2 = (EditText)findViewById(R.id.password_input);
        String registerUsername = temp1.getText().toString();
        String registerPassword = temp2.getText().toString();

        //don't do anything if the username or password is empty
        if (registerUsername.isEmpty() || registerPassword.isEmpty()) {
            loginEmptyFieldsToast.show();
            return;
        }

        //Get a RequestQueue, create the url from the inputted username and password
        String url = "http://762ffcaf.ngrok.io/register?user=" + registerUsername + "&pass=" + registerPassword;

        //add the request to queue
        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url,registerCallback);
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
