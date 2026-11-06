CREATE TABLE categorias (
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome       VARCHAR(80)  NOT NULL,
    descricao  VARCHAR(255)
);

CREATE UNIQUE INDEX uk_categorias_nome ON categorias (LOWER(nome));