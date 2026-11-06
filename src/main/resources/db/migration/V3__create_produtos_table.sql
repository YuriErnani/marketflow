CREATE TABLE produtos (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome          VARCHAR(120)  NOT NULL,
    descricao     VARCHAR(1000),
    preco         NUMERIC(10,2) NOT NULL CHECK (preco > 0),
    ativo         BOOLEAN       NOT NULL DEFAULT TRUE,
    categoria_id  BIGINT        NOT NULL REFERENCES categorias (id),
    criado_em     TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_produtos_categoria_id ON produtos (categoria_id);