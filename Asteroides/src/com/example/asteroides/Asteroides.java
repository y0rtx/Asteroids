package com.example.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Asteroides extends Activity {

	public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();
	private MediaPlayer mediaPlayer;
	private int posicionCancion = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button bJugar = (Button) findViewById(R.id.botonJugar);
		bJugar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				lanzarJuego(null);
			}
		});

		Button bAcercaDe = (Button) findViewById(R.id.botonAcercaDe);
		bAcercaDe.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				lanzarAcercaDe(null);
			}
		});

		Button bPreferencias = (Button) findViewById(R.id.botonConfigurar);
		bPreferencias.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				lanzarPreferencias(null);
			}
		});

		Button bPuntuaciones = (Button) findViewById(R.id.botonPuntuaciones);
		bPuntuaciones.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				lanzarPuntuaciones(null);
			}

		});

		// Button bSalir = (Button) findViewById(R.id.botonSalir);
		// bSalir.setOnClickListener(new OnClickListener() {
		// public void onClick(View view) {
		// finish();
		// }
		// });

		Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
		mediaPlayer = MediaPlayer.create(this, R.raw.audio);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
		mediaPlayer.seekTo(posicionCancion);
		mediaPlayer.start();
	}

	@Override
	protected void onPause() {
		Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
		mediaPlayer.pause();
		posicionCancion = mediaPlayer.getCurrentPosition();
		super.onPause();
	}

	@Override
	protected void onStop() {
		Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
		super.onStop();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
		// Dado que un objeto de la clase MediaPlayer consume muchos recursos,
		// resulta interesante liberarlos lo antes posible.
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
	}

	public void lanzarAcercaDe(View view) {
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}

	public void lanzarPreferencias(View view) {
		Intent i = new Intent(this, Preferencias.class);
		startActivity(i);
	}

	public void lanzarPuntuaciones(View view) {
		Intent i = new Intent(this, Puntuaciones.class);
		startActivity(i);
	}

	public void lanzarJuego(View view) {
		Intent i = new Intent(this, Juego.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
		/** true -> el menú ya está visible */
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.acercaDe:
			lanzarAcercaDe(null);
			break;
		case R.id.config:
			lanzarPreferencias(null);
			break;
		case R.id.salir:
			finish();
			break;

		}

		return true;
		/** true -> consumimos el item, no se propaga */
	}

	@Override
	protected void onSaveInstanceState(Bundle estadoGuardado) {
		super.onSaveInstanceState(estadoGuardado);
		if (mediaPlayer != null) {
			int pos = mediaPlayer.getCurrentPosition();
			estadoGuardado.putInt("posicion", pos);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle estadoGuardado) {
		super.onRestoreInstanceState(estadoGuardado);
		if (estadoGuardado != null && mediaPlayer != null) {
			posicionCancion = estadoGuardado.getInt("posicion");
			mediaPlayer.seekTo(posicionCancion);
		}
	}
}