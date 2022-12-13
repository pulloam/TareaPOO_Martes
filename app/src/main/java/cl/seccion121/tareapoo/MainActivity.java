package cl.seccion121.tareapoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout tilSerie, tilDescrip, tilValor;
    private Spinner spnTipo;
    private TextView tvPag;
    private Button btnGrabar, btnEliminar, btnRet, btnAva, btnCrearBD;

    private String[] tipos;
    private ArrayList<Equipo> losEquipos;

    private ArrayAdapter<String> miAdaptador;

    private int indiceActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poblar();
        referencias();
        eventos();
        limpiarPantalla();
        obtenerUltimoIndice();
    }

    private void obtenerUltimoIndice() {
        SharedPreferences sp = getSharedPreferences("datos", Context.MODE_PRIVATE);
        indiceActual = sp.getInt("indice", -1);
        mostrarDatos();
    }

    private void guardarIndiceActual(){
        SharedPreferences sp = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorSP = sp.edit();

        editorSP.putInt("indice", indiceActual);
        editorSP.putString("serie", tilSerie.getEditText().getText().toString());
        editorSP.commit();
    }


    @Override
    protected void onDestroy() {
        guardarIndiceActual();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        guardarIndiceActual();
        super.onPause();
    }

    private void poblar() {
        tipos = new String[6];
        tipos[0] = "Seleccione tipo";tipos[1] = "PC"; tipos[2] = "Notebook";
        tipos[3] = "Mouse"; tipos[4] = "Teclado"; tipos[5] = "Accesorios";

        losEquipos = new ArrayList<>();
        losEquipos.add(new Equipo(111, "HP 200SE", 10000, "Notebook"));
        losEquipos.add(new Equipo(222, "Genius", 3000, "Mouse"));
        losEquipos.add(new Equipo(333, "Pendrive", 1500, "Accesorios"));
    }

    //TODO: Mirko debe mostrar todos los datos
    private void mostrarDatos(){
        if(indiceActual >= 0 && indiceActual < losEquipos.size()){
            Equipo e = losEquipos.get(indiceActual);
            String serieStr = String.valueOf(e.getSerie());
            String valorStr = String.valueOf(e.getValor());
            tilSerie.getEditText().setText(serieStr);
            tilDescrip.getEditText().setText(e.getDescripcion());
            tilValor.getEditText().setText(valorStr);
            if(e.getTipo().equals("PC")) spnTipo.setSelection(1);

            if(e.getTipo().equals("Notebook")) spnTipo.setSelection(2);

            if(e.getTipo().equals("Mouse")) spnTipo.setSelection(3);

            if(e.getTipo().equals("Teclado")) spnTipo.setSelection(4);

            if(e.getTipo().equals("Accesorios")) spnTipo.setSelection(5);

            tvPag.setText((indiceActual + 1) + " de " + losEquipos.size());
        }
    }


    //region referencias y eventos
    private void referencias(){
        tilSerie = findViewById(R.id.tilSerie);
        tilDescrip = findViewById(R.id.tilDescripcion);
        tilValor = findViewById(R.id.tilValor);
        spnTipo = findViewById(R.id.spnTipo);
        tvPag = findViewById(R.id.tvPag);

        btnGrabar = findViewById(R.id.btnGrabar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnAva = findViewById(R.id.btnAvan);
        btnRet = findViewById(R.id.btnRetro);

        btnCrearBD = findViewById(R.id.btnCrearBD);

        miAdaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        spnTipo.setAdapter(miAdaptador);

    }

    //TODO: Validar 1ro que no sea el mismo serie, 2do datos obligatorios
    private void grabarEquipo(){
        Equipo e = new Equipo();
        e.setSerie(Integer.parseInt(tilSerie.getEditText().getText().toString()));
        e.setDescripcion(tilDescrip.getEditText().getText().toString());
        e.setTipo(spnTipo.getSelectedItem().toString());
        e.setValor(Integer.parseInt(tilValor.getEditText().getText().toString()));

        losEquipos.add(e);

        Toast.makeText(this, "Grabado exitosamente", Toast.LENGTH_SHORT).show();
        limpiarPantalla();
    }

    private void limpiarPantalla(){
        tilSerie.getEditText().setText("");
        tilDescrip.getEditText().setText("");
        tilValor.getEditText().setText("");
        spnTipo.setSelection(0);
        tvPag.setText("[" + losEquipos.size() + "]");
        indiceActual = -1;
    }

    private void crearBD(){
        AdministradorBaseDatos adbs = new AdministradorBaseDatos(this, "BDPrueba", null, 1);
        try{
            SQLiteDatabase miBD = adbs.getWritableDatabase();

            if(miBD != null){
                miBD.execSQL("insert into equipos (serie, descripcion) values(" + tilSerie.getEditText().getText().toString()  + ", 'Equipo 222')");
            }

            ContentValues registro = new ContentValues();
            registro.put("serie", 444);
            registro.put("descripcion", "Equipo 444");

            Log.d("TAG_", "Insertado " + miBD.insert("equipos", null, registro));
            miBD.close();
        }catch (Exception ex){
            Log.e("TAG_", ex.toString());
        }



    }

    private void eventos() {
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabarEquipo();
            }
        });

        //TODO: validar que efectivamente exista la serie en la lista, crear método
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                losEquipos.remove(indiceActual);
                limpiarPantalla();
            }
        });

        btnAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indiceActual = indiceActual + 1;
                if(indiceActual == losEquipos.size())
                    indiceActual = 0;

                mostrarDatos();
            }
        });

        btnRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indiceActual = indiceActual - 1;

                if(indiceActual == -1)
                    indiceActual = losEquipos.size() - 1;

                mostrarDatos();
            }
        });

        btnCrearBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearBD();
            }
        });
    }

    //endregion

}