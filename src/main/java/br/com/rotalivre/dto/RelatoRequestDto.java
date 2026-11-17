package br.com.rotalivre.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

//Dados recebidos para registrar ou alterar um relato. Sem login: o usuario e identificado pelo e-mail.
public class RelatoRequestDto {

    @NotBlank(message = "O e-mail do usuario e obrigatorio")
    @Email(message = "O e-mail do usuario e invalido")
    private String emailUsuario;

    //Mesmos valores do check do banco
    @NotBlank(message = "A situacao e obrigatoria")
    @Pattern(regexp = "ACESSIVEL|PARCIAL|INACESSIVEL",
            message = "A situacao deve ser ACESSIVEL, PARCIAL ou INACESSIVEL")
    private String situacao;

    @Size(max = 400, message = "A observacao deve ter no maximo 400 caracteres")
    private String observacao;

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
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
}
