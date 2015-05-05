package br.com.sebrae.rastrearcomida.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import br.com.sebrae.rastrearcomida.R;

/**
 * Created by Israel on 04/05/2015.
 */
public class TelaInicial extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_inicial);

        // Chamar a tela para buscar endereço da localização do usuário
        TextView buscaEndereco = (TextView) findViewById(R.id.buscarEndereco);
        buscaEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaInicial.this, BuscarEndereco.class);
                startActivity(intent);
            }
        });

        // Chamar a tela de login
        TextView login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaInicial.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
