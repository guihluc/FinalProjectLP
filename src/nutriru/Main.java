package nutriru;

import java.sql.Connection;
import java.sql.SQLException;

import nutriru.persistencia.FabricaConexao;
import nutriru.visao.MenuCLI;

public class Main {

    public static void main(String[] args) {
        // testa a conexao antes de abrir o menu
        try (Connection conexao = FabricaConexao.getConexao()) {
            System.out.println("Conectado ao banco " + conexao.getCatalog());
        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar ao banco: " + e.getMessage());
            System.out.println("Confira se o MySQL esta rodando, se o script sql/criar_tabelas.sql foi executado");
            System.out.println("e se a variavel NUTRIRU_SENHA esta definida.");
            return;
        }
        new MenuCLI().iniciar();
    }
}
