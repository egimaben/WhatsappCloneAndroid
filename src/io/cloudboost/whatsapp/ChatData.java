package io.cloudboost.whatsapp;

public class ChatData {
private String time;
private String name;
private String lastMsg;
public ChatData(String time, String name, String lastMsg) {
	super();
	this.time = time;
	this.name = name;
	this.lastMsg = lastMsg;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getLastMsg() {
	return lastMsg;
}
public void setLastMsg(String lastMsg) {
	this.lastMsg = lastMsg;
}

}
