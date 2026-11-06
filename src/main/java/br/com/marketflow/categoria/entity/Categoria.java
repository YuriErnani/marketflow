package br.com.marketflow.categoria.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorias")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    private String descricao;

    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public void atualizar(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

}
