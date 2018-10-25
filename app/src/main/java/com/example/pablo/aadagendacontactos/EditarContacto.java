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
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_contacto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nombre = findViewById(R.id.nombre);
        telefono = findViewById(R.id.telefono);
        editar = findViewById(R.id.editar);

        Bundle data = getIntent().getExtras();
        Contacto editC = data.getParcelable("contacto");
        i = data.getInt("i");

        Long id = editC.getId();
        String name = editC.getNombre();
        String phone = editC.getTelefono();

        nombre.setText(editC.getNombre());
        telefono.setText(editC.getTelefono());

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditarContacto.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nombre", nombre.getText().toString());
                bundle.putString("telefono", telefono.getText().toString());
                bundle.putInt("i", i);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
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
