package com.fatec.zl.amos.aula_mobile_12_exe1.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.fatec.zl.amos.aula_mobile_12_exe1.model.Jogador;
import com.fatec.zl.amos.aula_mobile_12_exe1.model.Time;

import java.util.ArrayList;
import java.util.List;

public class JogadorDao implements ICRUDDao<Jogador> {

    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase database;

    public JogadorDao(Context context) {
        this.context = context;
    }

    public JogadorDao open() throws SQLException {
        gDao = new GenericDao(context);
        database = gDao.getWritableDatabase();
        return this;
    }

    public void close() {
        gDao.close();
    }

    @Override
    public void inserir(Jogador jogador) throws SQLException {
        ContentValues contentValues = getContentValues(jogador);
        database.insert("Jogador", null, contentValues);
    }

    @Override
    public int atualizar(Jogador jogador) throws SQLException {
        ContentValues contentValues = getContentValues(jogador);
        return database.update("Jogador", contentValues, "id = ?", new String[]{String.valueOf(jogador.getId())});
    }

    @Override
    public void deletar(Jogador jogador) throws SQLException {
        database.delete("Jogador", "id = ?", new String[]{String.valueOf(jogador.getId())});
    }

    @SuppressLint("Range")
    @Override
    public Jogador consultar(Jogador jogador) throws SQLException {
        String sql = "SELECT id, nome, dataNasc, altura, peso, codigoTime FROM Jogador WHERE id = " + jogador.getId();
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) {
            jogador.setId(cursor.getInt(cursor.getColumnIndex("id")));
            jogador.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            jogador.setDataNasc(cursor.getString(cursor.getColumnIndex("dataNasc")));
            jogador.setAltura(cursor.getFloat(cursor.getColumnIndex("altura")));
            jogador.setPeso(cursor.getFloat(cursor.getColumnIndex("peso")));

            // Carregar Time
            int codigoTime = cursor.getInt(cursor.getColumnIndex("codigoTime"));
            Time time = new Time();
            time.setCodigo(codigoTime);
            // Assumindo que há um método em TimeDao para buscar por ID
            TimeDao timeDao = new TimeDao(context);
            timeDao.open();
            time = timeDao.consultar(time);
            timeDao.close();
            jogador.setTime(time);
        }

        if (cursor != null) {
            cursor.close();
        }
        return jogador;
    }

    @SuppressLint("Range")
    @Override
    public List<Jogador> listar() throws SQLException {
        List<Jogador> jogadores = new ArrayList<>();
        String sql = "SELECT id,nome,dataNasc,peso,codigoTime,altura FROM Jogador";
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Jogador jogador = new Jogador();
                jogador.setId(cursor.getInt(cursor.getColumnIndex("id")));
                jogador.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                jogador.setDataNasc(cursor.getString(cursor.getColumnIndex("dataNasc")));
                jogador.setAltura(cursor.getFloat(cursor.getColumnIndex("altura")));
                jogador.setPeso(cursor.getFloat(cursor.getColumnIndex("peso")));

                // Carregar Time
                int codigoTime = cursor.getInt(cursor.getColumnIndex("codigoTime"));
                Time time = new Time();
                time.setCodigo(codigoTime);
                // Assumindo que há um método em TimeDao para buscar por ID
                TimeDao timeDao = new TimeDao(context);
                timeDao.open();
                time = timeDao.consultar(time);
                timeDao.close();
                jogador.setTime(time);

                jogadores.add(jogador);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return jogadores;
    }

    private static ContentValues getContentValues(Jogador jogador) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", jogador.getId());
        contentValues.put("nome", jogador.getNome());
        contentValues.put("dataNasc", jogador.getDataNasc());
        contentValues.put("altura", jogador.getAltura());
        contentValues.put("peso", jogador.getPeso());
        contentValues.put("codigoTime", jogador.getTime().getCodigo());
        return contentValues;
    }
}
