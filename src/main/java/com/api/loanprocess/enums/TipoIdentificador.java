package com.api.loanprocess.enums;


import com.api.loanprocess.execoes.ExecoesTipoIdentificador;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoIdentificador {
    PESSOA_FISICA(11, 300L, 10000L ),
    PESSOA_JURIDICA(14, 1000L, 1000000L),
    ESTUDANTE_UNIVERSITARIO(8, 100L, 10000L),
    APOSENTADO(10, 400L, 25000L);

    private final int quantidadeCaracteres;
    private final long valorMinimoMensal;
    private final long valorMaximoEmprestimo;


    public static TipoIdentificador buscaPorIdentificador(Integer quantidadeCaracteres) {
        try {
            for (TipoIdentificador tipo : values()) {
                if (tipo.quantidadeCaracteres == quantidadeCaracteres) {
                    return tipo;
                }
            }
            throw new ExecoesTipoIdentificador("Identificador incorreto, informe um identificiador valido");
        } catch (ExecoesTipoIdentificador e) {
            throw new ExecoesTipoIdentificador(e.getMessage());
        }
    }
}
