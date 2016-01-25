package io.cloudboost.whatsapp.chat;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.whatsapp.App;
import io.cloudboost.whatsapp.R;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ChatBubbleActivity extends ActionBarActivity {
    private static final String TAG = "ChatActivity";

//    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageButton buttonSend;

    Intent intent;
    Toolbar toolbar;
    private String name = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        int drawable=i.getIntExtra("logo", 1);
        this.name=i.getStringExtra("name");
        setContentView(R.layout.activity_chat);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbar.setLogo(R.drawable.ic_dp);

        getSupportActionBar().setSubtitle("Online");
        getSupportActionBar().setTitle(name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        buttonSend = (ImageButton) findViewById(R.id.buttonSend);

        listView = (ListView) findViewById(R.id.listView1);
		if (!App.chats.containsKey(name)) {
			ChatArrayAdapter adapter = new ChatArrayAdapter(this,
					R.layout.activity_chat_singlemessage);
			App.chats.put(name, adapter);
		}
        listView.setAdapter(App.chats.get(name));

        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        //to scroll the list view to bottom on data change
        App.chats.get(name).registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(App.chats.get(name).getCount() - 1);
            }
        });
//        supportInvalidateOptionsMenu();
    }

    private boolean sendChatMessage(){
    	new sendMsg().execute(new String[]{});
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conversation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
	class sendMsg extends AsyncTask<String, String, String> {

		/**
		 * Before starting background, run this method
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
	    	App.chats.get(name).add(new ChatMessage(false, chatText.getText().toString()));
//	        chatText.setText("");
		}

		/**
		 * send message on a background thread
		 * */
		@Override
		protected String doInBackground(String... args) {
			CloudObject obj = new CloudObject("whatsapp_message");
			String user = App.CURRENT_USER;
			try {
				obj.set("from_user", user);
				obj.set("message", chatText.getText().toString());
				obj.set("to_user", name);
				obj.save(new CloudObjectCallback() {

					@Override
					public void done(final CloudObject arg0,
							final CloudException arg1)
							throws CloudException {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (arg1 != null)
									Toast.makeText(getApplicationContext(),
											"An error has occured",
											Toast.LENGTH_SHORT).show();
							}
						});

					}
				});
			} catch (CloudException e) {
				e.printStackTrace();
			}
			runOnUiThread(new Runnable() {
				public void run() {
					chatText.setText("");
				}
			});

			return null;
		}

		/**
		 * After completing background task, run this method
		 * **/
		protected void onPostExecute(String args) {
		}
	}

}