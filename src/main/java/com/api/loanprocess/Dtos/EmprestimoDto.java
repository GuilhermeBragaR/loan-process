package com.api.loanprocess.Dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class EmprestimoDto {
    @NotNull
    private Long valor_Emprestimo;
    @NotNull
    private Long numero_Parcelas;
}
