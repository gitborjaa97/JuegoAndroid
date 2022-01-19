package com.example.juegoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PantallaPreguntas extends AppCompatActivity {

    private Bundle datos;
    private int jugadorActual = 0;
    private int ronda = 0;
    private int jugadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_preguntas);

        if(savedInstanceState == null) {
            datos = getIntent().getExtras();
            String dificultad = datos.getString("dificultad");
            jugadores = datos.getInt("jugadores");

            actualizarRonda(true);
            actualizarJugador(true);
        }

        Button siguiente = findViewById(R.id.aceptar);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void actualizarRonda(boolean nuevo){
        if(nuevo)
            ronda++;
        String recurso = getResources().getString(R.string.ronda);
        String mostrar = String.format(recurso,ronda);
        TextView text = findViewById(R.id.ronda);
        text.setText(mostrar);
    }
}