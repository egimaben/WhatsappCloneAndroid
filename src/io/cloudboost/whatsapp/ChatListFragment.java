package io.cloudboost.whatsapp;

import io.cloudboost.whatsapp.chat.ChatBubbleActivity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChatListFragment extends Fragment {
	ListView listView;
	ClientArrayAdapter clientArrayAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView= (ListView) inflater.inflate(R.layout.list_chats, container, false);
		
        clientArrayAdapter = new ClientArrayAdapter(getActivity(), R.layout.row);
        listView.setAdapter(clientArrayAdapter);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(clientArrayAdapter);

        //to scroll the list view to bottom on data change
        clientArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(clientArrayAdapter.getCount() - 1);
            }
        });
        addRows();

        return listView;
	}
	public void addRows(){
		for(String s:StaticData.names){
			clientArrayAdapter.add(new ChatData("10:45 PM", s, "hey"));
		}
	}
	
	public class ClientArrayAdapter extends ArrayAdapter<ChatData>{
		private TextView name;
		private TextView lastMsg;
		private ImageView avatar;
		

		private List<ChatData> clientList = new ArrayList<>();

		

		@Override
		public void add(ChatData object) {
			clientList.add((ChatData)object);
			super.add(object);
		}

		public ClientArrayAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);

		}

		public int getCount() {
			return this.clientList.size();
		}

		public ChatData getItem(int index) {
			return this.clientList.get(index);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = inflater.inflate(R.layout.row, parent, false);
			}
			ChatData client = getItem(position);
			name = (TextView) row.findViewById(R.id.name);
			lastMsg = (TextView) row.findViewById(R.id.last_msg);
			avatar=(ImageView) row.findViewById(R.id.my_avatar);
			final int draw=StaticData.getRandomCheeseDrawable();
			avatar.setImageDrawable(getContext().getResources().getDrawable(draw));
			name.setText(client.getName());
//			lastMsg.setText(client.getLastMsg());
			
			row.setTag(client);
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ChatData cli=(ChatData) v.getTag();
//					listener.onClientSelected(cli);
					Intent intent=new Intent(getActivity(), ChatBubbleActivity.class);
					intent.putExtra("name", cli.getName());
					intent.putExtra("logo", draw);
					startActivity(intent);

					
				}
			});
			return row;
		}
		}
}
