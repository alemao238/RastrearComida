package br.com.sebrae.rastrearcomida.util;

import org.json.JSONException;
import org.json.JSONStringer;

import br.com.sebrae.rastrearcomida.modelo.Usuario;

/**
 * Created by Israel on 05/05/2015.
 */
public class UsuarioConverterJson {
    public String toJSON(Usuario usuario){
        try {
            JSONStringer jsonStringer = new JSONStringer();
            jsonStringer.object()
                    .key("nome").value(usuario.getNome())
                    .key("email").value(usuario.getEmail())
                    .key("password").value(usuario.getSenha())
                    .key("password_confirmation").value(usuario.getConfirmacao_senha())
                    .key("tipo").value(1)
                    .endObject();
            return jsonStringer.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
