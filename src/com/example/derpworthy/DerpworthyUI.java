package com.example.derpworthy;

import java.util.concurrent.ConcurrentHashMap;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DerpworthyUI extends UI{

	private static ConcurrentHashMap<Long, ChatSession> allChats;
	private static long nextSessionId;

	private long sessionId = 0; //where does this get passed in from?
	private String username = "caputit1"; //where does this get passed in from?
	private VerticalLayout layout = new VerticalLayout();
	private Label debugInfo;
	private TextArea textArea;
	private TextField textField;
	
	@Override
	protected void init(VaadinRequest request) {
		layout.setMargin(true);
		setContent(layout);
		
		sessionId = getNextSessionId();
		if(allChats == null) allChats = new ConcurrentHashMap<Long, ChatSession>();
		if(allChats.get(sessionId) == null) allChats.put(sessionId, new ChatSession());

		debugInfo = new Label("Username: " + username + " | SessionId: " + sessionId);
		textArea = new TextArea("Chat Log:");
		textField = new TextField("Type Here:");
		
		textField.addTextChangeListener(new TextChangeListener() {
		    public void textChange(TextChangeEvent event) {
		    	//dont know why this isnt working either...
		        if(event.getText().endsWith("\n")) enterMessage();
		    }
		});

		Button button = new Button("Enter");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				enterMessage();
			}
		});
		
		layout.addComponent(debugInfo);
		layout.addComponent(textArea);
		layout.addComponent(textField);
		layout.addComponent(button);
	}

	private long getNextSessionId() {
//		nextSessionId++;
//		return nextSessionId;
		return 0;
	}
	
	private void enterMessage(){
		ChatItem newCI = new ChatItem(textField.getValue(), username);
		ChatSession session = allChats.get(sessionId);
		session.setLastUser(username);
		session.getChatLog().add(newCI);
		updateChatLog();
		//notify other users to run updateChatLog();
		textField.setValue("");
	}

	private void updateChatLog(){
		ChatSession cs = allChats.get(sessionId);
		String chatDisplay = "";
		for(ChatItem ci : cs.getChatLog()){
			chatDisplay += ci.getUserName() + ": " + ci.getMessage() + "\n";
		}
		textArea.setValue(chatDisplay);
	}
}