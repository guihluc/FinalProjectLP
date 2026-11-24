package nutriru.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Almoco extends Refeicao {

    public Almoco(LocalDate data, String cardapio) {
        super("Almoco", data, cardapio, LocalTime.of(11, 0), LocalTime.of(14, 0));
    }

    public Almoco(int id, LocalDate data, String cardapio) {
        super(id, "Almoco", data, cardapio, LocalTime.of(11, 0), LocalTime.of(14, 0));
    }

    @Override
    public double getPrecoBase() {
        return 10.00;
    }

    @Override
    public String getTipo() {
        return "ALMOCO";
    }
}
