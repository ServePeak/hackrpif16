package chupalika.pleasepickaplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by admin on 11/13/2016.
 */

public class CreateGroup extends ActionBarActivity{
    Callback cgCallback;
    Toast createSuccessful;
    Toast unknownError;
    Toast invalidKeyToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_screen);

        createSuccessful = Toast.makeText(this.getApplicationContext(),"Create Group Successful",Toast.LENGTH_SHORT);
        invalidKeyToast = Toast.makeText(this.getApplicationContext(),"Invalid key code", Toast.LENGTH_SHORT);
        unknownError = Toast.makeText(this.getApplicationContext(), "Unknown error occured", Toast.LENGTH_SHORT);

        cgCallback = new CreateGroupCallback();
    }

    private class CreateGroupCallback implements Callback{
        @Override
        public void callback(Requester requester) {
            String response = requester.getLastMessage();
            if(response.contains("Error")){
                unknownError.show();
            }
            else if(!response.isEmpty()) {
                createSuccessful(response);
            }
            else{
                createFailed();
            }
        }
    }

    // Called when the user presses Join group
    public void createGroupButtonListener(View view){
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String user = sp.getString(getString(R.string.login_username),"");

        EditText temp1 = (EditText)findViewById(R.id.group_name_input);
        String groupName = temp1.getText().toString();
        String url = Requester.SERVERURL + "/makegroup?user=" + user + "&name=" + groupName;

        //add the request to queue
        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url,cgCallback);
    }

    private void createSuccessful(String groupkey){
        Intent intent = new Intent(this, GroupScreen.class);
        //Write to shared preference the current group

        SharedPreferences SP = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        editor.putString(getString(R.string.group_key),groupkey);
        editor.commit();

        createSuccessful.show();

        startActivity(intent);
    }

    private void createFailed(){
        invalidKeyToast.show();
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
