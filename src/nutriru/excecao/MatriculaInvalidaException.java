package nutriru.excecao;

/** Lancada quando a matricula nao segue o formato do tipo de usuario ou ja esta cadastrada. */
public class MatriculaInvalidaException extends Exception {

    public MatriculaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
