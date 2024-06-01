package com.fatec.zl.amos.aula_mobile_12_exe1;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fatec.zl.amos.aula_mobile_12_exe1.controller.JogadorController;
import com.fatec.zl.amos.aula_mobile_12_exe1.controller.TimeController;
import com.fatec.zl.amos.aula_mobile_12_exe1.model.Jogador;
import com.fatec.zl.amos.aula_mobile_12_exe1.model.Time;
import com.fatec.zl.amos.aula_mobile_12_exe1.persistence.JogadorDao;
import com.fatec.zl.amos.aula_mobile_12_exe1.persistence.TimeDao;

import java.sql.SQLException;
import java.util.List;

public class JogadorFragment extends Fragment {

    private View view;
    private EditText etIDJogador, etNomeJogador, etPesoJogador, etAlturaJogador, etDataJogador;
    private Spinner spinnerTimes;
    private TextView tvResJogador;
    private Button btnCadastrarJogador, btnAtualizarJogador, btnExcluirJogador, btnListarJogador, btnConsultarJogador;

    private JogadorController jcont;
    private TimeController tcont;

    public JogadorFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_jogador, container, false);

        // Initialize EditText fields
        etIDJogador = view.findViewById(R.id.etIDJogador);
        etNomeJogador = view.findViewById(R.id.etNomeJogador);
        etPesoJogador = view.findViewById(R.id.etPesoJogador);
        etAlturaJogador = view.findViewById(R.id.etAlturaJogador);
        etDataJogador = view.findViewById(R.id.etDataJogador);

        // Initialize Spinner
        spinnerTimes = view.findViewById(R.id.spinner);

        // Initialize Buttons
        btnCadastrarJogador = view.findViewById(R.id.btnCadastrarJ);
        btnAtualizarJogador = view.findViewById(R.id.btnAtualizarJ);
        btnExcluirJogador = view.findViewById(R.id.btnExcluirJ);
        btnListarJogador = view.findViewById(R.id.btnListarJ);
        btnConsultarJogador = view.findViewById(R.id.btnConsultarJ);

        // Initialize TextView
        tvResJogador = view.findViewById(R.id.tvResJogador);
        tvResJogador.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvResJogador.setMovementMethod(new ScrollingMovementMethod());

        // Initialize Controllers
        jcont = new JogadorController(new JogadorDao(view.getContext()));
        tcont = new TimeController(new TimeDao(view.getContext()));

        // Set button listeners
        btnCadastrarJogador.setOnClickListener(op -> acaoCadastrar());
        btnAtualizarJogador.setOnClickListener(op -> acaoAtualizar());
        btnListarJogador.setOnClickListener(op -> acaoListar());
        btnConsultarJogador.setOnClickListener(op -> acaoConsultar());
        btnExcluirJogador.setOnClickListener(op -> acaoExcluir());

        // Fill Spinner with Teams
        preencherSpinner();

        return view;
    }

    private void preencherSpinner() {
        Time t0 = new Time();
        t0.setCodigo(0);
        t0.setNome("Selecione um time");
        t0.setCidade("");

        try {
            List<Time> times = tcont.listar();
           // times.add(0, t0);
            ArrayAdapter<Time> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, times);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTimes.setAdapter(adapter);
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoListar() {
        try {
            List<Jogador> jogadores = jcont.listar();
            StringBuilder buffer = new StringBuilder();
            for (Jogador j : jogadores) {
                buffer.append(j.toString()).append("\n");
            }
            tvResJogador.setText(buffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoExcluir() {
        Jogador jogador = montaJogador();
        try {
            jcont.deletar(jogador);
            Toast.makeText(view.getContext(), "JOGADOR EXCLUÍDO COM SUCESSO", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void acaoConsultar() {
        Jogador jogador = montaJogador();
        try {
            jogador = jcont.buscar(jogador);
            if (jogador.getNome() != null) {
                preencherJogador(jogador);
            } else {
                Toast.makeText(view.getContext(), "JOGADOR NÃO ENCONTRADO", Toast.LENGTH_LONG).show();
                limparCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoAtualizar() {
        Jogador jogador = montaJogador();
        try {
            jcont.modificar(jogador);
            Toast.makeText(view.getContext(), "JOGADOR ATUALIZADO COM SUCESSO", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void acaoCadastrar() {
        Jogador jogador = montaJogador();
        try {
            jcont.inserir(jogador);
            Toast.makeText(view.getContext(), "JOGADOR CADASTRADO COM SUCESSO", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void limparCampos() {
        etIDJogador.setText("");
        etNomeJogador.setText("");
        etPesoJogador.setText("");
        etAlturaJogador.setText("");
        etDataJogador.setText("");
        spinnerTimes.setSelection(0);
    }

    private Jogador montaJogador() {
        Jogador j = new Jogador();

        if (!etIDJogador.getText().toString().isEmpty()) {
            j.setId(Integer.parseInt(etIDJogador.getText().toString()));
        }
        if (!etNomeJogador.getText().toString().isEmpty()) {
            j.setNome(etNomeJogador.getText().toString());
        }
        if (!etPesoJogador.getText().toString().isEmpty()) {
            j.setPeso(Float.parseFloat(etPesoJogador.getText().toString()));
        }
        if (!etAlturaJogador.getText().toString().isEmpty()) {
            j.setAltura(Float.parseFloat(etAlturaJogador.getText().toString()));
        }
        if (!etDataJogador.getText().toString().isEmpty()) {
            j.setDataNasc(etDataJogador.getText().toString());
        }
        if (spinnerTimes.getSelectedItem() != null) {
            Time time = (Time) spinnerTimes.getSelectedItem();
            j.setTime(time);
        }

        return j;
    }

    private void preencherJogador(Jogador j) {
        etIDJogador.setText(String.valueOf(j.getId()));
        etNomeJogador.setText(j.getNome());
        etPesoJogador.setText(String.valueOf(j.getPeso()));
        etAlturaJogador.setText(String.valueOf(j.getAltura()));
        etDataJogador.setText(j.getDataNasc());
        int spinnerPosition = ((ArrayAdapter<Time>) spinnerTimes.getAdapter()).getPosition(j.getTime());
        spinnerTimes.setSelection(spinnerPosition);
    }
}
