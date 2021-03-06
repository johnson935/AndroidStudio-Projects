package com.parse.twitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FollowingActivity extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    private static List<String> array = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.tweet_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tweet){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Send a tweet");
            final EditText tweetContentEditText = new EditText(this);
            builder.setView(tweetContentEditText);
            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("info", tweetContentEditText.getText().toString());

                    ParseObject tweet = new ParseObject("Tweet");
                    tweet.put("username", ParseUser.getCurrentUser().getUsername());
                    tweet.put("tweet", tweetContentEditText.getText().toString());

                    tweet.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                Toast.makeText(FollowingActivity.this, "Tweet Sent Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FollowingActivity.this, "Tweet failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else if (item.getItemId() == R.id.logout){
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.viewfeed){
            Intent intent = new Intent(getApplicationContext(), Feed.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        setTitle("User list");

        if (ParseUser.getCurrentUser().get("isFollowing") == null){
            List<String> emptyList = new ArrayList<>();
            ParseUser.getCurrentUser().put("isFollowing", emptyList);
        }
        final ListView listView = findViewById(R.id.listView);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, users);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view;

                if (checkedTextView.isChecked()){
                    Log.i("info", "checked");
                    array = ParseUser.getCurrentUser().getList("isFollowing");
                    array.add(users.get(position));

                    ParseUser.getCurrentUser().put("isFollowing" , array);
                    ParseUser.getCurrentUser().saveInBackground();

                } else {
                    Log.i("info", "Not checked");
                    array = ParseUser.getCurrentUser().getList("isFollowing");
                    array.remove(users.get(position));

                    ParseUser.getCurrentUser().put("isFollowing" , array);
                    ParseUser.getCurrentUser().saveInBackground();
                }
            }
        });

        users.clear();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){
                    if (objects.size() > 0){
                        for (ParseUser user : objects){
                            users.add(user.getUsername());
                        }

                        arrayAdapter.notifyDataSetChanged();

                        for (String username: users){
                            if (ParseUser.getCurrentUser().getList("isFollowing").contains(username))
                            {
                                listView.setItemChecked(users.indexOf(username), true);
                            } else {
                                listView.setItemChecked(users.indexOf(username), false);
                            }
                        }
                    }
                }
            }
        });
    }
}

