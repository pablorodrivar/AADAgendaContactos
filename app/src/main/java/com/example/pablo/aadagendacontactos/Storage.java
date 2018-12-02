package com.example.pablo.aadagendacontactos;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    private Context context;
    private String contactList;

    public Storage(){}

    public Storage(Context context){
        this.context = context;
    }

    public Storage(Context context, List<Contacto> nombres){
        this.context = context;
        this.contactList = listaToCsv(nombres);
    }

    public Context getActivityC() {
        return context;
    }

    public void setActivityC(Context context) {
        this.context = context;
    }

    public void internalWriting(){
        String file = context.getResources().getString(R.string.interna);
        String fileContents = contactList;
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(file, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void externalWriting(){
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Android/data");
        dir.mkdir();
        File file = new File(dir, context.getResources().getString(R.string.externa));
        String fileContents = contactList;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);
            pw.println(contactList);
            pw.flush();
            pw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Contacto> internalReading(){
        List<Contacto> nombres = new ArrayList<Contacto>();
        try {
            FileInputStream in = context.openFileInput(context.getResources().getString(R.string.interna));
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null){
                Contacto temp = new Contacto();
                line = line.replace("'", "");
                String[] array = line.split(";");
                temp.setId(Long.parseLong(array[0]));
                temp.setNombre(array[1]);
                temp.setTelefono(array[2]);
                nombres.add(temp);
            }
            br.close();
            isr.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nombres;
    }

    public List<Contacto> externalReading(){
        List<Contacto> nombres = new ArrayList<Contacto>();
        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/Android/data");
        File file = new File(dir, context.getResources().getString(R.string.externa));
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null){
                if(!line.equals("")) {
                    Contacto temp = new Contacto();
                    line = line.replace("'", "");
                    String[] array = line.split(";");
                    temp.setId(Long.parseLong(array[0]));
                    temp.setNombre(array[1]);
                    temp.setTelefono(array[2]);
                    nombres.add(temp);
                }
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nombres;
    }


    public String listaToCsv(List<Contacto> nombres){
        String contactos = "";
        for (Contacto nombre : nombres){
            contactos += "'"+nombre.getId()+"';'"+nombre.getNombre()+"';'"+nombre.getTelefono()+"';\n";
        }
        return contactos;
    }

}
