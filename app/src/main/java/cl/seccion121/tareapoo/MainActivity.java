package cl.seccion121.tareapoo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextInputLayout tilSerie, tilDescrip, tilValor;
    private Spinner spnTipo;
    private TextView tvPag;
    private Button btnGrabar, btnEliminar, btnRet, btnAva;

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

        indiceActual = -1;

        Log.d("TAG_", "Probar");
    }


    private void poblar() {
        tipos = new String[6];
        tipos[0] = "Seleccione tipo";tipos[1] = "PC"; tipos[2] = "Notebook";
        tipos[3] = "Mouse"; tipos[4] = "Teclado"; tipos[5] = "Accesorios";

        losEquipos = new ArrayList<>();
        losEquipos.add(new Equipo(111, "HP 200SE", 10000, "Notebook"));
        losEquipos.add(new Equipo(222, "Genius", 3000, "Mouse"));
        losEquipos.add(new Equipo(333, "Pendrive", 1500, "Accesorio"));
    }

    //TODO: Mirko debe mostrar todos los datos
    private void mostrarDatos(){
        if(indiceActual >= 0 && indiceActual < losEquipos.size()){
            Equipo e = losEquipos.get(indiceActual);
            String serieStr = "";
            serieStr = String.valueOf(e.getSerie());
            tilSerie.getEditText().setText(serieStr);

            tvPag.setText((indiceActual + 1) + " de " + losEquipos.size());

            btnAva.setEnabled(false);
            btnRet.setVisibility(View.INVISIBLE);


        }else{
            //TODO: Hermanos campos
            Log.d("TAG_", "Índice actual " + indiceActual);
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

        miAdaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        spnTipo.setAdapter(miAdaptador);

    }

    private void eventos() {
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indiceActual = indiceActual + 1;
                mostrarDatos();
            }
        });

        btnRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indiceActual = indiceActual - 1;
                mostrarDatos();
            }
        });
    }

    //endregion

}