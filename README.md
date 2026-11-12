# NutriRU

Sistema de gestão e controle de acesso ao Restaurante Universitário (projeto final de Linguagem de Programação II - IMD/UFRN).

Equipe: Luis Barreto, Brunno Cesar e Rafael Medeiros.

## Descrição

O sistema gerencia o cardápio, a recarga de créditos e o controle de acesso nas catracas do RU.

- `UsuarioRU` (classe abstrata): base de `Estudante` e `Servidor`, com o método abstrato `calcularPrecoRefeicao()`.
- `Autenticavel` (interface): contrato de validação de matrícula e autorização de entrada.
- `Refeicao` (classe abstrata): base de `Almoco` e `Jantar`.
- `FabricaConexao`: cria a conexão com o MySQL via JDBC.

## Versão do JDK

Java 21 (JDK 21 LTS).

## Banco de dados

1. Rode o script `sql/criar_tabelas.sql` no MySQL 8.0.
2. Baixe o `mysql-connector-j` e coloque o `.jar` na pasta `lib/`.
3. Defina a senha do MySQL na variável de ambiente `NUTRIRU_SENHA`.

## Como compilar e executar

Na raiz do projeto (Linux/Mac):

```
javac -d out $(find src -name "*.java")
java -cp out:lib/* nutriru.persistencia.TesteConexao
```

No Windows (PowerShell):

```
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "out;lib/*" nutriru.persistencia.TesteConexao
```

## Estrutura

```
src/nutriru/modelo        classes do domínio
src/nutriru/persistencia  conexão com o banco
sql/                      script de criação das tabelas
docs/                     diagrama de classes e DER
test/                     testes unitários (em andamento)
```

## Status

Checkpoint 1: estrutura do projeto, hierarquias de `UsuarioRU` e `Refeicao`, banco de dados e conexão JDBC.
