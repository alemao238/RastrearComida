package br.com.sebrae.rastrearcomida.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.sebrae.rastrearcomida.R;
import br.com.sebrae.rastrearcomida.modelo.Empresa;

/**
 * Created by dmapc on 30/04/2015.
 */
public class ListaEmpresaAdapter extends ArrayAdapter<Empresa>{
    //Itens de exibição/filtrados
    private List<Empresa> itens_exibicao;
    private Context context;
    //Essa lista contém todos os itens
    private List<Empresa> empresas;

    public ListaEmpresaAdapter(Context context, List<Empresa> empresas) {
        super(context, 0, empresas);
        this.context = context;
        this.empresas = empresas;
        this.itens_exibicao = empresas;
    }

    @Override
    public int getCount() {
        return itens_exibicao.size();
    }

    @Override
    public Empresa getItem(int position) {
        return itens_exibicao.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itens_exibicao.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Empresa empresa = itens_exibicao.get(position);
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.layout_lista_empresa, null);

        TextView textViewNome = (TextView) view.findViewById(R.id.nome);
        textViewNome.setText(empresa.getNome());

        TextView textViewDescricao = (TextView) view.findViewById(R.id.descricao);
        textViewDescricao.setText(empresa.getDescricao());

        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                //se não foi realizado nenhum filtro insere todos os itens.
                if (filtro == null || filtro.length() == 0) {
                    results.count = empresas.size();
                    results.values = empresas;
                } else {
                    //cria um array para armazenar os objetos filtrados.
                    List<Empresa> itens_filtrados = new ArrayList<Empresa>();

                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                    for (int i = 0; i < empresas.size(); i++) {
                        Empresa data = empresas.get(i);

                        filtro = filtro.toString().toLowerCase();
                        String condicao = data.getNome().toLowerCase();

                        if (condicao.contains(filtro)) {
                            //se conter adiciona na lista de itens filtrados.
                            itens_filtrados.add(data);
                        }
                    }
                    // Define o resultado do filtro na variavel FilterResults
                    results.count = itens_filtrados.size();
                    results.values = itens_filtrados;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                itens_exibicao = (List<Empresa>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }
        };
        return filter;
    }
}
