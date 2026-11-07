CREATE TABLE pagamentos (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pedido_id           BIGINT        NOT NULL REFERENCES pedidos (id),
    chave_idempotencia  VARCHAR(100)  NOT NULL UNIQUE,
    status              VARCHAR(20)   NOT NULL CHECK (status IN ('APROVADO', 'RECUSADO', 'ESTORNADO')),
    valor               NUMERIC(12,2) NOT NULL CHECK (valor > 0),
    criado_em           TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    atualizado_em       TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_pagamentos_pedido_id ON pagamentos (pedido_id);