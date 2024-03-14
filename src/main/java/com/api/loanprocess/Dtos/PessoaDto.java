package com.api.loanprocess.Dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PessoaDto {
    @NotBlank
    private String nome;
    @NotNull
    private Long identificador;
    @NotNull
    private LocalDate dataNascimento;
}
