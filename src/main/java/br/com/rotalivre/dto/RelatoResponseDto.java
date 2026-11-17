package br.com.rotalivre.dto;

import java.time.LocalDateTime;

//Dados do relato devolvidos pela API.
public class RelatoResponseDto {

    private int codigo;
    private int codigoLocal;
    private int codigoUsuario;
    private String situacao;
    private String observacao;
    private LocalDateTime dataRelato;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoLocal() {
        return codigoLocal;
    }

    public void setCodigoLocal(int codigoLocal) {
        this.codigoLocal = codigoLocal;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDateTime getDataRelato() {
        return dataRelato;
    }

    public void setDataRelato(LocalDateTime dataRelato) {
        this.dataRelato = dataRelato;
    }
}
