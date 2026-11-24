package nutriru.modelo;

public class Visitante extends UsuarioRU {

    // visitante paga 50% a mais que o preco base
    private static final double PERCENTUAL_VISITANTE = 1.5;

    private String motivoVisita;

    public Visitante(String codigo, String nome, String motivoVisita) {
        super(codigo, nome);
        this.motivoVisita = motivoVisita;
    }

    public Visitante(int id, String codigo, String nome, String motivoVisita, double saldo) {
        super(id, codigo, nome, saldo);
        this.motivoVisita = motivoVisita;
    }

    @Override
    public double calcularPrecoRefeicao(Refeicao refeicao) {
        return refeicao.getPrecoBase() * PERCENTUAL_VISITANTE;
    }

    // o visitante recebe um codigo de 5 digitos na portaria
    @Override
    public boolean validarMatricula() {
        String codigo = getMatricula();
        return codigo != null && codigo.matches("\\d{5}");
    }

    @Override
    public String getTipo() {
        return "VISITANTE";
    }

    @Override
    public String getDetalhe() {
        return motivoVisita;
    }

    public String getMotivoVisita() {
        return motivoVisita;
    }
}
