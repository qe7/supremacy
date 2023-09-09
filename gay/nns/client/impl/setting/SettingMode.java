package gay.nns.client.impl.setting;

import gay.nns.client.api.setting.AbstractSetting;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class SettingMode extends AbstractSetting<gay.nns.client.api.setting.annotations.SettingMode, String> {

	int index;

	public SettingMode(Field field, Object object) {
		super(field, object);
	}

	@Override
	public String getValue() {
		return getFieldValue();
	}

	@Override
	public void setValue(String t) {
		setFieldValue(t);
	}

	@Override
	public void loadSetting(String s) {
		setValue(s);
	}

	public List<String> getModes() {
		return Arrays.asList(getAnnotation().modes());
	}

	public void setMode(String s) {
		index = this.getModes().indexOf(s);
		setValue(s);
	}

	public boolean is(String mode) {
		return this.getValue().equals(mode);
	}

	public void cycle() {
		if (index < getModes().size() - 1) {
			index++;
		} else {
			index = 0;
		}
		setMode(getModes().get(index));
	}

	public void cycleReverse() {
		if (index > 0) {
			index--;
		} else {
			index = getModes().size() - 1;
		}
		setMode(getModes().get(index));
	}

}
