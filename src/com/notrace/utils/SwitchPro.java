package com.notrace.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;

public class SwitchPro {
	public static ConnectivityManager mConnectivityManager;
	private static WifiManager mWifiManager;
	private static BluetoothAdapter mBluetoothAdapter;

	/** 获取蓝牙当前状态 */
	public static int getBluetoothStatus() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		return mBluetoothAdapter.getState();
	}

	/** 设置蓝牙开关 */
	public static void setBluetoothStatus() {
		switch (getBluetoothStatus()) {
		case BluetoothAdapter.STATE_ON:
			mBluetoothAdapter.disable();
			break;
		case BluetoothAdapter.STATE_TURNING_ON:
			mBluetoothAdapter.disable();
			break;
		case BluetoothAdapter.STATE_OFF:
			mBluetoothAdapter.enable();
			break;
		case BluetoothAdapter.STATE_TURNING_OFF:
			mBluetoothAdapter.enable();
			break;
		}
	}

	/** 设置wifi状态 */
	public static void setWifiStatus(Context context, boolean enabled) {
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		mWifiManager.setWifiEnabled(enabled);
	}

	/** 获取wifi状态 */
	public static boolean getWifiStatus(Context context) {
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED
				|| mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
			return true;
		} else {
			return false;
		}
	}

	/** 获取移动数据开关状态 */
	public static boolean getMobileDataStatus(Context c) {
		mConnectivityManager = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		String methodName = "getMobileDataEnabled";
		Class cmClass = mConnectivityManager.getClass();
		Boolean isOpen = null;

		try {
			Method method = cmClass.getMethod(methodName, null);

			isOpen = (Boolean) method.invoke(mConnectivityManager, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isOpen;
	}

	/** 通过反射实现开启或关闭移动数据 */
	public static void setMobileDataStatus(boolean enabled) {
		try {
			Class<?> conMgrClass = Class.forName(mConnectivityManager
					.getClass().getName());
			// 得到ConnectivityManager类的成员变量mService（ConnectivityService类型）
			Field iConMgrField = conMgrClass.getDeclaredField("mService");
			iConMgrField.setAccessible(true);
			// mService成员初始化
			Object iConMgr = iConMgrField.get(mConnectivityManager);
			// 得到mService对应的Class对象
			Class<?> iConMgrClass = Class.forName(iConMgr.getClass().getName());
			/*
			 * 得到mService的setMobileDataEnabled(该方法在android源码的ConnectivityService类中实现
			 * )， 该方法的参数为布尔型，所以第二个参数为Boolean.TYPE
			 */
			Method setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			/*
			 * 调用ConnectivityManager的setMobileDataEnabled方法（方法是隐藏的），
			 * 实际上该方法的实现是在ConnectivityService(系统服务实现类)中的
			 */
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}
	}

	/** 获取Gps开启或关闭状态 */
	public static boolean getGpsStatus(Context context) {
		boolean status = Secure.isLocationProviderEnabled(
				context.getContentResolver(), LocationManager.GPS_PROVIDER);
		return status;
	}

	/** 打开或关闭Gps */
	public static void setGpsStatus(Context context, boolean enabled) {
		Secure.setLocationProviderEnabled(context.getContentResolver(),
				LocationManager.GPS_PROVIDER, enabled);
	}

	/**
	 * 获取飞行模式开关状态
	 * 
	 * @param context
	 * @return false为关闭中，true为开启中
	 */
	@SuppressLint("NewApi")
	public static boolean getAirPlaneModeStatus(Context context) {
		ContentResolver cr = context.getContentResolver();
		int isAirplaneMode = Settings.Global.getInt(cr,
				Settings.Global.AIRPLANE_MODE_ON, 0);
		// 如果当前飞行模式状态，返回的String值为0，或1。0为关闭飞行模式中，1为开启飞行模式中
		// 如果是关闭飞行模式中，则返回false
		// 否则为开启飞行模式中，则返回true
		return (isAirplaneMode == 1) ? true : false;
	}

	/**
	 * 切换飞行模式
	 * 
	 * @param state
	 *            true:开 false:关
	 */
	@SuppressLint("NewApi")
	public static boolean toggleAirPlaneMode(Context context, boolean state) {
		try {
			Settings.Global.putInt(context.getContentResolver(),
					Settings.Global.AIRPLANE_MODE_ON, state ? 1 : 0);
			Intent i = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			i.putExtra("state", state);
			Class<UserHandle> c = UserHandle.class;
			Field field = c.getDeclaredField("ALL");
			field.setAccessible(true);
			context.sendBroadcastAsUser(i, (UserHandle)field.get(UserHandle.class));
			return true;
		} catch (Exception e) {
			Log.e("飞行模式异常", e.toString());
			e.printStackTrace();
		}
		return false;
	}

}