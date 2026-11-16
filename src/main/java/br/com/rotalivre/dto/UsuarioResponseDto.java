package br.com.rotalivre.dto;

import java.time.LocalDate;

//Dados devolvidos pela API. A API nunca devolve o model direto.
public class UsuarioResponseDto {

    private int codigo;
    private String nome;
    private String email;
    private String perfilMobilidade;
    private LocalDate dataCadastro;

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
}
