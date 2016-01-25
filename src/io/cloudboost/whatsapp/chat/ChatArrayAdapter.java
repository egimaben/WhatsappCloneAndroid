package io.cloudboost.whatsapp.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.cloudboost.whatsapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

	private TextView chatText;
	private List<ChatMessage> chatMessageList = new ArrayList<>();
	private LinearLayout singleMessageContainer;
	private LinearLayout overallContainer;

	boolean left=true;

	

	@Override
	public void add(ChatMessage object) {
		chatMessageList.add(object);
		super.add(object);
	}

	public ChatArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public int getCount() {
		return this.chatMessageList.size();
	}

	public ChatMessage getItem(int index) {
		return this.chatMessageList.get(index);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		Log.d("ChatArrayAdapter", "top of getview");
		
		if (row == null) {
			Log.d("ChatArrayAdapter", "row is null");
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.activity_chat_singlemessage, parent, false);
		}
		singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
		overallContainer=(LinearLayout) row.findViewById(R.id.overall_container);
		ChatMessage chatMessageObj = getItem(position);
		chatText = (TextView) row.findViewById(R.id.singleMessage);
		chatText.setText(chatMessageObj.message);
		if(chatMessageObj.left){
			if(chatMessageObj.left==left)
				singleMessageContainer.setBackgroundResource(R.drawable.balloon_incoming_normal_ext);
			else singleMessageContainer.setBackgroundResource(R.drawable.balloon_incoming_normal);

		}else{
			if(chatMessageObj.left==left)
				singleMessageContainer.setBackgroundResource(R.drawable.balloon_outgoing_normal_ext);
			else singleMessageContainer.setBackgroundResource(R.drawable.balloon_outgoing_normal);
		}
		left=chatMessageObj.left;
		overallContainer.setGravity(chatMessageObj.left ? Gravity.LEFT : Gravity.RIGHT);
		return row;
	}

}