package br.com.sebrae.rastrearcomida.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.sebrae.rastrearcomida.R;
import br.com.sebrae.rastrearcomida.modelo.Empresa;

/**
 * Created by Israel on 07/05/2015.
 */
public class ListaProduto extends Activity{
    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_produto);

        empresa = (Empresa) getIntent().getSerializableExtra("empresa");

        TextView nomeEmpresa = (TextView) findViewById(R.id.nomeEmpresa);
        nomeEmpresa.setText(empresa.getNome());
    }
}
