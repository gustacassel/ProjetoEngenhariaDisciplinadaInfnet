package com.infnet.pb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "produtos")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;

    @Positive(message = "O preço do produto deve ser maior que zero.")
    private Double preco;

    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    private Integer quantidadeEstoque;
}
