package com.api.loanprocess.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emprestimo")
@Data
public class EmprestimoModel implements Serializable {
    private static final long serialVarsionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private PessoaModel id_Pessoa;
    @Column()
    private Long valor_Emprestimo;
    @Column()
    private Long numero_Parcelas;
    @Column()
    private String status_Pagamento;
    @Column()
    private LocalDateTime data_Criacao;


}
