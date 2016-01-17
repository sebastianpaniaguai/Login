package com.example.sebas.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebas on 1/15/2016.
 */
public class kidDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String KID_DATABASE_NAME = "kids.db";
    private static final String KID_TABLE_NAME = "kids";
    private static final String KID_COLUMN_ID = "id";
    private static final String KID_COLUMN_NAME = "name";
    private static final String KID_COLUMN_SEX = "sex";
    private static final String KID_COLUMN_FECHA = "date";
    private static final String KID_COLUMN_MINUTOS = "min";
    private static final String KID_COLUMN_HORA = "hour";
    private static final String KID_COLUMN_LENGUAJE = "language";

    SQLiteDatabase db;


    private static final String TABLE_CREATE = "create table kids (id integer primary key not null , "+
            "name text not null , sex text not null , date integer , min integer , hour integer , language text not null);";

    public kidDatabaseHelper(Context context){
        super(context, KID_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db=db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query= "DROP TABLE IF EXISTS "+KID_TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

    public void insertChild (Child c){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from kids";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();

        values.put(KID_COLUMN_ID,count);
        values.put(KID_COLUMN_NAME,c.getName());
        values.put(KID_COLUMN_SEX,c.getSexo());
        values.put(KID_COLUMN_FECHA,c.getFecha());
        values.put(KID_COLUMN_HORA, c.getHora());
        values.put(KID_COLUMN_MINUTOS, c.getMinutos());
        values.put(KID_COLUMN_LENGUAJE, c.getLenguaje());

        db.insert(KID_TABLE_NAME, null, values);
        db.close();
    }
    public String searchPass (String uname){
        db = getReadableDatabase();
        String query = "select username , pass from "+KID_TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String a,b;
        b="not found";
        if(cursor.moveToFirst()){
            do{
                a=cursor.getString(0);
                if(a.equals(uname)){
                    b=cursor.getString(1);
                    break;
                }
            }while(cursor.moveToNext());
        }
        return b;
    }

    public int getContactsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + KID_TABLE_NAME, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    public List<Child> getAllChilds() {
        List<Child> Childs = new ArrayList<Child>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + KID_TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                if((cursor.getString(2)).equals("Boy")){
                    Childs.add(new Child(R.drawable.cara_nino, cursor.getString(6),
                            cursor.getString(1),
                            cursor.getInt(3),cursor.getInt(4),cursor.getInt(5)));
                }
                else{
                    Childs.add(new Child(R.drawable.cara_nina, cursor.getString(6),
                            cursor.getString(1),
                            cursor.getInt(3),cursor.getInt(4),cursor.getInt(5)));
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return Childs;
    }


}
