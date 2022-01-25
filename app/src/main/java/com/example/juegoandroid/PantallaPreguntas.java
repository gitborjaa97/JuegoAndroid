package com.example.juegoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PantallaPreguntas extends AppCompatActivity {

    private Bundle datos;
    private int jugadorActual = 0;
    private int ronda = 0;
    private int jugadores;
    private ArrayList<Pregunta> preguntas = new ArrayList<>();
    private Pregunta preguntaActual;
    private int respuestaCorrecta;
    private int dificultad ;
    private int[] puntuacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_preguntas);

        if(savedInstanceState == null) {
            datos = getIntent().getExtras();
            jugadores = datos.getInt("jugadores");
            dificultad = datos.getInt("dificultad");
            puntuacion = new int[jugadores];
            preguntas = crearArrayDeFichero(this);
            preguntaActual = preguntaAleatoria();
            respuestaCorrecta = preguntaActual.getRespuestaCorrecta();
            cargarPregunta(preguntaActual);
            actualizarRonda(true);
            actualizarJugador(true);
        }

        Button siguiente = findViewById(R.id.aceptar);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup rg = findViewById(R.id.respuestas);

                if(rg.getCheckedRadioButtonId() != -1) {
                    if (comprobarRespuesta()) {
                        aniadirPuntuacion(2);
                        showToast(getString(R.string.acierto));
                    }else{
                        showToast(getString(R.string.fallo));
                    }
                    actualizarJugador(true);
                    cargarPregunta(preguntaAleatoria());
                }else{
                    showToast(getString(R.string.seleciona));
                }
            }
        });

        Button saltar = findViewById(R.id.saltar);
        saltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aniadirPuntuacion(-1);
                cargarPregunta(preguntaAleatoria());
                actualizarJugador(true);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("jugadorAc",jugadorActual);
        outState.putInt("nJug", jugadores);
        outState.putInt("ronda",ronda);
        outState.putInt("dificultad", dificultad);
        outState.putIntArray("puntuacion", puntuacion);
        outState.putParcelableArrayList("preguntas", preguntas);
        outState.putParcelable("preguntaActual", preguntaActual);
        outState.putInt("respuestaCorrecta", respuestaCorrecta);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        jugadorActual = savedInstanceState.getInt("jugadorAc");
        jugadores = savedInstanceState.getInt("nJug");
        ronda = savedInstanceState.getInt("ronda");
        dificultad = savedInstanceState.getInt("dificultad");
        actualizarRonda(false);
        actualizarJugador(false);
        preguntaActual = savedInstanceState.getParcelable("preguntaActual");
        respuestaCorrecta = savedInstanceState.getInt("respuestaCorrecta");
        cargarPregunta(preguntaActual);
        puntuacion = savedInstanceState.getIntArray("puntuacion");
        preguntas = savedInstanceState.getParcelableArrayList("preguntas");
    }

    private boolean comprobarRespuesta(){
        RadioGroup rg = findViewById(R.id.respuestas);
        int checked = rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId()));
        boolean acierto = false;
        if(checked == respuestaCorrecta)
            acierto = true;

        return acierto;
    }

    private void showToast(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void cargarPregunta(Pregunta pregunta){
        TextView text = findViewById(R.id.pregunta);
        text.setText(pregunta.getPregunta());
        respuestaCorrecta = pregunta.getRespuestaCorrecta();
        String[] respuestas = pregunta.getRespuestas();

        RadioButton respuesta =findViewById(R.id.respuesta1);
        respuesta.setText(respuestas[0]);
        respuesta =findViewById(R.id.respuesta2);
        respuesta.setText(respuestas[1]);
        respuesta =findViewById(R.id.respuesta3);
        respuesta.setText(respuestas[2]);

        RadioGroup rg = findViewById(R.id.respuestas);
        rg.check(-1);
    }

    private Pregunta preguntaAleatoria(){
        int nPregunta = (int) Math.round(Math.random() * (preguntas.size()-1));
        return preguntas.get(nPregunta);
    }

    private void actualizarJugador(boolean nuevo){
        if(nuevo) {
            jugadorActual++;
            if (jugadorActual > jugadores) {
                jugadorActual = 1;
                actualizarRonda(true);
            }
        }
        String recurso = getResources().getString(R.string.turno);
        String mostrar = String.format(recurso,jugadorActual);
        TextView text = findViewById(R.id.turno);
        text.setText(mostrar);
    }

    private ArrayList<Pregunta> crearArrayDeFichero(Context context){
        ArrayList<Pregunta> preguntas = new ArrayList<>();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.preguntas);
            InputStreamReader isr = new InputStreamReader(is, "UTF8");
            BufferedReader br = new BufferedReader(isr);
            String linea;
            while ((linea = br.readLine())!= null){
                String[] separado = linea.split(",");
                if(Integer.parseInt(separado[5]) <= dificultad)
                    preguntas.add(generarPregunta(separado));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return preguntas;
    }

    private Pregunta generarPregunta(String[] lectura){
        String[] respuestas = new String[3];
        for (int i = 0; i < 3; i++){
            respuestas[i] = lectura[i+1];
        }
        return new Pregunta(respuestas,lectura[0], Integer.parseInt(lectura[4]), Integer.parseInt(lectura[5]));
    }

    private void aniadirPuntuacion(int puntuacion){
        this.puntuacion[jugadorActual - 1] += puntuacion;
    }

    private void actualizarRonda(boolean nuevo){
        if(nuevo)
            ronda++;
        if(ronda == 6)
            terminarPartida();

        String recurso = getString(R.string.ronda);
        String mostrar = String.format(recurso,ronda);
        TextView text = findViewById(R.id.ronda);
        text.setText(mostrar);
    }

    private void terminarPartida(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        Intent intent = new Intent();
        intent.putExtra("ganador", getGanador());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private String getGanador(){
        int ganador = 0;
        int puntuacionGanador = 0;
        for (int i = 0; i < puntuacion.length; i++){
            if(puntuacion[i] > puntuacionGanador){
                puntuacionGanador = puntuacion[i];
                ganador = i;
            }
        }
        String recurso = getString(R.string.ganador);
        return String.format(recurso, ganador + 1);
    }
}