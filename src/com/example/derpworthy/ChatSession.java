package com.example.derpworthy;

import java.util.ArrayList;

import com.example.derpworthy.ChatDaemon.IChatListener;

public class ChatSession {

	private ArrayList<ChatItem> chatLog;
	private ArrayList<IChatListener> listeners;

	public ChatSession() {
		this.chatLog = new ArrayList<ChatItem>();
		this.listeners = new ArrayList<ChatDaemon.IChatListener>();
	}

	public ChatSession(ArrayList<ChatItem> chatLog) {
		this.chatLog = chatLog;
		this.listeners = new ArrayList<ChatDaemon.IChatListener>();
	}

	public void chat(String message, String userName) {
		ChatItem newChat = new ChatItem(message, userName);
		this.chatLog.add(newChat);
		notify(newChat);
	}

	private void notify(ChatItem newChat) {
		for (IChatListener listener : listeners) {
			if (listener != null) {
				listener.onChat(newChat);
			}
		}
	}

	public void addListener(IChatListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(IChatListener listener) {
		this.listeners.remove(listener);
	}
}
