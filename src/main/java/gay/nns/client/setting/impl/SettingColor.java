package gay.nns.client.setting.impl;

import gay.nns.client.setting.api.types.Setting;

import java.awt.*;
import java.lang.reflect.Field;

public class SettingColor extends Setting<SettingColor, Color> {

	public SettingColor(Field field, Object object) {
		super(field, object);
	}

	@Override
	public Color getValue() {
		return getFieldValue();
	}

	@Override
	public void setValue(Color t) {
		setFieldValue(t);
	}

	@Override
	public void loadSetting(String s) {
		String[] split = s.split(",");
		setValue(new Color(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])));
	}

}
