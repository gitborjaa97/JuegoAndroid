package com.example.juegoandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class CustomPreguntas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_preguntas);
        inicializarSpinner(this);

        Button guardar = findViewById(R.id.guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escribirFichero(generarPregunta());
            }
        });
    }

    private void inicializarSpinner(Context context){
        Spinner sp = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(context,
                R.array.opciones_respuesta_correcta, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adaptador);
    }

    private Pregunta generarPregunta(){
        String[] respuestas = new String[3];
        EditText et = findViewById(R.id.custom_respuesta1);
        respuestas[0] = et.getText().toString();
        et = findViewById(R.id.custom_respuesta2);
        respuestas[1] = et.getText().toString();
        et = findViewById(R.id.custom_respuesta3);
        respuestas[2] = et.getText().toString();
        et = findViewById(R.id.custom_pregunta);
        String pregunta = et.getText().toString();

        RadioGroup rg = findViewById(R.id.custom_dificultad);
        int dificultad = rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId()));
        Spinner sp = findViewById(R.id.spinner);
        int correcta = sp.getSelectedItemPosition();

        return new Pregunta(respuestas, pregunta, correcta, dificultad);
    }

    private void escribirFichero( Pregunta pregunta ){
        File fichero = new File(getFilesDir().toString()+"/custom.obj");

        if (fichero.exists()){
            try {
                OutputStream os = this.openFileOutput("custom.obj",
                        MODE_APPEND);
                MiObjectOS moos = new MiObjectOS(os);

                moos.writeObject(pregunta);
                moos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            try {
                OutputStream os = this.openFileOutput("custom.obj",
                        MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(os);

                oos.writeObject(pregunta);
                oos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}