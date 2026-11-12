package nutriru.modelo;

public interface Autenticavel {

    boolean validarMatricula();

    boolean autorizarEntrada(Refeicao refeicao);
}
