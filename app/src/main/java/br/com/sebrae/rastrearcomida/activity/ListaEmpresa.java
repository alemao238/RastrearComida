package br.com.sebrae.rastrearcomida.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import br.com.sebrae.rastrearcomida.modelo.Empresa;
import br.com.sebrae.rastrearcomida.modelo.Endereco;
import br.com.sebrae.rastrearcomida.modelo.Produto;

/**
 * Created by Israel on 06/05/2015.
 */
public class ListaEmpresa extends Activity{
    private ListaEmpresaAdapter listaEmpresaAdapter;
    private ListView lista;
    private Endereco endereco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_empresa);

        lista = (ListView) findViewById(R.id.listaEmpresa);

        endereco = (Endereco) getIntent().getSerializableExtra("endereco");
        new JsonDownload().execute("http://matafome.herokuapp.com/empresas.json");

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Empresa empresa = (Empresa) parent.getAdapter().getItem(position);

                Intent intent = new Intent(ListaEmpresa.this, ListaProduto.class);
                intent.putExtra("empresa", empresa);
                startActivity(intent);
            }
        });

    }

    class JsonDownload extends AsyncTask<String, Void, List<Empresa>> {
        ProgressDialog dialog;

        //Exibe pop-up indicando que está sendo feito o download do JSON
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(ListaEmpresa.this, "Aguarde",
                    "Buscando empresas...");
        }

        @Override
        protected List<Empresa> doInBackground(String... params) {
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
                    List<Empresa> empresas = getEmpresa(json);
                    return empresas;
                }
            } catch (Exception e) {
                Log.e("Erro", "Falha ao acessar Web service", e);
            }
            return null;
        }

        //Depois de executada a chamada do serviço
        @Override
        protected void onPostExecute(List<Empresa> result) {
            super.onPostExecute(result);
            dialog.dismiss();

            listaEmpresaAdapter = new ListaEmpresaAdapter(ListaEmpresa.this, result);
            lista.setAdapter(listaEmpresaAdapter);
        }

        private List<Empresa> getEmpresa(String jsonString){
            List<Empresa> empresas = new ArrayList<Empresa>();
            try {
                JSONArray pessoasJson = new JSONArray(jsonString);
                JSONObject pessoa;
                for(int i = 0; i < pessoasJson.length(); i++){
                    pessoa = new JSONObject(pessoasJson.getString(i));

                    Empresa obj = new Empresa();
                    obj.setId(Long.parseLong(pessoa.getString("id")));
                    obj.setNome(pessoa.getString("nome"));
                    obj.setDescricao(pessoa.getString("descricao"));
                    obj.setCnpj(pessoa.getString("cnpj"));

                    JSONArray produtosJson = new JSONArray(pessoa.getString("produtos"));
                    JSONObject produto;
                    for (int j = 0; j < produtosJson.length(); j++){
                        produto = new JSONObject(produtosJson.getString(j));

                        Produto p = new Produto();
                        p.setId(Long.parseLong(produto.getString("id")));
                        p.setDescricao(produto.getString("descricao"));
                        p.setValor_unitario(Double.parseDouble(produto.getString("valor_unitario")));

                        if(obj.getProdutos() == null)
                            obj.setProdutos(new ArrayList<Produto>());
                        obj.getProdutos().add(p);
                    }
                    empresas.add(obj);
                }
            } catch (Exception e) {
                Log.e("Erro", "Erro no parsing do JSON "+e.getMessage(), e);
            }
            return empresas;
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
