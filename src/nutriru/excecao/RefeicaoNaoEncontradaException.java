package nutriru.excecao;

/** Lancada quando nao existe refeicao com o id informado. */
public class RefeicaoNaoEncontradaException extends Exception {

    public RefeicaoNaoEncontradaException(int id) {
        super("Refeicao com id " + id + " nao encontrada");
    }
}
