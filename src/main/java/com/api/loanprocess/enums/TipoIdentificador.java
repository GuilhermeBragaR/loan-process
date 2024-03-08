package com.api.loanprocess.enums;


public enum TipoIdentificador {
    PESSOA_FISICA(11, 300L, 10000L ),
    PESSOA_JURIDICA(14, 10000L, 1000000L),
    ESTUDANTE_UNIVERSITARIO(8, 100L, 10000L),
    APOSENTADO(10, 400L, 25000L);

    private final int quantidadeCaracteres;
    private final long valorMinimoMensal;
    private final long valorMaximoEmprestimo;

    TipoIdentificador(int quantidadeCaracteres, long valorMinimoMensal, long valorMaximoMensal) {
        this.quantidadeCaracteres = quantidadeCaracteres;
        this.valorMinimoMensal = valorMinimoMensal;
        this.valorMaximoEmprestimo = valorMaximoMensal;
    }

    public static TipoIdentificador buscaPorIdentificador(int quantidadeCaracteres) {
        try {
            for (TipoIdentificador tipo : values()) {
                if (tipo.quantidadeCaracteres == quantidadeCaracteres) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Tipo Identificador inexistente");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public int getQuantidadeCaracteres() {
        return quantidadeCaracteres;
    }

    public long getValorMinimoMensal() {
        return valorMinimoMensal;
    }

    public long getValorMaximoMensal() {
        return valorMaximoEmprestimo;
    }
}
