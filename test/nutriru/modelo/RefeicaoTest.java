package nutriru.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

class RefeicaoTest {

    private LocalDate data = LocalDate.of(2026, 11, 24);

    @Test
    void almocoTemPrecoBaseDeDezReais() {
        Refeicao almoco = new Almoco(data, "Arroz e feijao");
        assertEquals(10.00, almoco.getPrecoBase(), 0.001);
        assertEquals("ALMOCO", almoco.getTipo());
        assertEquals(LocalTime.of(11, 0), almoco.getHorarioInicio());
    }

    @Test
    void jantarTemPrecoBaseDeOitoReais() {
        Refeicao jantar = new Jantar(data, "Macarrao");
        assertEquals(8.00, jantar.getPrecoBase(), 0.001);
        assertEquals("JANTAR", jantar.getTipo());
        assertEquals(LocalTime.of(19, 30), jantar.getHorarioFim());
    }

    @Test
    void refeicoesPodemSerTratadasPelaClasseAbstrata() {
        List<Refeicao> refeicoes = List.of(new Almoco(data, "A"), new Jantar(data, "B"));
        double soma = 0;
        for (Refeicao refeicao : refeicoes) {
            soma += refeicao.getPrecoBase();
        }
        assertEquals(18.00, soma, 0.001);
    }

    @Test
    void textoDaRefeicaoMostraDataECardapio() {
        Refeicao almoco = new Almoco(3, data, "Arroz e feijao");
        String texto = almoco.toString();
        assertTrue(texto.contains("24/11/2026"));
        assertTrue(texto.contains("Arroz e feijao"));
        assertEquals(3, almoco.getId());
    }
}
