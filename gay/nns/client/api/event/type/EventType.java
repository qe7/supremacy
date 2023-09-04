package gay.nns.client.api.event.type;

public class EventType {

	protected Type type;

	public EventType(Type type) {
		this.type = type;
	}

	public boolean isPre() {
		if (type == null) return false;
		return type == Type.PRE;
	}

	public boolean isPost() {
		if (type == null) return false;
		return type == Type.POST;
	}

	public enum Type {
		PRE,
		POST
	}

	public Type getType() {
		return type;
	}

}
