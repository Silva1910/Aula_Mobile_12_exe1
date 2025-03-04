package com.fatec.zl.amos.aula_mobile_12_exe1.controller;

import com.fatec.zl.amos.aula_mobile_12_exe1.model.Jogador;
import com.fatec.zl.amos.aula_mobile_12_exe1.persistence.JogadorDao;

import java.sql.SQLException;
import java.util.List;

public class JogadorController implements IController<Jogador> {
    private final JogadorDao jDao;

    public JogadorController(JogadorDao jDao) {
        this.jDao = jDao;
    }

    @Override
    public void inserir(Jogador jogador) throws SQLException {
        if (jDao.open() == null) {
            jDao.open();
        }
        jDao.inserir(jogador);
        jDao.close();
    }

    @Override
    public void modificar(Jogador jogador) throws SQLException {
        if (jDao.open() == null) {
            jDao.open();
        }
        jDao.atualizar(jogador);
        jDao.close();
    }

    @Override
    public void deletar(Jogador jogador) throws SQLException {
        if (jDao.open() == null) {
            jDao.open();
        }
        jDao.deletar(jogador);
        jDao.close();
    }

    @Override
    public Jogador buscar(Jogador jogador) throws SQLException {
        if (jDao.open() == null) {
            jDao.open();
        }
        Jogador result = jDao.consultar(jogador);
        jDao.close();
        return result;
    }

    @Override
    public List<Jogador> listar() throws SQLException {
        if (jDao.open() == null) {
            jDao.open();
        }
        List<Jogador> jogadores = jDao.listar();
        jDao.close();
        return jogadores;
    }
}
