package com.example.juegoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    int n = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button jugar = findViewById(R.id.btn_empezar);
        inicializarRGs();

        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nJugadores = getNumeroJugadores();
                String dificultad = getDificultad();

                if(nJugadores != -1 || dificultad != null){
                    Intent i = new Intent(MainActivity.this, PantallaPreguntas.class);
                    i.putExtra("jugadores", nJugadores);
                    i.putExtra("dificultad", dificultad);
                    startActivity(i);
                }else{
                    showToast(getString(R.string.error));
                }
            }
        });
    }

    private int getNumeroJugadores(){
        RadioGroup rg = findViewById(R.id.rg_jugadores);
        RadioButton checked = findViewById(rg.getCheckedRadioButtonId());
        return Integer.parseInt(checked.getText().toString());
    }

    private String getDificultad(){
        RadioGroup rg = findViewById(R.id.rg_dificultad);
        String dificultad = null;
        if(rg.getCheckedRadioButtonId() != -1) {
            RadioButton checked = findViewById(rg.getCheckedRadioButtonId());
            dificultad = checked.getText().toString();
        }
        return dificultad;
    }

    private void showToast(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private void inicializarRGs(){
        RadioGroup rg1 = findViewById(R.id.rg_dificultad);
        rg1.check(R.id.medio);
        rg1 = findViewById(R.id.rg_jugadores);
        rg1.check(R.id.j2);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        RadioGroup rg = findViewById(R.id.rg_dificultad);
        outState.putInt("dificultad", rg.getCheckedRadioButtonId());
        rg = findViewById(R.id.rg_jugadores);
        outState.putInt("jugadores", rg.getCheckedRadioButtonId());
        outState.putInt("n", n);
    }

}