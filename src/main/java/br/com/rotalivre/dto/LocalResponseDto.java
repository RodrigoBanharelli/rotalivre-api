package br.com.rotalivre.dto;

import java.time.LocalDate;

//Dados do local devolvidos pela API.
public class LocalResponseDto {

    private int codigo;
    private int codigoTipoLocal;
    private String nome;
    private String endereco;
    private double latitude;
    private double longitude;
    private double inclinacaoPct;
    private int larguraCm;
    private boolean ativo;
    private LocalDate dataCadastro;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoTipoLocal() {
        return codigoTipoLocal;
    }

    public void setCodigoTipoLocal(int codigoTipoLocal) {
        this.codigoTipoLocal = codigoTipoLocal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getInclinacaoPct() {
        return inclinacaoPct;
    }

    public void setInclinacaoPct(double inclinacaoPct) {
        this.inclinacaoPct = inclinacaoPct;
    }

    public int getLarguraCm() {
        return larguraCm;
    }

    public void setLarguraCm(int larguraCm) {
        this.larguraCm = larguraCm;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
