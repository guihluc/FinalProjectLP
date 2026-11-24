# NutriRU

Sistema de gestão e controle de acesso ao Restaurante Universitário (projeto final de Linguagem de Programação II - IMD/UFRN).

Equipe: Luis Barreto, Brunno Cesar e Rafael Medeiros.

## Descrição

O NutriRU controla o cardápio, a recarga de créditos e a entrada na catraca do RU. Cada tipo de usuário (estudante, servidor e visitante) paga um valor diferente pela refeição. Quando a pessoa passa na catraca, o sistema confere a matrícula, vê se o saldo cobre a refeição, desconta o valor e grava a entrada no histórico.

Funcionalidades do menu:

1. Cadastrar usuário (estudante, servidor ou visitante)
2. Recarregar crédito
3. Cadastrar refeição (cardápio do dia)
4. Consultar cardápio
5. Verificar autorização de entrada
6. Entrar no RU (catraca)
7. Ver saldo e histórico

Preços usados (valores de exemplo): almoço R$ 10,00 e jantar R$ 8,00 como preço base. O estudante paga 20% do preço base, o servidor paga o preço base e o visitante paga 150%.

## Versão do JDK

Java 21 (JDK 21 LTS). Banco: MySQL 8.0.

## Como compilar e executar

### 1. Banco de dados

1. Rode o script `sql/criar_tabelas.sql` no MySQL (ele apaga e recria o banco `nutriru`).
2. Opcional: rode `sql/dados_teste.sql` para ter usuários e refeições de exemplo.
3. Baixe o `mysql-connector-j` (versão 8.x) e coloque o `.jar` na pasta `lib/`.
4. Defina a senha do usuário `root` do MySQL na variável de ambiente `NUTRIRU_SENHA`.

Linux/Mac:

```
mysql -u root -p < sql/criar_tabelas.sql
mysql -u root -p < sql/dados_teste.sql
export NUTRIRU_SENHA=sua_senha
```

Windows (PowerShell):

```
Get-Content sql\criar_tabelas.sql | mysql -u root -p
Get-Content sql\dados_teste.sql | mysql -u root -p
$env:NUTRIRU_SENHA = "sua_senha"
```

### 2. Compilar e rodar o sistema

Linux/Mac:

```
javac -d out $(find src -name "*.java")
java -cp "out:lib/*" nutriru.Main
```

Windows (PowerShell):

```
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "out;lib/*" nutriru.Main
```

### 3. Rodar os testes unitários (JUnit 5)

Baixe o `junit-platform-console-standalone` (1.10.x) e coloque o `.jar` na pasta `lib/`.

Linux/Mac:

```
javac -d out -cp "lib/*" $(find src -name "*.java")
javac -d out -cp "out:lib/*" $(find test -name "*.java")
java -jar lib/junit-platform-console-standalone-1.10.2.jar execute --class-path out --scan-class-path
```

Windows (PowerShell):

```
javac -d out -cp "lib/*" (Get-ChildItem -Recurse src -Filter *.java).FullName
javac -d out -cp "out;lib/*" (Get-ChildItem -Recurse test -Filter *.java).FullName
java -jar lib/junit-platform-console-standalone-1.10.2.jar execute --class-path out --scan-class-path
```

## Estrutura do projeto

```
src/nutriru/Main.java        ponto de entrada
src/nutriru/modelo/          classes do domínio (usuários, refeições, histórico)
src/nutriru/excecao/         exceções customizadas
src/nutriru/persistencia/    conexão e DAOs (JDBC)
src/nutriru/servico/         regras do sistema (ControleAcesso)
src/nutriru/visao/           menu de linha de comando
test/nutriru/modelo/         testes unitários (JUnit 5)
sql/                         scripts do banco
docs/                        diagramas UML e DER
```

## Conceitos de POO no código

| Conceito | Onde está |
|---|---|
| Classe abstrata com subclasses | `UsuarioRU` (`Estudante`, `Servidor`, `Visitante`) e `Refeicao` (`Almoco`, `Jantar`) |
| Interface | `Autenticavel`, implementada por `UsuarioRU` e herdada pelas três subclasses |
| Polimorfismo | `calcularPrecoRefeicao()` e `validarMatricula()` mudam de acordo com o tipo de usuário. O `ControleAcesso` e os testes tratam todos como `UsuarioRU` ou `Autenticavel` |
| Encapsulamento | Atributos `private` com getters. O saldo só muda por `adicionarCredito()` e `debitar()` |
| Exceções | `SaldoInsuficienteException`, `MatriculaInvalidaException`, `UsuarioNaoEncontradoException` e `RefeicaoNaoEncontradaException` |
| Testes | `UsuarioRUTest` e `RefeicaoTest` (20 testes) |
| Extra | Banco de dados MySQL com JDBC |

## Padrões de projeto e justificativas

**Factory (`FabricaConexao`).** Todas as classes que acessam o banco pegam a conexão em `FabricaConexao.getConexao()`. Assim a URL, o usuário e a senha ficam em um lugar só. Se o banco mudar, só essa classe é alterada. A senha vem da variável de ambiente `NUTRIRU_SENHA` para não ir para o GitHub.

**DAO (`UsuarioDAO`, `RefeicaoDAO`, `HistoricoEntradaDAO`).** Todo o SQL fica nos DAOs. As classes do modelo não sabem que existe banco de dados, então dá para testá-las sem MySQL e trocar o banco sem mexer nelas. Os DAOs usam `PreparedStatement`, que evita SQL injection.

**Camadas (visão, serviço, persistência, modelo).** O `MenuCLI` só lê e mostra dados, o `ControleAcesso` concentra as regras e os DAOs falam com o banco. Cada camada só conversa com a de baixo.

### Princípios SOLID

- **S (responsabilidade única):** cada classe tem um papel. O menu não tem regra de negócio e os DAOs não têm regra de negócio.
- **O (aberto/fechado):** para criar um novo tipo de usuário (por exemplo, `Professor`), basta criar uma subclasse de `UsuarioRU`. O `ControleAcesso` não muda, porque usa só `UsuarioRU`.
- **L (substituição de Liskov):** qualquer subclasse de `UsuarioRU` pode ser usada no lugar da classe base, como mostra o teste `polimorfismoComListaDeUsuarios`.
- **I (segregação de interfaces):** `Autenticavel` tem só dois métodos, os que a catraca precisa.
- **D (inversão de dependência):** o `ControleAcesso` depende da abstração `UsuarioRU`/`Autenticavel` e não de `Estudante` ou `Servidor`. (Ele ainda cria os DAOs diretamente, o que seria o próximo passo de melhoria.)

## Diagramas

- `docs/diagrama-classes.png`: diagrama de classes UML
- `docs/diagrama-casos-de-uso.png`: diagrama de casos de uso
- `docs/diagrama-sequencia.png`: diagrama de sequência (entrada na catraca)
- `docs/der.png`: diagrama entidade-relacionamento do banco

Os arquivos `.dot` e `.mmd` são o código-fonte dos diagramas.
