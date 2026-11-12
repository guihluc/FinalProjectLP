package nutriru.persistencia;

import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexao {

    public static void main(String[] args) {
        try (Connection conexao = FabricaConexao.getConexao()) {
            System.out.println("Conectou no banco: " + conexao.getCatalog());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }
}
