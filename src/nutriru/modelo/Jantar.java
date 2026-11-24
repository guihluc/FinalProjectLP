package nutriru.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Jantar extends Refeicao {

    public Jantar(LocalDate data, String cardapio) {
        super("Jantar", data, cardapio, LocalTime.of(17, 30), LocalTime.of(19, 30));
    }

    public Jantar(int id, LocalDate data, String cardapio) {
        super(id, "Jantar", data, cardapio, LocalTime.of(17, 30), LocalTime.of(19, 30));
    }

    @Override
    public double getPrecoBase() {
        return 8.00;
    }

    @Override
    public String getTipo() {
        return "JANTAR";
    }
}
