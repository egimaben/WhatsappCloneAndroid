package io.cloudboost.whatsapp;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.whatsapp.chat.ChatArrayAdapter;
import io.cloudboost.whatsapp.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {
	CloudQuery query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(false);
//        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
//        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).select();
		if (App.CURRENT_USER == null)
			openDialog();
		/*
		 * create a query on table chat, remember we use notification queries
		 * for this example, when u send a chat, we just save it as a
		 * CloudObject in the chat table which the chat widget listens to.
		 */
		query = new CloudQuery("whatsapp_message");
		/*
		 * we also listen to "created" events on table chat, difference is we
		 * query records where admin column is set to false, that means we shall
		 * not be receiving echoes of our own messages The chat widget on the
		 * site has to query messages where admin column is set to true, so that
		 * it only receives notifications on messages from an agent not an echo
		 * of its own client message
		 */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ChatListFragment(), "CALLS");
        adapter.addFragment(new ChatListFragment(), "CHATS");
        adapter.addFragment(new ChatListFragment(), "CONTACTS");
        viewPager.setAdapter(adapter);
        
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
	public void openDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog);
		Spinner spinner=(Spinner) dialog.findViewById(R.id.Spinner01);
		System.out.println("dialog="+dialog);
		System.out.println("spinner="+spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
		        android.R.layout.simple_spinner_item, StaticData.names);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String user = parent.getItemAtPosition(position).toString();
				App.CURRENT_USER=user;
				System.out.println("item selected is="+user);				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		Button ok=(Button) dialog.findViewById(R.id.dialogButtonOK);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("current user="+App.CURRENT_USER);
				query.equalTo("to_user", App.CURRENT_USER);
				try {
					CloudObject.on("whatsapp_message", "created", query,
							new CloudObjectCallback() {

								@Override
								public void done(final CloudObject arg0,
										CloudException arg1) throws CloudException {
									System.out.println("receiving message");
									if (arg0 != null)
										runOnUiThread(new Runnable() {

											@Override
											public void run() {
												receiveMessage(arg0);

											}
										});
									else System.out.println("error="+arg1.getMessage());

								}
							});
				} catch (CloudException e) {
					e.printStackTrace();
				}
				dialog.dismiss();
				
			}
		});
		dialog.setTitle("Chat name");
		dialog.show();

	}
	/**
	 * this method takes a cloudobject and retrieves 'message' out of it and
	 * adds it to the adapter
	 * 
	 * @param object
	 * @return
	 */
	private boolean receiveMessage(CloudObject object) {
		String msg = object.getString("message");
		String sender = object.getString("from_user");
		System.out.println(sender+":"+msg);
		if (!App.chats.containsKey(sender)) {
			ChatArrayAdapter adapter = new ChatArrayAdapter(this,
					R.layout.activity_chat_singlemessage);
			App.chats.put(sender, adapter);
		}
		System.out.println("Current appchats are "+App.chats.keySet());
		App.chats.get(sender).add(new ChatMessage(true, msg));
		return true;
	}
}
