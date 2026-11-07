CREATE TABLE pedidos (
    id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id        BIGINT        NOT NULL REFERENCES usuarios (id),
    status            VARCHAR(30)   NOT NULL CHECK (status IN (
    'AGUARDANDO_PAGAMENTO', 'PAGAMENTO_APROVADO', 'EM_PREPARACAO',
    'ENVIADO', 'ENTREGUE', 'CANCELADO')),
    total             NUMERIC(12,2) NOT NULL CHECK (total > 0),
    endereco_entrega  VARCHAR(500)  NOT NULL,
    criado_em         TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    atualizado_em     TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_pedidos_usuario_id ON pedidos (usuario_id);

CREATE TABLE itens_pedido (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pedido_id       BIGINT        NOT NULL REFERENCES pedidos (id),
    produto_id      BIGINT        NOT NULL REFERENCES produtos (id),
    nome_produto    VARCHAR(120)  NOT NULL,
    preco_unitario  NUMERIC(10,2) NOT NULL,
    quantidade      INTEGER       NOT NULL CHECK (quantidade > 0),
    subtotal        NUMERIC(12,2) NOT NULL
);

CREATE INDEX idx_itens_pedido_pedido_id ON itens_pedido (pedido_id);

ALTER TABLE movimentacoes_estoque
    ADD COLUMN pedido_id BIGINT REFERENCES pedidos (id);