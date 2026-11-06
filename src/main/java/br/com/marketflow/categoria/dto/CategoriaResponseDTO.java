package br.com.marketflow.categoria.dto;

import br.com.marketflow.categoria.entity.Categoria;

public record CategoriaResponseDTO(
        Long id,
        String nome,
        String descricao
) {

    public static CategoriaResponseDTO from(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );
    }

}
