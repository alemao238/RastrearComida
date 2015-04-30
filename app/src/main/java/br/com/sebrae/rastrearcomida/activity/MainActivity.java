package br.com.sebrae.rastrearcomida.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import br.com.sebrae.rastrearcomida.R;
import br.com.sebrae.rastrearcomida.adapter.ListaEmpresaAdapter;


public class MainActivity extends Activity {
    private ListView lista;
    private ListaEmpresaAdapter empresaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = (ListView) findViewById(R.id.listaFood);

        EditText campoPesquisa = (EditText) findViewById(R.id.campoPesquisa);
        campoPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                empresaAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
