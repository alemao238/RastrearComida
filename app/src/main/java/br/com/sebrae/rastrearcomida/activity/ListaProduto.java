package br.com.sebrae.rastrearcomida.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
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

import br.com.sebrae.rastrearcomida.R;
import br.com.sebrae.rastrearcomida.adapter.ListaEmpresaAdapter;
import br.com.sebrae.rastrearcomida.adapter.ListaProdutoAdapter;
import br.com.sebrae.rastrearcomida.modelo.Empresa;
import br.com.sebrae.rastrearcomida.modelo.Produto;

/**
 * Created by Israel on 07/05/2015.
 */
public class ListaProduto extends Activity{
    private ListaProdutoAdapter listaProdutoAdapter;
    private ListView lista;
    private Empresa empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_produto);

        empresa = (Empresa) getIntent().getSerializableExtra("empresa");

        TextView nomeEmpresa = (TextView) findViewById(R.id.nomeEmpresa);
        nomeEmpresa.setText(empresa.getNome());

        Toast.makeText(this, "Produtos: "+empresa.getProdutos().toString(), Toast.LENGTH_LONG).show();
    }

    class JsonDownload extends AsyncTask<String, Void, List<Produto>> {
        ProgressDialog dialog;

        //Exibe pop-up indicando que está sendo feito o download do JSON
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ListaProduto.this, "Aguarde",
                    "Buscando empresas...");
        }

        @Override
        protected List<Produto> doInBackground(String... params) {
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
                    List<Produto> produtos = getProduto(json);
                    return produtos;
                }
            } catch (Exception e) {
                Log.e("Erro", "Falha ao acessar Web service", e);
            }
            return null;
        }

        //Depois de executada a chamada do serviço
        @Override
        protected void onPostExecute(List<Produto> result) {
            super.onPostExecute(result);
            dialog.dismiss();

            listaProdutoAdapter = new ListaProdutoAdapter(ListaProduto.this, result);
            lista.setAdapter(listaProdutoAdapter);
        }

        private List<Produto> getProduto(String jsonString){
            List<Produto> produtos = new ArrayList<Produto>();
            try {
                JSONArray pessoasJson = new JSONArray(jsonString);
                JSONObject pessoa;
                for(int i = 0; i < pessoasJson.length(); i++){
                    pessoa = new JSONObject(pessoasJson.getString(i));

                    Produto obj = new Produto();
                    obj.setId(Long.parseLong(pessoa.getString("id")));
                    obj.setDescricao(pessoa.getString("descricao"));
                    obj.setValor_unitario(Double.parseDouble(pessoa.getString("valor_unitario")));
                    produtos.add(obj);
                }
            } catch (Exception e) {
                Log.e("Erro", "Erro no parsing do JSON "+e.getMessage(), e);
            }
            return produtos;
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
}
