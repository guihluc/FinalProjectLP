-- recria o banco do zero (apaga os dados que ja existem)
DROP DATABASE IF EXISTS nutriru;
CREATE DATABASE nutriru;
USE nutriru;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    matricula VARCHAR(20) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    tipo ENUM('ESTUDANTE', 'SERVIDOR', 'VISITANTE') NOT NULL,
    detalhe VARCHAR(100),
    saldo DECIMAL(8,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE refeicoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('ALMOCO', 'JANTAR') NOT NULL,
    data DATE NOT NULL,
    cardapio VARCHAR(255),
    preco_base DECIMAL(6,2) NOT NULL
);

CREATE TABLE historico_entradas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    refeicao_id INT NOT NULL,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor_cobrado DECIMAL(6,2) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (refeicao_id) REFERENCES refeicoes(id)
);
