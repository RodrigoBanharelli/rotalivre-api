package br.com.rotalivre.exception;

//Registro nao existe no banco. O handler devolve 404.
public class EntidadeNaoEncontradaException extends Exception {

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
