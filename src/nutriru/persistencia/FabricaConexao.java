package nutriru.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabricaConexao {

    private static final String URL = "jdbc:mysql://localhost:3306/nutriru";
    private static final String USUARIO = "root";

    // a senha vem da variavel de ambiente NUTRIRU_SENHA para nao ir pro GitHub
    private static String lerSenha() {
        String senha = System.getenv("NUTRIRU_SENHA");
        return senha == null ? "" : senha;
    }

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, lerSenha());
    }
}
