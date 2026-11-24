package nutriru.modelo;

/** Contrato de quem passa na catraca do RU. */
public interface Autenticavel {

    boolean validarMatricula();

    boolean autorizarEntrada(Refeicao refeicao);
}
