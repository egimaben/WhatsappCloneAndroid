#SlackCloneMaster
#####In this section
In the past, we have looked at different ways of taking advantage of CloudBoost functionality to build your realtime applications. That is just a tip of the iceberg for what you can accomplish with cloudboost. 

In this tutorial, we are going to take it a notch higher and build a clone of a popular real time application, especially among developers. Slack is  a popular collaboration app, widely used by developers so I assume that you are already familiar with it.
<img class="center-img" alt="Slack android Clone" src="https://github.com/egimaben/SlackCloneAndroid/blob/master/animated_slack_clone.gif">
<p>&nbsp;</p>
><span class="tut-imp">Important:</span> this is not a production ready example, in the real chat application, you would have to route messages from different site visitors to available agents etc. At the same time, this example covers the barest skeleton of a webchat application.
<p>&nbsp;</p>
><span class="tut-info">Info</span> here you will learn how to use CloudBoost notification queries i.e how to apply a query on real time notifications so that you are only notified of specific events that meet the query criteria.
#Assumptions
I will assume that :
<ul>
<li>You have checked out our first java tutorial in this series and know you to set up your environment to develop CloudBoost apps, including the libraries to add to the classpath.<li>
<li>You know android (We shall not focus so much on learning the the android specific concepts bit of the code).</li>
<li>You already have an app on cloudboost, and have created a table called <code>slack_message</code>, with the following columns
<ul>
<li>message, type text</li>
<li>from_user, type text</li>
<li>to_user, type text</li>

</ul>
</ul>
#The Android project
Create a new android project in your IDE(I use Eclipse 3.7 with ADT) and name it <code>SlackAndroidClone</code>, place all the required <code>jar</code>'s in the libs folder.

We are going to use the UI patterns used in Slack  android application. When we take a look at the slack android application,
A navigation drawer is used to display the list of your team members under a header called <code>DIRECT MESSAGES</code>, your private groups under a header called <code>PRIVATE GROUPS</code> and then there is another header called <code>CHANNELS</code> which hosts all channels in Slack.

Navigation drawer makes UI navigation of our application very user friendly since the user does not have to leave the main screen to be able to access other important features of the app. This is why Google introduced this pattern together with android-Lollipop as part of material design patterns.

Therefore its very important that we as developers keep incorporating latest developments in the tools we use.
So let us  discuss further the flow of the application before we dive into the code.
When you click on a team name or the name of a team member in the navigation drawer, the messages between you and the group are displayed in the main screen. Then if you want to chat with another user or team, you just have to open the drawer again and select the person you would like to chat with. very simple and user-friendly navigation indeed.

When on the chat screen, you just type your message in the edit text widget and press enter if on the emulator of click the sending arrow if on a real device.

Next, we shall see the code that pulls off the different parts of our application. After that, we shall then see how to inject in ##CloudBoost## functionality.
#The Code
##MainActivity.java##
This is the only activity we shall use in our application. This is a simple but powerful advantage of Navigation drawer pattern. All our core activities will take place in this activity as we switch around using the drawer.

A number of things happen inside this Class:
#activity_main.xml
Inflating the xml file that defines the layout of widgets in this activity
==xml==
<span class="xml-lines" data-query="init">
```
<android.support.v4.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res/io.cloudboost.slackclone"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >

    <!-- This FrameLayout defines what will appear on our main screen -->

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical" >

        <android.support.v7.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="60dp"
            android:background="@color/toolbar_background"
            android:gravity="center_vertical"
            android:minHeight="?attr/actionBarSize"
            app:navigationIcon="@drawable/slack_logo" >
        </android.support.v7.widget.Toolbar>

        <FrameLayout
            android:id="@+id/content_frame"
            android:layout_width="fill_parent"
            android:layout_height="fill_parent"
            android:layout_marginTop="56.0dip" />
    </FrameLayout>
    <!-- this ListView is the widget to appear in our Navigation drawer -->

    <ListView
        android:id="@+id/left_drawer"
        android:layout_width="240dp"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:background="#382626"
        android:choiceMode="singleChoice"
        android:divider="@android:color/transparent"
        android:dividerHeight="0dp" />

</android.support.v4.widget.DrawerLayout>
```
</span>
As you can see, we use a DrawerLayout as the root of our xml definition. Additionally we use the material design toolbar in order to have more control over our ActionBar.

#Sign in
We ask the user to input their preferred username (as a hack to allow you to use ur own names). For testing purposes, I have put static names of some team members in CloudBoost. Am assuming you will test on 2 emulators, 2 devices or a device and an emulator, which ever suits you best. So my testing environment used <code>bengi</code> and <code>egima</code>(I know, I know it seems egoistic but please where I come from, you don't just get to use people's names)
Please feel free to use any name (Granny's, your Ex etc). So if one device is using <code>bengi</code> then you'll realize <code>egima</code> is on your team list or vice versae(customize accordingly). Below is the code for the dialog:


==Java==
<span class="java-lines" data-query="init">
```
    public void openDialog(){
    	final Dialog dialog=new Dialog(this);
    	dialog.setContentView(R.layout.dialog);
    	dialog.setTitle("Chat name");
    	Button ok=(Button) dialog.findViewById(R.id.dialogButtonOK);
    	final EditText name=(EditText) dialog.findViewById(R.id.user);
    	ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String user="@"+name.getText();
				if(!App.staticUsers.contains(user)){
					name.setText("");
				}
				else{
					if(user.equals("@egima")){
						App.CURRENT_USER="@egima";
						adapter.add(new DrawerItem("@bengi", false));
						teamMembers.add("@bengi");

					}
					else {
						App.CURRENT_USER="@bengi";
						adapter.add(new DrawerItem("@egima", false));}
					teamMembers.add("@egima");

					dialog.dismiss();
				}
				
			}
		});
    	dialog.show();
    			
    }
```
</span>
#Dialog.xml
And below is the xml inflated for our dialog
==xml==
<span class="xml-lines" data-query="init">
```
<?xml version="1.0" encoding="utf-8"?>
<TableLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent" >
    <TableRow>
        <TextView
            android:text="User"
            android:width="120dp" />

        <EditText
            android:id="@+id/user"
            android:width="200dp" />
    </TableRow>    
	<TableRow>
        <Button
            android:id="@+id/dialogButtonOK"
            android:layout_width="100px"
            android:layout_height="wrap_content"
            android:layout_marginRight="5dp"
            android:layout_marginTop="5dp"
            android:text=" Ok " />
    </TableRow>

</TableLayout>
```
</span>
How our dialog will look:
<img class="center-img" alt="Chat name dialog" src="https://github.com/egimaben/SlackCloneAndroid/blob/master/inputchatname.PNG">

#Routing chat messageges
We use CloudBoost notification query to filter realtime alerts whenever messages are received.
Here, we are telling the SDK to only notify us when the message is addressed to <code>@bengi</code>. This will be replaced by the chat name you fill in the  dialog box above.
==Java==
<span class="java-lines" data-query="init">
```
CloudQuery query = new CloudQuery("slack_message");
	query.equalTo("to_user", "@bengi");
		try {
			CloudObject.on("slack_message", "created", query,
				new CloudObjectCallback() {
				@Override
				public void done(final CloudObject arg0,
					CloudException arg1) throws CloudException 
					{
					
					if (arg0 != null)
						runOnUiThread(new Runnable() {
						@Override
						public void run() {
						receiveMessage(arg0);
						}
						});

					}
				});
		} 
		catch (CloudException e) {
			e.printStackTrace();
	}

```
</span>
<p>&nbsp;</p>
><span class="tut-info">Info</span>the <code>receiveMessage(CloudObject obj)</code> simply retrieves the message and sender chat name of received message, locates the Adapter store in the Application class and then adds the message to the adapter. So whether the fragment containing the particular chats of the sender has been active or not, the chats will appear the next time you select the sender from the navigation drawer.
<img class="center-img" alt="Chat user list" src="https://github.com/egimaben/SlackCloneAndroid/blob/master/navigation_drawer_withnames.PNG">

#Posting chats
We use an asyncronous class to handle the posting of our chat messages to the ##CloudBoost## realtime server, this way the network call will run on a background thread. Other than this, your app can crush due to slow networkd calls and it would block the UI thread.
So here is our async class which I place inside the Fragment class:
==Java==
<span class="java-lines" data-query="init">
```
class sendMsg extends AsyncTask<String, String, String> {
	/**
	* Before starting background, run this method
	* */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		adapter.add(new ChatMessage(editor.getText().toString(),
				App.CURRENT_USER, 0));
	}

	/**
	* send message on a background thread
	* */
	@Override
	protected String doInBackground(String... args) {
		CloudObject obj = new CloudObject("slack_message");
		String user = App.CURRENT_USER;
		try {
			obj.set("from_user", user);
			obj.set("message", editor.getText().toString());
			obj.set("to_user", otherUser);
			obj.save(new CloudObjectCallback() {

				@Override
				public void done(final CloudObject arg0,
						final CloudException arg1)
						throws CloudException {
						getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (arg1 != null)
								Toast.makeText(getActivity(),
										"An error has occured",
										Toast.LENGTH_SHORT).show();
								}
							});

						}
					});
				} catch (CloudException e) {
					e.printStackTrace();
				}
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						editor.setText("");
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
```
</span>
<p>&nbsp;</p>
><span class="tut-info">Info:</span>Notice that this is just a normal CloudObject save operation. This makes implementing Realtime functionality a no brainer. All the boiler plate cold has been abstracted in the backend and implementing real time functionality has been reduced to one additional line of code which we looked at earlier(creating a listener on the chat table).
<img class="center-img" alt="Chats List view" src="https://github.com/egimaben/SlackCloneAndroid/blob/master/chatscreenwithmessages.PNG">

This is it for our slack clone, you can get the full source code from the our git account.





