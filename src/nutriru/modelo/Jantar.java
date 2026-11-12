package nutriru.modelo;

import java.time.LocalTime;

public class Jantar extends Refeicao {

    public Jantar() {
        super("Jantar", LocalTime.of(17, 30), LocalTime.of(19, 30));
    }

    @Override
    public double getPrecoBase() {
        return 8.00;
    }
}
