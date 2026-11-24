package nutriru.persistencia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import nutriru.modelo.Almoco;
import nutriru.modelo.Jantar;
import nutriru.modelo.Refeicao;

/** Acesso a tabela refeicoes. */
public class RefeicaoDAO {

    public void inserir(Refeicao refeicao) throws SQLException {
        String sql = "INSERT INTO refeicoes (tipo, data, cardapio, preco_base) VALUES (?, ?, ?, ?)";
        try (Connection conexao = FabricaConexao.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, refeicao.getTipo());
            ps.setDate(2, Date.valueOf(refeicao.getData()));
            ps.setString(3, refeicao.getCardapio());
            ps.setDouble(4, refeicao.getPrecoBase());
            ps.executeUpdate();
            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    refeicao.setId(chaves.getInt(1));
                }
            }
        }
    }

    /** Retorna null se nao achar. */
    public Refeicao buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM refeicoes WHERE id = ?";
        try (Connection conexao = FabricaConexao.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montarRefeicao(rs);
                }
            }
        }
        return null;
    }

    public List<Refeicao> listarPorData(LocalDate data) throws SQLException {
        List<Refeicao> refeicoes = new ArrayList<>();
        String sql = "SELECT * FROM refeicoes WHERE data = ? ORDER BY id";
        try (Connection conexao = FabricaConexao.getConexao();
                PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(data));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    refeicoes.add(montarRefeicao(rs));
                }
            }
        }
        return refeicoes;
    }

    private Refeicao montarRefeicao(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDate data = rs.getDate("data").toLocalDate();
        String cardapio = rs.getString("cardapio");

        if (rs.getString("tipo").equals("ALMOCO")) {
            return new Almoco(id, data, cardapio);
        } else {
            return new Jantar(id, data, cardapio);
        }
    }
}
