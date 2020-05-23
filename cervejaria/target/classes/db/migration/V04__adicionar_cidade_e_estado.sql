CREATE TABLE estado (
    codigo BIGINT(20) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    sigla VARCHAR(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE cidade (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    codigo_estado BIGINT(20) NOT NULL,
    FOREIGN KEY (codigo_estado) REFERENCES estado(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO estado (codigo, nome, sigla) VALUES (1,'Acre', 'AC');
INSERT INTO estado (codigo, nome, sigla) VALUES (2,'Bahia', 'BA');
INSERT INTO estado (codigo, nome, sigla) VALUES (3,'Goiás', 'GO');
INSERT INTO estado (codigo, nome, sigla) VALUES (4,'Minas Gerais', 'MG');
INSERT INTO estado (codigo, nome, sigla) VALUES (5,'Mato Grosso', 'MT');
INSERT INTO estado (codigo, nome, sigla) VALUES (6,'Mato Grosso do Sul', 'MS');
INSERT INTO estado (codigo, nome, sigla) VALUES (7,'Santa Catarina', 'SC');
INSERT INTO estado (codigo, nome, sigla) VALUES (8,'São Paulo', 'SP');


INSERT INTO cidade (nome, codigo_estado) VALUES ('Rio Branco', 1);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Cruzeiro do Sul', 1);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Salvador', 2);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Porto Seguro', 2);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Santana', 2);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Goiânia', 3);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Itumbiara', 3);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Novo Brasil', 3);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Belo Horizonte', 4);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Uberlândia', 4);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Montes Claros', 4);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Florianópolis', 7);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Criciúma', 7);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Camboriú', 7);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Lages', 7);
INSERT INTO cidade (nome, codigo_estado) VALUES ('São Paulo', 8);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Ribeirão Preto', 8);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Campinas', 8);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Santos', 8);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Cuiabá', 5);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Sinop', 5);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Barra do Garças', 5);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Campo Grande', 6);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Ponta Porã', 6);
INSERT INTO cidade (nome, codigo_estado) VALUES ('Três Lagoas', 6);
