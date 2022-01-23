package com.example.juegoandroid;

import android.os.Parcel;
import android.os.Parcelable;

public class Pregunta implements Parcelable {

    private String[] respuestas;
    private String pregunta;
    private int respuestaCorrecta;//De 0 a 2
    private int dificultad;

    public Pregunta(String[] respuestas, String pregunta, int respuestaCorrecta, int dificultad) {
        this.respuestas = respuestas;
        this.pregunta = pregunta;
        this.respuestaCorrecta = respuestaCorrecta;
        this.dificultad = dificultad;
    }

    protected Pregunta(Parcel in) {
        respuestas = in.createStringArray();
        pregunta = in.readString();
        respuestaCorrecta = in.readInt();
        dificultad = in.readInt();
    }

    public static final Creator<Pregunta> CREATOR = new Creator<Pregunta>() {
        @Override
        public Pregunta createFromParcel(Parcel in) {
            return new Pregunta(in);
        }

        @Override
        public Pregunta[] newArray(int size) {
            return new Pregunta[size];
        }
    };

    public String[] getRespuestas() {
        return respuestas;
    }

    public String getPregunta() {
        return pregunta;
    }

    public int getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public int getDificultad() {
        return dificultad;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(respuestas);
        dest.writeString(pregunta);
        dest.writeInt(respuestaCorrecta);
        dest.writeInt(dificultad);
    }
}
