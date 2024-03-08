package com.api.loanprocess.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "pessoas")
@Data
public class PessoaModel implements Serializable { // <- Utilizado para converter objetos java para bites para ser salvos no banco
    private static final long serialVarsionUID = 1L; // <- Controle sobre essas conversoes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private Long identificador;
    @Column(nullable = false)
    private String dataNascimento;
    @Column()
    private String tipoIdentificador;
    @Column()
    private Long  valorMinimoMensal;
    @Column()
    private Long valorMaximoEmprestimo;
}
