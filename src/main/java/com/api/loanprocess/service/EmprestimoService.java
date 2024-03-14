package com.api.loanprocess.service;

import com.api.loanprocess.Dtos.EmprestimoDto;
import com.api.loanprocess.execoes.ExecoesEmprestimo;
import com.api.loanprocess.execoes.ExecoesTipoIdentificador;
import com.api.loanprocess.model.EmprestimoModel;
import com.api.loanprocess.model.PessoaModel;
import com.api.loanprocess.repository.EmprestimoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmprestimoService {

    final EmprestimoRepository emprestimoRepository;
    final PessoaService pessoaService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, PessoaService pessoaService) {
        this.emprestimoRepository = emprestimoRepository;
        this.pessoaService = pessoaService;
    }

    public Optional<EmprestimoModel> validaPessoa(UUID id) {
        return emprestimoRepository.findById(id);
    }

    public EmprestimoModel validaEmprestimoCNPJCPF(UUID id, EmprestimoModel emprestimoModel) {
        Optional<PessoaModel> pessoaModelOptional = pessoaService.buscaPorId(id);
        String tipoIdentificador = pessoaModelOptional.get().getIdentificador().toString();
        Integer quantiadeCaracteres = tipoIdentificador.length();

        validaCNPJCPF(quantiadeCaracteres);
        validaLimite(id, emprestimoModel);
        validaValorParcela(id, emprestimoModel);

        return emprestimoRepository.save(emprestimoModel);
    }

    public EmprestimoModel validaEmprestimoEU(UUID id, EmprestimoModel emprestimoModel){
        validaEU(id);
        validaLimite(id,emprestimoModel);
        validaValorParcela(id, emprestimoModel);

        return emprestimoRepository.save(emprestimoModel);
    }
    public EmprestimoModel validaEmprestimoAP(UUID id, EmprestimoModel emprestimoModel) {
        validaAP(id);
        validaLimite(id, emprestimoModel);
        validaValorParcela(id, emprestimoModel);

        return emprestimoRepository.save(emprestimoModel);
    }

    private void validaCNPJCPF(Integer quantidadeCaracteres) {
        if(quantidadeCaracteres != 11 && quantidadeCaracteres != 14){
            throw new ExecoesEmprestimo("Digito incorreto Valida CNPJ");
        }
    }

    private void validaEU(UUID id) {
        Optional<PessoaModel> pessoaModelOptional = pessoaService.buscaPorId(id);
        String tipoIdentificador = pessoaModelOptional.get().getIdentificador().toString();
        Integer quantiadeCaracteres = tipoIdentificador.length();
        Integer primeiroDigito = Character.getNumericValue(tipoIdentificador.charAt(0));
        Integer ultimoDigito = Character.getNumericValue(tipoIdentificador.charAt(tipoIdentificador.length() -1));
        Integer somaDosNumeros = primeiroDigito + ultimoDigito;
        if(quantiadeCaracteres != 8){
            throw new ExecoesEmprestimo("Digito incorreto ValidaEU caracter diferente 8");
        }
        if(somaDosNumeros != 9) {
            throw new ExecoesEmprestimo("Digito incorreto soma diferente de 9");
        }
    }

    private void validaAP(UUID id) {
        Optional<PessoaModel> pessoaModelOptional = pessoaService.buscaPorId(id);
        String tipoIdentificador = pessoaModelOptional.get().getIdentificador().toString();
        Integer quantidadeCaracteres = tipoIdentificador.length();
        Integer ultimoDigito = Character.getNumericValue(tipoIdentificador.charAt(tipoIdentificador.length() -1));

        if (quantidadeCaracteres != 10) {
            throw new ExecoesEmprestimo("Digito incorreto ValidaAP caracterers diferente de 10");
        }

        for (var i = 0; i < quantidadeCaracteres; i++){
            Integer digito = Character.getNumericValue(tipoIdentificador.charAt(i));

            if(digito.equals(ultimoDigito)){
                throw new ExecoesEmprestimo("Ultimo digito existe nos restantes dos digitos");
            }
        }
    }

    private void validaLimite(UUID id, EmprestimoModel emprestimoModel) {
        Optional<PessoaModel> pessoaModelOptional = pessoaService.buscaPorId(id);
        Long valorMaximo = pessoaModelOptional.get().getValorMaximoEmprestimo();
        Long valorSolicitado = emprestimoModel.getValor_Emprestimo();
        if(valorSolicitado > valorMaximo) {
            throw new ExecoesEmprestimo("Você não pode pegar esse valor maximo como emprestimo");
        }
    }

    private void validaValorParcela(UUID id, EmprestimoModel emprestimoModel) {
        Optional<PessoaModel> pessoaModelOptional = pessoaService.buscaPorId(id);
        Long numeroParcelas = emprestimoModel.getNumero_Parcelas();
        Long valorEmprestimo = emprestimoModel.getValor_Emprestimo();
        Long valorParcelas = valorEmprestimo/numeroParcelas;
        if(valorParcelas < pessoaModelOptional.get().getValorMinimoMensal()) {
            throw new ExecoesEmprestimo("Valor minimo não atingido");
        }
    }

    public Optional<EmprestimoModel> buscaPorId(UUID id) {
        return emprestimoRepository.findById(id);
    }

    @Transactional
    public EmprestimoModel salvarPagamento(EmprestimoModel emprestimoModel) {
        return emprestimoRepository.save(emprestimoModel);
    }

    public List<EmprestimoModel> consultaEmprestimo(){
        return emprestimoRepository.findAll();
    }
}
