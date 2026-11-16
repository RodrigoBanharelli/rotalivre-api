package br.com.rotalivre.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

//Relato de um usuario sobre um local (tabela tb_relato). Lado N dos dois relacionamentos 1:N.
public class Relato {

    private int codigo;
    private int codigoLocal;
    private int codigoUsuario;
    private String situacao; //ACESSIVEL, PARCIAL ou INACESSIVEL
    private String observacao;
    private LocalDateTime dataRelato;

    public Relato() {
    }

    public Relato(int codigo, int codigoLocal, int codigoUsuario, String situacao, String observacao,
                  LocalDateTime dataRelato) {
        this.codigo = codigo;
        this.codigoLocal = codigoLocal;
        this.codigoUsuario = codigoUsuario;
        this.situacao = situacao;
        this.observacao = observacao;
        this.dataRelato = dataRelato;
    }

    //Pontos da situacao: ACESSIVEL 100, PARCIAL 50, INACESSIVEL 0.
    public int pontuacao() {
        if (situacao.equals("ACESSIVEL")) {
            return 100;
        } else if (situacao.equals("PARCIAL")) {
            return 50;
        }
        return 0;
    }

    //Peso pela idade do relato: ate 7 dias 1.0, ate 15 dias 0.6, ate 30 dias 0.3, mais antigo 0.
    public double peso(LocalDate hoje) {
        long dias = ChronoUnit.DAYS.between(dataRelato.toLocalDate(), hoje);
        if (dias <= 7) {
            return 1.0;
        } else if (dias <= 15) {
            return 0.6;
        } else if (dias <= 30) {
            return 0.3;
        }
        return 0;
    }

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

    @Override
    public String toString() {
        return "Relato{codigo=" + codigo + ", local=" + codigoLocal + ", situacao='" + situacao + "'}";
    }
}
