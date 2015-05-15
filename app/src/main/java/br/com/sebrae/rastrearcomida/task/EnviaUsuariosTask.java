package br.com.sebrae.rastrearcomida.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import br.com.sebrae.rastrearcomida.modelo.Usuario;
import br.com.sebrae.rastrearcomida.util.UsuarioConverterJson;
import br.com.sebrae.rastrearcomida.util.WebClient;

/**
 * Created by Israel on 05/05/2015.
 */
public class EnviaUsuariosTask extends AsyncTask<Object, Object, String>{

    private Context ctx;
    private Usuario usuario;
    private ProgressDialog progress;

    public EnviaUsuariosTask(Context ctx, Usuario usuario){
        this.ctx = ctx;
        this.usuario = usuario;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(ctx, "Aguarde...", "Salvando dados");
    }

    @Override
    protected String doInBackground(Object... params) {
        String json = new UsuarioConverterJson().toJSON(usuario);
        String url = "http://matafome.herokuapp.com/usuarios/";
        String media = new WebClient(url).post(json);
        return media;
    }

    @Override
    protected void onPostExecute(String s) {
        progress.dismiss();
        Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
    }
}
