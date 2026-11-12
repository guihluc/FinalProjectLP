package nutriru.modelo;

public abstract class UsuarioRU implements Autenticavel {

    private String matricula;
    private String nome;
    private double saldo;

    public UsuarioRU(String matricula, String nome) {
        this.matricula = matricula;
        this.nome = nome;
        this.saldo = 0;
    }

    // cada tipo de usuario paga um valor diferente
    public abstract double calcularPrecoRefeicao(Refeicao refeicao);

    public void adicionarCredito(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da recarga deve ser maior que zero");
        }
        saldo += valor;
    }

    @Override
    public boolean autorizarEntrada(Refeicao refeicao) {
        return validarMatricula() && saldo >= calcularPrecoRefeicao(refeicao);
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }

    @Override
    public String toString() {
        return nome + " (" + matricula + ") - saldo: R$ " + String.format("%.2f", saldo);
    }
}
