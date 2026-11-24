package nutriru.visao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import nutriru.excecao.MatriculaInvalidaException;
import nutriru.excecao.RefeicaoNaoEncontradaException;
import nutriru.excecao.SaldoInsuficienteException;
import nutriru.excecao.UsuarioNaoEncontradoException;
import nutriru.modelo.Almoco;
import nutriru.modelo.Estudante;
import nutriru.modelo.HistoricoEntrada;
import nutriru.modelo.Jantar;
import nutriru.modelo.Refeicao;
import nutriru.modelo.Servidor;
import nutriru.modelo.UsuarioRU;
import nutriru.modelo.Visitante;
import nutriru.servico.ControleAcesso;

/** Menu de linha de comando do sistema. */
public class MenuCLI {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Scanner scanner = new Scanner(System.in);
    private ControleAcesso controle = new ControleAcesso();

    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            mostrarMenu();
            opcao = lerInteiro("Escolha uma opcao: ");
            try {
                switch (opcao) {
                    case 1:
                        cadastrarUsuario();
                        break;
                    case 2:
                        recarregarCredito();
                        break;
                    case 3:
                        cadastrarRefeicao();
                        break;
                    case 4:
                        consultarCardapio();
                        break;
                    case 5:
                        verificarAutorizacao();
                        break;
                    case 6:
                        entrarNoRU();
                        break;
                    case 7:
                        verSaldoEHistorico();
                        break;
                    case 0:
                        System.out.println("Ate mais!");
                        break;
                    default:
                        System.out.println("Opcao invalida.");
                }
            } catch (UsuarioNaoEncontradoException | RefeicaoNaoEncontradaException
                    | MatriculaInvalidaException | SaldoInsuficienteException e) {
                System.out.println("Nao foi possivel concluir: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Valor invalido: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== NutriRU - Controle de Acesso do RU =====");
        System.out.println("1 - Cadastrar usuario");
        System.out.println("2 - Recarregar credito");
        System.out.println("3 - Cadastrar refeicao (cardapio)");
        System.out.println("4 - Consultar cardapio");
        System.out.println("5 - Verificar autorizacao de entrada");
        System.out.println("6 - Entrar no RU (catraca)");
        System.out.println("7 - Ver saldo e historico");
        System.out.println("0 - Sair");
    }

    private void cadastrarUsuario() throws MatriculaInvalidaException, SQLException {
        System.out.println("Tipo: 1 - Estudante | 2 - Servidor | 3 - Visitante");
        int tipo = lerInteiro("Tipo: ");
        String matricula = lerTexto("Matricula (estudante 11 digitos, servidor 7, visitante 5): ");
        String nome = lerTexto("Nome: ");

        UsuarioRU usuario;
        if (tipo == 1) {
            usuario = new Estudante(matricula, nome, lerTexto("Curso: "));
        } else if (tipo == 2) {
            usuario = new Servidor(matricula, nome, lerTexto("Departamento: "));
        } else if (tipo == 3) {
            usuario = new Visitante(matricula, nome, lerTexto("Motivo da visita: "));
        } else {
            System.out.println("Tipo invalido.");
            return;
        }
        controle.cadastrarUsuario(usuario);
        System.out.println("Usuario cadastrado: " + usuario);
    }

    private void recarregarCredito() throws UsuarioNaoEncontradoException, SQLException {
        String matricula = lerTexto("Matricula: ");
        double valor = lerDecimal("Valor da recarga: R$ ");
        UsuarioRU usuario = controle.recarregar(matricula, valor);
        System.out.println("Recarga feita. " + usuario);
    }

    private void cadastrarRefeicao() throws SQLException {
        System.out.println("Tipo: 1 - Almoco | 2 - Jantar");
        int tipo = lerInteiro("Tipo: ");
        LocalDate data = lerData("Data (dd/MM/yyyy, vazio = hoje): ");
        String cardapio = lerTexto("Cardapio: ");

        Refeicao refeicao;
        if (tipo == 1) {
            refeicao = new Almoco(data, cardapio);
        } else if (tipo == 2) {
            refeicao = new Jantar(data, cardapio);
        } else {
            System.out.println("Tipo invalido.");
            return;
        }
        controle.cadastrarRefeicao(refeicao);
        System.out.println("Refeicao cadastrada: " + refeicao);
    }

    private void consultarCardapio() throws SQLException {
        LocalDate data = lerData("Data (dd/MM/yyyy, vazio = hoje): ");
        List<Refeicao> refeicoes = controle.consultarCardapio(data);
        if (refeicoes.isEmpty()) {
            System.out.println("Nenhuma refeicao cadastrada para essa data.");
        }
        for (Refeicao refeicao : refeicoes) {
            System.out.println(refeicao);
        }
    }

    private void verificarAutorizacao() throws UsuarioNaoEncontradoException, RefeicaoNaoEncontradaException,
            SQLException {
        String matricula = lerTexto("Matricula: ");
        int idRefeicao = lerInteiro("Id da refeicao: ");
        if (controle.verificarAutorizacao(matricula, idRefeicao)) {
            System.out.println("Entrada AUTORIZADA.");
        } else {
            System.out.println("Entrada NEGADA (matricula invalida ou saldo insuficiente).");
        }
    }

    private void entrarNoRU() throws UsuarioNaoEncontradoException, RefeicaoNaoEncontradaException,
            MatriculaInvalidaException, SaldoInsuficienteException, SQLException {
        String matricula = lerTexto("Matricula: ");
        int idRefeicao = lerInteiro("Id da refeicao: ");
        double valor = controle.entrar(matricula, idRefeicao);
        System.out.println("Catraca liberada! Valor cobrado: R$ " + String.format("%.2f", valor));
    }

    private void verSaldoEHistorico() throws UsuarioNaoEncontradoException, SQLException {
        String matricula = lerTexto("Matricula: ");
        UsuarioRU usuario = controle.buscarUsuario(matricula);
        System.out.println(usuario);
        List<HistoricoEntrada> historico = controle.consultarHistorico(matricula);
        if (historico.isEmpty()) {
            System.out.println("Nenhuma entrada registrada.");
        }
        for (HistoricoEntrada entrada : historico) {
            System.out.println(entrada);
        }
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(mensagem));
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro.");
            }
        }
    }

    private double lerDecimal(String mensagem) {
        while (true) {
            try {
                return Double.parseDouble(lerTexto(mensagem).replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor valido, por exemplo 10.50");
            }
        }
    }

    private LocalDate lerData(String mensagem) {
        while (true) {
            String texto = lerTexto(mensagem);
            if (texto.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(texto, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Data invalida, use dd/MM/yyyy.");
            }
        }
    }
}
