#WhatsappCloneMaster
#####In this section
In this tutorial, we are going to take it a notch higher and build  a clone for WhatsApp messenger, a widely used IM application.
<img class="center-img" alt="Whatsapp android Clone" src="https://github.com/egimaben/WhatsappCloneAndroid/blob/master/chat_members_screen.png">
<p>&nbsp;</p>
><span class="tut-info">Info</span> here you will learn how to use CloudBoost notification queries i.e how to apply a query on real time notifications so that you are only notified of specific events that meet the query criteria.
#Assumptions
I will assume that :
<ul>
<li>You have checked out our first java tutorial in this series and know you to set up your environment to develop CloudBoost apps, including the libraries to add to the classpath i.e.</li>
<ul>
<li>Javasdk for cloudboost Javasdk-1.0.1.jar </li>
<li>Socket-client.jar</li>
<li>Okhttp-2.4.0.jar</li>
<li>Okhttp-ws-2.4.0.jar</li>
<li>Okio-1.4.0.jar</li>
</ul>
<li>You know android (We shall not focus so much on learning the the android specific concepts bit of the code).</li>
<li>You already have an app on cloudboost, and have created a table called <code>whatsapp_message</code>, with the following columns
<ul>
<li>message, type text</li>
<li>from_user, type text</li>
<li>to_user, type text</li>

</ul>
</ul>
#The Android project
Create a new android project in your IDE(I use Eclipse 3.7 with ADT) and name it <code>WhatsappAndroidClone</code>, place all the required <code>jar</code>'s in the libs folder.

We are going to use the UI patterns used in Whatsapp  android application. When we take a look at the Whatsapp android application, 3 fragments are used with a viewpager to navigate between whatsapp major views of calls, chats and contacts. This was the most powerful navigation paradigm before the launch of the Navigation Drawer pattern. 

#How it works
##Getting chat name##
We first launch a dialog box to allow the user to choose a screen name to chat with, this just eases our work since we are not creating a persistent account for the user.
<img class="center-img" alt="Whatsapp android chats" src="https://github.com/egimaben/WhatsappCloneAndroid/blob/master/screen_name_chooser.PNG"> 
##Receiving messages##
With this name set as the <code>App.CURRENT_USER</code> in our android application class, we can create a notification query on our <code>whatsapp_message</code> table. The notificiation query tells ##CloudBoost## that any record saved in <code>whatsapp_message</code> that is addressed to <code>App.CURRENT_USER</code> in the <code>to_user</code> column warrants a notification to <code>App.CURRENT_USER</code>.
##Processing received messages##
We there fore have a listener for <code>created</code> events on that table. When this notification arrives, we simply create a record out of it and add it to the adapter attached to our Chats ListView.
##Message State##
When a user clicks the name of a person to chat with from the first screen above, we launch the <code>ChatBubbleActivity</code>. This activity hosts our chats with a given user. It has a material design <code>ToolBar</code> for navigation just like it is on whatsapp.
<img class="center-img" alt="Whatsapp android chats" src="https://github.com/egimaben/WhatsappCloneAndroid/blob/master/chatscreen.PNG">
When user navigates back to <code>MainActivity</code> using the up arrow, we have the adapter of the current listview saved in <code>App</code> under the current chat name. This way, when user returns to continue chat with this person, we recreate the view but just pick the existing adapter to continue from where we stopped.




