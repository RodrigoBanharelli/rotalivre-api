package br.com.rotalivre.model;

import java.time.LocalDate;

//Cidadao que relata barreiras (tabela tb_usuario). Um usuario tem varios relatos (1:N).
public class Usuario {

    private int codigo;
    private String nome;
    private String email;
    private String perfilMobilidade;
    private LocalDate dataCadastro;

    public Usuario() {
    }

    public Usuario(int codigo, String nome, String email, String perfilMobilidade, LocalDate dataCadastro) {
        this.codigo = codigo;
        this.nome = nome;
        this.email = email;
        this.perfilMobilidade = perfilMobilidade;
        this.dataCadastro = dataCadastro;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfilMobilidade() {
        return perfilMobilidade;
    }

    public void setPerfilMobilidade(String perfilMobilidade) {
        this.perfilMobilidade = perfilMobilidade;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        //Sem e-mail no toString para nao expor dado pessoal em log.
        return "Usuario{codigo=" + codigo + ", nome='" + nome + "'}";
    }
}
