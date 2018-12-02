package com.example.pablo.aadagendacontactos;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class Contacto implements Parcelable {

    private Long id;
    private String nombre, telefono;


    public Contacto(Long id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    public Contacto(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Contacto(Long id, String nombre, String telefono){
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Contacto(){}

    protected Contacto(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        nombre = in.readString();
        telefono = in.readString();
    }

    public static final Creator<Contacto> CREATOR = new Creator<Contacto>() {
        @Override
        public Contacto createFromParcel(Parcel in) {
            return new Contacto(in);
        }

        @Override
        public Contacto[] newArray(int size) {
            return new Contacto[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public Contacto setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getTelefono() {
        return telefono;
    }

    public Contacto setTelefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public Long getId(){return id;}

    public Contacto setId(Long id){
        this.id = id;
        return this;
    }

    public void deleteContacto(){
        this.id = null;
        this.nombre = null;
        this.telefono = null;
    }
    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(nombre);
        parcel.writeString(telefono);
    }
}
