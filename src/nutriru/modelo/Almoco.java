package nutriru.modelo;

import java.time.LocalTime;

public class Almoco extends Refeicao {

    public Almoco() {
        super("Almoco", LocalTime.of(11, 0), LocalTime.of(14, 0));
    }

    @Override
    public double getPrecoBase() {
        return 10.00;
    }
}
