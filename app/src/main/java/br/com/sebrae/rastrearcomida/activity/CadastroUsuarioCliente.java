package br.com.sebrae.rastrearcomida.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import br.com.sebrae.rastrearcomida.R;
import br.com.sebrae.rastrearcomida.modelo.Usuario;
import br.com.sebrae.rastrearcomida.task.EnviaUsuariosTask;

/**
 * Created by Israel on 04/05/2015.
 */
public class CadastroUsuarioCliente extends Activity {
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_usuario_cliente);

        Button btnAdicionar = (Button) findViewById(R.id.btnCadastrar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EnqueteRest rest = new EnqueteRest();
                //rest.enviar();

                Usuario usuario = new Usuario();
                EditText txtNome = (EditText) findViewById(R.id.nome);
                EditText txtEmail = (EditText) findViewById(R.id.email);
                EditText txtSenha = (EditText) findViewById(R.id.senha);
                usuario.setNome(txtNome.getText().toString());
                usuario.setEmail(txtEmail.getText().toString());
                usuario.setSenha(txtSenha.getText().toString());

                new EnviaUsuariosTask(CadastroUsuarioCliente.this, usuario).execute();
            }
        });

    }

    class EnqueteRest{
        public void enviar(){
            showDialog("Cadastrando usuário...");

            //String url = ChicoUtils.PATH + "/votos/salvar";
            //UserPreferences user = new UserPreferences(getActivity());

            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams p = new RequestParams();

            //p.put("nome", "Teste1");
            //p.put("email", "teste@teste.com");
            //p.put("senha", "12345678");
            //p.put("tipo", 1);

            p.put("nome", "Jhonny gay");

            client.post("http://irish-drunk-204978.sae1.nitrousbox.com/tipos_restaurantes/new/", p, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] arg1, byte[] arg2) {
                    if(statusCode == 200){
                        Toast.makeText(CadastroUsuarioCliente.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(CadastroUsuarioCliente.this, "Erro: " + statusCode, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    Toast.makeText(CadastroUsuarioCliente.this, "Por favor, verifique a sua conexão com a Internet.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    closeDialog();
                }
            });
        }
    }

    public void showDialog(String msg){
        dialog = ProgressDialog.show(CadastroUsuarioCliente.this, null, msg);
    }

    public void closeDialog(){
        dialog.dismiss();
    }
}
