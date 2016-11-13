package chupalika.pleasepickaplace;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by admin on 11/13/2016.
 */

public class CreateGroup extends ActionBarActivity{
    Callback cgCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_screen);

        cgCallback = new CreateGroupCallback();
        //TODO: make api call to /makegroup
    }

    private class CreateGroupCallback implements Callback{
        @Override
        public void callback(Requester requester) {
            //TODO: Write callback function
        }
    }

    // Called when user presses Vote
    private void vote(View view){
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String user = sp.getString(getString(R.string.group_key),"");
        String url = Requester.SERVERURL + "/vote?" + user;

        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url,cgCallback);
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
