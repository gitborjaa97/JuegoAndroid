package com.example.juegoandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button jugar = findViewById(R.id.btn_empezar);
        if(savedInstanceState == null)
            inicializarRGs();

        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nJugadores = getNumeroJugadores();
                int dificultad = getDificultad();

                Intent i = new Intent(MainActivity.this, PantallaPreguntas.class);
                i.putExtra("jugadores", nJugadores);
                i.putExtra("dificultad", dificultad);
                startActivityForResult(i, 100);

            }
        });

        ImageButton insta = findViewById(R.id.insta);
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToURI("https://www.instagram.com/?hl=es");
            }
        });

        Button terminar = findViewById(R.id.btn_salir);
        terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CustomPreguntas.class);
                startActivity(i);
            }
        });

    }

    private void goToURI(String url){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if(resultCode == RESULT_OK){
                Bundle datos = data.getExtras();
                showToast(datos.getString("ganador"));
            }
        }
    }

    private int getNumeroJugadores(){
        RadioGroup rg = findViewById(R.id.rg_jugadores);
        RadioButton checked = findViewById(rg.getCheckedRadioButtonId());
        return Integer.parseInt(checked.getText().toString());
    }

    private int getDificultad(){
        RadioGroup rg = findViewById(R.id.rg_dificultad);
        RadioButton rb = findViewById(rg.getCheckedRadioButtonId());
        return rg.indexOfChild(rb);
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
        for (int i = 0; i < 3; i++) {
            RadioButton rb = (RadioButton) rg.getChildAt(i);
            if(rb.isChecked()){
                outState.putInt("dificultad", i);
                break;
            }
        }
        rg = findViewById(R.id.rg_jugadores);
        for (int i = 0; i < 3; i++) {
            RadioButton rb = (RadioButton) rg.getChildAt(i);
            if(rb.isChecked()){
                outState.putInt("jugadores", i);
                break;
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        RadioGroup rg = findViewById(R.id.rg_dificultad);
        int index = savedInstanceState.getInt("dificultad");
        rg.check(rg.getChildAt(index).getId());
        rg = findViewById(R.id.rg_jugadores);
        index = savedInstanceState.getInt("jugadores");
        rg.check(rg.getChildAt(index).getId());
    }
}