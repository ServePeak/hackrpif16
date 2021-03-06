package chupalika.pleasepickaplace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by aleung013 on 11/12/2016.
 */

public class GroupScreen extends ActionBarActivity{
    Callback leaderCallback;
    Callback memberCallback;
    Callback endVoteCallback;
    String groupName;
    String groupKey;

    ArrayList<String> members;

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
        endVoteCallback = new EndVoteCallback();

        SharedPreferences sp = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        groupKey = sp.getString(getString(R.string.group_key),"");

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
                System.out.println("No members");
            } else{
                response = response.replaceAll("\\[","");
                response = response.replaceAll("\\]","");
                response = response.replaceAll("\'","");
                response = response.replaceAll(" ","");
                members = new ArrayList<String>(Arrays.asList(response.split(",")));
                //System.out.println(members.get(0));

                String membersText = "Members: ";
                for (int i = 0; i < members.size(); i++) {
                    membersText = membersText.concat(members.get(i));
                    if (i != members.size()-1) membersText = membersText.concat(", ");
                }

                TextView textView = (TextView)findViewById(R.id.group_members);
                textView.setText(membersText);
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

    public void startVote(View view){
        Intent intent = new Intent(this, VoteScreen.class);
        startActivity(intent);
    }

    public void endVote(View view) {
        String url = Requester.SERVERURL + "/parsevotes?group=" + groupKey;
        Requester requester = Requester.getInstance(getApplicationContext());
        requester.addRequest(url, endVoteCallback);
    }

    private class EndVoteCallback implements Callback {
        @Override
        public void callback(Requester requester) {
            String response = requester.getLastMessage();

            TextView textView = (TextView)findViewById(R.id.group_screen_winner);
            textView.setText("Winner: " + response);
        }
    }
}
