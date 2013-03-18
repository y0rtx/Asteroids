package com.example.asteroides;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

public class Juego extends Activity {
	private VistaJuego vistaJuego;
	private SensorManager mSensorManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.juego);
		vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
	}

	@Override
	protected void onPause() {
		super.onPause();
		vistaJuego.getThread().pausar();
		mSensorManager.unregisterListener(vistaJuego);
	}

	@Override
	protected void onResume() {
		super.onResume();
		vistaJuego.getThread().reanudar();
		mSensorManager = (SensorManager) vistaJuego.getContext()
				.getSystemService(Context.SENSOR_SERVICE);

		List<Sensor> listSensors = mSensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if (!listSensors.isEmpty()) {
			Sensor orientationSensor = listSensors.get(0);
			mSensorManager.registerListener(vistaJuego, orientationSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	@Override
	protected void onDestroy() {
		vistaJuego.getThread().detener();
		super.onDestroy();
	}
}