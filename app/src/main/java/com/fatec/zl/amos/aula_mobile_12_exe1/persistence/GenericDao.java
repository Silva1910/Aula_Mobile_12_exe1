package com.fatec.zl.amos.aula_mobile_12_exe1.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GenericDao extends SQLiteOpenHelper {

    private static final String DATABASE = "SPORTS.DB";
    private static final int DATABASE_VER = 1;

    private static final String CREATE_TABLE_TIME =
            "CREATE TABLE Time ( " +
                    "codigoTime INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "nome VARCHAR(50) NOT NULL, " +
                    "cidade VARCHAR(80) NOT NULL );";

    private static final String CREATE_TABLE_JOGADOR =
            "CREATE TABLE Jogador ( " +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "dataNasc VARCHAR(100) NOT NULL, " +
                    "altura REAL NOT NULL, " +
                    "peso REAL NOT NULL, " +
                    "codigoTime INTEGER NOT NULL, " +
                    "FOREIGN KEY (codigoTime) REFERENCES Time(codigoTime));";

    public GenericDao(Context context) {
        super(context, DATABASE, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TIME);
        sqLiteDatabase.execSQL(CREATE_TABLE_JOGADOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Jogador");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Time");
            onCreate(sqLiteDatabase);
        }
    }
}
