package br.com.rotalivre.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

//Dados recebidos para cadastrar ou atualizar um local. Validados com @Valid (erro 400).
public class LocalRequestDto {

    @NotNull(message = "O codigo do tipo de local e obrigatorio")
    @Positive(message = "O codigo do tipo de local deve ser positivo")
    private Integer codigoTipoLocal;

    @NotBlank(message = "O nome e obrigatorio")
    @Size(max = 120, message = "O nome deve ter no maximo 120 caracteres")
    private String nome;

    @NotBlank(message = "O endereco e obrigatorio")
    @Size(max = 200, message = "O endereco deve ter no maximo 200 caracteres")
    private String endereco;

    @NotNull(message = "A latitude e obrigatoria")
    @DecimalMin(value = "-90", message = "A latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90", message = "A latitude deve estar entre -90 e 90")
    private Double latitude;

    @NotNull(message = "A longitude e obrigatoria")
    @DecimalMin(value = "-180", message = "A longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180", message = "A longitude deve estar entre -180 e 180")
    private Double longitude;

    //Mesma regra do check do banco: inclinacao de 0 a 100%
    @NotNull(message = "A inclinacao e obrigatoria")
    @DecimalMin(value = "0", message = "A inclinacao deve estar entre 0 e 100")
    @DecimalMax(value = "100", message = "A inclinacao deve estar entre 0 e 100")
    private Double inclinacaoPct;

    //Mesma regra do check do banco: largura maior que zero
    @NotNull(message = "A largura e obrigatoria")
    @Positive(message = "A largura deve ser maior que 0")
    private Integer larguraCm;

    public Integer getCodigoTipoLocal() {
        return codigoTipoLocal;
    }

    public void setCodigoTipoLocal(Integer codigoTipoLocal) {
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getInclinacaoPct() {
        return inclinacaoPct;
    }

    public void setInclinacaoPct(Double inclinacaoPct) {
        this.inclinacaoPct = inclinacaoPct;
    }

    public Integer getLarguraCm() {
        return larguraCm;
    }

    public void setLarguraCm(Integer larguraCm) {
        this.larguraCm = larguraCm;
    }
}
