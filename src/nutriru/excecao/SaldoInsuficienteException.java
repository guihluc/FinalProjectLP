package nutriru.excecao;

/** Lancada quando o usuario tenta pagar uma refeicao e nao tem saldo. */
public class SaldoInsuficienteException extends Exception {

    private double saldoAtual;
    private double valorNecessario;

    public SaldoInsuficienteException(double saldoAtual, double valorNecessario) {
        super("Saldo insuficiente: saldo de R$ " + String.format("%.2f", saldoAtual)
                + " e a refeicao custa R$ " + String.format("%.2f", valorNecessario));
        this.saldoAtual = saldoAtual;
        this.valorNecessario = valorNecessario;
    }

    public double getSaldoAtual() {
        return saldoAtual;
    }

    public double getValorNecessario() {
        return valorNecessario;
    }
}
