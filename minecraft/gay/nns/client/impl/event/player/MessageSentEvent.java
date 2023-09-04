package gay.nns.client.impl.event.player;

import gay.nns.client.api.event.type.EventCancelable;

public class MessageSentEvent extends EventCancelable {

	private final String message;

	public MessageSentEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
