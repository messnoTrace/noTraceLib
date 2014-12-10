package com.notrace.adapter.base;

import java.lang.reflect.Field;

import android.view.View;

/**
 * use java reflect to inject view to the obj whose member has the same name to
 * the subViewId(R.id.name)
 * 
 * @author noTrace
 * 
 */
public class ViewInjectorByReflect {

	public static final void injectView(UnMixable obj, View v) {
		Class<?> mClass = obj.getClass();
		while (mClass != null) {
			Field[] fields = mClass.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				int id = v.getResources().getIdentifier(field.getName(), "id",
						v.getContext().getPackageName());
				if (id > 0) {
					View fieldView = v.findViewById(id);
					if (fieldView == null) {

					} else
						try {
							field.set(obj, fieldView);
						} catch (IllegalArgumentException e) {

							e.printStackTrace();
						} catch (IllegalAccessException e) {

							e.printStackTrace();
						}
				}
				field.setAccessible(false);
			}
			mClass = mClass.getSuperclass();
		}
	}
}
