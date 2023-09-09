package gay.nns.client.impl.event.player;

import gay.nns.client.api.event.type.EventCancelable;

public class EventMessageSent extends EventCancelable {

	private final String message;

	public EventMessageSent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
