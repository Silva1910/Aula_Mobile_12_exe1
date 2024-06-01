package com.fatec.zl.amos.aula_mobile_12_exe1.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.fatec.zl.amos.aula_mobile_12_exe1.model.Time;

import java.util.ArrayList;
import java.util.List;

public class TimeDao implements ICRUDDao<Time> {

    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase database;

    public TimeDao(Context context) {
        this.context = context;
    }

    public TimeDao open() throws SQLException {
        gDao = new GenericDao(context);
        database = gDao.getWritableDatabase();
        return this;
    }

    public void close() {
        gDao.close();
    }

    @Override
    public void inserir(Time time) throws SQLException {
        ContentValues contentValues = getContentValues(time);
        database.insert("Time", null, contentValues);
    }

    @Override
    public int atualizar(Time time) throws SQLException {
        ContentValues contentValues = getContentValues(time);
        return database.update("Time", contentValues, "codigoTime = ?", new String[]{String.valueOf(time.getCodigo())});
    }

    @Override
    public void deletar(Time time) throws SQLException {
        database.delete("Time", "codigoTime = ?", new String[]{String.valueOf(time.getCodigo())});
    }

    @SuppressLint("Range")
    @Override
    public Time consultar(Time time) throws SQLException {
        String sql = "SELECT codigoTime, nome, cidade FROM Time WHERE codigoTime = " + time.getCodigo();
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) {
            time.setCodigo(cursor.getInt(cursor.getColumnIndex("codigoTime")));
            time.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            time.setCidade(cursor.getString(cursor.getColumnIndex("cidade")));
        }

        if (cursor != null) {
            cursor.close();
        }
        return time;
    }

    @SuppressLint("Range")
    @Override
    public List<Time> listar() throws SQLException {
        List<Time> times = new ArrayList<>();
        String sql = "SELECT codigoTime, nome, cidade FROM Time";
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Time time = new Time();
                time.setCodigo(cursor.getInt(cursor.getColumnIndex("codigoTime")));
                time.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                time.setCidade(cursor.getString(cursor.getColumnIndex("cidade")));
                times.add(time);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return times;
    }

    private static ContentValues getContentValues(Time time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("codigoTime", time.getCodigo());
        contentValues.put("nome", time.getNome());
        contentValues.put("cidade", time.getCidade());
        return contentValues;
    }
}
