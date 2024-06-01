package com.fatec.zl.amos.aula_mobile_12_exe1;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fatec.zl.amos.aula_mobile_12_exe1.controller.TimeController;
import com.fatec.zl.amos.aula_mobile_12_exe1.model.Time;
import com.fatec.zl.amos.aula_mobile_12_exe1.persistence.TimeDao;

import java.sql.SQLException;
import java.util.List;

public class TimeFragment extends Fragment {

    private View view;
    private EditText etCodigoT, etNomeT, etCidadeT;
    private TextView tvResTime;
    private Button btnCadastrarT, btnAtualizarT, btnExcluirT, btnListarT, btnConsultarT;

    private TimeController tcont;

    public TimeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_time, container, false);

        // Initialize EditText fields
        etCodigoT = view.findViewById(R.id.etCodigoT);
        etNomeT = view.findViewById(R.id.etNomeT);
        etCidadeT = view.findViewById(R.id.etCidadeT);

        // Initialize Buttons
        btnCadastrarT = view.findViewById(R.id.btnCadastrarT);
        btnAtualizarT = view.findViewById(R.id.btnAtualizarT);
        btnExcluirT = view.findViewById(R.id.btnExcluirT);
        btnListarT = view.findViewById(R.id.btnListarT);
        btnConsultarT = view.findViewById(R.id.btnConsultarT);

        // Initialize TextView
        tvResTime = view.findViewById(R.id.tvResTIme);
        tvResTime.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvResTime.setMovementMethod(new ScrollingMovementMethod());

        // Initialize Controller
        tcont = new TimeController(new TimeDao(view.getContext()));

        // Set button listeners
        btnCadastrarT.setOnClickListener(op -> acaoCadastrar());
        btnAtualizarT.setOnClickListener(op -> acaoAtualizar());
        btnListarT.setOnClickListener(op -> acaoListar());
        btnConsultarT.setOnClickListener(op -> acaoConsultar());
        btnExcluirT.setOnClickListener(op -> acaoExcluir());

        return view;
    }

    private void acaoListar() {
        try {
            List<Time> times = tcont.listar();
            StringBuilder buffer = new StringBuilder();
            for (Time t : times) {
                buffer.append(t.toString()).append("\n");
            }
            tvResTime.setText(buffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoExcluir() {
        Time time = montaTime();
        try {
            tcont.deletar(time);
            Toast.makeText(view.getContext(), "TIME EXCLUÍDO COM SUCESSO", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void acaoConsultar() {
        Time time = montaTime();
        try {
            time = tcont.buscar(time);
            if (time.getNome() != null) {
                preencherTime(time);
            } else {
                Toast.makeText(view.getContext(), "TIME NÃO ENCONTRADO", Toast.LENGTH_LONG).show();
                limparCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoAtualizar() {
        Time time = montaTime();
        try {
            tcont.modificar(time);
            Toast.makeText(view.getContext(), "TIME ATUALIZADO COM SUCESSO", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void acaoCadastrar() {
        Time time = montaTime();
        try {
            tcont.inserir(time);
            Toast.makeText(view.getContext(), "TIME CADASTRADO COM SUCESSO", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void limparCampos() {
        etCodigoT.setText("");
        etNomeT.setText("");
        etCidadeT.setText("");
    }

    private Time montaTime() {
        Time t = new Time();

        if (!etCodigoT.getText().toString().isEmpty()) {
            t.setCodigo(Integer.parseInt(etCodigoT.getText().toString()));
        }
        if (!etNomeT.getText().toString().isEmpty()) {
            t.setNome(etNomeT.getText().toString());
        }
        if (!etCidadeT.getText().toString().isEmpty()) {
            t.setCidade(etCidadeT.getText().toString());
        }

        return t;
    }

    private void preencherTime(Time t) {
        etCodigoT.setText(String.valueOf(t.getCodigo()));
        etNomeT.setText(t.getNome());
        etCidadeT.setText(t.getCidade());
    }
}
