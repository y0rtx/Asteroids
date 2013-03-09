package com.example.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Asteroides extends Activity {

	public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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
		//
		// bSalir.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View view) {
		//
		// finish();
		//
		// }
		//
		// });
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
}
