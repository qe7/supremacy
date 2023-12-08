package gay.nns.client.event.impl.player;

import gay.nns.client.event.api.types.EventCancelable;

public class EventMessageSent extends EventCancelable {

	private final String message;

	public EventMessageSent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
