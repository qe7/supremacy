package gay.nns.client.setting.impl;

import gay.nns.client.setting.api.types.Setting;

import java.lang.reflect.Field;

public class SettingBoolean extends Setting<gay.nns.client.setting.api.annotations.SettingBoolean, Boolean> {

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
