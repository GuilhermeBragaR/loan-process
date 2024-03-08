package com.api.loanprocess.Dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PessoaDto {
    @NotBlank
    private String nome;
    @NotNull
    private Long identificador;
    @NotBlank
    private String dataNascimento;
}
