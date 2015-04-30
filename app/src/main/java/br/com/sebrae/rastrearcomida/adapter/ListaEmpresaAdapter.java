package br.com.sebrae.rastrearcomida.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

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
        return super.getFilter();
    }
}
