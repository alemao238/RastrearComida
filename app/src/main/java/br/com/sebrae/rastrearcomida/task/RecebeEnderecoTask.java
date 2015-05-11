package br.com.sebrae.rastrearcomida.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.com.sebrae.rastrearcomida.modelo.Endereco;

/**
 * Created by Israel on 06/05/2015.
 */
public class RecebeEnderecoTask extends AsyncTask<String, Void, List<Endereco>>{
    ProgressDialog dialog;
    private Context context;

    public RecebeEnderecoTask(Context context){
        this.context = context;
    }

    //Exibe pop-up indicando que está sendo feito o download do JSON
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "Aguarde",
                "Buscando endereço...");
    }

    @Override
    protected List<Endereco> doInBackground(String... params) {
        String urlString = params[0];
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(urlString);
        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String json = getStringFromInputStream(instream);
                instream.close();
                List<Endereco> enderecos = getEndereco(json);
                return enderecos;
            }
        } catch (Exception e) {
            Log.e("Erro", "Falha ao acessar Web service", e);
        }
        return null;
    }

    //Depois de executada a chamada do serviço
    @Override
    protected void onPostExecute(List<Endereco> result) {
        super.onPostExecute(result);
        dialog.dismiss();
        //Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
    }

    private List<Endereco> getEndereco(String jsonString){
        List<Endereco> enderecos = new ArrayList<Endereco>();
        try {
            //JSONArray pessoasJson = new JSONArray(jsonString);
            JSONObject pessoa = new JSONObject(jsonString);
            Endereco obj = new Endereco();
            obj.setLogradouro(pessoa.getString("logradouro"));
            obj.setBairro(pessoa.getString("bairro"));
            obj.setCidade(pessoa.getString("localidade"));
            obj.setEstado(pessoa.getString("uf"));
            enderecos.add(obj);

            /*for(int i = 0; i < pessoasJson.length(); i++){
                pessoa = new JSONObject(pessoasJson.getString(i));
                Endereco objeto = new Endereco();
                objeto.setLogradouro(pessoa.getString("logradouro"));
                objeto.setBairro(pessoa.getString("bairro"));
                objeto.setCidade(pessoa.getString("localidade"));
                objeto.setEstado(pessoa.getString("uf"));
                enderecos.add(objeto);
            }*/
        } catch (Exception e) {
            Log.e("Erro", "Erro no parsing do JSON", e);
        }
        return enderecos;
    }

    //Converte objeto InputStream para String
    private String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
