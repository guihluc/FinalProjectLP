package nutriru.servico;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import nutriru.excecao.MatriculaInvalidaException;
import nutriru.excecao.RefeicaoNaoEncontradaException;
import nutriru.excecao.SaldoInsuficienteException;
import nutriru.excecao.UsuarioNaoEncontradoException;
import nutriru.modelo.Autenticavel;
import nutriru.modelo.HistoricoEntrada;
import nutriru.modelo.Refeicao;
import nutriru.modelo.UsuarioRU;
import nutriru.persistencia.FabricaConexao;
import nutriru.persistencia.HistoricoEntradaDAO;
import nutriru.persistencia.RefeicaoDAO;
import nutriru.persistencia.UsuarioDAO;

/** Regras do RU: cadastro, recarga e entrada na catraca. A tela so conversa com essa classe. */
public class ControleAcesso {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private RefeicaoDAO refeicaoDAO = new RefeicaoDAO();
    private HistoricoEntradaDAO historicoDAO = new HistoricoEntradaDAO();

    public void cadastrarUsuario(UsuarioRU usuario) throws MatriculaInvalidaException, SQLException {
        if (!usuario.validarMatricula()) {
            throw new MatriculaInvalidaException("Matricula invalida para " + usuario.getTipo());
        }
        if (usuarioDAO.buscarPorMatricula(usuario.getMatricula()) != null) {
            throw new MatriculaInvalidaException("Matricula ja cadastrada");
        }
        usuarioDAO.inserir(usuario);
    }

    public UsuarioRU buscarUsuario(String matricula) throws UsuarioNaoEncontradoException, SQLException {
        UsuarioRU usuario = usuarioDAO.buscarPorMatricula(matricula);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException(matricula);
        }
        return usuario;
    }

    public UsuarioRU recarregar(String matricula, double valor) throws UsuarioNaoEncontradoException, SQLException {
        UsuarioRU usuario = buscarUsuario(matricula);
        usuario.adicionarCredito(valor);
        usuarioDAO.atualizarSaldo(usuario);
        return usuario;
    }

    public void cadastrarRefeicao(Refeicao refeicao) throws SQLException {
        refeicaoDAO.inserir(refeicao);
    }

    public List<Refeicao> consultarCardapio(LocalDate data) throws SQLException {
        return refeicaoDAO.listarPorData(data);
    }

    public Refeicao buscarRefeicao(int id) throws RefeicaoNaoEncontradaException, SQLException {
        Refeicao refeicao = refeicaoDAO.buscarPorId(id);
        if (refeicao == null) {
            throw new RefeicaoNaoEncontradaException(id);
        }
        return refeicao;
    }

    /** So consulta, nao cobra nada. */
    public boolean verificarAutorizacao(String matricula, int idRefeicao)
            throws UsuarioNaoEncontradoException, RefeicaoNaoEncontradaException, SQLException {
        Autenticavel usuario = buscarUsuario(matricula);
        Refeicao refeicao = buscarRefeicao(idRefeicao);
        return usuario.autorizarEntrada(refeicao);
    }

    /** Libera a entrada: cobra a refeicao e grava no historico. Retorna o valor cobrado. */
    public double entrar(String matricula, int idRefeicao) throws UsuarioNaoEncontradoException,
            RefeicaoNaoEncontradaException, MatriculaInvalidaException, SaldoInsuficienteException, SQLException {
        UsuarioRU usuario = buscarUsuario(matricula);
        Refeicao refeicao = buscarRefeicao(idRefeicao);

        if (!usuario.validarMatricula()) {
            throw new MatriculaInvalidaException("Matricula invalida para " + usuario.getTipo());
        }

        double preco = usuario.calcularPrecoRefeicao(refeicao);
        usuario.debitar(preco); // lanca SaldoInsuficienteException se nao tiver saldo

        // desconta o saldo e grava o historico juntos: se um falhar, desfaz os dois
        Connection conexao = FabricaConexao.getConexao();
        try {
            conexao.setAutoCommit(false);
            usuarioDAO.atualizarSaldo(conexao, usuario);
            historicoDAO.registrar(conexao, usuario.getId(), refeicao.getId(), preco);
            conexao.commit();
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.close();
        }
        return preco;
    }

    public List<HistoricoEntrada> consultarHistorico(String matricula)
            throws UsuarioNaoEncontradoException, SQLException {
        UsuarioRU usuario = buscarUsuario(matricula);
        return historicoDAO.listarPorUsuario(usuario.getId());
    }
}
