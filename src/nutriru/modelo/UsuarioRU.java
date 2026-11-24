package nutriru.modelo;

import nutriru.excecao.SaldoInsuficienteException;

/** Base de todos os usuarios do RU. Cada tipo paga um valor diferente pela refeicao. */
public abstract class UsuarioRU implements Autenticavel {

    private int id;
    private String matricula;
    private String nome;
    private double saldo;

    // usuario novo, ainda sem id (o banco gera) e com saldo zerado
    public UsuarioRU(String matricula, String nome) {
        this.matricula = matricula;
        this.nome = nome;
        this.saldo = 0;
    }

    // usuario que ja existe no banco
    public UsuarioRU(int id, String matricula, String nome, double saldo) {
        this.id = id;
        this.matricula = matricula;
        this.nome = nome;
        this.saldo = saldo;
    }

    /** Quanto esse tipo de usuario paga pela refeicao. */
    public abstract double calcularPrecoRefeicao(Refeicao refeicao);

    /** Tipo gravado no banco: ESTUDANTE, SERVIDOR ou VISITANTE. */
    public abstract String getTipo();

    /** Curso, departamento ou motivo da visita, dependendo do tipo. */
    public abstract String getDetalhe();

    public void adicionarCredito(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor da recarga deve ser maior que zero");
        }
        saldo += valor;
    }

    public void debitar(double valor) throws SaldoInsuficienteException {
        if (valor > saldo) {
            throw new SaldoInsuficienteException(saldo, valor);
        }
        saldo -= valor;
    }

    @Override
    public boolean autorizarEntrada(Refeicao refeicao) {
        return validarMatricula() && saldo >= calcularPrecoRefeicao(refeicao);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return getTipo() + " - " + nome + " (" + matricula + ") - saldo: R$ " + String.format("%.2f", saldo);
    }
}
