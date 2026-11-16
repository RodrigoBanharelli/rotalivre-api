package br.com.rotalivre.model;

//Tipo do local: CALCADA, RAMPA, ELEVADOR, ESTABELECIMENTO ou OBSTACULO (tabela tb_tipo_local).
public class TipoLocal {

    private int codigo;
    private String nome;
    private String descricao;

    public TipoLocal() {
    }

    public TipoLocal(int codigo, String nome, String descricao) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "TipoLocal{codigo=" + codigo + ", nome='" + nome + "'}";
    }
}
