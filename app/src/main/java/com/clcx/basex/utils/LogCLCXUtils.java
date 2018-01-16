package com.clcx.basex.utils;

import android.util.Log;

public class LogCLCXUtils {
	// 是否打印log
	private static final boolean DEBUG = true;

	public static void e(String msg) {
		if (DEBUG) {
			StackTraceElement[] elements = Thread.currentThread()
					.getStackTrace();
			if (elements.length < 4) {
				Log.e("Log", "Stack to Shallow");
			} else {
				String fullClassName = elements[3].getClassName();
				String className = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);
				String methodName = elements[3].getMethodName();
				int lineNumber = elements[3].getLineNumber();
				Log.e(className + "." + methodName + "():Line" + lineNumber,
						msg);
			}
		}
	}

	public static void v(String msg) {
		if (DEBUG) {
			StackTraceElement[] elements = Thread.currentThread()
					.getStackTrace();
			if (elements.length < 4) {
				Log.v("Log", "Stack to Shallow");
			} else {
				String fullClassName = elements[3].getClassName();
				String className = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);
				String methodName = elements[3].getMethodName();
				int lineNumber = elements[3].getLineNumber();
				Log.v(className + "." + methodName + "():Line" + lineNumber,
						msg);
			}
		}
	}

	public static void w(String msg) {
		if (DEBUG) {
			StackTraceElement[] elements = Thread.currentThread()
					.getStackTrace();
			if (elements.length < 4) {
				Log.w("Log", "Stack to Shallow");
			} else {
				String fullClassName = elements[3].getClassName();
				String className = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);
				String methodName = elements[3].getMethodName();
				int lineNumber = elements[3].getLineNumber();
				Log.w(className + "." + methodName + "():Line" + lineNumber,
						msg);
			}
		}
	}

	public static void d(String msg) {
		if (DEBUG) {
			StackTraceElement[] elements = Thread.currentThread()
					.getStackTrace();
			if (elements.length < 4) {
				Log.d("Log", "Stack to Shallow");
			} else {
				String fullClassName = elements[3].getClassName();
				String className = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);
				String methodName = elements[3].getMethodName();
				int lineNumber = elements[3].getLineNumber();
				Log.d(className + "." + methodName + "():Line" + lineNumber,
						msg);
			}
		}
	}

	public static void i(String msg) {
		if (DEBUG) {
			StackTraceElement[] elements = Thread.currentThread()
					.getStackTrace();
			if (elements.length < 4) {
				Log.i("Log", "Stack to Shallow");
			} else {
				String fullClassName = elements[3].getClassName();
				String className = fullClassName.substring(fullClassName
						.lastIndexOf(".") + 1);
				String methodName = elements[3].getMethodName();
				int lineNumber = elements[3].getLineNumber();
				Log.i(className + "." + methodName + "():Line" + lineNumber,
						msg);
			}
		}
	}
}
