package nutriru.modelo;

public class Servidor extends UsuarioRU {

    private String departamento;

    public Servidor(String matricula, String nome, String departamento) {
        super(matricula, nome);
        this.departamento = departamento;
    }

    public Servidor(int id, String matricula, String nome, String departamento, double saldo) {
        super(id, matricula, nome, saldo);
        this.departamento = departamento;
    }

    // servidor paga o preco base inteiro
    @Override
    public double calcularPrecoRefeicao(Refeicao refeicao) {
        return refeicao.getPrecoBase();
    }

    // matricula SIAPE com 7 digitos
    @Override
    public boolean validarMatricula() {
        String matricula = getMatricula();
        return matricula != null && matricula.matches("\\d{7}");
    }

    @Override
    public String getTipo() {
        return "SERVIDOR";
    }

    @Override
    public String getDetalhe() {
        return departamento;
    }

    public String getDepartamento() {
        return departamento;
    }
}
