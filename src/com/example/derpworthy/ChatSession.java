package com.example.derpworthy;

import java.util.ArrayList;

public class ChatSession{
	
	private String lastUser;
	private ArrayList<ChatItem> chatLog;
	
	public ChatSession(){
		this.lastUser = "";
		this.chatLog = new ArrayList<ChatItem>();
	}
	
	public ChatSession(ArrayList<ChatItem> chatLog) {
		this.lastUser = "";
		this.chatLog = chatLog;
	}
	
	public ArrayList<ChatItem> getChatLog() {
		return chatLog;
	}
	
	public void setChatLog(ArrayList<ChatItem> chatLog) {
		this.chatLog = chatLog;
	}

	public String getLastUser() {
		return lastUser;
	}

	public void setLastUser(String lastUser) {
		this.lastUser = lastUser;
	}
}
