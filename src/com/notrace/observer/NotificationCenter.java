package com.notrace.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在需要刷新的activity implements
 * NotificationObserver,调用NotificationCenter.getInstance().addObserver("tag",)，
 * 重写 onReceiveNotification()方法，在别的页面调用NotificationCenter.getInstance().
 * postNotification(); Created by notrace
 * 
 */
public class NotificationCenter {

	private static NotificationCenter instance;
	private Map<String, List<NotificationObserver>> map_obervers = new HashMap<String, List<NotificationObserver>>();

	public static NotificationCenter getInstance() {
		if (instance == null) {
			instance = new NotificationCenter();
		}
		return instance;
	}

	public void addObserver(String tag, NotificationObserver observer) {
		List<NotificationObserver> observers = map_obervers.get(tag);

		if (observers == null) {
			observers = new ArrayList<NotificationObserver>();

		}
		if (observers.contains(observer)) {
			return;
		}
		observers.add(observer);
		map_obervers.put(tag, observers);
	}

	public void removeObserver(NotificationObserver observer) {
		for (List<NotificationObserver> list_observers : map_obervers.values()) {
			list_observers.remove(observer);
		}
	}

	public void removeObserver(String tag, NotificationObserver observer) {
		List<NotificationObserver> observers = map_obervers.get(tag);
		if (observers == null) {
			return;
		}
		observers.remove(observer);
	}

	public void postNotification(String tag, Object data) {
		List<NotificationObserver> observers = map_obervers.get(tag);

		if (observers == null) {
			return;
		}
		for (NotificationObserver observer : observers) {
			observer.onReceiveNotification(tag, data);
		}
	}

	public void postNotification(String tag) {
		postNotification(tag, null);
	}

	public Map<String, List<NotificationObserver>> getObservers() {
		return map_obervers;
	}

	public List<NotificationObserver> getObservers(String notification) {
		return map_obervers.get(notification);
	}
}
