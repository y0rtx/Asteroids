package com.example.asteroides;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferencias extends PreferenceActivity {

	// http://stackoverflow.com/questions/13454979/how-to-use-addpreferencesfromresource-for-android-2-x
	// @SuppressWarnings("deprecation")
	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	//
	// // Usamos addPreferencesfromResource si el API level es menor que 11
	// if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
	// addPreferencesFromResource(R.xml.preferencias);
	// }
	//
	// }
	//
	// // Usamos onBuildHeaders() para API level 11 y superior
	// @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	// @Override
	// public void onBuildHeaders(List<Header> target) {
	// loadHeadersFromResource(R.xml.preferencias, target);
	// }
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferencias);

	}
}
