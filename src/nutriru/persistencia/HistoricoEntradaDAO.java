package nutriru.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nutriru.modelo.HistoricoEntrada;

/** Acesso a tabela historico_entradas. */
public class HistoricoEntradaDAO {

    // recebe a conexao porque roda junto com o desconto do saldo na mesma transacao
    public void registrar(Connection conexao, int usuarioId, int refeicaoId, double valorCobrado)
            throws SQLException {
        String sql = "INSERT INTO historico_entradas (usuario_id, refeicao_id, valor_cobrado) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, refeicaoId);
            ps.setDouble(3, valorCobrado);
            ps.executeUpdate();
        }
    }

    public List<HistoricoEntrada> listarPorUsuario(int usuarioId) throws SQLException {
        List<HistoricoEntrada> historico = new ArrayList<>();
        String sql = "SELECT h.data_hora, h.valor_cobrado, r.tipo "
                + "FROM historico_entradas h JOIN refeicoes r ON h.refeicao_id = r.id "
                + "WHERE h.usuario_id = ? ORDER BY h.data_hora DESC";
        try (Connection conexao = FabricaConexao.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historico.add(new HistoricoEntrada(
                            rs.getString("tipo"),
                            rs.getTimestamp("data_hora").toLocalDateTime(),
                            rs.getDouble("valor_cobrado")));
                }
            }
        }
        return historico;
    }
}
