package owlbe.skriptLuckPerms.utils.events;

/**
 * A utility enum for events that are called for users and groups
 */
public enum Type {

	USER("user"),
	GROUP("group");

	private final String name;

	Type(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
