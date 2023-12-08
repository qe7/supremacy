package gay.nns.client.setting.api.types;

import gay.nns.client.setting.api.annotations.SerializeSetting;

import java.lang.reflect.Field;

public abstract class Setting<A, V> {

	private final String name;
	private final String description;
	private final Field field;
	private final A annotation;
	private final Object object;
	private Setting<?, ?> parent;
	private String parentMode;
	private String group;

	public Setting(Field field, Object object) {
		this.field = field;
		this.name = field.getAnnotation(SerializeSetting.class).name();
		this.description = field.getAnnotation(SerializeSetting.class).desc();
		this.annotation = (A) field.getAnnotations()[1];
		this.object = object;
	}

	protected V getFieldValue() {
		try {
			return (V) this.field.get(this.object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void setFieldValue(V value) {
		try {
			this.field.set(this.object, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public A getAnnotation() {
		return annotation;
	}

	public String getName() {
		return name;
	}

	public Field getField() {
		return field;
	}

	public Object getObject() {
		return object;
	}

	public Setting<?, ?> getParent() {
		return parent;
	}

	public void setParent(Setting<?, ?> parent) {
		this.parent = parent;
	}

	public void setParentMode(String parentMode) {
		this.parentMode = parentMode;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public abstract V getValue();

	public abstract void setValue(V t);

	public abstract void loadSetting(String s);

}
