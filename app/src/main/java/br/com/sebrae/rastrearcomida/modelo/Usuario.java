package br.com.sebrae.rastrearcomida.modelo;

import java.io.Serializable;

/**
 * Created by Israel on 05/05/2015.
 */
public class Usuario implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String senha;
    private String confirmacao_senha;

    public String getConfirmacao_senha() {
        return confirmacao_senha;
    }

    public void setConfirmacao_senha(String confirmacao_senha) {
        this.confirmacao_senha = confirmacao_senha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario obj = (Usuario) o;

        if (!id.equals(obj.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return nome;
    }
}
