package nutriru.modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Base das refeicoes servidas no RU (almoco e jantar). */
public abstract class Refeicao {

    private int id;
    private String nome;
    private LocalDate data;
    private String cardapio;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;

    public Refeicao(String nome, LocalDate data, String cardapio, LocalTime horarioInicio, LocalTime horarioFim) {
        this.nome = nome;
        this.data = data;
        this.cardapio = cardapio;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
    }

    public Refeicao(int id, String nome, LocalDate data, String cardapio, LocalTime horarioInicio, LocalTime horarioFim) {
        this(nome, data, cardapio, horarioInicio, horarioFim);
        this.id = id;
    }

    public abstract double getPrecoBase();

    /** Tipo gravado no banco: ALMOCO ou JANTAR. */
    public abstract String getTipo();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getData() {
        return data;
    }

    public String getCardapio() {
        return cardapio;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    @Override
    public String toString() {
        String dataTexto = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return "[" + id + "] " + nome + " " + dataTexto + " (" + horarioInicio + " - " + horarioFim + ") - "
                + cardapio + " - base R$ " + String.format("%.2f", getPrecoBase());
    }
}
