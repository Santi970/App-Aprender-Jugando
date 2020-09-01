package com.example.frutiapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity2_Nivel3 extends AppCompatActivity {

    private TextView tv_nombre, tv_score;
    private ImageView iv_Auno, iv_Ados, iv_vidas;
    private EditText et_respuesta;
    private MediaPlayer mp, mp_great, mp_bad;

    int score, numAleatoreo_uno, numAleatoreo_dos, resultado, vidas = 3;
    String nombre_jugador, string_score, string_vidas;

    String numero[] = {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2__nivel3);

        Toast.makeText(this, "Nivel 3 - Restas", Toast.LENGTH_SHORT).show();

        tv_nombre = (TextView) findViewById(R.id.textView_nombre);
        tv_score = (TextView) findViewById(R.id.textView2_score);
        iv_Auno = (ImageView) findViewById(R.id.imageView2_num1);
        iv_Ados = (ImageView) findViewById(R.id.imageView_num2);
        iv_vidas = (ImageView) findViewById(R.id.imageView_vidas);
        et_respuesta = (EditText) findViewById(R.id.editTextNumber_resultado);

        nombre_jugador = getIntent().getStringExtra("Jugador");
        tv_nombre.setText("Jugador: " + nombre_jugador);


        string_score = getIntent().getStringExtra("Score");
        score = Integer.parseInt(string_score);
        tv_score.setText("Score: " + score);


        string_vidas = getIntent().getStringExtra("Vidas");
        vidas = Integer.parseInt(string_vidas);
        if (vidas == 3) {
            iv_vidas.setImageResource(R.drawable.tresvidas);
        }
        if (vidas == 2) {
            iv_vidas.setImageResource(R.drawable.dosvidas);
        }
        if (vidas == 1) {
            iv_vidas.setImageResource(R.drawable.unavida);
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mp = MediaPlayer.create(this, R.raw.goats);
        mp.start();
        mp.setLooping(true);

        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        mp_bad = MediaPlayer.create(this, R.raw.bad);

        NumAleatorio();
    }
    public void Comparar (View view){
        String respuesta = et_respuesta.getText().toString();

        if (!respuesta.equals("")) {

            int respuesta_jugador = Integer.parseInt(respuesta);
            if (resultado == respuesta_jugador) {
                mp_great.start();
                score++;
                tv_score.setText("Score: " + score);
                et_respuesta.setText("");
                BaseDeDatos();

            } else {

                mp_bad.start();
                vidas--;
                BaseDeDatos();

                switch (vidas) {
                    case 3:
                        iv_vidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this, "Te quedan 2 manzanas", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this, "Te queda 1 manzana", Toast.LENGTH_SHORT).show();
                        iv_vidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this, "Ya no te quedan manzanas!" + " \n " + "   Intentalo de nuevo!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        mp.stop();
                        mp.release();
                        break;
                }

                et_respuesta.setText("");
            }

            NumAleatorio();

        } else {
            Toast.makeText(this, "Escribe tu respuesta", Toast.LENGTH_SHORT).show();
        }
    }
    private void NumAleatorio() {
        if (score <= 15) {

            numAleatoreo_uno = (int) (Math.random() * 10);
            numAleatoreo_dos = (int) (Math.random() * 10);

            resultado = numAleatoreo_uno - numAleatoreo_dos;

            if (resultado >= 0 ){
                for (int i = 0; i < numero.length; i++) {
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                    if (numAleatoreo_uno == i) {
                        iv_Auno.setImageResource(id);
                    }
                    if (numAleatoreo_dos == i) {
                        iv_Ados.setImageResource(id);
                    }
                }

            }else{
                NumAleatorio();
            }

        } else {
            Intent intent = new Intent(this, MainActivity2_Nivel4.class);

            string_score = String.valueOf(score);
            string_vidas = String.valueOf(vidas);
            intent.putExtra("Jugador", nombre_jugador);
            intent.putExtra("Score", string_score);
            intent.putExtra("Vidas", string_vidas);


            startActivity(intent);
            finish();
            mp.stop();
            mp.release();
        }
    }

    public void BaseDeDatos() {
        AdminSQLiteOpenHelper adming = new AdminSQLiteOpenHelper(this, "BD", null, 1);
        SQLiteDatabase BD = adming.getWritableDatabase();

        Cursor consulta = BD.rawQuery("Select * from puntaje where score = (select max(score) from puntaje)", null);

        if (consulta.moveToFirst()) {
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);

            int bestScore = Integer.parseInt(temp_score);

            if (score > bestScore) {
                ContentValues modificacion = new ContentValues();
                modificacion.put("nombre", nombre_jugador);
                modificacion.put("Score", score);

                BD.update("puntaje", modificacion, "score=" + bestScore, null);
                BD.close();
            }

        } else {
            ContentValues insertar = new ContentValues();
            insertar.put("Nombre", nombre_jugador);
            insertar.put("Score", score);

            BD.insert("puntaje", null, insertar);
            BD.close();
        }
    }
    @Override
    public void onBackPressed(){
    }
}

