package com.example.asteroides;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VistaJuego extends View implements SensorEventListener {

	private float mX = 0, mY = 0;

	private boolean disparo = false;

	// //// THREAD Y TIEMPO //////

	// Thread encargado de procesar el juego

	private ThreadJuego thread = new ThreadJuego();

	// Cada cuanto queremos procesar cambios (ms)

	private static int PERIODO_PROCESO = 50;

	// Cuando se realiz� el �ltimo proceso

	private long ultimoProceso = 0;

	// //// NAVE //////

	private Grafico nave;// Gr�fico de la nave

	private int giroNave; // Incremento de direcci�n

	private float aceleracionNave; // aumento de velocidad

	// Incremento est�ndar de giro y aceleraci�n

	private static final int PASO_GIRO_NAVE = 5;

	private static final float PASO_ACELERACION_NAVE = 0.5f;

	// //// MISIL //////

	private Grafico misil;

	private static int PASO_VELOCIDAD_MISIL = 12;

	private boolean misilActivo = false;

	private int tiempoMisil;

	// //// ASTEROIDES //////

	private Vector<Grafico> Asteroides; // Vector con los Asteroides

	private int numAsteroides = 5; // N�mero inicial de asteroides

	private int numFragmentos = 3; // Fragmentos en que se divide

	public VistaJuego(Context context, AttributeSet attrs) {

		super(context, attrs);

		Drawable drawableMisil = context.getResources().getDrawable(
				R.drawable.misil1);
		misil = new Grafico(this, drawableMisil);
		Drawable drawableAsteroide = context.getResources().getDrawable(
				R.drawable.asteroide1);
		Asteroides = new Vector<Grafico>();
		for (int i = 0; i < numAsteroides; i++) {
			Grafico asteroide = new Grafico(this, drawableAsteroide);
			asteroide.setIncY(Math.random() * 4 - 2);
			asteroide.setIncX(Math.random() * 4 - 2);
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			Asteroides.add(asteroide);
		}

		Drawable drawableNave = context.getResources().getDrawable(
				R.drawable.nave);
		nave = new Grafico(this, drawableNave);

		ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
		dMisil.getPaint().setColor(Color.WHITE);
		dMisil.getPaint().setStyle(Style.STROKE);
		dMisil.setIntrinsicWidth(15);
		dMisil.setIntrinsicHeight(3);
		drawableMisil = dMisil;
	}

	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anter,
			int alto_anter) {

		super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

		// Una vez que conocemos nuestro ancho y alto.
		nave.setPosX((ancho - nave.getAncho()) / 2);
		nave.setPosY((alto - nave.getAlto()) / 2);
		for (Grafico asteroide : Asteroides) {
			do {
				asteroide.setPosX(Math.random()
						* (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
			} while (asteroide.distancia(nave) < (ancho + alto) / 5);
		}

		ultimoProceso = System.currentTimeMillis();
		thread.start();

	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		for (Grafico asteroide : Asteroides) {
			asteroide.dibujaGrafico(canvas);
		}
		nave.dibujaGrafico(canvas);
		if (misilActivo) {
			misil.dibujaGrafico(canvas);
		}

	}

	protected synchronized void actualizaFisica() {

		long ahora = System.currentTimeMillis();

		// No hagas nada si el per�odo de proceso no se ha cumplido.

		if (ultimoProceso + PERIODO_PROCESO > ahora) {
			return;
		}

		// Para una ejecuci�n en tiempo real calculamos retardo

		double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora; // Para la pr�xima vez

		// Actualizamos velocidad y direcci�n de la nave a partir de
		// giroNave y aceleracionNave (seg�n la entrada del jugador)

		nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
		double nIncX = nave.getIncX() + aceleracionNave
				* Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
		double nIncY = nave.getIncY() + aceleracionNave
				* Math.sin(Math.toRadians(nave.getAngulo())) * retardo;

		// Actualizamos si el m�dulo de la velocidad no excede el m�ximo

		if (Math.hypot(nIncX, nIncY) <= Grafico.getMaxVelocidad()) {
			nave.setIncX(nIncX);
			nave.setIncY(nIncY);
		}

		// Actualizamos posiciones X e Y

		nave.incrementaPos(retardo);
		for (Grafico asteroide : Asteroides) {
			asteroide.incrementaPos(retardo);
		}

		// Actualizamos posici�n de misil

		if (misilActivo) {
			misil.incrementaPos(retardo);
			tiempoMisil -= retardo;
			if (tiempoMisil < 0) {
				misilActivo = false;
			} else {
				for (int i = 0; i < Asteroides.size(); i++)
					if (misil.verificaColision(Asteroides.elementAt(i))) {
						destruyeAsteroide(i);
						break;
					}
			}
		}

	}

	class ThreadJuego extends Thread {
		private boolean pausa, corriendo;

		public synchronized void pausar() {
			pausa = true;
		}

		public synchronized void reanudar() {
			pausa = false;
			notify();
		}

		public void detener() {
			corriendo = false;
			if (pausa)
				reanudar();
		}

		@Override
		public void run() {
			corriendo = true;
			while (corriendo) {
				actualizaFisica();
				synchronized (this) {
					while (pausa) {
						try {
							wait();
						} catch (Exception e) {
						}
					}
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		super.onTouchEvent(event);
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			disparo = true;
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dy < 6 && dx > 6) {
				giroNave = Math.round((x - mX) / 2);
				disparo = false;
			} else if (dx < 6 && dy > 6) {
				int aceleracionNueva = Math.round((mY - y) / 25);
				// if (aceleracionNueva > aceleracionNave) {
				aceleracionNave = aceleracionNueva;
				// }
				disparo = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			giroNave = 0;
			aceleracionNave = 0;
			if (disparo) {
				activaMisil();
			}
			break;

		}

		mX = x;
		mY = y;
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private boolean hayValorInicial = false;

	private float valorInicial;

	@Override
	public void onSensorChanged(SensorEvent event) {

		float valor = event.values[1];
		if (!hayValorInicial) {
			valorInicial = valor;
			hayValorInicial = true;
		}
		giroNave = (int) (valor - valorInicial) / 3;

	}

	private void destruyeAsteroide(int i) {
		Asteroides.remove(i);
		misilActivo = false;
	}

	private void activaMisil() {

		misil.setPosX(nave.getPosX() + nave.getAncho() / 2 - misil.getAncho()
				/ 2);

		misil.setPosY(nave.getPosY() + nave.getAlto() / 2 - misil.getAlto() / 2);

		misil.setAngulo(nave.getAngulo());

		misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo()))
				* PASO_VELOCIDAD_MISIL);

		misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo()))
				* PASO_VELOCIDAD_MISIL);

		tiempoMisil = (int) Math.min(
				this.getWidth() / Math.abs(misil.getIncX()), this.getHeight()
						/ Math.abs(misil.getIncY())) - 2;

		misilActivo = true;

	}

	public ThreadJuego getThread() {
		return thread;
	}

}