package com.example.derpworthy;

import java.util.Map;

import org.vaadin.artur.icepush.ICEPush;

import com.example.derpworthy.ChatDaemon.IChatListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DerpworthyUI extends UI {
	private static final String SESSION_ID_PARAM_KEY = "sessionId";
	private static final String USERNAME_PARAM_KEY = "userName";

	private static final Long SESSION_ID_DEFAULT = 0L;
	private static final String USER_NAME_DEFAULT = "Le Derpface";

	private ICEPush pusher = new ICEPush();
	private VerticalLayout layout = new VerticalLayout();
	private Label debugInfo;
	private TextArea textArea;
	private TextField textField;

	private Long sessionId = null;
	private String userName = null;
	private ChatSession chatSession = null;

	@Override
	protected void init(VaadinRequest request) {
		// Get the sessionId and userName from HTTP GET parameters
		Map<String, String[]> paramMap = request.getParameterMap();
		if (paramMap != null) {
			String[] params = paramMap.get(SESSION_ID_PARAM_KEY);
			if (params != null && params.length == 1) {
				if (params[0] != null && !params[0].equals("")) {
					this.sessionId = Long.parseLong(params[0]);
				}
			}
			params = paramMap.get(USERNAME_PARAM_KEY);
			if (params != null && params.length == 1) {
				if (params[0] != null && !params[0].equals("")) {
					this.userName = params[0];
				}
			}
		}
		// Make the parameters default if left undefined
		if (this.sessionId == null) {
			this.sessionId = SESSION_ID_DEFAULT;
		}
		if (this.userName == null) {
			this.userName = USER_NAME_DEFAULT;
		}

		this.chatSession = ChatDaemon.getInstance().joinSession(this.sessionId, new LocalChatListener());
		layout.setMargin(true);
		setContent(layout);

		debugInfo = new Label("Username: " + userName + " | SessionId: " + sessionId);
		textArea = new TextArea("Chat Log:");
		textField = new TextField("Type Here:");

		ShortcutListener submitListener = new ShortcutListener("Chat Submit Shortcut", ShortcutAction.KeyCode.ENTER, null) {

			@Override
			public void handleAction(Object sender, Object target) {
				chatSession.chat(textField.getValue(), userName);
				textField.setValue("");
				textField.focus();
			}
		};

		textField.setImmediate(true);
		textField.addShortcutListener(submitListener);

		layout.addComponent(debugInfo);
		layout.addComponent(textArea);
		layout.addComponent(textField);
		
		// Attaches ICEPush to the UI
		this.pusher.extend(this);
	}

	private class LocalChatListener implements IChatListener {

		@Override
		public void onChat(ChatItem item) {
			StringBuilder builder = new StringBuilder(textArea.getValue());
			builder.append(item.getUserName());
			builder.append(": ");
			builder.append(item.getMessage());
			builder.append('\n');
			textArea.setValue(builder.toString());
			pusher.push();
		}

	}
}