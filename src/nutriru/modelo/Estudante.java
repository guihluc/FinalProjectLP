package nutriru.modelo;

public class Estudante extends UsuarioRU {

    // estudante paga so 20% do preco base
    private static final double PERCENTUAL_ESTUDANTE = 0.20;

    private String curso;

    public Estudante(String matricula, String nome, String curso) {
        super(matricula, nome);
        this.curso = curso;
    }

    @Override
    public double calcularPrecoRefeicao(Refeicao refeicao) {
        return refeicao.getPrecoBase() * PERCENTUAL_ESTUDANTE;
    }

    // matricula de aluno da UFRN tem 11 digitos
    @Override
    public boolean validarMatricula() {
        String matricula = getMatricula();
        return matricula != null && matricula.matches("\\d{11}");
    }

    public String getCurso() {
        return curso;
    }
}
