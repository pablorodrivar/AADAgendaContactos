package com.example.pablo.aadagendacontactos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditarContacto extends AppCompatActivity {

    private EditText nombre,telefono;
    private Button editar;
    private Contacto editC;
    private boolean onEdit;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_contacto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nombre = findViewById(R.id.nombre);
        telefono = findViewById(R.id.telefono);
        editar = findViewById(R.id.editar);


        editC = getIntent().getParcelableExtra("contacto");
        i = getIntent().getIntExtra("position", 0);
        onEdit = getIntent().getBooleanExtra("onEdit",false);

        nombre.setText(editC.getNombre());
        telefono.setText(editC.getTelefono());

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarContacto.this, MainActivity.class);
                String nombreC = nombre.getText().toString();
                String telefonoC = telefono.getText().toString();
                intent.putExtra("nombre", nombreC);
                intent.putExtra("telefono", telefonoC);
                intent.putExtra("contacto",new Contacto(nombreC,telefonoC));
                intent.putExtra("i", i);
                intent.putExtra("onEdit", true);
                startActivity(intent);
            }
        });

        /*button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarContacto.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nombre", getResources().getString(R.string.borrando));
                bundle.putString("telefono", "");
                bundle.putInt("i", i);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });*/

    }

    @Override
    public void onBackPressed(){
        //super.onBackPressed();
    }
}
