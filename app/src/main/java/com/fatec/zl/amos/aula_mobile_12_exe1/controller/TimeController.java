package com.fatec.zl.amos.aula_mobile_12_exe1.controller;

import com.fatec.zl.amos.aula_mobile_12_exe1.model.Time;
import com.fatec.zl.amos.aula_mobile_12_exe1.persistence.TimeDao;

import java.sql.SQLException;
import java.util.List;

public class TimeController implements IController<Time> {
    private final TimeDao tDao;

    public TimeController(TimeDao tDao) {
        this.tDao = tDao;
    }

    @Override
    public void inserir(Time time) throws SQLException {
        if (tDao.open() == null) {
            tDao.open();
        }
        tDao.inserir(time);
        tDao.close();
    }

    @Override
    public void modificar(Time time) throws SQLException {
        if (tDao.open() == null) {
            tDao.open();
        }
        tDao.atualizar(time);
        tDao.close();
    }

    @Override
    public void deletar(Time time) throws SQLException {
        if (tDao.open() == null) {
            tDao.open();
        }
        tDao.deletar(time);
        tDao.close();
    }

    @Override
    public Time buscar(Time time) throws SQLException {
        if (tDao.open() == null) {
            tDao.open();
        }
        Time result = tDao.consultar(time);
        tDao.close();
        return result;
    }

    @Override
    public List<Time> listar() throws SQLException {
        if (tDao.open() == null) {
            tDao.open();
        }
        List<Time> times = tDao.listar();
        tDao.close();
        return times;
    }
}
