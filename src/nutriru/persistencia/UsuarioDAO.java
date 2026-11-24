package nutriru.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import nutriru.modelo.Estudante;
import nutriru.modelo.Servidor;
import nutriru.modelo.UsuarioRU;
import nutriru.modelo.Visitante;

/** Acesso a tabela usuarios. */
public class UsuarioDAO {

    public void inserir(UsuarioRU usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (matricula, nome, tipo, detalhe, saldo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = FabricaConexao.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getMatricula());
            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getTipo());
            ps.setString(4, usuario.getDetalhe());
            ps.setDouble(5, usuario.getSaldo());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    usuario.setId(chaves.getInt(1));
                }
            }
        }
    }

    /** Retorna null se nao achar. */
    public UsuarioRU buscarPorMatricula(String matricula) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE matricula = ?";
        try (Connection conexao = FabricaConexao.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montarUsuario(rs);
                }
            }
        }
        return null;
    }

    public List<UsuarioRU> listar() throws SQLException {
        List<UsuarioRU> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        try (Connection conexao = FabricaConexao.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(montarUsuario(rs));
            }
        }
        return usuarios;
    }

    public void atualizarSaldo(UsuarioRU usuario) throws SQLException {
        try (Connection conexao = FabricaConexao.getConexao()) {
            atualizarSaldo(conexao, usuario);
        }
    }

    // versao que recebe a conexao, para usar dentro de uma transacao
    public void atualizarSaldo(Connection conexao, UsuarioRU usuario) throws SQLException {
        String sql = "UPDATE usuarios SET saldo = ? WHERE id = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setDouble(1, usuario.getSaldo());
            ps.setInt(2, usuario.getId());
            ps.executeUpdate();
        }
    }

    // cria o objeto da subclasse certa de acordo com a coluna tipo
    private UsuarioRU montarUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String matricula = rs.getString("matricula");
        String nome = rs.getString("nome");
        String detalhe = rs.getString("detalhe");
        double saldo = rs.getDouble("saldo");
        String tipo = rs.getString("tipo");

        if (tipo.equals("ESTUDANTE")) {
            return new Estudante(id, matricula, nome, detalhe, saldo);
        } else if (tipo.equals("SERVIDOR")) {
            return new Servidor(id, matricula, nome, detalhe, saldo);
        } else {
            return new Visitante(id, matricula, nome, detalhe, saldo);
        }
    }
}
