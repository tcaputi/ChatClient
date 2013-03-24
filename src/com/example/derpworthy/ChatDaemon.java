package com.example.derpworthy;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class ChatDaemon {
	private ConcurrentHashMap<Long, ChatSession> sessions = new ConcurrentHashMap<Long, ChatSession>();
	private static ChatDaemon instance;
	
	private ChatDaemon() {
		// Cannot be constructed by anyone else
	}
	
	public static ChatDaemon getInstance() {
		if (ChatDaemon.instance == null) {
			ChatDaemon.instance = new ChatDaemon();
		}
		
		return ChatDaemon.instance;
	}
	
	public synchronized ChatSession joinSession(Long sessionId, IChatListener delegate) {
		ChatSession session = null;
		if (!sessions.contains(sessionId)) {
			session = new ChatSession();
			sessions.put(sessionId, session);
		} else {
			session = sessions.get(sessionId);
		}
		
		session.addListener(delegate);
		
		return session;
	}
	
	public synchronized void leaveSession(Long sessionId, IChatListener delegate) {
		ChatSession session = sessions.get(sessionId);
		if (session != null) {
			session.removeListener(delegate);
		}
	}
	
	public interface IChatListener extends Serializable {
		public void onChat(ChatItem item);
	}
}
