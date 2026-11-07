CREATE TABLE estoques (
    id                     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    produto_id             BIGINT  NOT NULL UNIQUE REFERENCES produtos (id),
    quantidade_disponivel  INTEGER NOT NULL DEFAULT 0 CHECK (quantidade_disponivel >= 0),
    quantidade_reservada   INTEGER NOT NULL DEFAULT 0 CHECK (quantidade_reservada >= 0)
);

CREATE TABLE movimentacoes_estoque (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    produto_id  BIGINT      NOT NULL REFERENCES produtos (id),
    tipo        VARCHAR(20) NOT NULL CHECK (tipo IN ('ENTRADA', 'RESERVA', 'BAIXA', 'LIBERACAO', 'DEVOLUCAO')),
    quantidade  INTEGER     NOT NULL CHECK (quantidade > 0),
    criado_em   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_movimentacoes_estoque_produto_id ON movimentacoes_estoque (produto_id);

INSERT INTO estoques (produto_id)
SELECT id FROM produtos;