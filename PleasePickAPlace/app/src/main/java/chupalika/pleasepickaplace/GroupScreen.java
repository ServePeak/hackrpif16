package chupalika.pleasepickaplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by aleung013 on 11/12/2016.
 */

public class GroupScreen extends ActionBarActivity{
    Callback leaderCallback;
    Callback memberCallback;
    String groupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_screen);

        Intent intent = getIntent();
        groupName = intent.getStringExtra(MainMenu.EXTRA_GROUP_NAME);
        TextView textView = (TextView)findViewById(R.id.group_screen_message);
        textView.setText("Welcome to " + groupName + "!");

        leaderCallback = new LeaderCallback();
        memberCallback = new MemberCallback();
        SharedPreferences sp = this.getSharedPreferences(getString(R.string.preference_group_key), Context.MODE_PRIVATE);
        String groupKey = sp.getString(getString(R.string.group_key),"");
        String url = Requester.SERVERURL + "/getleader?group=" + groupKey;
        String url3 = Requester.SERVERURL + "/getmembers?group=" + groupKey;
        Requester requester = Requester.getInstance(this.getApplicationContext());
        requester.addRequest(url,leaderCallback);
        requester.addRequest(url3,memberCallback);
    }

    private class MemberCallback implements Callback{
        @Override
        public void callback(Requester requester){
            String response = requester.getLastMessage();
            if(response.isEmpty()){

            }else{
                System.out.println(response);
            }
        }
    }

    private class LeaderCallback implements Callback{
        @Override
        public void callback(Requester requester) {
            String response = requester.getLastMessage();
            //System.out.println("Leader: " + response);

            if (response.isEmpty()){

            } else{// if (response.contains("{")){
                //response = response.substring(1,response.length() - 1);
                SharedPreferences sp = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                String user = sp.getString(getString(R.string.login_username),"");
                //System.out.println("User: " + user);
                if(user.contains(response)){
                    //System.out.println("You are the leader!");
                    Button svote = (Button)findViewById(R.id.start_vote_button);
                    svote.setVisibility(View.VISIBLE);

                    TextView gc = (TextView) findViewById(R.id.group_key_message);
                    SharedPreferences gk = getSharedPreferences(getString(R.string.preference_group_key),Context.MODE_PRIVATE);
                    String kc = gk.getString(getString(R.string.group_key),"");
                    gc.setText("Key code: " + kc);

                    String url3 = Requester.SERVERURL + "/getmembers?group=" + kc;
                    Requester requesters = Requester.getInstance(getApplicationContext());
                    requesters.addRequest(url3,memberCallback);
                }
            }
        }
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
