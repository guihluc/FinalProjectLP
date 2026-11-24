USE nutriru;

INSERT INTO usuarios (matricula, nome, tipo, detalhe, saldo) VALUES
('20240001111', 'Ana Souza', 'ESTUDANTE', 'Tecnologia da Informacao', 10.00),
('20240002222', 'Pedro Lima', 'ESTUDANTE', 'Engenharia Civil', 1.00),
('1234567', 'Marcos Silva', 'SERVIDOR', 'Instituto Metropole Digital', 30.00),
('12345', 'Carla Dias', 'VISITANTE', 'Palestra no IMD', 5.00);

INSERT INTO refeicoes (tipo, data, cardapio, preco_base) VALUES
('ALMOCO', CURDATE(), 'Arroz, feijao, frango grelhado e salada', 10.00),
('JANTAR', CURDATE(), 'Macarrao com carne moida e salada', 8.00);
