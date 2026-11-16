package br.com.rotalivre.model;

import java.time.LocalDate;

//Local mapeado (tabela tb_local_acessivel). Pertence a um tipo e tem varios relatos (1:N).
public class LocalAcessivel {

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

    public LocalAcessivel() {
    }

    public LocalAcessivel(int codigo, int codigoTipoLocal, String nome, String endereco, double latitude,
                          double longitude, double inclinacaoPct, int larguraCm, boolean ativo,
                          LocalDate dataCadastro) {
        this.codigo = codigo;
        this.codigoTipoLocal = codigoTipoLocal;
        this.nome = nome;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
        this.inclinacaoPct = inclinacaoPct;
        this.larguraCm = larguraCm;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }

    //Distancia em km ate um ponto (formula de Haversine). Usada na busca por proximidade.
    public double distanciaKm(double lat, double lon) {
        double raioTerraKm = 6371;
        double dLat = Math.toRadians(lat - latitude);
        double dLon = Math.toRadians(lon - longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(lat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return raioTerraKm * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

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

    @Override
    public String toString() {
        return "LocalAcessivel{codigo=" + codigo + ", nome='" + nome + "'}";
    }
}
