package nutriru.modelo;

import java.time.LocalTime;

public abstract class Refeicao {

    private String nome;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;

    public Refeicao(String nome, LocalTime horarioInicio, LocalTime horarioFim) {
        this.nome = nome;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
    }

    public abstract double getPrecoBase();

    public String getNome() {
        return nome;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    @Override
    public String toString() {
        return nome + " (" + horarioInicio + " - " + horarioFim + ")";
    }
}
