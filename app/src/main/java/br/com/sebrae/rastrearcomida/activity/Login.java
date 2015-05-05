package br.com.sebrae.rastrearcomida.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.sebrae.rastrearcomida.R;

/**
 * Created by Israel on 04/05/2015.
 */
public class Login extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button btnCriarLogin = (Button) findViewById(R.id.btnCriarLogin);
        btnCriarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, CadastroUsuarioCliente.class);
                startActivity(intent);
            }
        });
    }
}
