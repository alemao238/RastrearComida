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
import br.com.sebrae.rastrearcomida.modelo.Produto;

/**
 * Created by DMADESENV01 on 13/05/2015.
 */
public class ListaProdutoAdapter extends ArrayAdapter<Produto>{
    //Itens de exibição/filtrados
    private List<Produto> itens_exibicao;
    private Context context;
    //Essa lista contém todos os itens
    private List<Produto> produtos;

    public ListaProdutoAdapter(Context context, List<Produto> produtos) {
        super(context, 0, produtos);
        this.context = context;
        this.produtos = produtos;
        this.itens_exibicao = produtos;
    }

    @Override
    public int getCount() {
        return itens_exibicao.size();
    }

    @Override
    public Produto getItem(int position) {
        return itens_exibicao.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itens_exibicao.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Produto produto = itens_exibicao.get(position);
        if(view == null)
            view = LayoutInflater.from(context).inflate(R.layout.layout_lista_produto, null);

        TextView textViewNome = (TextView) view.findViewById(R.id.descricaoProduto);
        textViewNome.setText(produto.getDescricao());

        TextView textViewDescricao = (TextView) view.findViewById(R.id.valorProduto);
        textViewDescricao.setText(String.valueOf(produto.getValor_unitario()));

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
                    results.count = produtos.size();
                    results.values = produtos;
                } else {
                    //cria um array para armazenar os objetos filtrados.
                    List<Produto> itens_filtrados = new ArrayList<Produto>();

                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                    for (int i = 0; i < produtos.size(); i++) {
                        Produto data = produtos.get(i);

                        filtro = filtro.toString().toLowerCase();
                        String condicao = data.getDescricao().toLowerCase();

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
                itens_exibicao = (List<Produto>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }
        };
        return filter;
    }
}
