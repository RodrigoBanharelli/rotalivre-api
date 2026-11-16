package br.com.rotalivre.exception;

//Regra do Rotalivre violada (ex.: e-mail repetido, relato repetido no mesmo dia). O handler devolve 409.
public class RegraNegocioException extends Exception {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
