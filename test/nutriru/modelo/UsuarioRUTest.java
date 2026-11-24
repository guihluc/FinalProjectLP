package nutriru.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nutriru.excecao.SaldoInsuficienteException;

class UsuarioRUTest {

    private Estudante estudante;
    private Servidor servidor;
    private Visitante visitante;
    private Refeicao almoco;
    private Refeicao jantar;

    @BeforeEach
    void preparar() {
        estudante = new Estudante("20240070366", "Brunno", "Tecnologia da Informacao");
        servidor = new Servidor("1234567", "Maria", "IMD");
        visitante = new Visitante("12345", "Carla", "Palestra");
        almoco = new Almoco(LocalDate.of(2026, 11, 24), "Arroz e feijao");
        jantar = new Jantar(LocalDate.of(2026, 11, 24), "Macarrao");
    }

    @Test
    void estudantePagaVintePorCentoDoPrecoBase() {
        assertEquals(2.00, estudante.calcularPrecoRefeicao(almoco), 0.001);
        assertEquals(1.60, estudante.calcularPrecoRefeicao(jantar), 0.001);
    }

    @Test
    void servidorPagaPrecoBaseInteiro() {
        assertEquals(10.00, servidor.calcularPrecoRefeicao(almoco), 0.001);
        assertEquals(8.00, servidor.calcularPrecoRefeicao(jantar), 0.001);
    }

    @Test
    void visitantePagaCinquentaPorCentoAMais() {
        assertEquals(15.00, visitante.calcularPrecoRefeicao(almoco), 0.001);
        assertEquals(12.00, visitante.calcularPrecoRefeicao(jantar), 0.001);
    }

    @Test
    void matriculaDeEstudanteTemOnzeDigitos() {
        assertTrue(estudante.validarMatricula());
        assertFalse(new Estudante("123", "Joao", "TI").validarMatricula());
        assertFalse(new Estudante("2024007036A", "Joao", "TI").validarMatricula());
    }

    @Test
    void matriculaDeServidorTemSeteDigitos() {
        assertTrue(servidor.validarMatricula());
        assertFalse(new Servidor("12345678", "Ana", "IMD").validarMatricula());
    }

    @Test
    void codigoDeVisitanteTemCincoDigitos() {
        assertTrue(visitante.validarMatricula());
        assertFalse(new Visitante("1234", "Rui", "Visita").validarMatricula());
    }

    @Test
    void recargaAumentaOSaldo() {
        estudante.adicionarCredito(10);
        estudante.adicionarCredito(5.5);
        assertEquals(15.5, estudante.getSaldo(), 0.001);
    }

    @Test
    void recargaComValorInvalidoLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> estudante.adicionarCredito(0));
        assertThrows(IllegalArgumentException.class, () -> estudante.adicionarCredito(-3));
        assertEquals(0, estudante.getSaldo(), 0.001);
    }

    @Test
    void debitarDiminuiOSaldo() throws SaldoInsuficienteException {
        estudante.adicionarCredito(10);
        estudante.debitar(2);
        assertEquals(8, estudante.getSaldo(), 0.001);
    }

    @Test
    void debitarSemSaldoLancaSaldoInsuficienteException() {
        estudante.adicionarCredito(1);
        SaldoInsuficienteException erro = assertThrows(SaldoInsuficienteException.class,
                () -> estudante.debitar(2));
        assertEquals(1, erro.getSaldoAtual(), 0.001);
        assertEquals(2, erro.getValorNecessario(), 0.001);
        // o saldo nao pode mudar quando da erro
        assertEquals(1, estudante.getSaldo(), 0.001);
    }

    @Test
    void entradaAutorizadaComMatriculaValidaESaldo() {
        estudante.adicionarCredito(2);
        assertTrue(estudante.autorizarEntrada(almoco));
    }

    @Test
    void entradaNegadaSemSaldo() {
        estudante.adicionarCredito(1);
        assertFalse(estudante.autorizarEntrada(almoco));
    }

    @Test
    void entradaNegadaComMatriculaInvalida() {
        Estudante invalido = new Estudante("123", "Joao", "TI");
        invalido.adicionarCredito(50);
        assertFalse(invalido.autorizarEntrada(almoco));
    }

    @Test
    void tipoEDetalheDependemDaSubclasse() {
        assertEquals("ESTUDANTE", estudante.getTipo());
        assertEquals("SERVIDOR", servidor.getTipo());
        assertEquals("VISITANTE", visitante.getTipo());
        assertEquals("IMD", servidor.getDetalhe());
    }

    @Test
    void polimorfismoComListaDeUsuarios() {
        List<UsuarioRU> usuarios = List.of(estudante, servidor, visitante);
        double total = 0;
        for (UsuarioRU usuario : usuarios) {
            total += usuario.calcularPrecoRefeicao(almoco);
        }
        // 2 + 10 + 15
        assertEquals(27.00, total, 0.001);
    }

    @Test
    void polimorfismoComAInterfaceAutenticavel() {
        List<Autenticavel> catraca = List.of(estudante, servidor, visitante);
        for (Autenticavel pessoa : catraca) {
            assertTrue(pessoa.validarMatricula());
            // ninguem tem saldo ainda
            assertFalse(pessoa.autorizarEntrada(almoco));
        }
    }
}
