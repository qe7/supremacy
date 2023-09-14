package gay.nns.client.api.event;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.event.type.EventCancelable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Eternal
 * <p>
 * Thanks for letting me use, Edits ofc done by me - Shae
 */
public class EventBus {

	//List of registered classes.
	private final List<Object> eventObjects = new CopyOnWriteArrayList<>();
	//List of Methods in the classes that are annotated with the "@Subscribe" annotation.
	private final List<Method> eventMethods = new CopyOnWriteArrayList<>();
	//Map of the Methods and their Owners.
	private final Map<Method, Object> methodObjectMap = new ConcurrentHashMap<>();

	public void register(Object object) {
		eventObjects.add(object);
		updateMethods();
	}

	public void unregister(Object object) {
		if (!eventObjects.contains(object))
			return;

		eventObjects.remove(object);
		updateMethods();
	}

	public void post(Object event) {
		eventMethods.stream()
				.filter(method -> method.getParameterTypes()[0] == event.getClass())
				.forEach(method -> {
					try {
						method.invoke(methodObjectMap.get(method), event);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
		if (event instanceof EventCancelable) {
			if (((EventCancelable) event).isCancelled()) {

			}
		}
	}

	private void updateMethods() {
		eventMethods.clear();
		eventObjects.stream()
				.map(Object::getClass)
				.map(Class::getDeclaredMethods)
				.map(Arrays::asList)
				.flatMap(List::stream)
				.filter(method -> method.isAnnotationPresent(Subscribe.class) && method.getParameterTypes().length == 1)
				.forEach(this::methodSetup);
		eventMethods.sort(Comparator.comparingInt(value -> value.getDeclaredAnnotation(Subscribe.class).priority().ordinal()));
	}

	private void methodSetup(Method method) {
		eventMethods.add(method);
		methodObjectMap.put(method, eventObjects.stream().filter(o -> method.getDeclaringClass().equals(o.getClass())).findFirst().orElseThrow(RuntimeException::new));
	}

}