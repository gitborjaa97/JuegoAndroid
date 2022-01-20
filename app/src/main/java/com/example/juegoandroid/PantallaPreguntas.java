package com.example.juegoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private int dificultad = 3;
    private int[] puntuacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_preguntas);

        if(savedInstanceState == null) {
            datos = getIntent().getExtras();
            jugadores = datos.getInt("jugadores");
            puntuacion = new int[jugadores];
            preguntas = crearArrayDeFichero(this);
            preguntaActual = preguntaAleatoria();
            cargarPregunta(preguntaActual);
            actualizarRonda(true);
            actualizarJugador(true);
        }

        Button siguiente = findViewById(R.id.aceptar);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aniadirPuntuacion(-1);
                cargarPregunta(preguntaAleatoria());
                actualizarJugador(true);
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
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        jugadorActual = savedInstanceState.getInt("jugadorAc");
        jugadores = savedInstanceState.getInt("nJug");
        ronda = savedInstanceState.getInt("ronda");
        actualizarRonda(false);
        actualizarJugador(false);
    }

    private void showToast(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
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
            InputStreamReader isr = new InputStreamReader(context.openFileInput("preguntas.txt"));
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
        String recurso = getResources().getString(R.string.ronda);
        String mostrar = String.format(recurso,ronda);
        TextView text = findViewById(R.id.ronda);
        text.setText(mostrar);
    }
}