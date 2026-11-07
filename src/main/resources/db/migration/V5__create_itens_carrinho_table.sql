CREATE TABLE itens_carrinho (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id  BIGINT  NOT NULL REFERENCES usuarios (id),
    produto_id  BIGINT  NOT NULL REFERENCES produtos (id),
    quantidade  INTEGER NOT NULL CHECK (quantidade > 0),
    CONSTRAINT uk_itens_carrinho_usuario_produto UNIQUE (usuario_id, produto_id)
);