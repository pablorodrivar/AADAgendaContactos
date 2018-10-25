package com.example.pablo.aadagendacontactos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    public static final int REQUEST_CODE = 1;
    private List<Contacto> contactos;
    private ListView listView;
    boolean tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = findViewById(R.id.listView);

        setTipo();
        getContactos();
        listViewOnClick();
    }

    private void getContactos(){
        if(tipo){
            Memoria memoria = new Memoria(this);
            File root = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath() + "/contactos");
            File file = new File(dir, this.getResources().getString(R.string.externa));
            if(file == null || !file.exists()){
                leerContactos();
            }else{
                contactos = memoria.externalReading();
                ListAdapter adapter = new ListAdapter(MainActivity.this, contactos);
                listView.setAdapter(adapter);
            }
        }else{
            Memoria saver = new Memoria(this);
            File file = this.getFileStreamPath(getResources().getString(R.string.interna));
            if(file == null || !file.exists()){
                leerContactos();
            }else{
                contactos = saver.internalReading();
                ListAdapter adapter = new ListAdapter(MainActivity.this, contactos);
                listView.setAdapter(adapter);
            }
        }
    }

    public List<Contacto> getListaContactos(){
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ? and " +
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";
        String argumentos[] = new String[]{"1","1"};
        String orden = ContactsContract.Contacts.DISPLAY_NAME + " collate localized asc";
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int indiceNombre = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        List<Contacto> lista = new ArrayList<>();
        Contacto contacto;
        while(cursor.moveToNext()){
            contacto = new Contacto();
            contacto.setId(cursor.getLong(indiceId));
            contacto.setNombre(cursor.getString(indiceNombre));
            lista.add(contacto);
        }
        return lista;
    }

    public List<String> getListaTelefonos(long id){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String argumentos[] = new String[]{id+""};
        String orden = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceNumero = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        List<String> lista = new ArrayList<>();
        String numero;
        while(cursor.moveToNext()){
            numero = cursor.getString(indiceNumero);
            lista.add(numero);
        }
        return lista;
    }

    public void importarContactos(){
        List<Contacto> temporal = getListaContactos();
        for (Contacto nombre : temporal){
            List<String> telfs = getListaTelefonos(nombre.getId());
            nombre.setTelefono(telfs.get(0));
        }

        for(int i = 0; i<temporal.size(); i++){
            boolean existe = false;
            Contacto temp = temporal.get(i);
            String tempN = temp.getNombre();
            String tempT = temp.getTelefono();
            Long tempI = temp.getId();
            for(int j = 0; j<contactos.size(); j++){
                Contacto comp = contactos.get(j);
                String compN = comp.getNombre();
                String compT = comp.getTelefono();
                Long compI = comp.getId();
                if(tempN.equals(compN)&&tempT.equals(compT)&&tempI==compI){
                    existe = true;
                }
            }

            if(!existe){
                contactos.add(temp);
            }
        }
        ListAdapter adapter = new ListAdapter(MainActivity.this, contactos);
        listView.setAdapter(adapter);
    }

    private void leerContactos(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                Snackbar.make(getCurrentFocus(),R.string.permisos, Snackbar.LENGTH_LONG).show();
            } else {
                pedirPermisos();
            }
        }else{
            contactos = getListaContactos();
            for (Contacto nombre : contactos){
                List<String> telfs = getListaTelefonos(nombre.getId());
                nombre.setTelefono(telfs.get(0));
                ListAdapter adapter = new ListAdapter(MainActivity.this, contactos);
                listView.setAdapter(adapter);
            }
        }
    }

    public void listViewOnClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Contacto item = (Contacto) listView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this, EditarContacto.class);
                intent.putExtra("contacto", item);
                intent.putExtra("position", i);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
    }

    public void pedirPermisos(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    private void setTipo(){
        SharedPreferences pref = getSharedPreferences(getString(R.string.memoria),Context.MODE_PRIVATE);
        tipo = pref.getBoolean(getString(R.string.tipo),false);
    }

    private void storeTipo(boolean memoryStyle){
        SharedPreferences pref = this.getSharedPreferences(getString(R.string.tipo),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(getString(R.string.tipo), memoryStyle);
        editor.commit();
    }

    private void switchGuardado(){
        if(tipo){
            tipo = false;
            Memoria memoria = new Memoria(MainActivity.this, contactos);
            memoria.internalWriting();
        }else{
            tipo = true;
            Memoria memoria = new Memoria(MainActivity.this, contactos);
            memoria.externalWriting();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            importarContactos();
                            return false;
                        }
                    });
                return true;
            case R.id.item2:
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switchGuardado();
                            Memoria memoria = new Memoria(MainActivity.this, contactos);
                            storeTipo(tipo);
                            return false;
                        }
                    });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                pedirPermisos();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
