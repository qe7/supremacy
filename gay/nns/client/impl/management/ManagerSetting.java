package gay.nns.client.impl.management;

import gay.nns.client.api.setting.annotations.*;
import gay.nns.client.api.setting.Setting;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Eternal
 *
 * some changes by **Shae**zbreizh
 */
public class ManagerSetting {

	private final List<Setting<?, ?>> settings = new ArrayList<>();
	private final Map<Class<? extends Annotation>, Class<? extends Setting<?, ?>>>
			annotationToSetting =
			new HashMap() {
				{
					put(SettingBoolean.class, gay.nns.client.impl.setting.SettingBoolean.class);
					put(SettingSlider.class, gay.nns.client.impl.setting.SettingSlider.class);
					put(SettingMode.class, gay.nns.client.impl.setting.SettingMode.class);
					put(SettingColor.class, gay.nns.client.impl.setting.SettingColor.class);
				}
			};

	// State of the art "I can't think of a better implementation"
	public void init() {
		settings.forEach(
				setting -> {
					if (setting.getField().isAnnotationPresent(Parent.class)) {
						Setting<?, ?> parent =
								getSetting(
										setting.getObject().getClass(),
										setting.getField().getAnnotation(Parent.class).parent());
						setting.setParent(parent);
						if (parent instanceof gay.nns.client.impl.setting.SettingMode) setting.setParentMode(parent.getName());
					} else if (setting.getField().isAnnotationPresent(SettingGroup.class)) {
						setting.setGroup(setting.getField().getAnnotation(SettingGroup.class).groupName());
					}
				});
	}

	public void addToSettingManager(Object o) {
		for (Field field : o.getClass().getFields()) {
			if (field.isAnnotationPresent(SerializeSetting.class)) {
				Class<? extends Annotation> settingType = field.getAnnotations()[1].annotationType();
				Class<? extends Setting<?, ?>> setting = annotationToSetting.get(settingType);

				try {
					Setting<?, ?> instance = setting.getConstructor(Field.class, Object.class).newInstance(field, o);
					settings.add(instance);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Setting<?, ?> getSetting(Class<?> clazz, String name) {
		return settings.stream()
				.filter(
						setting -> setting.getObject().getClass() == clazz && setting.getName().equalsIgnoreCase(name))
				.findFirst()
				.orElseThrow(NoSuchElementException::new);
	}

	public List<Setting<?, ?>> getSettingsFromType(Class<?> clazz) {
		return settings.stream().filter(setting -> setting.getObject().getClass() == clazz).toList();
	}
}
