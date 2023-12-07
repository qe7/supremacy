package gay.nns.client.api.event.type;

public class EventCancelable {

	private boolean cancelled;

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
