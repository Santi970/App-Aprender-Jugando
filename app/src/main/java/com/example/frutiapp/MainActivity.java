package com.example.frutiapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_nombre;
    private ImageView iv_personaje;
    private TextView tv_BestScore;
    private MediaPlayer mp;

    int num_aleatoreo = (int)(Math.random() * 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_nombre = (EditText) findViewById(R.id.txt_nombre);
        iv_personaje = (ImageView) findViewById(R.id.imageView_Personaje);
        tv_BestScore = (TextView) findViewById(R.id.textView_BestScore);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        int id;
        if (num_aleatoreo == 0 || num_aleatoreo == 10) {

            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (num_aleatoreo == 1 || num_aleatoreo == 9) {

            id = getResources().getIdentifier("fresa", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (num_aleatoreo == 2 || num_aleatoreo == 8) {

            id = getResources().getIdentifier("manzana", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (num_aleatoreo == 3 || num_aleatoreo == 7) {

            id = getResources().getIdentifier("sandia", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        } else if (num_aleatoreo == 4 || num_aleatoreo == 5 || num_aleatoreo == 6) {

            id = getResources().getIdentifier("uva", "drawable", getPackageName());
            iv_personaje.setImageResource(id);
        }

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"BD",null, 1);
        SQLiteDatabase BD = admin.getWritableDatabase();

        Cursor consulta = BD.rawQuery(
                "Select * from puntaje where score = (select max(score) from puntaje)", null);

        if (consulta.moveToFirst()){
            String temp_nombre = consulta.getString(0);
            String temp_score = consulta.getString(1);
            tv_BestScore.setText("Record: " + temp_score + " de " + temp_nombre);
            BD.close();
        }else {
            BD.close();
        }

        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);
    }

    public void Jugar(View view){

        String nombre = et_nombre.getText().toString();

        if (!nombre.equals("")){
            mp.stop();
            mp.release();

            Intent intent = new Intent(this, MainActivity2_Nivel1.class);

            intent.putExtra("jugador", nombre);
            startActivity(intent);
            finish();

        }else{
            Toast.makeText(this, "Primero debes escribir tu nombre", Toast.LENGTH_SHORT).show();

            et_nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_nombre, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onBackPressed(){
    }
}