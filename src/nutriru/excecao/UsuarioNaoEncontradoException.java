package nutriru.excecao;

/** Lancada quando nao existe usuario com a matricula informada. */
public class UsuarioNaoEncontradoException extends Exception {

    public UsuarioNaoEncontradoException(String matricula) {
        super("Usuario com matricula " + matricula + " nao encontrado");
    }
}
