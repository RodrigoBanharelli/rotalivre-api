package br.com.rotalivre.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

//Dados recebidos para cadastrar ou atualizar um usuario. Validados com @Valid (erro 400).
public class UsuarioRequestDto {

    @NotBlank(message = "O nome e obrigatorio")
    @Size(max = 100, message = "O nome deve ter no maximo 100 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail e obrigatorio")
    @Email(message = "O e-mail e invalido")
    @Size(max = 120, message = "O e-mail deve ter no maximo 120 caracteres")
    private String email;

    //Opcional. Mesmos valores do check do banco.
    @Pattern(regexp = "CADEIRANTE|MOBILIDADE_REDUZIDA|DEFICIENCIA_VISUAL|IDOSO|OUTRO",
            message = "Perfil deve ser CADEIRANTE, MOBILIDADE_REDUZIDA, DEFICIENCIA_VISUAL, IDOSO ou OUTRO")
    private String perfilMobilidade;

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
}
