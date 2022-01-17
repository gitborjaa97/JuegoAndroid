package com.example.juegoandroid;

public class Pregunta {

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
}
