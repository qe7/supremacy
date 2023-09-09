package gay.nns.client.impl.setting;

import gay.nns.client.api.setting.AbstractSetting;

import java.lang.reflect.Field;

public class SettingBoolean extends AbstractSetting<gay.nns.client.api.setting.annotations.SettingBoolean, Boolean> {

	public SettingBoolean(Field field, Object object) {
		super(field, object);
	}

	@Override
	public Boolean getValue() {
		return getFieldValue();
	}

	@Override
	public void setValue(Boolean aBoolean) {
		setFieldValue(aBoolean);
	}

	@Override
	public void loadSetting(String s) {
		setValue(Boolean.parseBoolean(s));
	}


}
