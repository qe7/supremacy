package gay.nns.client.impl.management;

import gay.nns.client.api.setting.annotations.*;
import gay.nns.client.impl.setting.SettingSlider;
import gay.nns.client.api.setting.Setting;
import gay.nns.client.api.setting.annotations.*;
import gay.nns.client.impl.setting.SettingCheckBox;
import gay.nns.client.impl.setting.SettingColor;
import gay.nns.client.impl.setting.SettingMode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Annotation based setting system.
 * <br/><br/>
 * The system was created by Eternal, but I (Shae) had to make some modifications to get it functioning properly due to recurring crashes, possibly stemming from using a different version of Lombok. However, the issue should now be resolved.
 * <br/><br/>
 *
 * @author Eternal and Shae
 */
public class SettingManager {

	private final List<Setting<?, ?>> settings = new ArrayList<>();
	private final Map<Class<? extends Annotation>, Class<? extends Setting<?, ?>>>
			annotationToSetting =
			new HashMap() {
				{
					put(CheckBox.class, SettingCheckBox.class);
					put(Slider.class, SettingSlider.class);
					put(Mode.class, SettingMode.class);
					put(ColorBox.class, SettingColor.class);
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
						if (parent instanceof SettingMode) setting.setParentMode(parent.getName());
					} else if (setting.getField().isAnnotationPresent(SettingGroup.class)) {
						setting.setGroup(setting.getField().getAnnotation(SettingGroup.class).groupName());
					}
				});
	}

	public void addToSettingManager(Object o) {
		for (Field field : o.getClass().getFields()) {
			if (field.isAnnotationPresent(Serialize.class)) {
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
