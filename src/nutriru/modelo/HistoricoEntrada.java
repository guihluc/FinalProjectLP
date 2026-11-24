package nutriru.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Registro de uma passagem pela catraca. */
public class HistoricoEntrada {

    private String nomeRefeicao;
    private LocalDateTime dataHora;
    private double valorCobrado;

    public HistoricoEntrada(String nomeRefeicao, LocalDateTime dataHora, double valorCobrado) {
        this.nomeRefeicao = nomeRefeicao;
        this.dataHora = dataHora;
        this.valorCobrado = valorCobrado;
    }

    public String getNomeRefeicao() {
        return nomeRefeicao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public double getValorCobrado() {
        return valorCobrado;
    }

    @Override
    public String toString() {
        return dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + " - " + nomeRefeicao
                + " - R$ " + String.format("%.2f", valorCobrado);
    }
}
